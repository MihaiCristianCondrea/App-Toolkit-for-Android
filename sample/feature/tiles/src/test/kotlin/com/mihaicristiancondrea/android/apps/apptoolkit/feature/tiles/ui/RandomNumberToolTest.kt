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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RandomNumberToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.RandomNumberTool
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.text.NumberFormat
import java.util.Locale

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "en-rUS-w411dp-h891dp")
class RandomNumberToolTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun animatedResultKeepsTheCompleteValueThroughRapidChangesAndReset() {
        val state = mutableStateOf(RandomNumberToolUiState(result = 999))
        compose.setContent {
            MaterialTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Box(Modifier.width(280.dp)) {
                        RandomNumberTool(state.value, onEvent = {})
                    }
                }
            }
        }
        compose.onNodeWithText("999").assertIsDisplayed()
        compose.mainClock.autoAdvance = false
        compose.runOnIdle { state.value = state.value.copy(result = 1000) }
        compose.mainClock.advanceTimeBy(64)
        compose.runOnIdle { state.value = state.value.copy(result = Long.MIN_VALUE) }
        compose.mainClock.advanceTimeBy(1000)
        compose.mainClock.autoAdvance = true
        compose.onNodeWithText(NumberFormat.getIntegerInstance(Locale.US).format(Long.MIN_VALUE)).assertIsDisplayed()
        compose.runOnIdle { state.value = state.value.copy(result = null) }
        compose.onNodeWithText("…").assertIsDisplayed()
    }
}
