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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.RoundedCornerCompat
import androidx.core.view.ViewCompat

/**
 * The shape of the display's own rounded corners, or a rectangle where the device reports none
 * (below Android 12, or in a freeform window).
 *
 * Read from the root view's insets when the page is composed rather than through a listener, so
 * pages never replace a listener another part of the app installed.
 *
 * @param squareStart Leaves the start corners square, for a page that does not reach the start
 * edge of the screen: beside a rail or a drawer, its start corners meet the navigation, not the
 * display's corners, and rounding them would round the app bar next to the rail.
 */
@Composable
fun rememberDeviceCornerShape(squareStart: Boolean = false): Shape {
    val view = LocalView.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val insets = ViewCompat.getRootWindowInsets(view)
    fun radius(position: Int): Int = insets?.getRoundedCorner(position)?.radius ?: 0
    val ltr = layoutDirection == LayoutDirection.Ltr
    val topLeft = if (squareStart && ltr) 0 else radius(RoundedCornerCompat.POSITION_TOP_LEFT)
    val topRight = if (squareStart && !ltr) 0 else radius(RoundedCornerCompat.POSITION_TOP_RIGHT)
    val bottomRight = if (squareStart && !ltr) 0 else radius(RoundedCornerCompat.POSITION_BOTTOM_RIGHT)
    val bottomLeft = if (squareStart && ltr) 0 else radius(RoundedCornerCompat.POSITION_BOTTOM_LEFT)
    return remember(topLeft, topRight, bottomRight, bottomLeft, density, layoutDirection) {
        if (topLeft + topRight + bottomRight + bottomLeft == 0) {
            RectangleShape
        } else {
            with(density) {
                RoundedCornerShape(
                    topStart = (if (ltr) topLeft else topRight).toDp(),
                    topEnd = (if (ltr) topRight else topLeft).toDp(),
                    bottomEnd = (if (ltr) bottomRight else bottomLeft).toDp(),
                    bottomStart = (if (ltr) bottomLeft else bottomRight).toDp(),
                )
            }
        }
    }
}
