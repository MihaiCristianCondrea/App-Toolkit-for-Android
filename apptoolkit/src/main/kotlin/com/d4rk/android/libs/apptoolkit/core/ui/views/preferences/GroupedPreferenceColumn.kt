package com.d4rk.android.libs.apptoolkit.core.ui.views.preferences

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Position of an item inside a grouped list.
 */
enum class GroupedItemPosition {
    FIRST, MIDDLE, LAST, SINGLE
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
