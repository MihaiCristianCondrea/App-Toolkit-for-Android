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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.SensorRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.LevelToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.LevelToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * Shows the device tilt for the Bubble Level tool. The route starts the sensor when the app comes to
 * the foreground and stops it when it leaves, so the sensor never runs in the background. The level
 * is used by looking at it, so use is reported once the sheet has stayed open.
 */
class LevelToolViewModel(
    private val repository: SensorRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<LevelToolUiState, LevelToolEvent>(
    initialState = LevelToolUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "LevelToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.BUBBLE_LEVEL)
    private var sensorJob: Job? = null
    private var watchJob: Job? = null

    override fun handleEvent(event: LevelToolEvent) {
        when (event) {
            LevelToolEvent.Open -> open()
            LevelToolEvent.StartSensor -> startSensor()
            LevelToolEvent.StopSensor -> stopSensor()
            LevelToolEvent.Dismiss -> dismiss()
        }
    }

    private fun open() {
        watchJob = watchJob.restart {
            launchReport(action = Actions.OPEN) {
                delay(ToolUsageTracker.WATCHED_USE_DELAY_MS.milliseconds)
                usage.markUsed()
            }
        }
    }

    private fun startSensor() {
        sensorJob = sensorJob.restart {
            repository.getLevelOrientation().collectReport(action = Actions.OBSERVE_ORIENTATION) { (pitch, roll) ->
                setState { copy(pitch = pitch, roll = roll) }
            }
        }
    }

    private fun stopSensor() {
        sensorJob?.cancel()
        sensorJob = null
    }

    private fun dismiss() {
        stopSensor()
        watchJob?.cancel()
        watchJob = null
        usage.endSession()
    }

    private object Actions {
        const val OPEN: String = "openLevel"
        const val OBSERVE_ORIENTATION: String = "observeOrientation"
    }
}
