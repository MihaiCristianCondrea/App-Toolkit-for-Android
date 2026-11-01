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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.BillCurrency
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.BillSplitterRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.BillSplitterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.BillSplitterToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.extensions.toToolLongOrNull

class BillSplitterToolViewModel(
    private val repository: BillSplitterRepository,
    telemetryRepository: TelemetryRepository,
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : LoggedScreenViewModel<BillSplitterToolUiState, BillSplitterToolEvent>(
    initialState = BillSplitterToolUiState(
        bill = savedStateHandle["bill"] ?: "", people = savedStateHandle["people"] ?: "1",
        currency = BillCurrency.entries.firstOrNull { it.name == savedStateHandle.get<String>("currency") } ?: BillCurrency.USD,
        tipEnabled = savedStateHandle["tipEnabled"] ?: true, tipPercent = savedStateHandle["tipPercent"] ?: 15,
    ),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.NUMBERS.name,
    viewModelName = "BillSplitterToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.BILL_SPLITTER)

    init { recalculate(trackUse = false) }

    override fun handleEvent(event: BillSplitterToolEvent) {
        when (event) {
            is BillSplitterToolEvent.CurrencySelected -> { setState { copy(currency = event.currency) }; recalculate() }
            is BillSplitterToolEvent.TipEnabled -> { setState { copy(tipEnabled = event.enabled) }; recalculate() }
            is BillSplitterToolEvent.BillChanged -> {
                if (event.input.length > 64) return
                setState { copy(bill = event.input) }
                recalculate()
            }
            is BillSplitterToolEvent.PeopleChanged -> {
                if (event.input.length > 20) return
                setState { copy(people = event.input) }
                recalculate()
            }
            is BillSplitterToolEvent.TipSelected -> {
                if (event.percent !in listOf(10, 15, 20)) return
                setState { copy(tipPercent = event.percent) }
                recalculate()
            }
            BillSplitterToolEvent.AddPerson -> changePeople(1)
            BillSplitterToolEvent.RemovePerson -> changePeople(-1)
            BillSplitterToolEvent.Dismiss -> {
                usage.endSession()
            }
        }
    }

    private fun changePeople(delta: Int) {
        val people = currentState.people.toToolLongOrNull() ?: return
        if (people < 1 || (delta < 0 && people == 1L) || (delta > 0 && people == Long.MAX_VALUE)) return
        setState { copy(people = (people + delta).toString()) }
        recalculate()
    }

    private fun recalculate(trackUse: Boolean = true) {
        val state = currentState
        val result = repository.split(state.bill, if (state.tipEnabled) state.tipPercent else 0, state.people, state.currency.digits)
        setState { copy(result = result, invalidInput = bill.isNotBlank() && result == null) }
        savedStateHandle["bill"] = state.bill
        savedStateHandle["people"] = state.people
        savedStateHandle["currency"] = state.currency.name
        savedStateHandle["tipEnabled"] = state.tipEnabled
        savedStateHandle["tipPercent"] = state.tipPercent
        if (trackUse && result != null) usage.markUsed()
    }
}
