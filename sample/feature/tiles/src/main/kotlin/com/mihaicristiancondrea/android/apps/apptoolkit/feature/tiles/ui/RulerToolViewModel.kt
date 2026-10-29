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
import androidx.lifecycle.SavedStateHandle
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
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : LoggedScreenViewModel<RulerToolUiState, RulerToolEvent>(
    initialState = RulerToolUiState(scale = savedStateHandle["draftScale"] ?: 1f,
        start = savedStateHandle["start"] ?: 0f, end = savedStateHandle["end"] ?: .5f, inches = savedStateHandle["inches"] ?: false),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "RulerToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.RULER)

    init {
        repository.scale.collectReport(action = Actions.LOAD, onError = { setState { copy(saveFailed = true) } }) { scale ->
            if (!savedStateHandle.contains("draftScale")) setState { copy(scale = scale) }
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
                if (!event.scale.isFinite()) return
                setState { copy(scale = event.scale.coerceIn(0.5f, 2f), saveFailed = false) }
            }
            RulerToolEvent.SaveScale -> {
                val scale = currentState.scale
                launchReport(action = Actions.SAVE, onError = { setState { copy(saveFailed = true) } }) {
                    repository.saveScale(scale)
                    savedStateHandle.remove<Float>("draftScale")
                }
            }
            RulerToolEvent.ToggleUnits -> setState { copy(inches = !inches) }
            RulerToolEvent.Dismiss -> {
                usage.endSession()
            }
        }
        savedStateHandle["start"] = currentState.start
        savedStateHandle["end"] = currentState.end
        savedStateHandle["inches"] = currentState.inches
        if (event is RulerToolEvent.ScaleChanged) savedStateHandle["draftScale"] = currentState.scale
    }

    private object Actions {
        const val LOAD = "loadRulerCalibration"
        const val SAVE = "saveRulerCalibration"
    }
}
