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

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.BellRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.BellToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.BellToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

internal class BellToolViewModel(
    private val repository: BellRepository,
) : ScreenViewModel<BellToolUiState, BellToolEvent>(initialState = BellToolUiState()) {
    private val strikes = MutableSharedFlow<Float>(extraBufferCapacity = 8)
    private var session: Job? = null

    override fun handleEvent(event: BellToolEvent) {
        when (event) {
            BellToolEvent.Open -> open()
            BellToolEvent.Close -> {
                session?.cancel()
                session = null
                setState { BellToolUiState() }
            }
            BellToolEvent.Ring -> if (currentState.audioReady) strikes.tryEmit(1f) else Unit
        }
    }

    private fun open() {
        if (session?.isActive == true) return
        session = viewModelScope.launch {
            launch {
                repository.shakes().collect { strength ->
                    if (strength == null) setState { copy(motionAvailable = false) }
                    else if (currentState.audioReady) strikes.tryEmit(strength)
                }
            }
            try {
                repository.play(
                    strikes = strikes,
                    onAudioReady = { ready -> setState { copy(audioReady = ready, audioFailed = !ready) } },
                    onStrike = { rate ->
                        val now = System.nanoTime()
                        setState { copy(strike = strike + 1, playbackRate = rate, strikeNanos = now) }
                    },
                )
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: RuntimeException) {
                setState { copy(audioReady = false, audioFailed = true) }
            }
        }
    }
}
