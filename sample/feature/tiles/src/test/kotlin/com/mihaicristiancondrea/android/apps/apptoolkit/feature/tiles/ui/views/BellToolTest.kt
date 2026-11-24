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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.BellToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.BellTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.bellAngle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.LocalShellMotion
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.rememberShellMotion
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "en-rUS-w411dp-h891dp")
class BellToolTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun strikeCompletesUnderTestFrameClockAndAnnouncesViaDedicatedLiveRegion() {
        val state = mutableStateOf(BellToolUiState(audioReady = true))
        compose.setContent {
            MaterialTheme {
                val motion = rememberShellMotion(
                    tabStyle = TabTransitionStyle.Directional,
                    durationScale = 1f,
                )
                CompositionLocalProvider(LocalShellMotion provides motion) {
                    BellTool(state = state.value, onRing = {})
                }
            }
        }

        compose.onNodeWithText("Ring bell")
            .assertIsDisplayed()
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.LiveRegion))

        compose.mainClock.autoAdvance = false
        compose.runOnIdle { state.value = state.value.copy(strike = 1) }
        compose.mainClock.advanceTimeBy(100L)

        compose.onNodeWithContentDescription("Ringing")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite))

        compose.mainClock.advanceTimeBy(2_000L)
        compose.mainClock.autoAdvance = true
        compose.waitForIdle()
        compose.onNodeWithContentDescription("Ringing").assertDoesNotExist()
    }

    @Test
    fun reducedMotionStrikeCompletesAndClearsRingingAnnouncement() {
        val state = mutableStateOf(BellToolUiState(audioReady = true))
        compose.setContent {
            MaterialTheme {
                val motion = rememberShellMotion(
                    tabStyle = TabTransitionStyle.Directional,
                    durationScale = 0f,
                )
                CompositionLocalProvider(LocalShellMotion provides motion) {
                    BellTool(state = state.value, onRing = {})
                }
            }
        }

        compose.mainClock.autoAdvance = false
        compose.runOnIdle { state.value = state.value.copy(strike = 1) }
        compose.mainClock.advanceTimeBy(100L)

        compose.onNodeWithContentDescription("Ringing")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite))

        compose.mainClock.advanceTimeBy(2_000L)
        compose.mainClock.autoAdvance = true
        compose.waitForIdle()
        compose.onNodeWithContentDescription("Ringing").assertDoesNotExist()
    }

    @Test
    fun audioFailureDisablesButtonAndMarksErrorTextAsPoliteLiveRegion() {
        compose.setContent {
            MaterialTheme {
                val motion = rememberShellMotion(
                    tabStyle = TabTransitionStyle.Directional,
                    durationScale = 1f,
                )
                CompositionLocalProvider(LocalShellMotion provides motion) {
                    BellTool(
                        state = BellToolUiState(audioReady = false, audioFailed = true),
                        onRing = {},
                    )
                }
            }
        }

        compose.onNodeWithText("Ring bell").assertIsNotEnabled()
        compose.onNodeWithText("Bell audio is unavailable.")
            .assertIsDisplayed()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite))
    }

    @Test
    fun bellAngleBlendsFromCurrentAngleWithoutSnappingToZero() {
        assertEquals(15f, bellAngle(elapsed = 0f, direction = -1, startAngle = 15f), 0.001f)
        assertEquals(0f, bellAngle(elapsed = 0f, direction = 1, startAngle = 0f), 0.001f)
        assertEquals(0f, bellAngle(elapsed = 1_500f, direction = 1, startAngle = 15f), 0.001f)
    }
}
