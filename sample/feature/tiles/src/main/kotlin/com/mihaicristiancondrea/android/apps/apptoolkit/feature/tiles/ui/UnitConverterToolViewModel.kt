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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionUnit
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.UnitConversionRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.UnitConverterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.UnitConverterToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel

class UnitConverterToolViewModel(
    private val repository: UnitConversionRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<UnitConverterToolUiState, UnitConverterToolEvent>(
    initialState = UnitConverterToolUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "UnitConverterToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.UNIT_CONVERTER)

    override fun handleEvent(event: UnitConverterToolEvent) {
        when (event) {
            is UnitConverterToolEvent.CategorySelected -> {
                if (event.category == currentState.category) return
                val units = ConversionUnit.entries.filter { it.category == event.category }
                setState { copy(category = event.category, from = units[0], to = units[1], left = "1", editingLeft = true) }
                recalculate()
            }
            is UnitConverterToolEvent.UnitSelected -> {
                if (event.unit.category != currentState.category) return
                setState { if (event.left) copy(from = event.unit) else copy(to = event.unit) }
                recalculate()
            }
            is UnitConverterToolEvent.InputChanged -> {
                if (event.input.length > 64) return
                setState {
                    if (event.left) copy(left = event.input, editingLeft = true)
                    else copy(right = event.input, editingLeft = false)
                }
                recalculate()
            }
            UnitConverterToolEvent.Dismiss -> {
                setState { UnitConverterToolUiState() }
                usage.endSession()
            }
        }
    }

    private fun recalculate() {
        val state = currentState
        val input = if (state.editingLeft) state.left else state.right
        val result = if (state.editingLeft) repository.convert(input, state.from, state.to)
        else repository.convert(input, state.to, state.from)
        setState {
            if (editingLeft) copy(right = result.orEmpty(), invalidInput = input.isNotBlank() && result == null)
            else copy(left = result.orEmpty(), invalidInput = input.isNotBlank() && result == null)
        }
        if (result != null) usage.markUsed()
    }
}
