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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchPreset
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.BreathingRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.CaffeineRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.MorseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.SensorRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.SosRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.SystemRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.TorchRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.models.BreathingState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.models.CaffeineState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.models.RingerMode
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CoinFlipToolState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.DiceRollToolState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.LevelToolState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.MorseInputError
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.MorseToolState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale
import kotlin.random.Random

class CoinFlipToolViewModel : ViewModel() {
    private val mutableState = MutableStateFlow(CoinFlipToolState())
    val state: StateFlow<CoinFlipToolState> = mutableState.asStateFlow()
    fun flip() {
        mutableState.value = CoinFlipToolState(Random.nextBoolean(), state.value.request + 1)
    }

    fun dismiss() {
        mutableState.value = CoinFlipToolState()
    }
}

class DiceRollToolViewModel : ViewModel() {
    private val mutableState = MutableStateFlow(DiceRollToolState())
    val state: StateFlow<DiceRollToolState> = mutableState.asStateFlow()
    fun roll() {
        mutableState.value = DiceRollToolState(Random.nextInt(1, 7), state.value.request + 1)
    }

    fun dismiss() {
        mutableState.value = DiceRollToolState()
    }
}

class CounterToolViewModel : ViewModel() {
    private val mutableCount = MutableStateFlow(0)
    val count: StateFlow<Int> = mutableCount.asStateFlow()
    fun increment() {
        mutableCount.value++
    }

    fun reset() {
        mutableCount.value = 0
    }

    fun dismiss() = reset()
}

abstract class FlowToolViewModel<T>(initial: T) : ViewModel() {
    protected val mutableState = MutableStateFlow(initial)
    val state: StateFlow<T> = mutableState.asStateFlow()
    protected var observation: Job? = null
    fun dismiss() {
        observation?.cancel(); observation = null
    }
}

class CompassToolViewModel(private val repository: SensorRepository) :
    FlowToolViewModel<Float>(0f) {
    fun open() {
        observation?.cancel(); observation =
            repository.getCompassAzimuth().onEach { mutableState.value = it }
                .launchIn(viewModelScope)
    }
}

class LevelToolViewModel(private val repository: SensorRepository) :
    FlowToolViewModel<LevelToolState>(LevelToolState()) {
    fun open() {
        observation?.cancel(); observation = repository.getLevelOrientation()
            .onEach { mutableState.value = LevelToolState(it.first, it.second) }
            .launchIn(viewModelScope)
    }
}

class BreathingToolViewModel(private val repository: BreathingRepository) :
    FlowToolViewModel<BreathingState>(BreathingState()) {
    fun open() {
        observation?.cancel()
        repository.start()
        observation =
            repository.breathingState.onEach { mutableState.value = it }.launchIn(viewModelScope)
    }

    fun close() {
        dismiss(); repository.stop(); mutableState.value = BreathingState()
    }
}

class CaffeineToolViewModel(private val repository: CaffeineRepository) : ViewModel() {
    val state: StateFlow<CaffeineState> = repository.currentState
    fun cycle() = repository.cycleState()
}

class SoundModeToolViewModel(private val repository: SystemRepository) :
    FlowToolViewModel<RingerMode>(RingerMode.Normal) {
    fun open() {
        observation?.cancel(); observation =
            repository.getRingerMode().onEach { mutableState.value = it }.launchIn(viewModelScope)
    }

    fun cycle() {
        val next = when (state.value) {
            RingerMode.Normal -> RingerMode.Vibrate
            RingerMode.Vibrate, RingerMode.Silent -> RingerMode.Normal
        }
        repository.setRingerMode(next)
    }
}

class MusicSearchToolViewModel(private val repository: SystemRepository) : ViewModel() {
    fun launch() = repository.launchMusicSearch()
}

class SosToolViewModel(private val repository: SosRepository) : ViewModel() {
    val state = repository.state
    fun toggle() = repository.toggle()
    fun dismiss() = repository.stop()
}

class MorseToolViewModel(private val repository: MorseRepository) : ViewModel() {
    private val mutableState = MutableStateFlow(MorseToolState(playback = repository.state.value))
    val state: StateFlow<MorseToolState> = mutableState.asStateFlow()

    init {
        repository.state
            .onEach { playback ->
                mutableState.value = mutableState.value.copy(playback = playback)
            }
            .launchIn(viewModelScope)
    }

    fun updateInput(input: String) {
        mutableState.value = mutableState.value.copy(input = input, inputError = null)
    }

    fun toggle() {
        if (state.value.playback.isActive) {
            repository.stop()
            return
        }

        val message = state.value.input.trim()
        val error = when {
            message.isEmpty() -> MorseInputError.Empty
            message.length > MorseRepository.MAXIMUM_MESSAGE_LENGTH -> MorseInputError.TooLong
            !MorseRepository.isSupportedMessage(message.uppercase(Locale.ROOT)) ->
                MorseInputError.UnsupportedCharacters

            else -> null
        }
        mutableState.value = mutableState.value.copy(inputError = error)
        if (error == null) repository.start(message)
    }

    fun dismiss() = repository.stop()
}

class FlashDimmerToolViewModel(
    private val torchRepository: TorchRepository,
    private val morseRepository: MorseRepository,
) : ViewModel() {
    val state: StateFlow<TorchState> = torchRepository.state
    fun setLevel(level: Int) {
        morseRepository.stop(); torchRepository.setLevel(level)
    }

    fun applyPreset(preset: TorchPreset) {
        morseRepository.stop(); torchRepository.applyPreset(preset)
    }

    fun dismiss() {
        morseRepository.stop(); torchRepository.turnOff()
    }
}
