package com.d4rk.android.libs.apptoolkit.core.ui.views.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * Position of a row inside a grouped preference container.
 */
enum class GroupedItemPosition {
    FIRST, MIDDLE, LAST, SINGLE
}

/**
 * Provides the adaptive shape used by grouped preference rows.
 */
fun groupedItemShape(
    position: GroupedItemPosition,
    outerRadiusDp: Int = 16,
    innerRadiusDp: Int = 2,
): Shape {
    val outer = outerRadiusDp.dp
    val inner = innerRadiusDp.dp
    return when (position) {
        GroupedItemPosition.FIRST -> RoundedCornerShape(
            topStart = outer,
            topEnd = outer,
            bottomStart = inner,
            bottomEnd = inner,
        )

        GroupedItemPosition.MIDDLE -> RoundedCornerShape(inner)
        GroupedItemPosition.LAST -> RoundedCornerShape(
            topStart = inner,
            topEnd = inner,
            bottomStart = outer,
            bottomEnd = outer,
        )

        GroupedItemPosition.SINGLE -> RoundedCornerShape(outer)
    }
}

/**
 * Shared grouped container used by settings-style lists.
 */
@Composable
fun GroupedPreferenceColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = SizeConstants.LargeSize)
            .background(shape = RoundedCornerShape(SizeConstants.ExtraLargeSize), color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHigh),
        content = content,
    )
}
