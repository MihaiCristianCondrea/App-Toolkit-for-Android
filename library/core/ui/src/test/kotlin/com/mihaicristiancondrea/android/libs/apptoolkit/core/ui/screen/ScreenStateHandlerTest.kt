/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.FabHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.LocalFabHost
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** Without an ad, none of these start Koin: the default state content needs it only for the ad. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class ScreenStateHandlerTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val tryAgain: String get() = compose.activity.getString(com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R.string.try_again)

    private fun text(value: String) = UiTextHelper.DynamicString(value)

    @Test
    fun `an empty result offers a retry only when asked to`() {
        var retryWhenEmpty by mutableStateOf(false)
        var retries = 0
        compose.setContent {
            ScreenStateHandler(
                state = Loadable.Empty(text("Nothing here")),
                onRetry = { retries++ },
                retryWhenEmpty = retryWhenEmpty,
                showAd = false,
            ) {}
        }
        compose.onNodeWithText("Nothing here").assertExists()
        compose.onNodeWithText(tryAgain).assertDoesNotExist()

        retryWhenEmpty = true
        compose.onNodeWithText(tryAgain).performClick()
        assertEquals(1, retries)
    }

    @Test
    fun `a failure offers a retry only when it is retryable and there is one`() {
        var state: Loadable<String> by mutableStateOf(Loadable.Failed(text("Offline"), retryable = false))
        var retries = 0
        compose.setContent {
            ScreenStateHandler(state = state, onRetry = { retries++ }, showAd = false) {}
        }
        compose.onNodeWithText("Offline").assertExists()
        compose.onNodeWithText(tryAgain).assertDoesNotExist()

        state = Loadable.Failed(text("Timed out"), retryable = true)
        compose.onNodeWithText(tryAgain).performClick()
        assertEquals(1, retries)
    }

    @Test
    fun `the app's screen states draw every case but the content`() {
        var state: Loadable<String> by mutableStateOf(Loadable.Loading)
        val states = ScreenStates(
            loading = { Text("App loading") },
            empty = { _, _ -> Text("App empty") },
            failed = { slot, failed -> Text("App failed: ${(failed.message as UiTextHelper.DynamicString).content}, retry ${slot.onRetry != null}") },
        )
        compose.setContent {
            CompositionLocalProvider(LocalScreenStates provides states) {
                ScreenStateHandler(state = state, onRetry = {}) { ready -> Text("Ready ${ready.value}") }
            }
        }
        compose.onNodeWithText("App loading").assertExists()

        state = Loadable.Empty()
        compose.onNodeWithText("App empty").assertExists()

        state = Loadable.Failed(text("Offline"))
        compose.onNodeWithText("App failed: Offline, retry true").assertExists()

        state = Loadable.Ready("news")
        compose.onNodeWithText("Ready news").assertExists()
    }

    @Test
    fun `a section's own content wins over the app's`() {
        compose.setContent {
            CompositionLocalProvider(LocalScreenStates provides ScreenStates(loading = { Text("App loading") })) {
                ScreenStateHandler(state = Loadable.Loading, onLoading = { Text("Skeleton") }) {}
            }
        }
        compose.onNodeWithText("Skeleton").assertExists()
        compose.onNodeWithText("App loading").assertDoesNotExist()
    }

    @Test
    fun `state content switches between the values of an enum`() {
        var phase by mutableStateOf(Phase.Idle)
        compose.setContent {
            StateContent(state = phase) { Text("Phase $it") }
        }
        compose.onNodeWithText("Phase Idle").assertExists()

        phase = Phase.Cleaning
        compose.waitForIdle()
        compose.onNodeWithText("Phase Cleaning").assertExists()
        compose.onNodeWithText("Phase Idle").assertDoesNotExist()
    }

    @Test
    fun `a screen's buttons follow its state and leave with it`() {
        val host = FabHost()
        var cleaning by mutableStateOf(false)
        var shown by mutableStateOf(true)
        compose.setContent {
            CompositionLocalProvider(LocalFabHost provides host, LocalTelemetry provides FakeTelemetryRepository()) {
                if (shown) {
                    ScreenHost(
                        screen = TrackedScreen(name = "Cleaner"),
                        fabs = listOf(ToolkitFab(ToolkitIcon.Vector(Icons.Outlined.Add), onClick = {}, label = if (cleaning) "Stop" else "Clean")),
                    ) {}
                }
            }
        }
        compose.waitForIdle()
        assertEquals(listOf("Clean"), host.fabs.map { it.label })

        cleaning = true
        compose.waitForIdle()
        assertEquals(listOf("Stop"), host.fabs.map { it.label })

        shown = false
        compose.waitForIdle()
        assertEquals(emptyList<String?>(), host.fabs.map { it.label })
    }

    private enum class Phase { Idle, Cleaning }
}
