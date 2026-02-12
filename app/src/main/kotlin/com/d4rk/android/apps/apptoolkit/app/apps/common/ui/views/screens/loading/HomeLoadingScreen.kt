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

package com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views.screens.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.platform.WindowItemFit

@Composable
fun HomeLoadingScreen(
    paddingValues: PaddingValues,
    windowWidthSizeClass: AppWindowWidthSizeClass,
    itemAspectRatio: Float = 1f,
) {
    val numberOfColumns: Int by remember(windowWidthSizeClass) {
        derivedStateOf {
            when (windowWidthSizeClass) {
                AppWindowWidthSizeClass.Compact -> 2
                AppWindowWidthSizeClass.Medium -> 3
                AppWindowWidthSizeClass.Expanded -> 4
                AppWindowWidthSizeClass.Large -> 5
                AppWindowWidthSizeClass.ExtraLarge -> 6
            }
        }
    }

    val fittedRows: Int = WindowItemFit.count(
        itemHeight = SizeConstants.OneEightySize,
        itemSpacing = SizeConstants.LargeSize,
        paddingValues = paddingValues
    )

    val totalRowsToDisplay: Int by remember(fittedRows) {
        derivedStateOf { if (fittedRows == 0) 1 else fittedRows + 1 }
    }
    val actualItemCount: Int by remember(totalRowsToDisplay, numberOfColumns) {
        derivedStateOf { totalRowsToDisplay * numberOfColumns }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = numberOfColumns),
        contentPadding = paddingValues,
        horizontalArrangement = Arrangement.spacedBy(space = SizeConstants.LargeSize),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.LargeSize),
        modifier = Modifier.padding(horizontal = SizeConstants.LargeSize),
        userScrollEnabled = false
    ) {
        items(
            count = actualItemCount,
            key = { index: Int -> index }
        ) {
            ShimmerPlaceholderAppCard(aspectRatio = itemAspectRatio)
        }
    }
}
