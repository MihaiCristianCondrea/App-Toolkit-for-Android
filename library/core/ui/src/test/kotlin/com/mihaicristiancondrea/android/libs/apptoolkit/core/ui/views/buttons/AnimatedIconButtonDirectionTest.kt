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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class AnimatedIconButtonDirectionTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private fun neighbourLeft(): Float = compose.onNodeWithTag("neighbour").fetchSemanticsNode().boundsInRoot.left

    @Test
    fun `the button beside one that leaves glides over instead of jumping`() {
        var visible by mutableStateOf(true)
        compose.mainClock.autoAdvance = false
        compose.setContent {
            MaterialTheme {
                Row {
                    AnimatedIconButtonDirection(
                        visible = visible,
                        icon = ToolkitIcon.Vector(Icons.Outlined.Search),
                        contentDescription = "Search",
                        onClick = {},
                    )
                    Box(Modifier.testTag("neighbour").size(48.dp))
                }
            }
        }
        compose.mainClock.advanceTimeBy(2_000)
        val start = neighbourLeft()
        assertTrue("The button took no room: $start", start > 0f)

        visible = false
        Snapshot.sendApplyNotifications()
        compose.mainClock.advanceTimeBy(120)
        val midway = neighbourLeft()
        assertTrue("The neighbour jumped or stood still: $start -> $midway", midway > 0f && midway < start)

        compose.mainClock.advanceTimeBy(2_000)
        assertEquals(0f, neighbourLeft())
    }
}
