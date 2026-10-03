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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.BreathingRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.models.BreathingPhase
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.BreathingToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.BreathingToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onStart

/**
 * Runs a guided breathing session while the tool's sheet is open. The hold after breathing out
 * closes a cycle, so reaching it is reported as use: one full breath.
 */
class BreathingToolViewModel(
    private val repository: BreathingRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<BreathingToolUiState, BreathingToolEvent>(
    initialState = BreathingToolUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "BreathingToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.BREATHING)
    private var sessionJob: Job? = null

    override fun handleEvent(event: BreathingToolEvent) {
        when (event) {
            BreathingToolEvent.Open -> startSession()
            BreathingToolEvent.Dismiss -> stopSession()
        }
    }

    private fun startSession() {
        sessionJob = sessionJob.restart {
            repository.breathingState
                .onStart { repository.start() }
                .collectReport(action = Actions.START_SESSION) { breathing ->
                    setState { copy(breathing = breathing) }
                    if (breathing.phase == BreathingPhase.HOLD_EMPTY) usage.markUsed()
                }
        }
    }

    private fun stopSession() {
        sessionJob?.cancel()
        sessionJob = null
        usage.endSession()
        launchReport(action = Actions.STOP_SESSION) {
            repository.stop()
        }
        setState { BreathingToolUiState() }
    }

    private object Actions {
        const val START_SESSION: String = "startBreathing"
        const val STOP_SESSION: String = "stopBreathing"
    }
}
