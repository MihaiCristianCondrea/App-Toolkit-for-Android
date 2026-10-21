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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTopPositionInRootIsEqualTo
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** An entering item lays its children out as a plain lazy item does, one under the other. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class AnimatedItemsTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `an item with several children stacks them`() {
        compose.setContent {
            LazyColumn {
                animatedItem {
                    Box(Modifier.size(width = 40.dp, height = 24.dp).testTag("first"))
                    Box(Modifier.size(width = 60.dp, height = 16.dp).testTag("second"))
                }
                animatedItems(items = listOf(0)) {
                    Box(Modifier.size(10.dp).testTag("next"))
                }
            }
        }

        compose.onNodeWithTag("first").assertTopPositionInRootIsEqualTo(0.dp)
        compose.onNodeWithTag("second").assertTopPositionInRootIsEqualTo(24.dp)
        compose.onNodeWithTag("next").assertTopPositionInRootIsEqualTo(40.dp)
        assertEquals(16.dp, compose.onNodeWithTag("second").getUnclippedBoundsInRoot().let { it.bottom - it.top })
    }
}
