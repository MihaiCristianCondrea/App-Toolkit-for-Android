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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class PreferenceItemDefaultsTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `a single row takes the outer shape on every corner`() {
        lateinit var shapes: ListItemShapes
        lateinit var outer: CornerBasedShape
        compose.setContent {
            MaterialTheme {
                outer = MaterialTheme.shapes.extraLarge
                shapes = PreferenceItemDefaults.segmentedShapes(index = 0, count = 1, outerShape = outer)
            }
        }

        compose.runOnIdle { assertEquals(outer, shapes.shape) }
    }

    @Test
    fun `the first, middle and last rows round only the group's outer edge`() {
        lateinit var first: ListItemShapes
        lateinit var middle: ListItemShapes
        lateinit var last: ListItemShapes
        lateinit var outer: CornerBasedShape
        lateinit var inner: CornerBasedShape
        compose.setContent {
            MaterialTheme {
                outer = MaterialTheme.shapes.largeIncreased
                inner = MaterialTheme.shapes.extraSmall
                first = PreferenceItemDefaults.segmentedShapes(index = 0, count = 3)
                middle = PreferenceItemDefaults.segmentedShapes(index = 1, count = 3)
                last = PreferenceItemDefaults.segmentedShapes(index = 2, count = 3)
            }
        }

        compose.runOnIdle {
            assertEquals(inner.copy(topStart = outer.topStart, topEnd = outer.topEnd), first.shape)
            assertEquals(inner, middle.shape)
            assertEquals(
                inner.copy(bottomStart = outer.bottomStart, bottomEnd = outer.bottomEnd),
                last.shape,
            )
        }
    }

    @Test
    fun `a selected row takes the outer shape and other states keep the resting shape`() {
        lateinit var shapes: ListItemShapes
        lateinit var outer: CornerBasedShape
        compose.setContent {
            MaterialTheme {
                outer = MaterialTheme.shapes.largeIncreased
                shapes = PreferenceItemDefaults.segmentedShapes(index = 1, count = 3)
            }
        }

        compose.runOnIdle {
            assertEquals(outer, shapes.selectedShape)
            listOf(
                shapes.pressedShape,
                shapes.focusedShape,
                shapes.hoveredShape,
                shapes.draggedShape,
            ).forEach { stateShape ->
                assertEquals(shapes.shape, stateShape)
            }
        }
    }
}
