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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.componentEntrance
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * A screen's entrance, read as how much of a red box on white shows: it waits for the screen to
 * settle, plays again on each return, and stays through the background and within the visit.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36])
class ScreenEntranceTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private var cap by mutableStateOf(Lifecycle.State.RESUMED)
    private var onScreen by mutableStateOf(true)
    private var boxShown by mutableStateOf(true)
    private var replay by mutableStateOf(EntranceReplay.EveryVisit)

    /** Which copy of the screen is open: a new copy is the screen opened again after it closed. */
    private var copy by mutableStateOf(0)

    /** Each copy's destination store, which outlives the screen leaving composition, as in the shell. */
    private val destinations = mutableMapOf<Int, ViewModelStoreOwner>()

    private fun destination(copy: Int): ViewModelStoreOwner = destinations.getOrPut(copy) {
        object : ViewModelStoreOwner {
            override val viewModelStore = ViewModelStore()
        }
    }

    @Before
    fun startRun() = OpenedScreens.forget()

    private fun shown(tag: String = "frame"): Float {
        val bitmap = compose.onNodeWithTag(tag).captureToImage().asAndroidBitmap()
        return 1f - android.graphics.Color.green(bitmap.getPixel(bitmap.width / 2, bitmap.height / 2)) / 255f
    }

    private fun set(change: () -> Unit) {
        change()
        Snapshot.sendApplyNotifications()
    }

    private fun settle() = compose.mainClock.advanceTimeBy(3_000)

    private fun setContent() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            val saveable = rememberSaveableStateHolder()
            MaterialTheme {
                CompositionLocalProvider(
                    LocalTelemetry provides FakeTelemetryRepository(),
                    LocalViewModelStoreOwner provides destination(copy),
                    LocalLifecycleOwner provides rememberLifecycleOwner(maxLifecycle = cap),
                    LocalEntranceReplay provides replay,
                ) {
                    Box(Modifier.testTag("frame").size(80.dp).background(Color.White)) {
                        if (onScreen) {
                            saveable.SaveableStateProvider("news_$copy") {
                                ScreenHost(screen = TrackedScreen(name = "News")) {
                                    // Saved by key, as a lazy list saves each item it scrolls away.
                                    if (boxShown) saveable.SaveableStateProvider("item") { RedBox() }
                                }
                            }
                        }
                    }
                }
            }
        }
        compose.mainClock.advanceTimeByFrame()
    }

    @Composable
    private fun RedBox() {
        Box(Modifier.animateVisibility().size(80.dp).background(Color.Red))
    }

    @Test
    fun `elements wait for the screen to settle before entering`() {
        cap = Lifecycle.State.STARTED
        setContent()
        settle()
        assertTrue("Entered while the screen was still in its transition: ${shown()}", shown() < 0.05f)

        set { cap = Lifecycle.State.RESUMED }
        settle()
        assertTrue("Did not enter once settled: ${shown()}", shown() > 0.98f)
    }

    @Test
    fun `returning to the screen plays the entrance again`() {
        setContent()
        settle()
        assertTrue(shown() > 0.98f)

        set { onScreen = false }
        settle()
        set { onScreen = true }
        compose.mainClock.advanceTimeByFrame()
        assertTrue("Shown at once on return: ${shown()}", shown() < 0.5f)
        settle()
        assertTrue("Did not enter on return: ${shown()}", shown() > 0.98f)
    }

    /** Leaves the screen and comes back to the same copy, as going back to it does. */
    private fun goBackToIt() {
        set { onScreen = false }
        settle()
        set { onScreen = true }
        compose.mainClock.advanceTimeByFrame()
        compose.mainClock.advanceTimeByFrame()
    }

    /** Closes the screen and opens a new copy of it. */
    private fun openAgain() {
        set { onScreen = false }
        settle()
        set {
            copy += 1
            onScreen = true
        }
        compose.mainClock.advanceTimeByFrame()
        compose.mainClock.advanceTimeByFrame()
    }

    @Test
    fun `on open, going back to the screen shows the element at once`() {
        replay = EntranceReplay.OnOpen
        setContent()
        settle()

        goBackToIt()
        assertTrue("Played again on going back: ${shown()}", shown() > 0.98f)
    }

    @Test
    fun `on open, a screen opened again enters again`() {
        replay = EntranceReplay.OnOpen
        setContent()
        settle()

        openAgain()
        assertTrue("Shown at once when opened again: ${shown()}", shown() < 0.5f)
        settle()
        assertTrue("Did not enter when opened again: ${shown()}", shown() > 0.98f)
    }

    @Test
    fun `with first visit only, going back shows the element at once`() {
        replay = EntranceReplay.FirstVisit
        setContent()
        settle()

        goBackToIt()
        assertTrue("Played again on going back: ${shown()}", shown() > 0.98f)
    }

    @Test
    fun `with first visit only, a screen opened again shows the element at once`() {
        replay = EntranceReplay.FirstVisit
        setContent()
        settle()
        assertTrue("The first copy did not enter: ${shown()}", shown() > 0.98f)

        openAgain()
        assertTrue("Played again in a new copy: ${shown()}", shown() > 0.98f)
    }

    @Test
    fun `coming back from the background does not replay`() {
        setContent()
        settle()

        set { cap = Lifecycle.State.CREATED }
        settle()
        set { cap = Lifecycle.State.RESUMED }
        compose.mainClock.advanceTimeByFrame()
        assertTrue("Replayed after the background: ${shown()}", shown() > 0.98f)
    }

    @Test
    fun `an element scrolled away and back within the visit stays shown`() {
        setContent()
        settle()

        set { boxShown = false }
        compose.mainClock.advanceTimeByFrame()
        set { boxShown = true }
        compose.mainClock.advanceTimeByFrame()
        compose.mainClock.advanceTimeByFrame()
        assertTrue("Replayed within the visit: ${shown()}", shown() > 0.98f)
    }

    @Test
    fun `a Toolkit component enters with its screen and appears at once in a dialog`() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            MaterialTheme {
                CompositionLocalProvider(
                    LocalTelemetry provides FakeTelemetryRepository(),
                    LocalViewModelStoreOwner provides destination(copy),
                ) {
                    ScreenHost(screen = TrackedScreen(name = "Settings")) {
                        Box(Modifier.testTag("frame").size(80.dp).background(Color.White)) {
                            Box(Modifier.componentEntrance().size(80.dp).background(Color.Red))
                        }
                        Dialog(onDismissRequest = {}) {
                            Box(Modifier.testTag("dialog").size(80.dp).background(Color.White)) {
                                Box(Modifier.componentEntrance().size(80.dp).background(Color.Red))
                            }
                        }
                    }
                }
            }
        }
        compose.mainClock.advanceTimeByFrame()
        compose.mainClock.advanceTimeByFrame()
        assertTrue("The screen's component did not start hidden: ${shown()}", shown() < 0.5f)
        assertTrue("The dialog's component animated: ${shown("dialog")}", shown("dialog") > 0.98f)

        settle()
        assertTrue("The screen's component did not enter: ${shown()}", shown() > 0.98f)
    }
}
