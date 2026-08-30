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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models

import androidx.compose.runtime.Immutable

/** Device capabilities relevant to steady and patterned torch operation. */
data class TorchCapabilities(
    val isAvailable: Boolean = false,
    val maximumLevel: Int = 0,
) {
    val supportsDimming: Boolean
        get() = maximumLevel > 1
}

/**
 * Current torch state exposed to in-app tools and the Quick Settings service.
 *
 * `@Immutable` because [error] is a `Throwable`, whose stability Compose cannot infer, which left
 * every composable taking a `TorchState` unskippable. Instances are built once per emission and
 * never mutated, so the promise holds.
 */
@Immutable
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

