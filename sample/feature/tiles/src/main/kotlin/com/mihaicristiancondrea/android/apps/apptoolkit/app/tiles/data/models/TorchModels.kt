/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models

/** Device capabilities relevant to steady and patterned torch operation. */
data class TorchCapabilities(
    val isAvailable: Boolean = false,
    val maximumLevel: Int = 0,
) {
    val supportsDimming: Boolean
        get() = maximumLevel > 1
}

/** Current torch state exposed to in-app tools and the Quick Settings service. */
data class TorchState(
    val capabilities: TorchCapabilities = TorchCapabilities(),
    val currentLevel: Int = 0,
    val error: Throwable? = null,
) {
    val isEnabled: Boolean
        get() = currentLevel > 0
}

/** Named controls shared by the dimmer UI and Quick Settings tile. */
enum class TorchPreset {
    Off,
    Minimum,
    Half,
    Maximum,
}

