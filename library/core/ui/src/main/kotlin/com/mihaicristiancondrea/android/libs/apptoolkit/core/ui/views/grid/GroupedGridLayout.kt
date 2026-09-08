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

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/** Marks the cell that carries the ad rather than one of the caller's items. */
internal const val GROUPED_GRID_AD_INDEX: Int = -1

/** Row key of the ad row, stable across the ad loading, failing, and being re-requested. */
internal const val GROUPED_GRID_AD_ROW_KEY: String = "grouped-grid-ad"

/** Whether an ad row takes part in the layout, and whether it currently occupies any space. */
internal enum class GroupedGridAdRow {
    /** No ad was requested, or the grid is too small to carry one. */
    None,

    /**
     * An ad was requested but none is on screen. The row stays in the plan so the slot keeps its
     * place in composition and can finish loading, but it takes no space and the corners of the
     * group are cut as if it were not there.
     */
    Collapsed,

    /** An ad is on screen and is one more row of the group. */
    Visible,
}

/** Which of a cell's four corners sit at the outside of the group. */
@Immutable
internal data class GroupedGridCorners(
    val topStart: Boolean,
    val topEnd: Boolean,
    val bottomStart: Boolean,
    val bottomEnd: Boolean,
)

/** One cell of a planned row: the item it draws, and how its corners are cut. */
@Immutable
internal data class GroupedGridCell(
    val itemIndex: Int,
    val corners: GroupedGridCorners,
) {
    val isAd: Boolean get() = itemIndex == GROUPED_GRID_AD_INDEX
}

/** One planned row. A [collapsed] row is composed but draws nothing and takes no space. */
@Immutable
internal data class GroupedGridRow(
    val key: String,
    val cells: ImmutableList<GroupedGridCell>,
    val collapsed: Boolean,
)

/**
 * Plans the rows of a grid, and which corners of which cells are cut at the group's outer radius.
 *
 * The rules the grid is drawn by live here rather than in the composable so they can be read and
 * tested without a device:
 * - Items fill rows of [columns] in order. A last row that is short shares its width between the
 *   cells it does have, so a lone leftover item spans the row.
 * - The ad row, when there is one, sits in the middle of the block and spans the full width: it
 *   splits the rows of cells in half, keeping the larger half above it when there is an odd number
 *   of them. A single row of cells has no middle, so the ad goes under it. The ad is only planned
 *   from [GroupedGridDefaults.MinItemsForAd] items up.
 * - A corner is cut at the outer radius when it is at the outside of the group: the top row's
 *   leading and trailing corners, and the last row's. Every other corner is an inner one. A grid of
 *   one item is its own top and last row, so all four of its corners are outer ones.
 * - A [GroupedGridAdRow.Collapsed] ad row is skipped when deciding which row is last, so a grid
 *   whose ad never arrives is cut exactly like a grid that never asked for one.
 */
internal fun groupedGridRows(
    itemCount: Int,
    columns: Int,
    adRow: GroupedGridAdRow,
): ImmutableList<GroupedGridRow> {
    if (itemCount <= 0 || columns <= 0) return persistentListOf()

    val plannedRows: List<PlannedRow> = buildList {
        val itemRows: List<List<Int>> = (0 until itemCount).chunked(size = columns)
        val carriesAd: Boolean =
            adRow != GroupedGridAdRow.None && itemCount >= GroupedGridDefaults.MinItemsForAd
        // Rounded up, so a single row of cells puts the ad underneath rather than on top of itself.
        val rowsAboveAd: Int = (itemRows.size + 1) / 2

        itemRows.forEachIndexed { rowIndex, indices ->
            add(
                PlannedRow(
                    key = "grouped-grid-row-${indices.first()}",
                    indices = indices,
                    collapsed = false,
                )
            )
            if (rowIndex == rowsAboveAd - 1 && carriesAd) {
                add(
                    PlannedRow(
                        key = GROUPED_GRID_AD_ROW_KEY,
                        indices = listOf(GROUPED_GRID_AD_INDEX),
                        collapsed = adRow == GroupedGridAdRow.Collapsed,
                    )
                )
            }
        }
    }

    val lastVisibleRow: Int = plannedRows.indexOfLast { !it.collapsed }

    return plannedRows.mapIndexed { rowIndex, row ->
        val isTopRow: Boolean = rowIndex == 0
        val isLastRow: Boolean = rowIndex == lastVisibleRow

        GroupedGridRow(
            key = row.key,
            collapsed = row.collapsed,
            cells = row.indices.mapIndexed { cellIndex, itemIndex ->
                val isStartCell: Boolean = cellIndex == 0
                val isEndCell: Boolean = cellIndex == row.indices.lastIndex

                GroupedGridCell(
                    itemIndex = itemIndex,
                    corners = GroupedGridCorners(
                        topStart = isTopRow && isStartCell,
                        topEnd = isTopRow && isEndCell,
                        bottomStart = isLastRow && isStartCell,
                        bottomEnd = isLastRow && isEndCell,
                    ),
                )
            }.toImmutableList(),
        )
    }.toImmutableList()
}

/** Resolves the four flags into the shape a cell is clipped with. */
internal fun GroupedGridCorners.cornerShape(outerRadius: Dp, innerRadius: Dp): RoundedCornerShape =
    RoundedCornerShape(
        topStart = if (topStart) outerRadius else innerRadius,
        topEnd = if (topEnd) outerRadius else innerRadius,
        bottomStart = if (bottomStart) outerRadius else innerRadius,
        bottomEnd = if (bottomEnd) outerRadius else innerRadius,
    )

private data class PlannedRow(
    val key: String,
    val indices: List<Int>,
    val collapsed: Boolean,
)
