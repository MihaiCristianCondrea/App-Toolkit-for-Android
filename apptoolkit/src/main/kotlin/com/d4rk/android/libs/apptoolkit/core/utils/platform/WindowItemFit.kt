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

package com.d4rk.android.libs.apptoolkit.core.utils.platform

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

object WindowItemFit {
    @Composable
    fun count(
        itemHeight: Dp,
        itemSpacing: Dp = SizeConstants.ZeroSize,
        paddingValues: PaddingValues = PaddingValues(all = SizeConstants.ZeroSize)
    ): Int {
        val windowInfo = LocalWindowInfo.current
        val density = LocalDensity.current
        val containerHeightPx = windowInfo.containerSize.height.toFloat()

        val contentPaddingTopPx =
            with(receiver = density) { paddingValues.calculateTopPadding().toPx() }
        val contentPaddingBottomPx =
            with(receiver = density) { paddingValues.calculateBottomPadding().toPx() }
        val totalVerticalContentPaddingPx = contentPaddingTopPx + contentPaddingBottomPx

        val itemHeightPx = with(receiver = density) { itemHeight.toPx() }
        val itemSpacingPx = with(receiver = density) { itemSpacing.toPx() }

        val availableHeightForItemsAndSpacingPx = containerHeightPx - totalVerticalContentPaddingPx

        if (availableHeightForItemsAndSpacingPx < itemHeightPx) {
            return 0
        }

        val singleRowEffectiveHeightPx = itemHeightPx + itemSpacingPx

        if (singleRowEffectiveHeightPx <= 0f) {
            return if (itemHeightPx > 0f && availableHeightForItemsAndSpacingPx > 0f) Int.MAX_VALUE else 0
        }

        val rowCount =
            ((availableHeightForItemsAndSpacingPx + itemSpacingPx) / singleRowEffectiveHeightPx).toInt()

        return rowCount.coerceAtLeast(minimumValue = 0)
    }
}
