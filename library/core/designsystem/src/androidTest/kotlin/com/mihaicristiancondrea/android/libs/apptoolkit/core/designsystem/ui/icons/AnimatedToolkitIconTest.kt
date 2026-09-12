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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.test.R
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class AnimatedToolkitIconTest {
    @get:Rule val rule = createComposeRule()

    @Test
    fun lottieReplaysOnEveryClick() = assertReplay(
        ToolkitIcon.Lottie(R.raw.toolkit_add_icon, tintable = true),
    )

    @Test
    fun animatedVectorReplaysOnEveryClick() = assertReplay(
        ToolkitIcon.AnimatedVector(R.drawable.test_animated_icon),
    )

    private fun assertReplay(icon: ToolkitIcon) {
        val clicks = mutableIntStateOf(0)
        val context = InstrumentationRegistry.getInstrumentation().context
        rule.setContent {
            CompositionLocalProvider(LocalContext provides context) {
                AnimatedToolkitIcon(
                    icon = icon,
                    clickCount = clicks.intValue,
                    contentDescription = "Replay icon",
                    modifier = Modifier.size(64.dp).background(Color.White).testTag("icon"),
                    tint = Color.Black,
                )
            }
        }
        val node = rule.onNodeWithTag("icon")
        rule.waitUntil(10_000) {
            val bitmap = node.captureToImage().asAndroidBitmap()
            bitmap.getPixel(bitmap.width / 2, bitmap.height / 2) != android.graphics.Color.WHITE
        }
        rule.mainClock.autoAdvance = false
        repeat(3) {
            val resting = node.captureToImage().asAndroidBitmap()
            rule.runOnIdle { clicks.intValue++ }
            rule.mainClock.advanceTimeBy(160)
            val playing = node.captureToImage().asAndroidBitmap()
            assertFalse("Click must produce intermediate frames", resting.sameAs(playing))
            rule.mainClock.advanceTimeBy(1000)
        }
    }
}
