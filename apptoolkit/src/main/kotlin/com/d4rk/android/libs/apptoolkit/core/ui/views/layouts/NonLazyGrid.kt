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

package com.d4rk.android.libs.apptoolkit.core.ui.views.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * Simple grid that measures and places all items without lazy behaviour.
 *
 * The grid arranges [itemCount] items into the given number of [columns] and
 * invokes [content] for each index. Useful for small grids where the overhead
 * of a `Lazy` component is unnecessary.
 *
 * @param columns Number of columns in the grid.
 * @param itemCount Total number of items to show.
 * @param modifier Modifier applied to the container.
 * @param content Composable lambda called for each item index.
 */
@Composable
fun NonLazyGrid(
    columns: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit,
) {
    Column(modifier = modifier) {
        val rows: Int = (itemCount + columns - 1) / columns

        (0 until rows).forEach { row ->
            Row {
                (0 until columns).forEachIndexed { col: Int, _: Int ->
                    val index: Int = row * columns + col
                    if (index < itemCount) {
                        Box(
                            modifier = Modifier
                                .weight(weight = 1f)
                                .padding(all = SizeConstants.SmallSize)
                                .aspectRatio(ratio = 1f)
                        ) {
                            content(index)
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(weight = 1f))
                    }
                }
            }
        }
    }
}