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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.carousel

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sin

private const val AOSP_ANIMATION_DURATION = 250
private const val AOSP_INACTIVE_ALPHA = 0.42f
private const val CAPSULE_STRETCH = 0.7f

/**
 * An Android 10 Quick Settings-inspired page indicator that tracks a [PagerState] continuously.
 *
 * Unlike the original SystemUI implementation, this indicator reads the pager's continuous scroll
 * offset during the draw phase instead of maintaining a pending-transition queue, preventing stale
 * target desynchronization under rapid swipes or button jumps.
 *
 * State ownership:
 * - Callers own [pagerState], sizing, colors, and [pageDescription].
 * - Nothing is rendered when `pagerState.pageCount <= 1`.
 * - For page counts above [maxVisibleDots], the visible dots form a continuous sliding window.
 *
 * @param pagerState Shared pager state providing `pageCount`, `currentPage`, and `currentPageOffsetFraction`.
 * @param modifier Modifier applied to the indicator canvas.
 * @param dotSize Diameter of each circular dot (`> 0.dp`).
 * @param dotSpacing Horizontal gap between adjacent dots (`>= 0.dp`).
 * @param maxVisibleDots Maximum number of dots visible at once before sliding-window scrolling begins (`>= 2`).
 * @param selectedColor Fill color of the active capsule.
 * @param unselectedColor Fill color of inactive dots, defaulting to 42% of [selectedColor]'s alpha.
 * @param pageDescription Optional localized accessibility state description overriding the default English label.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AospPageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    dotSize: Dp = 6.4.dp,
    dotSpacing: Dp = 3.2.dp,
    maxVisibleDots: Int = 7,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = selectedColor.copy(
        alpha = selectedColor.alpha * AOSP_INACTIVE_ALPHA
    ),
    pageDescription: String? = null,
) {
    val pageCount = pagerState.pageCount
    if (pageCount <= 1) return

    PageIndicatorCanvas(
        pageCount = pageCount,
        selectedIndex = pagerState.currentPage,
        positionProvider = {
            pageIndicatorPosition(
                currentPage = pagerState.currentPage,
                offsetFraction = pagerState.currentPageOffsetFraction,
                pageCount = pageCount,
            )
        },
        modifier = modifier,
        dotSize = dotSize,
        dotSpacing = dotSpacing,
        maxVisibleDots = maxVisibleDots,
        selectedColor = selectedColor,
        unselectedColor = unselectedColor,
        pageDescription = pageDescription,
    )
}

/**
 * An Android 10 Quick Settings-inspired page indicator for components that manage an integer
 * [selectedIndex] rather than a [PagerState].
 *
 * State ownership:
 * - Callers own [pageCount] and [selectedIndex].
 * - Single-step transitions animate over [animationDurationMillis]; new targets interrupt in-flight
 *   animations, and multi-page jumps (`abs(delta) > 1`) snap directly to the destination.
 *
 * @param pageCount Total number of pages. Nothing is rendered when `pageCount <= 1`.
 * @param selectedIndex Zero-based active page index, clamped to `0 until pageCount`.
 * @param modifier Modifier applied to the indicator canvas.
 * @param dotSize Diameter of each circular dot (`> 0.dp`).
 * @param dotSpacing Horizontal gap between adjacent dots (`>= 0.dp`).
 * @param maxVisibleDots Maximum number of dots visible at once before sliding-window scrolling begins (`>= 2`).
 * @param animationDurationMillis Duration in milliseconds for single-step page transitions (`>= 0`).
 * @param selectedColor Fill color of the active capsule.
 * @param unselectedColor Fill color of inactive dots, defaulting to 42% of [selectedColor]'s alpha.
 * @param pageDescription Optional localized accessibility state description overriding the default English label.
 */
@Composable
fun AospPageIndicator(
    pageCount: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    dotSize: Dp = 6.4.dp,
    dotSpacing: Dp = 3.2.dp,
    maxVisibleDots: Int = 7,
    animationDurationMillis: Int = AOSP_ANIMATION_DURATION,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = selectedColor.copy(
        alpha = selectedColor.alpha * AOSP_INACTIVE_ALPHA
    ),
    pageDescription: String? = null,
) {
    if (pageCount <= 1) return

    require(animationDurationMillis >= 0)

    val selectedPage = selectedIndex.coerceIn(0, pageCount - 1)

    key(pageCount) {
        val animatedPosition = remember {
            Animatable(selectedPage.toFloat())
        }

        var previousTarget by remember {
            mutableIntStateOf(selectedPage)
        }

        LaunchedEffect(selectedPage, animationDurationMillis) {
            val isLargeJump = abs(selectedPage - previousTarget) > 1
            previousTarget = selectedPage

            if (isLargeJump || animationDurationMillis == 0) {
                animatedPosition.snapTo(selectedPage.toFloat())
            } else {
                animatedPosition.animateTo(
                    targetValue = selectedPage.toFloat(),
                    animationSpec = tween(
                        durationMillis = animationDurationMillis,
                        easing = FastOutSlowInEasing,
                    ),
                )
            }
        }

        PageIndicatorCanvas(
            pageCount = pageCount,
            selectedIndex = selectedPage,
            positionProvider = { animatedPosition.value },
            modifier = modifier,
            dotSize = dotSize,
            dotSpacing = dotSpacing,
            maxVisibleDots = maxVisibleDots,
            selectedColor = selectedColor,
            unselectedColor = unselectedColor,
            pageDescription = pageDescription,
        )
    }
}

@Composable
private fun PageIndicatorCanvas(
    pageCount: Int,
    selectedIndex: Int,
    positionProvider: () -> Float,
    modifier: Modifier,
    dotSize: Dp,
    dotSpacing: Dp,
    maxVisibleDots: Int,
    selectedColor: Color,
    unselectedColor: Color,
    pageDescription: String?,
) {
    require(dotSize > 0.dp)
    require(dotSpacing >= 0.dp)
    require(maxVisibleDots >= 2)

    val visibleDots = minOf(pageCount, maxVisibleDots)
    val dotStep = dotSize + dotSpacing
    val indicatorWidth = dotSize + dotStep * (visibleDots - 1)
    val indicatorHeight = maxOf(dotSize, 8.dp)
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val description = pageDescription
        ?: "Page ${selectedIndex.coerceIn(0, pageCount - 1) + 1} of $pageCount"

    Canvas(
        modifier = modifier
            .semantics {
                stateDescription = description
            }
            .width(indicatorWidth)
            .height(indicatorHeight),
    ) {
        val diameter = dotSize.toPx()
        val radius = diameter / 2f
        val step = dotStep.toPx()
        val centerY = size.height / 2f

        // Read the animation position during drawing, rather than recomposing the whole
        // component on every frame.
        val position = positionProvider()
            .coerceIn(0f, (pageCount - 1).toFloat())

        val windowStart = pageIndicatorWindowStart(
            position = position,
            pageCount = pageCount,
            visibleDots = visibleDots,
        )

        fun centerX(page: Float): Float {
            val logicalX = radius + (page - windowStart) * step
            return if (isRtl) size.width - logicalX else logicalX
        }

        clipRect {
            val firstVisible = floor(windowStart)
                .toInt()
                .coerceAtLeast(0)

            val lastVisible = ceil(windowStart + visibleDots)
                .toInt()
                .coerceAtMost(pageCount - 1)

            // Inactive dots keep their fixed circular shape.
            for (index in firstVisible..lastVisible) {
                drawCircle(
                    color = unselectedColor,
                    radius = radius,
                    center = Offset(
                        x = centerX(index.toFloat()),
                        y = centerY,
                    ),
                )
            }

            // The fraction determines how far the active dot has travelled towards the next page.
            val fraction = position - floor(position)

            // At the midpoint the circle becomes a capsule; at integer page positions it returns to a circle.
            val stretch = sin(PI * fraction).toFloat()
            val capsuleWidth = diameter + step * CAPSULE_STRETCH * stretch

            val activeCenterX = centerX(position)

            drawRoundRect(
                color = selectedColor,
                topLeft = Offset(
                    x = activeCenterX - capsuleWidth / 2f,
                    y = centerY - radius,
                ),
                size = Size(
                    width = capsuleWidth,
                    height = diameter,
                ),
                cornerRadius = CornerRadius(radius),
            )
        }
    }
}

/**
 * Converts [PagerState]'s nearest-page representation into a single continuous position across all pages.
 */
internal fun pageIndicatorPosition(
    currentPage: Int,
    offsetFraction: Float,
    pageCount: Int,
): Float {
    if (pageCount <= 0) return 0f

    return (currentPage + offsetFraction)
        .coerceIn(0f, (pageCount - 1).toFloat())
}

/**
 * Keeps the active page near the middle of the visible window so long carousels scroll smoothly.
 */
internal fun pageIndicatorWindowStart(
    position: Float,
    pageCount: Int,
    visibleDots: Int,
): Float {
    if (pageCount <= visibleDots) return 0f

    val centerOffset = (visibleDots - 1) / 2f
    val maximumStart = (pageCount - visibleDots).toFloat()

    return (position - centerOffset)
        .coerceIn(0f, maximumStart)
}
