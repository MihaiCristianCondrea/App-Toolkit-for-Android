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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.views

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants

/**
 * A round preview of a palette: [primary] across the top half, [secondary] and [tertiary] sharing
 * the bottom.
 *
 * The mosaic is drawn by one cached drawing node (three rectangles clipped to a cached circle)
 * rather than a column and row of colored boxes, so a row of swatches adds one node each instead
 * of eight.
 *
 * The selection check is drawn in [primary] on a disc of the same hue, darkened or lightened,
 * whichever stands further from [primary]. Both come from the swatch rather than the app's theme:
 * the selected swatch shows the palette the app is drawn in, and on dark schemes the theme's
 * `onPrimaryContainer` sat so close to the swatch's primary that the check disappeared.
 */
@Composable
fun MaterialYouCircleSwatch(
    primary: Color,
    secondary: Color,
    tertiary: Color,
    selected: Boolean,
    modifier: Modifier = Modifier,
    indicatorFraction: Float = 0.58f,
) {
    val progress = animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "swatchSelectionProgress",
    )

    Box(
        modifier = modifier.drawWithCache {
            val circle = Path().apply { addOval(Rect(Offset.Zero, size)) }
            val half = size.height / 2f
            onDrawBehind {
                clipPath(circle) {
                    drawRect(primary, size = Size(size.width, half))
                    drawRect(secondary, topLeft = Offset(0f, half), size = Size(size.width / 2f, half))
                    drawRect(
                        tertiary,
                        topLeft = Offset(size.width / 2f, half),
                        size = Size(size.width / 2f, half),
                    )
                }
            }
        },
        contentAlignment = Alignment.Center,
    ) {
        val badgeColor: Color = remember(primary) { selectionBadgeColor(primary) }
        Box(
            modifier = Modifier
                .fillMaxSize(indicatorFraction)
                .graphicsLayer {
                    // Read in the draw phase, so the selection spring never recomposes the swatch.
                    val value = progress.value
                    alpha = value.coerceIn(0f, 1f)
                    val scale = 0.8f + 0.2f * value
                    scaleX = scale
                    scaleY = scale
                }
                .drawWithCache { onDrawBehind { drawCircle(badgeColor) } }
                .padding(SizeConstants.ExtraSmallSize),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = primary,
                modifier = Modifier.fillMaxSize(CHECK_FRACTION),
            )
        }
    }
}

/**
 * The disc behind the selection check: [primary] taken most of the way to black or to white,
 * whichever contrasts more with it, so the check drawn in [primary] on top always shows.
 */
internal fun selectionBadgeColor(primary: Color): Color {
    val deep = lerp(primary, Color.Black, BADGE_DEEP_FRACTION)
    val pale = lerp(primary, Color.White, BADGE_PALE_FRACTION)
    return if (contrast(deep, primary) >= contrast(pale, primary)) deep else pale
}

private fun contrast(first: Color, second: Color): Float {
    val lighter = maxOf(first.luminance(), second.luminance())
    val darker = minOf(first.luminance(), second.luminance())
    return (lighter + 0.05f) / (darker + 0.05f)
}

private const val BADGE_DEEP_FRACTION: Float = 0.72f
private const val BADGE_PALE_FRACTION: Float = 0.8f
private const val CHECK_FRACTION: Float = 0.7f
