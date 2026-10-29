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
package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models

/** Which scales the ruler draws: centimetres and millimetres, inches, or one along each edge. */
enum class RulerUnits { Metric, Imperial, Both }

/**
 * A screen edge the ruler is drawn against: the left edge in portrait and the top in landscape for
 * [Start], the right edge or the bottom for [End].
 */
enum class RulerEdge {
    Start,
    End;

    val opposite: RulerEdge get() = if (this == Start) End else Start
}

/**
 * How the ruler is set up. The simple ruler and the advanced one draw the same scales; advanced only
 * adds the controls that change them.
 *
 * @property units The scales to draw. With [RulerUnits.Both], centimetres sit on [edge] and inches on
 * the opposite edge.
 * @property edge The edge the (first) scale is drawn against.
 * @property countFromEnd Whether zero is at the bottom (right in landscape) instead of the top (left).
 * @property inchDivisions The smallest inch step: 8, 16 or 32 parts.
 * @property advanced Whether the ruler opens with its settings and measurement range.
 */
data class RulerSettings(
    val units: RulerUnits = RulerUnits.Both,
    val edge: RulerEdge = RulerEdge.Start,
    val countFromEnd: Boolean = false,
    val inchDivisions: Int = 16,
    val advanced: Boolean = false,
) {
    companion object {
        val InchDivisions: List<Int> = listOf(8, 16, 32)
    }
}
