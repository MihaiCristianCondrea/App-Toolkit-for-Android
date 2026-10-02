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

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.CounterRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.CounterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CounterToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel

/**
 * Shows the count shared with the Counter Quick Settings tile, so closing the sheet keeps it. The
 * stored count is followed for the ViewModel's lifetime, which costs nothing between changes.
 */
class CounterToolViewModel(
    private val repository: CounterRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<CounterToolUiState, CounterToolEvent>(
    initialState = CounterToolUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "CounterToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.COUNTER)

    init {
        observeCount()
    }

    override fun handleEvent(event: CounterToolEvent) {
        when (event) {
            CounterToolEvent.Increment -> increment()
            CounterToolEvent.Reset -> reset()
            CounterToolEvent.Dismiss -> usage.endSession()
        }
    }

    private fun observeCount() {
        repository.count.collectReport(action = Actions.OBSERVE_COUNT) { count ->
            setState { copy(count = count) }
        }
    }

    private fun increment() {
        launchReport(action = Actions.INCREMENT) {
            repository.increment()
            usage.markUsed()
        }
    }

    private fun reset() {
        launchReport(action = Actions.RESET) {
            repository.reset()
        }
    }

    private object Actions {
        const val OBSERVE_COUNT: String = "observeCount"
        const val INCREMENT: String = "incrementCount"
        const val RESET: String = "resetCount"
    }
}
