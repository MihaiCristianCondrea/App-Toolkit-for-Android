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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.MorsePlaybackState

/**
 * State rendered by the Morse tool.
 *
 * @property input The message the user typed.
 * @property inputError Why [input] cannot be sent, set when the user tries to send it.
 * @property playback The shared Morse playback, which SOS also drives.
 */
@Immutable
data class MorseToolUiState(
    val input: String = "",
    val inputError: MorseInputError? = null,
    val playback: MorsePlaybackState = MorsePlaybackState(),
)
