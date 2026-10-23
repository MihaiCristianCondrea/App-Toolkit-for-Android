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

import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class ScreenHostTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val screen = TrackedScreen(name = "News", className = "NewsScreen")
    private val telemetry = FakeTelemetryRepository()

    private fun views(): Int = telemetry.loggedScreenViews.size

    private fun states(): List<String> = telemetry.loggedEvents
        .filter { it.name == "screen_state" }
        .map { (it.params.getValue("state") as AnalyticsValue.Str).value }

    @Test
    fun `a screen is reported once it is resumed, not while it only animates in`() {
        var cap by mutableStateOf(Lifecycle.State.STARTED)
        compose.setContent {
            CompositionLocalProvider(
                LocalTelemetry provides telemetry,
                LocalLifecycleOwner provides rememberLifecycleOwner(maxLifecycle = cap),
            ) {
                ScreenHost(screen = screen) { Text("News content") }
            }
        }
        compose.onNodeWithText("News content").assertExists()
        assertEquals(0, views())

        cap = Lifecycle.State.RESUMED
        compose.waitForIdle()
        assertEquals(1, views())

        // An overlay or a cancelled back gesture caps it again; it was never left.
        cap = Lifecycle.State.STARTED
        compose.waitForIdle()
        cap = Lifecycle.State.RESUMED
        compose.waitForIdle()
        assertEquals(1, views())
    }

    @Test
    fun `recomposition and repeated labels report nothing new`() {
        var status: Loadable<String> by mutableStateOf(Loadable.Loading)
        var unrelated by mutableIntStateOf(0)
        compose.setContent {
            CompositionLocalProvider(LocalTelemetry provides telemetry) {
                ScreenHost(screen = screen, status = status) { Text("count $unrelated") }
            }
        }
        compose.waitForIdle()

        unrelated++
        compose.waitForIdle()
        status = Loadable.Ready("first")
        compose.waitForIdle()
        status = Loadable.Ready("second", refreshing = true)
        compose.waitForIdle()
        status = Loadable.Failed(UiText.DynamicString("offline"), retryable = true)
        compose.waitForIdle()

        assertEquals(1, views())
        assertEquals(listOf("loading", "success", "error"), states())
    }

    @Test
    fun `leaving the screen ends the visit and returning starts a new one`() {
        var shown by mutableStateOf(true)
        compose.setContent {
            CompositionLocalProvider(LocalTelemetry provides telemetry) {
                if (shown) ScreenHost(screen = screen, status = Loadable.Ready(Unit)) { Text("News content") }
            }
        }
        compose.waitForIdle()

        shown = false
        compose.waitForIdle()
        shown = true
        compose.waitForIdle()

        assertEquals(2, views())
        assertEquals(listOf("success", "success"), states())
    }

    @Test
    fun `coming back from the background is a new visit`() {
        compose.setContent {
            CompositionLocalProvider(LocalTelemetry provides telemetry) {
                ScreenHost(screen = screen) { Text("News content") }
            }
        }
        compose.waitForIdle()

        compose.activityRule.scenario.moveToState(Lifecycle.State.STARTED)
        compose.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
        assertEquals(1, views())

        compose.activityRule.scenario.moveToState(Lifecycle.State.CREATED)
        compose.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
        compose.waitForIdle()
        assertEquals(2, views())
    }

    @Test
    fun `the view model's messages are shown`() {
        val viewModel = MessagesViewModel()
        compose.setContent {
            CompositionLocalProvider(LocalTelemetry provides telemetry) {
                ScreenHost(screen = screen, viewModel = viewModel) { Text("News content") }
            }
        }

        viewModel.say("Saved")

        compose.waitUntil(timeoutMillis = 5_000) {
            compose.onAllNodes(hasText("Saved")).fetchSemanticsNodes().isNotEmpty()
        }
    }

    private class MessagesViewModel : ScreenViewModel<Unit, Unit>(Unit) {
        override fun handleEvent(event: Unit) = Unit

        fun say(text: String) = showMessage(UiMessage(UiText.DynamicString(text)))
    }
}

/** Sets its content in onCreate, as an app's activity does, so recreating it recomposes the screen. */
class ScreenHostTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(LocalTelemetry provides telemetry) {
                ScreenHost(screen = TrackedScreen(name = "Recreated")) { Text("Recreated content") }
            }
        }
    }

    companion object {
        val telemetry = FakeTelemetryRepository()
    }
}

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class ScreenHostRecreationTest {

    private fun idle() = shadowOf(Looper.getMainLooper()).idle()

    @Test
    fun `a configuration change keeps the visit and process death starts a new one`() {
        val telemetry = ScreenHostTestActivity.telemetry
        telemetry.loggedScreenViews.clear()
        val controller = Robolectric.buildActivity(ScreenHostTestActivity::class.java).setup()
        idle()
        assertEquals(1, telemetry.loggedScreenViews.size)

        controller.recreate()
        idle()
        assertEquals(1, telemetry.loggedScreenViews.size)

        // Process death: the saved state comes back, the ViewModels do not.
        val saved = Bundle()
        controller.saveInstanceState(saved).pause().stop().destroy()
        Robolectric.buildActivity(ScreenHostTestActivity::class.java).setup(saved)
        idle()
        assertEquals(2, telemetry.loggedScreenViews.size)
    }
}
