/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models

/** Observable state for the single patterned flashlight playback session. */
data class MorsePlaybackState(
    val isAvailable: Boolean = false,
    val isActive: Boolean = false,
    val message: String = "",
    val currentCharacter: Char? = null,
    val currentCode: String = "",
)

