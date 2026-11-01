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
package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerSettings

/**
 * The ruler on screen. Positions are fractions of the ruler's length counted from its zero edge, so
 * they keep their meaning when zero moves to the other end.
 *
 * @property scale Calibration applied to the display's reported pixels per millimetre.
 * @property marker The simple ruler's reading line, measured from zero.
 * @property start The start of the advanced ruler's measurement range.
 * @property end The end of the advanced ruler's measurement range.
 */
@Immutable
data class RulerToolUiState(
    val scale: Float = 1f,
    val settings: RulerSettings = RulerSettings(),
    val marker: Float = 0.5f,
    val start: Float = 0f,
    val end: Float = 0.5f,
    val saveFailed: Boolean = false,
)
