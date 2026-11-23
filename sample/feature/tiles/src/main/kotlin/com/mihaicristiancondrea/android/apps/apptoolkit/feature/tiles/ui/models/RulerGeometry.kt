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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerEdge

/**
 * Maps between a distance from zero, as a fraction of the ruler's length, and the screen. The ruler
 * runs down the screen in portrait and across it in landscape, from either end.
 */
internal class RulerGeometry(val landscape: Boolean, val countFromEnd: Boolean, val length: Float) {
    /** The position along the ruler of a distance from zero given as a fraction of its length. */
    fun along(fraction: Float): Float = if (countFromEnd) length * (1f - fraction) else length * fraction

    /** The distance from zero of a point on screen, as a fraction of the ruler's length. */
    fun fraction(point: Offset): Float {
        val raw = ((if (landscape) point.x else point.y) / length.coerceAtLeast(1f)).coerceIn(0f, 1f)
        return if (countFromEnd) 1f - raw else raw
    }

    /** A point at [along] on the ruler and [across] in from [edge]. */
    fun point(along: Float, across: Float, edge: RulerEdge, size: Size): Offset {
        val cross = if (edge == RulerEdge.Start) across else (if (landscape) size.height else size.width) - across
        return if (landscape) Offset(along, cross) else Offset(cross, along)
    }
}
