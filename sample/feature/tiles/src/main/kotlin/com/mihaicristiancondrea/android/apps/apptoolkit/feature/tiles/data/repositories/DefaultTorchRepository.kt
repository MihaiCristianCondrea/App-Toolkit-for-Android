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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.torch.TorchDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchPreset
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/** App-scoped source of truth for torch capabilities and current strength. */
class DefaultTorchRepository(
    private val dataSource: TorchDataSource,
    dispatchers: DispatcherProvider,
) : TorchRepository {

    private val scope = CoroutineScope(SupervisorJob() + dispatchers.default)
    private val mutableState = MutableStateFlow(TorchState(capabilities = dataSource.capabilities))
    override val state: StateFlow<TorchState> = mutableState.asStateFlow()

    init {
        dataSource.hardwareState
            .onEach { hardware ->
                mutableState.value = mutableState.value.copy(
                    currentLevel = if (hardware.isEnabled) hardware.currentLevel else 0,
                    error = null,
                )
            }
            .catch { error -> mutableState.value = mutableState.value.copy(error = error) }
            .launchIn(scope)
    }

    override fun setLevel(level: Int) {
        val capabilities = state.value.capabilities
        if (!capabilities.isAvailable) return
        runCatching { dataSource.setLevel(level.coerceIn(0, capabilities.maximumLevel)) }
            .onSuccess {
                mutableState.value = mutableState.value.copy(
                    currentLevel = level.coerceIn(0, capabilities.maximumLevel),
                    error = null,
                )
            }
            .onFailure { error -> mutableState.value = mutableState.value.copy(error = error) }
    }

    override fun applyPreset(preset: TorchPreset) {
        val maximum = state.value.capabilities.maximumLevel
        val level = when (preset) {
            TorchPreset.Off -> 0
            TorchPreset.Minimum -> 1
            TorchPreset.Half -> (maximum + 1) / 2
            TorchPreset.Maximum -> maximum
        }
        setLevel(level)
    }

    override fun cyclePreset() {
        val levels = presetLevels()
        if (levels.isEmpty()) return
        val current = state.value.currentLevel
        val next = levels.firstOrNull { it > current } ?: 0
        setLevel(next)
    }

    override fun turnOff() = setLevel(0)

    override fun clearError() {
        mutableState.value = mutableState.value.copy(error = null)
    }

    private fun presetLevels(): List<Int> {
        val maximum = state.value.capabilities.maximumLevel
        if (maximum <= 0) return emptyList()
        return listOf(1, (maximum + 1) / 2, maximum).distinct().sorted()
    }
}
