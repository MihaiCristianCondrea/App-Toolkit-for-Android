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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dividers

import androidx.compose.runtime.Immutable

/**
 * The measurements of one wavy line, in pixels, along an axis of any direction.
 *
 * The line runs from [start] along its length in [halfWaveCount] half-waves of [halfWaveLength],
 * each a cubic curve that leaves and rejoins the centre line at [centre]. The curve's two control
 * points sit a third and two thirds of the way along, [controlOffset] off the centre line on
 * alternating sides, which is exactly how `il_wavy_line` is drawn.
 */
@Immutable
internal data class WavyLineGeometry(
    val start: Float,
    val centre: Float,
    val halfWaveCount: Int,
    val halfWaveLength: Float,
    val controlOffset: Float,
)
