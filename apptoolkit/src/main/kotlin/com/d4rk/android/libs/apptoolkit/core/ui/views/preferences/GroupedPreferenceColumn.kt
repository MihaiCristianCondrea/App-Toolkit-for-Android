package com.d4rk.android.libs.apptoolkit.core.ui.views.preferences

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * Position of an item inside a grouped list.
 */
enum class GroupedItemPosition {
    FIRST, MIDDLE, LAST, SINGLE
}

/**
 * Returns the item position for an indexed list row.
 */
fun groupedItemPosition(index: Int, size: Int): GroupedItemPosition {
    return when {
        size <= 1 -> GroupedItemPosition.SINGLE
        index == 0 -> GroupedItemPosition.FIRST
        index == size - 1 -> GroupedItemPosition.LAST
        else -> GroupedItemPosition.MIDDLE
    }
}

/**
 * Clips an item with adaptive grouped-corner radii based on [position].
 *
 * Modifier ordering matters: apply this modifier before drawing modifiers like `background`
 * so the drawn content respects the rounded clipping.
 */
fun Modifier.groupedCorners(
    position: GroupedItemPosition,
    outerRadius: Dp = 16.dp,
    innerRadius: Dp = 2.dp,
): Modifier {
    val shape = when (position) {
        GroupedItemPosition.FIRST -> RoundedCornerShape(
            topStart = outerRadius,
            topEnd = outerRadius,
            bottomStart = innerRadius,
            bottomEnd = innerRadius,
        )

        GroupedItemPosition.MIDDLE -> RoundedCornerShape(innerRadius)
        GroupedItemPosition.LAST -> RoundedCornerShape(
            topStart = innerRadius,
            topEnd = innerRadius,
            bottomStart = outerRadius,
            bottomEnd = outerRadius,
        )

        GroupedItemPosition.SINGLE -> RoundedCornerShape(outerRadius)
    }
    return this.clip(shape)
}

/**
 * Applies standard grouped-list item spacing and grouped corners in one call.
 */
fun Modifier.groupedPreferenceItem(
    position: GroupedItemPosition,
    horizontalPadding: Dp = SizeConstants.LargeSize,
    singleItemVerticalPadding: Dp = SizeConstants.ExtraTinySize,
): Modifier {
    val verticalPadding = if (position == GroupedItemPosition.SINGLE) {
        singleItemVerticalPadding
    } else {
        SizeConstants.ZeroSize
    }

    return this
        .padding(
            start = horizontalPadding,
            end = horizontalPadding,
            top = verticalPadding,
            bottom = verticalPadding,
        )
        .groupedCorners(position = position)
}
