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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

/**
 * A round preview of a palette: [primary] across the top half, [secondary] and [tertiary] sharing
 * the bottom, split by thin [dividerColor] lines.
 *
 * The mosaic is one draw call over a cached circle, rather than a column and row of colored boxes,
 * so a row of swatches adds one node each instead of eight.
 *
 * The selection check sits in a badge filled with the swatch's own [primary], ringed with
 * [dividerColor], and drawn in black or white, whichever stands out more. It used to take the app's
 * current theme colors, which on some palettes (every dark one among them) matched the swatch
 * underneath closely enough to hide the check.
 */
@Composable
fun MaterialYouCircleSwatch(
    primary: Color,
    secondary: Color,
    tertiary: Color,
    selected: Boolean,
    modifier: Modifier = Modifier,
    indicatorFraction: Float = 0.5f,
    dividerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
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
            val divider = DIVIDER_WIDTH.toPx()
            onDrawBehind {
                clipPath(circle) {
                    drawRect(primary, size = Size(size.width, half))
                    drawRect(secondary, topLeft = Offset(0f, half), size = Size(size.width / 2f, half))
                    drawRect(
                        tertiary,
                        topLeft = Offset(size.width / 2f, half),
                        size = Size(size.width / 2f, half),
                    )
                    drawLine(dividerColor, Offset(0f, half), Offset(size.width, half), divider)
                    drawLine(
                        dividerColor,
                        Offset(size.width / 2f, half),
                        Offset(size.width / 2f, size.height),
                        divider,
                    )
                }
            }
        },
        contentAlignment = Alignment.Center,
    ) {
        val checkColor = if (primary.luminance() > DARK_CHECK_LUMINANCE) Color.Black else Color.White
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
                .drawWithCache {
                    val ring = DIVIDER_WIDTH.toPx()
                    onDrawBehind {
                        drawCircle(dividerColor)
                        drawCircle(primary, radius = size.minDimension / 2f - ring)
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = checkColor,
                modifier = Modifier.fillMaxSize(CHECK_FRACTION),
            )
        }
    }
}

private val DIVIDER_WIDTH = 2.dp

/** Above this luminance black reads better on the badge than white does. */
private const val DARK_CHECK_LUMINANCE: Float = 0.18f

private const val CHECK_FRACTION: Float = 0.62f
