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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchCapabilities
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchPreset
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** An in-memory torch with four strength levels by default. */
internal class FakeTorchRepository(
    capabilities: TorchCapabilities = TorchCapabilities(isAvailable = true, maximumLevel = 4),
) : TorchRepository {
    private val mutableState = MutableStateFlow(TorchState(capabilities = capabilities))
    override val state: StateFlow<TorchState> = mutableState.asStateFlow()

    override fun setLevel(level: Int) {
        mutableState.update { torch ->
            torch.copy(currentLevel = level.coerceIn(0, torch.capabilities.maximumLevel))
        }
    }

    override fun applyPreset(preset: TorchPreset) {
        val maximum: Int = state.value.capabilities.maximumLevel
        setLevel(
            when (preset) {
                TorchPreset.Off -> 0
                TorchPreset.Minimum -> 1
                TorchPreset.Half -> (maximum + 1) / 2
                TorchPreset.Maximum -> maximum
            },
        )
    }

    override fun cyclePreset() = Unit

    override fun turnOff() = setLevel(0)

    override fun clearError() {
        mutableState.update { torch -> torch.copy(error = null) }
    }
}
