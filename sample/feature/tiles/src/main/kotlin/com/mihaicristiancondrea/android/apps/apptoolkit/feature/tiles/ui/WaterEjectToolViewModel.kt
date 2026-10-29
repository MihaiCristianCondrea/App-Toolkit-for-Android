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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.WaterEjectRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.WaterEjectToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.WaterEjectToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import kotlinx.coroutines.Job

class WaterEjectToolViewModel(private val repository: WaterEjectRepository, telemetryRepository: TelemetryRepository) :
    LoggedScreenViewModel<WaterEjectToolUiState, WaterEjectToolEvent>(initialState = WaterEjectToolUiState(),
        telemetryRepository = telemetryRepository, screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name, viewModelName = "WaterEjectToolViewModel") {
    private var cycle: Job? = null
    private var generation = 0
    override fun handleEvent(event: WaterEjectToolEvent) {
        when (event) {
            WaterEjectToolEvent.Start -> {
                if (currentState.running) return
                val currentGeneration = ++generation
                setState { copy(running = true, failed = false, seconds = 30) }
                cycle = launchReport(action = Actions.PLAY, onError = { if (generation == currentGeneration) setState { copy(failed = true) } }) {
                    try { repository.playCycle { seconds -> setState { copy(seconds = seconds) } } }
                    finally { if (generation == currentGeneration) setState { copy(running = false) } }
                }
            }
            WaterEjectToolEvent.Stop -> { generation++; cycle?.cancel(); setState { copy(running = false) } }
        }
    }
    private object Actions { const val PLAY = "playSpeakerCycle" }
}
