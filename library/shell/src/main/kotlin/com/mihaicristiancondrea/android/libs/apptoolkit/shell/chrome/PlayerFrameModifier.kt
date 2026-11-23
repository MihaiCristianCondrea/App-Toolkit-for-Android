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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp

/**
 * Sizes and places the player between its docked pill and the full window, reading [progress] and
 * [dockBottom] while measuring. A dock that moves on every frame, as the bottom bar does while it
 * hides, and every frame of a drag or settle then lay the player out again without recomposing it.
 *
 * Docked, the pill is inset by [dockStart] plus a margin at the start, a margin at the end, and
 * sits above [dockBottom]; expanded, it fills the width and [fullHeight].
 */
internal fun Modifier.playerFrame(
    progress: () -> Float,
    dockStart: Dp,
    dockBottom: () -> Dp,
    fullHeight: Dp,
): Modifier = layout { measurable, constraints ->
    val fraction = progress()
    val startPx = lerp(dockStart + 12.dp, 0.dp, fraction).roundToPx().coerceAtLeast(0)
    val endPx = lerp(12.dp, 0.dp, fraction).roundToPx().coerceAtLeast(0)
    val bottomPx = lerp(dockBottom() + 8.dp, 0.dp, fraction).roundToPx().coerceAtLeast(0)
    val width = (constraints.maxWidth - startPx - endPx).coerceAtLeast(0)
    val height = lerp(MiniPlayerHeight, fullHeight, fraction).roundToPx()
        .coerceIn(0, (constraints.maxHeight - bottomPx).coerceAtLeast(0))
    val placeable = measurable.measure(Constraints.fixed(width, height))
    layout(constraints.maxWidth, constraints.constrainHeight(height + bottomPx)) {
        placeable.place(startPx, 0)
    }
}
