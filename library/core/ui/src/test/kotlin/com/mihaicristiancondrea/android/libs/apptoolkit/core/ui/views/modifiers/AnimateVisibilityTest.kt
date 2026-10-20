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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * A red box on white: how much of it shows is how far its entrance has come, read from the green
 * channel of its middle pixel, white while hidden and none once fully shown.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36])
class AnimateVisibilityTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private fun shown(): Float {
        val bitmap = compose.onNodeWithTag("frame").captureToImage().asAndroidBitmap()
        val green = android.graphics.Color.green(bitmap.getPixel(bitmap.width / 2, bitmap.height / 2))
        return 1f - green / 255f
    }

    /** Changes state and applies it, as the paused clock does not. */
    private fun set(change: () -> Unit) {
        change()
        Snapshot.sendApplyNotifications()
    }

    private fun setContent(content: @Composable () -> Unit) {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            MaterialTheme {
                Box(Modifier.testTag("frame").size(80.dp).background(Color.White)) { content() }
            }
        }
        compose.mainClock.advanceTimeByFrame()
    }

    @Composable
    private fun RedBox(modifier: Modifier) {
        Box(modifier.size(80.dp).background(Color.Red))
    }

    @Test
    fun `an element enters from hidden to fully shown`() {
        setContent { RedBox(Modifier.animateVisibility()) }
        assertTrue("Shown on its first frame: ${shown()}", shown() < 0.2f)

        compose.mainClock.advanceTimeBy(2_000)
        assertTrue("Not fully shown once settled: ${shown()}", shown() > 0.98f)
    }

    @Test
    fun `an element composed ahead of time enters only once it is placed`() {
        var placed by mutableStateOf(false)
        setContent {
            Layout(content = { RedBox(Modifier.animateVisibility()) }) { measurables, constraints ->
                val placeable = measurables.single().measure(constraints)
                layout(placeable.width, placeable.height) { if (placed) placeable.place(0, 0) }
            }
        }
        compose.mainClock.advanceTimeBy(2_000)

        set { placed = true }
        compose.mainClock.advanceTimeByFrame()
        assertTrue("Played before it was placed: ${shown()}", shown() < 0.2f)

        compose.mainClock.advanceTimeBy(2_000)
        assertTrue("Not fully shown once settled: ${shown()}", shown() > 0.98f)
    }

    @Test
    fun `with no entrance style an element appears at once`() {
        setContent { RedBox(Modifier.animateVisibility(style = EntranceStyle.None)) }

        assertTrue("Hidden on its first frame: ${shown()}", shown() > 0.98f)
    }

    @Test
    fun `an element that comes and goes animates both ways`() {
        var visible by mutableStateOf(false)
        setContent { RedBox(Modifier.animateVisibility(visible = visible)) }
        assertTrue("Shown while not visible: ${shown()}", shown() < 0.02f)

        set { visible = true }
        compose.mainClock.advanceTimeBy(2_000)
        assertTrue("Not shown once visible: ${shown()}", shown() > 0.98f)

        set { visible = false }
        compose.mainClock.advanceTimeByFrame()
        compose.mainClock.advanceTimeByFrame()
        assertTrue("Hid at once instead of animating: ${shown()}", shown() > 0.5f)
        compose.mainClock.advanceTimeBy(2_000)
        assertTrue("Still shown once hidden: ${shown()}", shown() < 0.02f)
    }
}
