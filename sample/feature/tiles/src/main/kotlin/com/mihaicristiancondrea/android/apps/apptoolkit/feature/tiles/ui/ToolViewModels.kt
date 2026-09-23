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

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchPreset
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.BreathingRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.CounterRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.MorseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.SensorRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.SosRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.TorchRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.models.BreathingPhase
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.models.BreathingState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.logReactionScore
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CoinFlipToolState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.DiceRollToolState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.LevelToolState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.MorseInputError
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.MorseToolState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionRating
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionTestPhase
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionTestToolState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class CoinFlipToolViewModel(firebaseController: FirebaseController) : ViewModel() {
    private val usage = ToolUsageTracker(firebaseController, ToolkitTileIds.COIN_FLIP)
    private val mutableState = MutableStateFlow(CoinFlipToolState())
    val state: StateFlow<CoinFlipToolState> = mutableState.asStateFlow()
    fun flip() {
        mutableState.value = CoinFlipToolState(Random.nextBoolean(), state.value.request + 1)
        usage.markUsed()
    }

    fun dismiss() {
        mutableState.value = CoinFlipToolState()
        usage.endSession()
    }
}

class DiceRollToolViewModel(firebaseController: FirebaseController) : ViewModel() {
    private val usage = ToolUsageTracker(firebaseController, ToolkitTileIds.DICE_ROLL)
    private val mutableState = MutableStateFlow(DiceRollToolState())
    val state: StateFlow<DiceRollToolState> = mutableState.asStateFlow()
    fun roll() {
        mutableState.value = DiceRollToolState(Random.nextInt(1, 7), state.value.request + 1)
        usage.markUsed()
    }

    fun dismiss() {
        mutableState.value = DiceRollToolState()
        usage.endSession()
    }
}

/** Shows the count shared with the Counter Quick Settings tile, so closing the sheet keeps it. */
class CounterToolViewModel(
    private val repository: CounterRepository,
    firebaseController: FirebaseController,
) : ViewModel() {
    private val usage = ToolUsageTracker(firebaseController, ToolkitTileIds.COUNTER)

    val count: StateFlow<Int> = repository.count.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0,
    )

    fun increment() {
        repository.increment()
        usage.markUsed()
    }

    fun reset() = repository.reset()

    fun dismiss() = usage.endSession()
}

abstract class FlowToolViewModel<T>(initial: T, protected val usage: ToolUsageTracker) :
    ViewModel() {
    protected val mutableState = MutableStateFlow(initial)
    val state: StateFlow<T> = mutableState.asStateFlow()
    protected var observation: Job? = null
    private var watchedUse: Job? = null

    /**
     * For a tool that is used by looking at it, such as the compass, reports use once the sheet
     * has stayed open for [WATCHED_USE_DELAY_MS]. Opening and closing it straight away is not use.
     */
    protected fun reportUseOnceWatched() {
        watchedUse?.cancel()
        watchedUse = viewModelScope.launch {
            delay(WATCHED_USE_DELAY_MS.milliseconds)
            usage.markUsed()
        }
    }

    fun dismiss() {
        observation?.cancel(); observation = null
        watchedUse?.cancel(); watchedUse = null
        usage.endSession()
    }

    companion object {
        const val WATCHED_USE_DELAY_MS: Long = 5_000L
    }
}

class CompassToolViewModel(
    private val repository: SensorRepository,
    firebaseController: FirebaseController,
) : FlowToolViewModel<Float>(0f, ToolUsageTracker(firebaseController, ToolkitTileIds.COMPASS)) {
    fun open() {
        observation?.cancel(); observation =
            repository.getCompassAzimuth().onEach { mutableState.value = it }
                .launchIn(viewModelScope)
        reportUseOnceWatched()
    }
}

class LevelToolViewModel(
    private val repository: SensorRepository,
    firebaseController: FirebaseController,
) : FlowToolViewModel<LevelToolState>(
    LevelToolState(),
    ToolUsageTracker(firebaseController, ToolkitTileIds.BUBBLE_LEVEL),
) {
    fun open() {
        observation?.cancel(); observation = repository.getLevelOrientation()
            .onEach { mutableState.value = LevelToolState(it.first, it.second) }
            .launchIn(viewModelScope)
        reportUseOnceWatched()
    }
}

class BreathingToolViewModel(
    private val repository: BreathingRepository,
    firebaseController: FirebaseController,
) : FlowToolViewModel<BreathingState>(
    BreathingState(),
    ToolUsageTracker(firebaseController, ToolkitTileIds.BREATHING),
) {
    fun open() {
        observation?.cancel()
        repository.start()
        observation = repository.breathingState.onEach { breathing ->
            mutableState.value = breathing
            // The hold after breathing out closes a cycle, so reaching it means one full breath.
            if (breathing.phase == BreathingPhase.HOLD_EMPTY) usage.markUsed()
        }.launchIn(viewModelScope)
    }

    fun close() {
        dismiss(); repository.stop(); mutableState.value = BreathingState()
    }
}

class SosToolViewModel(
    private val repository: SosRepository,
    firebaseController: FirebaseController,
) : ViewModel() {
    private val usage = ToolUsageTracker(firebaseController, ToolkitTileIds.SOS)
    val state = repository.state
    fun toggle() {
        val starting: Boolean = !repository.isActive
        repository.toggle()
        if (starting) usage.markUsed()
    }

    fun dismiss() {
        repository.stop()
        usage.endSession()
    }
}

class MorseToolViewModel(
    private val repository: MorseRepository,
    firebaseController: FirebaseController,
) : ViewModel() {
    private val usage = ToolUsageTracker(firebaseController, ToolkitTileIds.MORSE)
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
        if (error == null) {
            repository.start(message)
            usage.markUsed()
        }
    }

    fun dismiss() {
        repository.stop()
        usage.endSession()
    }
}

class FlashDimmerToolViewModel(
    private val torchRepository: TorchRepository,
    private val morseRepository: MorseRepository,
    firebaseController: FirebaseController,
) : ViewModel() {
    private val usage = ToolUsageTracker(firebaseController, ToolkitTileIds.FLASH_DIMMER)
    val state: StateFlow<TorchState> = torchRepository.state
    fun setLevel(level: Int) {
        morseRepository.stop(); torchRepository.setLevel(level)
        usage.markUsed()
    }

    fun applyPreset(preset: TorchPreset) {
        morseRepository.stop(); torchRepository.applyPreset(preset)
        usage.markUsed()
    }

    fun dismiss() {
        morseRepository.stop(); torchRepository.turnOff()
        usage.endSession()
    }
}

class ReactionTestToolViewModel(
    private val firebaseController: FirebaseController,
    private val timeProvider: () -> Long = SystemClock::elapsedRealtime,
) : ViewModel() {
    private val usage = ToolUsageTracker(firebaseController, ToolkitTileIds.REACTION_TEST)
    private val mutableState = MutableStateFlow(ReactionTestToolState())
    val state: StateFlow<ReactionTestToolState> = mutableState.asStateFlow()

    private var waitingJob: Job? = null
    private var signalTimeMs: Long = 0L

    fun startTest(delayMs: Long? = null) {
        if (state.value.phase == ReactionTestPhase.Waiting || state.value.phase == ReactionTestPhase.Signal) {
            return
        }
        waitingJob?.cancel()
        mutableState.value = state.value.copy(phase = ReactionTestPhase.Waiting)

        val actualDelay = delayMs ?: Random.nextLong(1500L, 5000L)
        waitingJob = viewModelScope.launch {
            delay(actualDelay.milliseconds)
            if (state.value.phase == ReactionTestPhase.Waiting) {
                signalTimeMs = timeProvider()
                mutableState.value = state.value.copy(phase = ReactionTestPhase.Signal)
            }
        }
    }

    fun handleTap() {
        when (state.value.phase) {
            ReactionTestPhase.Waiting -> {
                waitingJob?.cancel()
                mutableState.value = state.value.copy(phase = ReactionTestPhase.FalseStart)
            }

            ReactionTestPhase.Signal -> {
                val reactionTime = timeProvider() - signalTimeMs
                val updatedHistory = (state.value.history + reactionTime).takeLast(5).toImmutableList()
                val updatedBest = state.value.bestTimeMs?.let { minOf(it, reactionTime) } ?: reactionTime
                val updatedAverage = updatedHistory.average().toLong()
                val rating = when {
                    reactionTime < 200 -> ReactionRating.Lightning
                    reactionTime in 200..250 -> ReactionRating.Fast
                    reactionTime in 251..350 -> ReactionRating.Average
                    else -> ReactionRating.Slow
                }
                val nextRound = minOf(state.value.roundCount + 1, state.value.totalRounds)

                mutableState.value = state.value.copy(
                    phase = ReactionTestPhase.Result,
                    lastReactionTimeMs = reactionTime,
                    bestTimeMs = updatedBest,
                    averageTimeMs = updatedAverage,
                    history = updatedHistory,
                    roundCount = nextRound,
                    rating = rating,
                )
                firebaseController.logReactionScore(reactionTimeMs = reactionTime)
                usage.markUsed()
            }

            else -> {
                // Do nothing if tapped in Idle, Result, or FalseStart
            }
        }
    }

    fun resetSession() {
        waitingJob?.cancel()
        mutableState.value = ReactionTestToolState()
    }

    fun dismiss() {
        waitingJob?.cancel()
        mutableState.value = state.value.copy(phase = ReactionTestPhase.Idle)
        usage.endSession()
    }
}
