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

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants

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

/** Values a [GroupedGrid] falls back to when a caller does not choose its own. */
object GroupedGridDefaults {

    /**
     * Columns a grid lays its cells out in.
     *
     * Two is the width the cells are designed for: a badge, a title and a size or description still
     * fit side by side on a compact phone. Grids of icon-only actions can afford more.
     */
    const val Columns: Int = 2

    /**
     * Items a grid needs before it will place an ad row.
     *
     * A single cell is the whole group, so an ad under it would be the group's other half rather
     * than a row inside it; from two cells up the ad reads as one more row of the same block.
     */
    const val MinItemsForAd: Int = 2

    /** Radius of the four corners at the outside of the group. */
    val OuterRadius: Dp = SizeConstants.LargeIncreasedSize

    /** Radius of every corner inside the group, where two cells meet. */
    val InnerRadius: Dp = SizeConstants.ExtraTinySize

    /** Gap between neighbouring cells, horizontally and vertically. */
    val ItemSpacing: Dp = SizeConstants.ExtraTinySize

    /** Size class cells are drawn at. */
    val Measurements: GroupedGridMeasurements = GroupedGridMeasurements.Medium

    /**
     * Silhouette the icon badge is cut with.
     *
     * Any [Shape] is accepted, so the Material 3 shape set is available to callers as
     * `MaterialShapes.Cookie9Sided.toShape()` and friends without the toolkit shipping artwork of
     * its own. The default is the circle, which is what the badge was before the shape was a
     * parameter.
     *
     * It reads as a composable because `toShape` is one; a caller building items outside
     * composition should keep the badge shape out of them and pass it to the grid instead.
     */
    val IconShape: Shape
        @OptIn(ExperimentalMaterial3ExpressiveApi::class)
        @Composable
        get() = MaterialShapes.Circle.toShape()

    /** Grid colors, overridable one at a time. */
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor: Color = MaterialTheme.colorScheme.onSurface,
        iconContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
        iconContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
        subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    ): GroupedGridColors = GroupedGridColors(
        containerColor = containerColor,
        contentColor = contentColor,
        iconContainerColor = iconContainerColor,
        iconContentColor = iconContentColor,
        subtitleColor = subtitleColor,
    )
}
