/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.MorsePlaybackState

@Immutable
data class CoinFlipToolState(val isHeads: Boolean = true, val request: Int = 0)

@Immutable
data class DiceRollToolState(val result: Int = 1, val request: Int = 0)

@Immutable
data class LevelToolState(val pitch: Float = 0f, val roll: Float = 0f)

@Immutable
data class MorseToolState(
    val input: String = "",
    val inputError: MorseInputError? = null,
    val playback: MorsePlaybackState = MorsePlaybackState(),
)

enum class MorseInputError {
    Empty,
    TooLong,
    UnsupportedCharacters,
}
