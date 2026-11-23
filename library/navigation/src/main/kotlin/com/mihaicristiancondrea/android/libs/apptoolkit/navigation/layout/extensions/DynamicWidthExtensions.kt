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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

/**
 * `Modifier.width` with a width read while measuring, so a width that moves every frame lays the
 * element out again without recomposing it.
 */
internal fun Modifier.widthOf(width: () -> Dp): Modifier = layout { measurable, constraints ->
    val px = width().roundToPx().coerceIn(constraints.minWidth, constraints.maxWidth)
    val placeable = measurable.measure(constraints.copy(minWidth = px, maxWidth = px))
    layout(placeable.width, placeable.height) { placeable.place(0, 0) }
}

/** `Modifier.requiredWidth` with a width read while measuring, as [widthOf] is. */
internal fun Modifier.requiredWidthOf(width: () -> Dp): Modifier = layout { measurable, constraints ->
    val px = width().roundToPx().coerceAtLeast(0)
    val placeable = measurable.measure(constraints.copy(minWidth = px, maxWidth = px))
    layout(placeable.width, placeable.height) { placeable.place(0, 0) }
}
