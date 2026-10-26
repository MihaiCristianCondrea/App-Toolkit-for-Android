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

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AospPageIndicatorGeometryTest {

    @Test
    fun `position follows a forward swipe`() {
        val position = pageIndicatorPosition(
            currentPage = 0,
            offsetFraction = 0.4f,
            pageCount = 3,
        )

        assertEquals(0.4f, position, 0.0001f)
    }

    @Test
    fun `position remains continuous when current page changes`() {
        val before = pageIndicatorPosition(
            currentPage = 0,
            offsetFraction = 0.49f,
            pageCount = 3,
        )

        val after = pageIndicatorPosition(
            currentPage = 1,
            offsetFraction = -0.49f,
            pageCount = 3,
        )

        assertEquals(0.49f, before, 0.0001f)
        assertEquals(0.51f, after, 0.0001f)
    }

    @Test
    fun `position follows a backward swipe`() {
        val position = pageIndicatorPosition(
            currentPage = 2,
            offsetFraction = -0.4f,
            pageCount = 4,
        )

        assertEquals(1.6f, position, 0.0001f)
    }

    @Test
    fun `position cannot exceed last page`() {
        val position = pageIndicatorPosition(
            currentPage = 4,
            offsetFraction = 0.5f,
            pageCount = 5,
        )

        assertEquals(4f, position)
    }

    @Test
    fun `empty pager has safe position`() {
        assertEquals(
            0f,
            pageIndicatorPosition(0, 0f, 0),
        )
    }

    @Test
    fun `short carousel needs no moving window`() {
        assertEquals(
            0f,
            pageIndicatorWindowStart(
                position = 1f,
                pageCount = 3,
                visibleDots = 7,
            ),
        )
    }

    @Test
    fun `large carousel centers active page`() {
        assertEquals(
            1f,
            pageIndicatorWindowStart(
                position = 4f,
                pageCount = 12,
                visibleDots = 7,
            ),
        )
    }

    @Test
    fun `window cannot extend beyond final page`() {
        assertEquals(
            5f,
            pageIndicatorWindowStart(
                position = 11f,
                pageCount = 12,
                visibleDots = 7,
            ),
        )
    }
}
