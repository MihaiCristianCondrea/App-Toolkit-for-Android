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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.RulerRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RulerToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RulerToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel

class RulerToolViewModel(
    private val repository: RulerRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<RulerToolUiState, RulerToolEvent>(
    initialState = RulerToolUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "RulerToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.RULER)
    private var savedScale = 1f

    init {
        repository.scale.collectReport(action = Actions.LOAD, onError = { setState { copy(saveFailed = true) } }) { scale ->
            savedScale = scale
            setState { copy(scale = scale) }
        }
    }

    override fun handleEvent(event: RulerToolEvent) {
        when (event) {
            is RulerToolEvent.RangeChanged -> {
                if (!event.start.isFinite() || !event.end.isFinite()) return
                setState { copy(start = event.start.coerceIn(0f, 1f), end = event.end.coerceIn(event.start.coerceIn(0f, 1f), 1f)) }
                usage.markUsed()
            }
            is RulerToolEvent.ScaleChanged -> {
                if (event.scale.isFinite()) setState { copy(scale = event.scale.coerceIn(0.5f, 2f), saveFailed = false) }
            }
            RulerToolEvent.SaveScale -> {
                val scale = currentState.scale
                launchReport(action = Actions.SAVE, onError = { setState { copy(saveFailed = true) } }) {
                    repository.saveScale(scale)
                }
            }
            RulerToolEvent.ToggleUnits -> setState { copy(inches = !inches) }
            RulerToolEvent.Dismiss -> {
                setState { RulerToolUiState(scale = savedScale) }
                usage.endSession()
            }
        }
    }

    private object Actions {
        const val LOAD = "loadRulerCalibration"
        const val SAVE = "saveRulerCalibration"
    }
}
