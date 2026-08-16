/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.torch

import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.TorchCapabilities
import kotlinx.coroutines.flow.Flow

data class TorchHardwareState(
    val isEnabled: Boolean,
    val currentLevel: Int,
)

/** The single Android platform source used for torch discovery, observation, and commands. */
interface TorchDataSource {
    val capabilities: TorchCapabilities
    val hardwareState: Flow<TorchHardwareState>

    fun setLevel(level: Int)
}

