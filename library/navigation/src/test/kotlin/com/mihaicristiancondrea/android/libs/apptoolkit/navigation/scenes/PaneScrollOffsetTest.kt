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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PaneScrollOffsetTest {

    @Test
    fun `scrolling content down marks scrolledUnder and pulling past top resets to zero`() {
        val scrollOffset = PaneScrollOffset()
        assertFalse(scrollOffset.scrolledUnder)

        scrollOffset.onPostScroll(
            consumed = Offset(x = 0f, y = -12f),
            available = Offset.Zero,
            source = NestedScrollSource.UserInput,
        )
        assertTrue(scrollOffset.scrolledUnder)
        assertEquals(-12f, scrollOffset.offset)

        scrollOffset.onPostScroll(
            consumed = Offset.Zero,
            available = Offset(x = 0f, y = 4f),
            source = NestedScrollSource.UserInput,
        )
        assertFalse(scrollOffset.scrolledUnder)
        assertEquals(0f, scrollOffset.offset)
    }
}
