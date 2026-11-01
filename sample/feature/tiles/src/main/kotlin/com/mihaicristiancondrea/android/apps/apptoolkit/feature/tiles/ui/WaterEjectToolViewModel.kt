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

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.contracts.AppGa4Contract
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.WaterEjectRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.toolCycleFinishedEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.WaterEjectToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.WaterEjectToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job

class WaterEjectToolViewModel(
    private val repository: WaterEjectRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<WaterEjectToolUiState, WaterEjectToolEvent>(
    initialState = WaterEjectToolUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "WaterEjectToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.WATER_EJECT)
    private var cycle: Job? = null
    private var generation = 0

    override fun handleEvent(event: WaterEjectToolEvent) {
        when (event) {
            WaterEjectToolEvent.Start -> {
                if (currentState.running) return
                val currentGeneration = ++generation
                setState { copy(running = true, failed = false, seconds = 30) }
                var outcome = AppGa4Contract.ToolCycleOutcome.FAILED
                cycle = launchReport(
                    action = Actions.PLAY,
                    onError = {
                        if (generation == currentGeneration) setState { copy(failed = true) }
                    },
                ) {
                    try {
                        repository.playCycle { seconds ->
                            // The first countdown confirms playback, unlike an accepted Start tap.
                            if (generation == currentGeneration) {
                                usage.markUsed()
                                setState { copy(seconds = seconds) }
                            }
                        }
                        outcome = AppGa4Contract.ToolCycleOutcome.COMPLETED
                    } finally {
                        if (generation == currentGeneration) setState { copy(running = false) }
                    }
                }.also { job ->
                    // Completion also runs if Stop cancels a job before its coroutine starts.
                    job.invokeOnCompletion { cause ->
                        val result = if (cause is CancellationException) {
                            AppGa4Contract.ToolCycleOutcome.CANCELLED
                        } else outcome
                        telemetryRepository.logEvent(toolCycleFinishedEvent(ToolkitTileIds.WATER_EJECT, result))
                    }
                }
            }
            WaterEjectToolEvent.Stop -> {
                generation++
                cycle?.cancel()
                usage.endSession()
                setState { copy(running = false) }
            }
        }
    }

    private object Actions { const val PLAY = "playSpeakerCycle" }
}
