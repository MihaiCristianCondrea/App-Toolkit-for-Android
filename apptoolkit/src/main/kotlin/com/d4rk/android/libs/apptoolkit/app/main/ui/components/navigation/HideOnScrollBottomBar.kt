package com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HideOnScrollBottomBar(
    scrollBehavior: BottomAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)

                val fullHeight = placeable.height
                val limit = -fullHeight.toFloat()
                if (scrollBehavior.state.heightOffsetLimit != limit) {
                    scrollBehavior.state.heightOffsetLimit = limit
                }
                val visibleHeight = (fullHeight + scrollBehavior.state.heightOffset)
                    .roundToInt()
                    .coerceIn(0, fullHeight)

                layout(placeable.width, visibleHeight) {
                    placeable.placeRelative(0, 0)
                }
            },
        content = content
    )
}
