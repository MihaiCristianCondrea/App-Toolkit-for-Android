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
import androidx.compose.ui.graphics.Shape
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon

/**
 * One cell of a [GroupedGrid]: what it shows, how its badge is cut, and what tapping it does.
 *
 * [iconShape], [iconContainerColor] and [iconContentColor] are per-cell overrides. Leaving them
 * unset draws the cell with the grid's shared shape and colors, which is what a set of peers such
 * as storage buckets or quick actions wants; setting them lets one cell stand out, for example a
 * category badge cut from a different `MaterialShapes` silhouette than its neighbours.
 *
 * @property title Primary line, such as "Installed apps".
 * @property icon Glyph drawn inside the badge. Any [ToolkitIcon] source is accepted, animated
 *   vectors and bundled Lottie included.
 * @property subtitle Supporting line under [title], such as a formatted size. `null` renders a
 *   single-line cell, which is what an action grid without a measurement wants.
 * @property iconShape Silhouette the badge is cut with, typically `MaterialShapes.<name>.toShape()`.
 *   `null` uses the grid's shape.
 * @property iconContainerColor Badge fill. [Color.Unspecified] uses the grid's color.
 * @property iconContentColor Glyph tint. [Color.Unspecified] uses the grid's color.
 * @property enabled `false` renders the cell as unavailable and stops it reacting to taps.
 * @property onClick Invoked when the cell is tapped.
 */
@Immutable
data class GroupedGridItem(
    val title: String,
    val icon: ToolkitIcon,
    val subtitle: String? = null,
    val iconShape: Shape? = null,
    val iconContainerColor: Color = Color.Unspecified,
    val iconContentColor: Color = Color.Unspecified,
    val enabled: Boolean = true,
    val onClick: () -> Unit,
)
