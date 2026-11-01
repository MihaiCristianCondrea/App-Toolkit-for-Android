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
package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerEdge
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerUnits

sealed interface RulerToolEvent {
    data class MarkerMoved(val position: Float) : RulerToolEvent
    data class RangeChanged(val start: Float, val end: Float) : RulerToolEvent
    data class ScaleChanged(val scale: Float) : RulerToolEvent
    data object SaveScale : RulerToolEvent
    data object ResetScale : RulerToolEvent
    data class AdvancedChanged(val advanced: Boolean) : RulerToolEvent
    data class UnitsSelected(val units: RulerUnits) : RulerToolEvent
    data class EdgeSelected(val edge: RulerEdge) : RulerToolEvent
    data class CountFromEndChanged(val countFromEnd: Boolean) : RulerToolEvent
    data class InchDivisionsSelected(val divisions: Int) : RulerToolEvent
    data object Dismiss : RulerToolEvent
}
