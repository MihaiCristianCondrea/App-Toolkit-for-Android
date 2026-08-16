/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.TorchPreset
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.TorchState
import kotlinx.coroutines.flow.StateFlow

interface TorchRepository {
    val state: StateFlow<TorchState>

    fun setLevel(level: Int)
    fun applyPreset(preset: TorchPreset)
    fun cyclePreset()
    fun turnOff()
    fun clearError()
}

