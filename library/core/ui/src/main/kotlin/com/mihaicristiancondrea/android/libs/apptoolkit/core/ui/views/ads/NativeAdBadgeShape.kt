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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads

import android.graphics.Path
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.graphics.Path as ComposePath

/**
 * A Compose [Shape], flattened to the path an ad's icon badge is filled with.
 *
 * An ad's assets have to be rendered inside a real `NativeAdView`, so the badge behind the icon is
 * an Android view and cannot be given a Compose `Shape` directly. It can, however, be filled with
 * the path that shape describes, which is what this carries: any silhouette a caller can express in
 * Compose, `MaterialShapes` included, reaches the ad badge, drawn antialiased by the same
 * `PathShape` machinery the platform uses for its own shape drawables.
 *
 * @property path The outline, in pixels, at [sizePx].
 * @property sizePx The square size the path was measured at. The drawable rescales from it, so a
 * badge that ends up a pixel larger or smaller than the measurement still fills exactly.
 */
@Immutable
class NativeAdBadgeShape(
    val path: Path,
    val sizePx: Float,
)

/**
 * Flattens [shape] at [size] into the badge an ad row can fill.
 *
 * Remembered against the shape and the resolved size, because rebuilding the path on every
 * recomposition would hand the renderer a new object each time.
 */
@Composable
fun rememberNativeAdBadgeShape(shape: Shape, size: Dp): NativeAdBadgeShape {
    val density: Density = LocalDensity.current
    val layoutDirection: LayoutDirection = LocalLayoutDirection.current

    return remember(shape, size, density, layoutDirection) {
        val sizePx: Float = with(density) { size.toPx() }
        val outline = shape.createOutline(
            size = Size(width = sizePx, height = sizePx),
            layoutDirection = layoutDirection,
            density = density,
        )

        NativeAdBadgeShape(
            path = ComposePath().apply { addOutline(outline = outline) }.asAndroidPath(),
            sizePx = sizePx,
        )
    }
}
