package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object WindowItemFit {
    @Composable
    fun count(
        itemHeight: Dp,
        itemSpacing: Dp = 0.dp,
        paddingValues: PaddingValues = PaddingValues(all = 0.dp)
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