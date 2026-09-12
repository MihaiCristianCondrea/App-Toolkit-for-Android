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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.R
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class BundledAnimatedIconsTest(private val resource: Int, private val mode: ToolkitIconReplayMode) {
    @get:Rule val rule = createComposeRule()

    @Test fun bundledAnimationInflatesAndMovesOnRepeatedClicks() {
        val clicks = mutableIntStateOf(0)
        rule.setContent {
            AnimatedToolkitIcon(
                icon = ToolkitIcon.AnimatedVector(resource, replayMode = mode),
                clickCount = clicks.intValue, contentDescription = "Animation",
                modifier = Modifier.size(64.dp).background(Color.White).testTag("animation"),
                tint = Color.Black,
            )
        }
        rule.waitForIdle()
        rule.mainClock.autoAdvance = false
        val node = rule.onNodeWithTag("animation")
        repeat(3) {
            val resting = node.captureToImage().asAndroidBitmap()
            rule.runOnIdle { clicks.intValue++ }
            var changed = false
            // Include the delayed clock morph as well as the short grid/check animations.
            for (step in listOf(160L, 340L, 400L)) {
                rule.mainClock.advanceTimeBy(step)
                val frame = node.captureToImage().asAndroidBitmap()
                changed = changed || !resting.sameAs(frame)
            }
            assertTrue("Resource $resource must animate on click ${it + 1} in $mode mode", changed)
            rule.mainClock.advanceTimeBy(2500)
        }
    }

    companion object {
        @JvmStatic @Parameterized.Parameters(name = "resource={0}, mode={1}")
        fun cases(): List<Array<Any>> = listOf(
            R.drawable.anim_check, R.drawable.anim_grid, R.drawable.anim_grid_select,
            R.drawable.anim_settings, R.drawable.anim_share,
        ).flatMap { resource -> ToolkitIconReplayMode.entries.map { arrayOf(resource, it) } }
    }
}
