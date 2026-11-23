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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerEdge
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerSettings
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerUnits

/** The scales to draw and the edge each sits on: with both units, inches take the edge opposite centimetres. */
internal fun rulerScales(settings: RulerSettings): List<Pair<RulerUnits, RulerEdge>> = when (settings.units) {
    RulerUnits.Metric -> listOf(RulerUnits.Metric to settings.edge)
    RulerUnits.Imperial -> listOf(RulerUnits.Imperial to settings.edge)
    RulerUnits.Both -> listOf(RulerUnits.Metric to settings.edge, RulerUnits.Imperial to settings.edge.opposite)
}
