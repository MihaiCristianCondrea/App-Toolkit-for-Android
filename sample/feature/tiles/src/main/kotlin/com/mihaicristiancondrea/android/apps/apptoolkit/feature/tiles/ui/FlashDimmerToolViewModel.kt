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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchPreset
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.MorseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.TorchRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.FlashDimmerToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.FlashDimmerToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel

/**
 * Sets the torch strength. Any change stops Morse or SOS playback first, so the two never fight
 * over the torch, and closing the sheet turns the torch off.
 */
class FlashDimmerToolViewModel(
    private val torchRepository: TorchRepository,
    private val morseRepository: MorseRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<FlashDimmerToolUiState, FlashDimmerToolEvent>(
    initialState = FlashDimmerToolUiState(torch = torchRepository.state.value),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "FlashDimmerToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.FLASH_DIMMER)

    init {
        observeTorch()
    }

    override fun handleEvent(event: FlashDimmerToolEvent) {
        when (event) {
            is FlashDimmerToolEvent.LevelChanged -> setLevel(event.level)
            is FlashDimmerToolEvent.PresetSelected -> applyPreset(event.preset)
            FlashDimmerToolEvent.Dismiss -> dismiss()
        }
    }

    private fun observeTorch() {
        torchRepository.state.collectReport(action = Actions.OBSERVE_TORCH) { torch ->
            setState { copy(torch = torch) }
        }
    }

    private fun setLevel(level: Int) {
        launchReport(action = Actions.SET_LEVEL) {
            morseRepository.stop()
            torchRepository.setLevel(level)
            usage.markUsed()
        }
    }

    private fun applyPreset(preset: TorchPreset) {
        launchReport(action = Actions.APPLY_PRESET) {
            morseRepository.stop()
            torchRepository.applyPreset(preset)
            usage.markUsed()
        }
    }

    private fun dismiss() {
        usage.endSession()
        launchReport(action = Actions.TURN_OFF) {
            morseRepository.stop()
            torchRepository.turnOff()
        }
    }

    private object Actions {
        const val OBSERVE_TORCH: String = "observeTorch"
        const val SET_LEVEL: String = "setTorchLevel"
        const val APPLY_PRESET: String = "applyTorchPreset"
        const val TURN_OFF: String = "turnOffTorch"
    }
}
