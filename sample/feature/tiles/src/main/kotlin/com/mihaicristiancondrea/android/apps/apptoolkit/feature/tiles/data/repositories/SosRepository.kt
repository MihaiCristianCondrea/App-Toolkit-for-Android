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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.MorsePlaybackState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/** Provides the fixed SOS action through the shared Morse playback owner. */
class SosRepository(
    private val morseRepository: MorseRepository,
) {
    val state: StateFlow<MorsePlaybackState> = morseRepository.state

    /** Emits [isActive] now and on every change, so a custom Morse message never reads as SOS. */
    fun observeActive(): Flow<Boolean> = state.map { playback -> playback.isSos() }.distinctUntilChanged()

    fun toggle() {
        if (isActive) {
            stop()
        } else {
            morseRepository.start(SOS_MESSAGE)
        }
    }

    fun stop() {
        if (isActive) morseRepository.stop()
    }

    val isActive: Boolean
        get() = state.value.isSos()

    private fun MorsePlaybackState.isSos(): Boolean = isActive && message == SOS_MESSAGE

    private companion object {
        const val SOS_MESSAGE: String = "SOS"
    }
}
