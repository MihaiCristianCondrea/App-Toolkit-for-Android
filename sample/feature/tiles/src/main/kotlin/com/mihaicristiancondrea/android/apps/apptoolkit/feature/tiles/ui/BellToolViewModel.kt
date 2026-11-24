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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.BellRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.BellToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.BellToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Manages the foreground Bell tool session: audio loading, manual and shake-driven strikes, and
 * session usage telemetry.
 */
class BellToolViewModel(
    private val repository: BellRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<BellToolUiState, BellToolEvent>(
    initialState = BellToolUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "BellToolViewModel",
) {
    private val usage = ToolUsageTracker(
        telemetryRepository = telemetryRepository,
        toolId = ToolkitTileIds.BELL,
    )
    private val strikes = MutableSharedFlow<Float>(extraBufferCapacity = 8)
    private var session: Job? = null

    override fun handleEvent(event: BellToolEvent) {
        when (event) {
            BellToolEvent.Open -> open()
            BellToolEvent.Close -> close()
            BellToolEvent.Ring -> {
                if (currentState.audioReady) {
                    strikes.tryEmit(1f)
                }
            }
        }
    }

    private fun open() {
        if (session?.isActive == true) return
        session = launchReport(
            action = Actions.OPEN_SESSION,
            onError = {
                setState { copy(audioReady = false, audioFailed = true) }
            },
        ) {
            coroutineScope {
                val shakeJob = launch {
                    repository.shakes().collect { strength ->
                        if (strength == null) {
                            setState { copy(motionAvailable = false) }
                        } else if (currentState.audioReady) {
                            strikes.tryEmit(strength)
                        }
                    }
                }
                try {
                    repository.play(
                        strikes = strikes,
                        onAudioReady = { ready ->
                            setState { copy(audioReady = ready, audioFailed = !ready) }
                        },
                        onStrike = { rate ->
                            usage.markUsed()
                            setState { copy(strike = strike + 1, playbackRate = rate) }
                        },
                    )
                } finally {
                    shakeJob.cancel()
                }
            }
        }
    }

    private fun close() {
        session?.cancel()
        session = null
        usage.endSession()
        setState { BellToolUiState() }
    }

    private object Actions {
        const val OPEN_SESSION = "openBellSession"
    }
}
