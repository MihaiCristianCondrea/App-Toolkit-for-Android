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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CoinFlipToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CounterToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.DiceRollToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RandomNumberToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.WheelToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.LocalAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w430dp-h2200dp")
class OverviewAdPlacementTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    @Test fun measureAdSeparatesConversionFromCounting() {
        render { measure() }
        val convert = compose.onNodeWithText(compose.activity.getString(R.string.section_convert)).fetchSemanticsNode().boundsInRoot
        val count = compose.onNodeWithText(compose.activity.getString(R.string.section_count)).fetchSemanticsNode().boundsInRoot
        val ad = compose.onNodeWithTag("overview-ad").fetchSemanticsNode().boundsInRoot
        assertTrue(ad.top > convert.bottom)
        assertTrue(ad.bottom < count.top)
    }

    @Test fun playAdFollowsTheChanceTools() {
        render { play() }
        val dice = compose.onNodeWithText(compose.activity.getString(R.string.widget_dice_value)).fetchSemanticsNode().boundsInRoot
        val ad = compose.onNodeWithTag("overview-ad").fetchSemanticsNode().boundsInRoot
        assertTrue(ad.top > dice.bottom)
    }

    @Test fun measureSearchHidesTheAd() {
        render { measure("unit") }
        compose.onNodeWithTag("overview-ad").assertDoesNotExist()
    }

    @Test fun playSearchHidesTheAd() {
        render { play("dice") }
        compose.onNodeWithTag("overview-ad").assertDoesNotExist()
    }

    private fun render(content: @Composable () -> Unit) {
        compose.setContent {
            MaterialTheme {
                CompositionLocalProvider(LocalAdsEnabled provides false, LocalTelemetry provides FakeTelemetryRepository()) {
                    Surface { content() }
                }
            }
        }
        compose.waitForIdle()
    }

    @Composable private fun measure(query: String = "") {
        NumbersScreenContent(CounterToolUiState(), PaddingValues(), searchQuery = query,
            overviewAd = { Text("Sponsored content", Modifier.testTag("overview-ad")) })
    }

    @Composable private fun play(query: String = "") {
        PlayScreenContent(CoinFlipToolUiState(), DiceRollToolUiState(), RandomNumberToolUiState(), WheelToolUiState(), PaddingValues(), searchQuery = query,
            overviewAd = { Text("Sponsored content", Modifier.testTag("overview-ad")) })
    }
}
