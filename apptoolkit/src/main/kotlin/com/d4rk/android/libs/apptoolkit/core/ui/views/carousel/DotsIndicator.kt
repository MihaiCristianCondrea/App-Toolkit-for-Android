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

package com.d4rk.android.libs.apptoolkit.core.ui.views.carousel

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraTinyHorizontalSpacer

/**
 * Renders an animated row of dots that reflects the currently selected carousel page.
 *
 * State ownership:
 * - Callers own [totalDots], [selectedIndex], sizing, colors, and animation timing.
 * - This composable derives the visual selected/unselected state for each dot.
 *
 * Guardrails:
 * - Keep [selectedIndex] in `0 until totalDots` to ensure a valid selected item.
 * - Use [dotSize] values greater than `0.dp`.
 * - Keep [animationDuration] non-negative.
 *
 * @param modifier Modifier applied to the indicator row.
 * @param totalDots Total number of dots to render.
 * @param selectedIndex Currently selected dot index.
 * @param selectedColor Color used for the selected dot.
 * @param unSelectedColor Color used for unselected dots.
 * @param dotSize Base size of each dot before the selection animation is applied.
 * @param animationDuration Duration (in milliseconds) of the dot size transition.
 */
@Composable
fun DotsIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unSelectedColor: Color = Color.Gray,
    dotSize: Dp,
    animationDuration: Int = 300
) {
    val transition: Transition<Int> =
        updateTransition(targetState = selectedIndex, label = "Dot Transition")

    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .height(dotSize), verticalAlignment = Alignment.CenterVertically
    ) {
        items(count = totalDots, key = { index -> index }) { index ->
            val animatedDotSize: Dp by transition.animateDp(transitionSpec = {
                tween(durationMillis = animationDuration, easing = FastOutSlowInEasing)
            }, label = "Dot Size Animation") {
                if (it == index) dotSize else dotSize / 1.4f
            }

            val isSelected: Boolean = index == selectedIndex
            val size: Dp = if (isSelected) animatedDotSize else animatedDotSize

            IndicatorDot(
                color = if (isSelected) selectedColor else unSelectedColor, size = size
            )

            if (index != totalDots - 1) {
                ExtraTinyHorizontalSpacer()
            }
        }
    }
}
