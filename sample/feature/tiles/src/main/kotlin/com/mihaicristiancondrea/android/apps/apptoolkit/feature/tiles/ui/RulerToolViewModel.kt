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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerSettings
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.RulerRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RulerToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RulerToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel

/**
 * Owns the ruler's reading, range and calibration draft, and saves its calibration and set-up.
 *
 * The set-up changes the moment it is chosen and is saved straight away; the calibration is saved
 * when the slider is let go, and until then survives process death as a draft.
 */
class RulerToolViewModel(
    private val repository: RulerRepository,
    telemetryRepository: TelemetryRepository,
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : LoggedScreenViewModel<RulerToolUiState, RulerToolEvent>(
    initialState = RulerToolUiState(
        scale = savedStateHandle[DRAFT_SCALE] ?: 1f,
        marker = savedStateHandle[MARKER] ?: 0.5f,
        start = savedStateHandle[START] ?: 0f,
        end = savedStateHandle[END] ?: 0.5f,
    ),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "RulerToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.RULER)

    init {
        repository.scale.collectReport(action = Actions.LOAD, onError = { setState { copy(saveFailed = true) } }) { scale ->
            if (!savedStateHandle.contains(DRAFT_SCALE)) setState { copy(scale = scale) }
        }
        repository.settings.collectReport(action = Actions.LOAD_SETTINGS) { settings ->
            setState { copy(settings = settings) }
        }
    }

    override fun handleEvent(event: RulerToolEvent) {
        when (event) {
            is RulerToolEvent.MarkerMoved -> {
                if (!event.position.isFinite()) return
                setState { copy(marker = event.position.coerceIn(0f, 1f)) }
                usage.markUsed()
            }
            is RulerToolEvent.RangeChanged -> {
                if (!event.start.isFinite() || !event.end.isFinite()) return
                setState { copy(start = event.start.coerceIn(0f, 1f), end = event.end.coerceIn(event.start.coerceIn(0f, 1f), 1f)) }
                usage.markUsed()
            }
            is RulerToolEvent.ScaleChanged -> {
                if (!event.scale.isFinite()) return
                setState { copy(scale = event.scale.coerceIn(0.5f, 2f), saveFailed = false) }
                savedStateHandle[DRAFT_SCALE] = currentState.scale
            }
            RulerToolEvent.SaveScale -> saveScale()
            RulerToolEvent.ResetScale -> {
                setState { copy(scale = 1f, saveFailed = false) }
                saveScale()
            }
            is RulerToolEvent.AdvancedChanged -> updateSettings { copy(advanced = event.advanced) }
            is RulerToolEvent.UnitsSelected -> updateSettings { copy(units = event.units) }
            is RulerToolEvent.EdgeSelected -> updateSettings { copy(edge = event.edge) }
            is RulerToolEvent.CountFromEndChanged -> updateSettings { copy(countFromEnd = event.countFromEnd) }
            is RulerToolEvent.InchDivisionsSelected -> {
                if (event.divisions !in RulerSettings.InchDivisions) return
                updateSettings { copy(inchDivisions = event.divisions) }
            }
            RulerToolEvent.Dismiss -> usage.endSession()
        }
        savedStateHandle[MARKER] = currentState.marker
        savedStateHandle[START] = currentState.start
        savedStateHandle[END] = currentState.end
    }

    private fun saveScale() {
        val scale = currentState.scale
        launchReport(action = Actions.SAVE, onError = { setState { copy(saveFailed = true) } }) {
            repository.saveScale(scale)
            savedStateHandle.remove<Float>(DRAFT_SCALE)
        }
    }

    private fun updateSettings(change: RulerSettings.() -> RulerSettings) {
        setState { copy(settings = settings.change(), saveFailed = false) }
        val settings = currentState.settings
        launchReport(action = Actions.SAVE_SETTINGS, onError = { setState { copy(saveFailed = true) } }) {
            repository.saveSettings(settings)
        }
    }

    private object Actions {
        const val LOAD = "loadRulerCalibration"
        const val SAVE = "saveRulerCalibration"
        const val LOAD_SETTINGS = "loadRulerSettings"
        const val SAVE_SETTINGS = "saveRulerSettings"
    }

    private companion object {
        const val DRAFT_SCALE = "draftScale"
        const val MARKER = "marker"
        const val START = "start"
        const val END = "end"
    }
}
