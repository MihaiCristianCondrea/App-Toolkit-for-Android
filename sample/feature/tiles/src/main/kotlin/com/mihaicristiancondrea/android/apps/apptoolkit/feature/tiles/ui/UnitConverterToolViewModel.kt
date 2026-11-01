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

import androidx.lifecycle.SavedStateHandle

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
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : LoggedScreenViewModel<UnitConverterToolUiState, UnitConverterToolEvent>(
    initialState = UnitConverterToolUiState(
        category = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionCategory.entries.firstOrNull { it.name == savedStateHandle.get<String>("category") } ?: com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionCategory.Length,
        from = ConversionUnit.entries.firstOrNull { it.name == savedStateHandle.get<String>("from") } ?: ConversionUnit.Metre,
        to = ConversionUnit.entries.firstOrNull { it.name == savedStateHandle.get<String>("to") } ?: ConversionUnit.Centimetre,
        left = savedStateHandle["left"] ?: "1", right = savedStateHandle["right"] ?: "100", editingLeft = savedStateHandle["editingLeft"] ?: true,
    ),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.NUMBERS.name,
    viewModelName = "UnitConverterToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.UNIT_CONVERTER)

    init { recalculate(trackUse = false) }

    override fun handleEvent(event: UnitConverterToolEvent) {
        when (event) {
            UnitConverterToolEvent.Swap -> {
                setState { copy(from = to, to = from, left = right, right = left, editingLeft = !editingLeft) }
                recalculate()
            }
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
                usage.endSession()
            }
        }
        savedStateHandle["category"] = currentState.category.name
        savedStateHandle["from"] = currentState.from.name
        savedStateHandle["to"] = currentState.to.name
        savedStateHandle["left"] = currentState.left
        savedStateHandle["right"] = currentState.right
        savedStateHandle["editingLeft"] = currentState.editingLeft
    }

    private fun recalculate(trackUse: Boolean = true) {
        val state = currentState
        val input = if (state.editingLeft) state.left else state.right
        val result = if (state.editingLeft) repository.convert(input, state.from, state.to)
        else repository.convert(input, state.to, state.from)
        setState {
            if (editingLeft) copy(right = result.orEmpty(), invalidInput = input.isNotBlank() && result == null)
            else copy(left = result.orEmpty(), invalidInput = input.isNotBlank() && result == null)
        }
        if (trackUse && result != null) usage.markUsed()
    }
}
