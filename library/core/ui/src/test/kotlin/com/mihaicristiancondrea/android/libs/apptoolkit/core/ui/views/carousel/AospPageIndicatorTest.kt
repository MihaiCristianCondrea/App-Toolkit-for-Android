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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.carousel

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36])
class AospPageIndicatorTest {

    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `rapid target page changes settle on the latest selection`() {
        var selectedIndex by mutableIntStateOf(0)

        compose.setContent {
            MaterialTheme {
                AospPageIndicator(
                    pageCount = 5,
                    selectedIndex = selectedIndex,
                    modifier = Modifier.testTag("indicator"),
                )
            }
        }

        compose.mainClock.autoAdvance = false

        selectedIndex = 1
        compose.mainClock.advanceTimeBy(60)
        selectedIndex = 2
        compose.mainClock.advanceTimeBy(60)
        selectedIndex = 4
        compose.mainClock.advanceTimeBy(60)
        selectedIndex = 1

        compose.mainClock.autoAdvance = true
        compose.waitForIdle()

        val stateDesc = compose.onNodeWithTag("indicator")
            .fetchSemanticsNode()
            .config
            .getOrNull(SemanticsProperties.StateDescription)

        assertEquals("Page 2 of 5", stateDesc)
    }

    @Test
    fun `single page indicator does not render`() {
        compose.setContent {
            MaterialTheme {
                AospPageIndicator(
                    pageCount = 1,
                    selectedIndex = 0,
                    modifier = Modifier.testTag("indicator"),
                )
            }
        }

        compose.onNodeWithTag("indicator").assertDoesNotExist()
    }
}
