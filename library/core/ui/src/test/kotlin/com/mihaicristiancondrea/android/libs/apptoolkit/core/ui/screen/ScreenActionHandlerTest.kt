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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.effects.rememberTapThreshold
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class ScreenActionHandlerTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `dispatches events and restores the tap count without replaying taps`() {
        val events = mutableListOf<String>()
        val taps = mutableListOf<Int>()
        var milestones = 0
        val restoration = StateRestorationTester(compose)

        restoration.setContent {
            val countTap = rememberTapThreshold(
                threshold = 5,
                onTap = { taps.add(it) },
                onThresholdReached = { milestones += 1 },
            )
            ScreenActionHandler<Action, String>(
                onEvent = { events.add(it) },
                handleAction = { action, dispatch ->
                    when (action) {
                        Action.Reload -> dispatch("reload")
                        Action.CountTap -> countTap()
                    }
                },
            ) { onAction ->
                Column {
                    Box(
                        modifier = Modifier.size(size = 48.dp).testTag("reload").clickable {
                            onAction(Action.Reload)
                        },
                    )
                    Box(
                        modifier = Modifier.size(size = 48.dp).testTag("count").clickable {
                            onAction(Action.CountTap)
                        },
                    )
                }
            }
        }

        compose.onNodeWithTag("reload").performClick()
        repeat(4) { compose.onNodeWithTag("count").performClick() }
        restoration.emulateSavedInstanceStateRestore()
        compose.runOnIdle {
            assertEquals(listOf(1, 2, 3, 4), taps)
            assertEquals(0, milestones)
        }

        repeat(6) { compose.onNodeWithTag("count").performClick() }
        compose.runOnIdle {
            assertEquals(listOf("reload"), events)
            assertEquals((1..10).toList(), taps)
            assertEquals(2, milestones)
        }
    }

    private enum class Action { Reload, CountTap }
}
