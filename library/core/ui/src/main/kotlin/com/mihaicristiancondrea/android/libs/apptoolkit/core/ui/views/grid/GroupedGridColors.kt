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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.grid

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Colors one [GroupedGrid] draws its cells with.
 *
 * A cell may override the two badge colors on its own [GroupedGridItem]; the container and text
 * colors belong to the grid, so a group always reads as one surface.
 */
@Immutable
data class GroupedGridColors(
    val containerColor: Color,
    val contentColor: Color,
    val iconContainerColor: Color,
    val iconContentColor: Color,
    val subtitleColor: Color,
)
