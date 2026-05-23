package com.mihaicristiancondrea.mediastudio.core.ui.views

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.RoundedCornerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Immutable
data class DeviceRoundedCorners(
    val topLeft: Int = 0,
    val topRight: Int = 0,
    val bottomRight: Int = 0,
    val bottomLeft: Int = 0,
)

@Composable
fun rememberDeviceRoundedCornerShape(): Shape {
    val view = LocalView.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    var corners by remember {
        mutableStateOf(DeviceRoundedCorners())
    }

    DisposableEffect(view) {
        fun updateCorners(insets: WindowInsetsCompat?) {
            corners = DeviceRoundedCorners(
                topLeft = insets.radiusOrZero(RoundedCornerCompat.POSITION_TOP_LEFT),
                topRight = insets.radiusOrZero(RoundedCornerCompat.POSITION_TOP_RIGHT),
                bottomRight = insets.radiusOrZero(RoundedCornerCompat.POSITION_BOTTOM_RIGHT),
                bottomLeft = insets.radiusOrZero(RoundedCornerCompat.POSITION_BOTTOM_LEFT),
            )
        }

        updateCorners(ViewCompat.getRootWindowInsets(view))

        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            updateCorners(insets)
            insets
        }

        ViewCompat.requestApplyInsets(view)

        onDispose {
            ViewCompat.setOnApplyWindowInsetsListener(view, null)
        }
    }

    return remember(corners, density, layoutDirection) {
        corners.toRoundedCornerShape(
            density = density,
            layoutDirection = layoutDirection,
        )
    }
}

private fun WindowInsetsCompat?.radiusOrZero(
    position: Int,
): Int {
    return this
            ?.getRoundedCorner(position)
            ?.radius
        ?: 0
}

private fun DeviceRoundedCorners.toRoundedCornerShape(
    density: Density,
    layoutDirection: LayoutDirection,
): RoundedCornerShape {
    fun Int.toDpCorner() = with(density) {
        this@toDpCorner.toDp()
    }

    return if (layoutDirection == LayoutDirection.Ltr) {
        RoundedCornerShape(
            topStart = topLeft.toDpCorner(),
            topEnd = topRight.toDpCorner(),
            bottomEnd = bottomRight.toDpCorner(),
            bottomStart = bottomLeft.toDpCorner(),
        )
    } else {
        RoundedCornerShape(
            topStart = topRight.toDpCorner(),
            topEnd = topLeft.toDpCorner(),
            bottomEnd = bottomLeft.toDpCorner(),
            bottomStart = bottomRight.toDpCorner(),
        )
    }
}