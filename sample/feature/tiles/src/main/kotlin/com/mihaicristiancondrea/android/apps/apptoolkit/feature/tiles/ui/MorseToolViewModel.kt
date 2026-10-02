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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.MorseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.MorseToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.MorseInputError
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.MorseToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import java.util.Locale

/**
 * Flashes a typed message in Morse code through the shared playback, which SOS also drives. A
 * message that cannot be sent shows why in the state and is not counted as use. Closing the sheet
 * stops the playback.
 */
class MorseToolViewModel(
    private val repository: MorseRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<MorseToolUiState, MorseToolEvent>(
    initialState = MorseToolUiState(playback = repository.state.value),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "MorseToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.MORSE)

    init {
        observePlayback()
    }

    override fun handleEvent(event: MorseToolEvent) {
        when (event) {
            is MorseToolEvent.InputChanged -> setState { copy(input = event.input, inputError = null) }
            MorseToolEvent.Toggle -> toggle()
            MorseToolEvent.Dismiss -> dismiss()
        }
    }

    private fun observePlayback() {
        repository.state.collectReport(action = Actions.OBSERVE_PLAYBACK) { playback ->
            setState { copy(playback = playback) }
        }
    }

    private fun toggle() {
        if (currentState.playback.isActive) {
            stopMessage()
            return
        }

        val message: String = currentState.input.trim()
        val error: MorseInputError? = message.inputError()
        setState { copy(inputError = error) }
        if (error != null) return

        launchReport(action = Actions.START_MESSAGE) {
            repository.start(message)
            usage.markUsed()
        }
    }

    private fun dismiss() {
        stopMessage()
        usage.endSession()
    }

    private fun stopMessage() {
        launchReport(action = Actions.STOP_MESSAGE) {
            repository.stop()
        }
    }

    private fun String.inputError(): MorseInputError? = when {
        isEmpty() -> MorseInputError.Empty
        length > MorseRepository.MAXIMUM_MESSAGE_LENGTH -> MorseInputError.TooLong
        !MorseRepository.isSupportedMessage(uppercase(Locale.ROOT)) -> MorseInputError.UnsupportedCharacters
        else -> null
    }

    private object Actions {
        const val OBSERVE_PLAYBACK: String = "observePlayback"
        const val START_MESSAGE: String = "startMorse"
        const val STOP_MESSAGE: String = "stopMorse"
    }
}
