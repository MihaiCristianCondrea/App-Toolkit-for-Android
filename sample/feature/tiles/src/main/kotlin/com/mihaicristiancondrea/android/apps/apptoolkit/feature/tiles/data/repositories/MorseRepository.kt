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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchPreset
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

/** Owns custom and SOS Morse playback so only one patterned torch job can run at a time. */
class MorseRepository(
    private val torchRepository: TorchRepository,
    dispatchers: DispatcherProvider,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatchers.default)
    private val mutableState = MutableStateFlow(
        MorsePlaybackState(isAvailable = torchRepository.state.value.capabilities.isAvailable)
    )
    val state: StateFlow<MorsePlaybackState> = mutableState.asStateFlow()

    private var playbackJob: Job? = null
    private var playbackGeneration: Long = 0

    fun start(message: String): Boolean {
        val normalizedMessage = message.trim().uppercase(Locale.ROOT)
        if (!state.value.isAvailable || !isSupportedMessage(normalizedMessage)) return false

        stop()
        val generation = ++playbackGeneration
        mutableState.value = MorsePlaybackState(
            isAvailable = true,
            isActive = true,
            message = normalizedMessage,
        )
        playbackJob = scope.launch {
            try {
                while (currentCoroutineContext().isActive) {
                    flashOnce(normalizedMessage)
                    delay(wordGap)
                }
            } finally {
                if (generation == playbackGeneration && mutableState.value.isActive) {
                    mutableState.value = MorsePlaybackState(isAvailable = true)
                    playbackJob = null
                    withContext(NonCancellable) { torchRepository.turnOff() }
                }
            }
        }
        return true
    }

    fun stop() {
        if (!state.value.isActive && playbackJob == null) return
        playbackGeneration++
        mutableState.value = MorsePlaybackState(isAvailable = state.value.isAvailable)
        playbackJob?.cancel()
        playbackJob = null
        torchRepository.turnOff()
    }

    private suspend fun flashOnce(message: String) {
        val words = message.split(Regex(" +"))
        words.forEachIndexed { wordIndex, word ->
            word.forEachIndexed { characterIndex, character ->
                val code = MORSE_ALPHABET.getValue(character)
                mutableState.value = mutableState.value.copy(
                    currentCharacter = character,
                    currentCode = code,
                )
                code.forEachIndexed { symbolIndex, symbol ->
                    torchRepository.applyPreset(TorchPreset.Maximum)
                    delay(if (symbol == '.') dotDuration else dashDuration)
                    torchRepository.turnOff()
                    if (symbolIndex < code.lastIndex) delay(symbolGap)
                }
                if (characterIndex < word.lastIndex) delay(characterGap)
            }
            if (wordIndex < words.lastIndex) delay(wordGap)
        }
    }

    internal companion object {
        const val MAXIMUM_MESSAGE_LENGTH: Int = 200
        val MORSE_ALPHABET: Map<Char, String> = mapOf(
            'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".",
            'F' to "..-.", 'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---",
            'K' to "-.-", 'L' to ".-..", 'M' to "--", 'N' to "-.", 'O' to "---",
            'P' to ".--.", 'Q' to "--.-", 'R' to ".-.", 'S' to "...", 'T' to "-",
            'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-", 'Y' to "-.--",
            'Z' to "--..", '1' to ".----", '2' to "..---", '3' to "...--",
            '4' to "....-", '5' to ".....", '6' to "-....", '7' to "--...",
            '8' to "---..", '9' to "----.", '0' to "-----",
        )

        fun isSupportedMessage(message: String): Boolean =
            message.isNotEmpty() && message.all { it == ' ' || it in MORSE_ALPHABET }

        private val unitDuration = 200.milliseconds
        private val dotDuration = unitDuration
        private val dashDuration = unitDuration * 3
        private val symbolGap = unitDuration
        private val characterGap = unitDuration * 3
        private val wordGap = unitDuration * 7
    }
}
