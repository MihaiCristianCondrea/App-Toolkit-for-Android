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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.grid

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GroupedGridLayoutTest {

    @Test
    fun `a single cell is its own group and rounds all four corners`() {
        val rows = groupedGridRows(itemCount = 1, columns = 2, adRow = GroupedGridAdRow.Visible)

        assertEquals(1, rows.size)
        val corners = rows.single().cells.single().corners
        assertEquals(GroupedGridCorners(true, true, true, true), corners)
    }

    @Test
    fun `one cell is too small to carry an ad`() {
        val rows = groupedGridRows(itemCount = 1, columns = 2, adRow = GroupedGridAdRow.Visible)

        assertTrue(rows.none { row -> row.cells.any { it.isAd } })
    }

    @Test
    fun `a single row of cells has no middle, so the ad goes under it`() {
        val rows = groupedGridRows(itemCount = 2, columns = 2, adRow = GroupedGridAdRow.Visible)

        assertEquals(2, rows.size)
        val adRow = rows[1]
        assertEquals(GROUPED_GRID_AD_ROW_KEY, adRow.key)
        assertTrue(adRow.cells.single().isAd)
        // Last row of the group, so the ad carries its bottom corners and the cells above do not.
        assertEquals(GroupedGridCorners(false, false, true, true), adRow.cells.single().corners)
        assertEquals(GroupedGridCorners(true, false, false, false), rows[0].cells[0].corners)
        assertEquals(GroupedGridCorners(false, true, false, false), rows[0].cells[1].corners)
    }

    @Test
    fun `an ad that never loads leaves the group cut as if it was never asked for`() {
        val withCollapsedAd =
            groupedGridRows(itemCount = 2, columns = 2, adRow = GroupedGridAdRow.Collapsed)
        val withoutAd = groupedGridRows(itemCount = 2, columns = 2, adRow = GroupedGridAdRow.None)

        assertEquals(
            withoutAd.map { it.cells },
            withCollapsedAd.filterNot { row -> row.cells.any { it.isAd } }.map { it.cells },
        )
        assertTrue(withCollapsedAd.single { row -> row.cells.any { it.isAd } }.collapsed)
    }

    @Test
    fun `an ad between rows of cells carries no outer corner`() {
        val rows = groupedGridRows(itemCount = 5, columns = 2, adRow = GroupedGridAdRow.Visible)

        // Two rows of cells, the ad, then the leftover one.
        assertEquals(listOf(2, 2, 1, 1), rows.map { it.cells.size })
        assertEquals(GROUPED_GRID_AD_ROW_KEY, rows[2].key)
        assertEquals(
            GroupedGridCorners(false, false, false, false),
            rows[2].cells.single().corners,
        )
        assertEquals(GroupedGridCorners(false, false, true, true), rows[3].cells.single().corners)
    }

    @Test
    fun `the ad splits the rows of cells in half`() {
        // Four rows of cells: two above the ad, two below.
        val even = groupedGridRows(itemCount = 8, columns = 2, adRow = GroupedGridAdRow.Visible)
        assertEquals(2, even.indexOfFirst { it.key == GROUPED_GRID_AD_ROW_KEY })
        assertEquals(5, even.size)

        // The quick-action case: seven cells over four rows, the ad in the middle of them.
        val quickActions =
            groupedGridRows(itemCount = 7, columns = 2, adRow = GroupedGridAdRow.Visible)
        assertEquals(listOf(2, 2, 1, 2, 1), quickActions.map { it.cells.size })
        assertEquals(2, quickActions.indexOfFirst { it.key == GROUPED_GRID_AD_ROW_KEY })

        // Three rows do not divide evenly, so the larger half stays above the ad.
        val odd = groupedGridRows(itemCount = 6, columns = 2, adRow = GroupedGridAdRow.Visible)
        assertEquals(2, odd.indexOfFirst { it.key == GROUPED_GRID_AD_ROW_KEY })
    }

    @Test
    fun `a short last row shares its width and rounds both of its outer corners`() {
        val rows = groupedGridRows(itemCount = 3, columns = 2, adRow = GroupedGridAdRow.None)

        assertEquals(listOf(2, 1), rows.map { it.cells.size })
        assertEquals(GroupedGridCorners(false, false, true, true), rows[1].cells.single().corners)
    }

    @Test
    fun `every planned cell points at an item the caller supplied`() {
        val rows = groupedGridRows(itemCount = 7, columns = 3, adRow = GroupedGridAdRow.Visible)
        val itemIndices = rows.flatMap { row -> row.cells.filterNot { it.isAd }.map { it.itemIndex } }

        assertEquals((0 until 7).toList(), itemIndices)
        assertFalse(rows.any { it.collapsed })
    }

    @Test
    fun `an empty grid plans nothing`() {
        assertTrue(groupedGridRows(0, 2, GroupedGridAdRow.Visible).isEmpty())
        assertTrue(groupedGridRows(4, 0, GroupedGridAdRow.Visible).isEmpty())
    }
}
