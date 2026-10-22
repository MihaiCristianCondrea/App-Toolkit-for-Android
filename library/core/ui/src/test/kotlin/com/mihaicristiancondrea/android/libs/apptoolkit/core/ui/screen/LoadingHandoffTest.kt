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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * A result that arrives while its screen is still sliding in waits behind the loading content
 * until the screen's entrance can begin, so the screen is never blank between the two.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class LoadingHandoffTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private var cap by mutableStateOf(Lifecycle.State.STARTED)
    private var state: Loadable<String> by mutableStateOf(Loadable.Loading)
    private var cueReached by mutableStateOf(false)
    private val cue = EntranceCue { cueReached }

    private val destination = object : ViewModelStoreOwner {
        override val viewModelStore = ViewModelStore()
    }

    private fun setContent(withCue: Boolean = false) {
        compose.setContent {
            MaterialTheme {
                CompositionLocalProvider(
                    LocalTelemetry provides FakeTelemetryRepository(),
                    LocalViewModelStoreOwner provides destination,
                    LocalLifecycleOwner provides rememberLifecycleOwner(maxLifecycle = cap),
                    LocalEntranceCue provides if (withCue) cue else null,
                ) {
                    ScreenHost(screen = TrackedScreen(name = "Feed")) {
                        ScreenStateHandler(
                            state = state,
                            showAd = false,
                            onLoading = { Text("Loading") },
                        ) { ready -> Text(ready.value) }
                    }
                }
            }
        }
        compose.waitForIdle()
    }

    @Test
    fun `a result during the transition waits behind the loading content`() {
        setContent()
        state = Loadable.Ready("Articles")
        compose.waitForIdle()
        compose.onNodeWithText("Loading").assertExists()
        compose.onNodeWithText("Articles").assertDoesNotExist()

        cap = Lifecycle.State.RESUMED
        compose.waitForIdle()
        compose.onNodeWithText("Articles").assertExists()
        compose.onNodeWithText("Loading").assertDoesNotExist()
    }

    @Test
    fun `the entrance cue lets the result in before the transition ends`() {
        setContent(withCue = true)
        state = Loadable.Ready("Articles")
        compose.waitForIdle()
        compose.onNodeWithText("Articles").assertDoesNotExist()

        cueReached = true
        compose.waitForIdle()
        compose.onNodeWithText("Articles").assertExists()
    }

    @Test
    fun `a slow result replaces the loading content as soon as it arrives`() {
        cap = Lifecycle.State.RESUMED
        setContent()
        compose.onNodeWithText("Loading").assertExists()

        state = Loadable.Ready("Articles")
        compose.waitForIdle()
        compose.onNodeWithText("Articles").assertExists()
    }

    @Test
    fun `a failure during the transition waits too`() {
        setContent()
        state = Loadable.Failed(UiTextHelper.DynamicString("Offline"), retryable = false)
        compose.waitForIdle()
        compose.onNodeWithText("Offline").assertDoesNotExist()

        cap = Lifecycle.State.RESUMED
        compose.waitForIdle()
        compose.onNodeWithText("Offline").assertExists()
    }
}
