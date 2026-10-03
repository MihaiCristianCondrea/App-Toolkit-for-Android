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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.SosRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.SosToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.SosToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel

/**
 * Starts and stops SOS through the shared Morse playback. Only starting SOS counts as use, and
 * closing the sheet stops it.
 */
class SosToolViewModel(
    private val repository: SosRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<SosToolUiState, SosToolEvent>(
    initialState = SosToolUiState(isActive = repository.isActive),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "SosToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.SOS)

    init {
        observeActive()
    }

    override fun handleEvent(event: SosToolEvent) {
        when (event) {
            SosToolEvent.Toggle -> toggle()
            SosToolEvent.Dismiss -> dismiss()
        }
    }

    private fun observeActive() {
        repository.observeActive().collectReport(action = Actions.OBSERVE_SOS) { isActive ->
            setState { copy(isActive = isActive) }
        }
    }

    private fun toggle() {
        launchReport(action = Actions.TOGGLE_SOS) {
            val starting: Boolean = !repository.isActive
            repository.toggle()
            if (starting) usage.markUsed()
        }
    }

    private fun dismiss() {
        usage.endSession()
        launchReport(action = Actions.STOP_SOS) {
            repository.stop()
        }
    }

    private object Actions {
        const val OBSERVE_SOS: String = "observeSos"
        const val TOGGLE_SOS: String = "toggleSos"
        const val STOP_SOS: String = "stopSos"
    }
}
