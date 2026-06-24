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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui.state

import androidx.compose.runtime.Immutable
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileCategory
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.BreathingState
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.MemoryInfo
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.NetworkTraffic
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

/** State rendered by the Toolkit Tiles screen. */
@Immutable
data class ToolkitTilesUiState(
    val categories: ImmutableList<ToolkitTileCategory> = persistentListOf(),
    val selectedFilter: ToolkitTilesFilter = ToolkitTilesFilter.All,
    val expandedCategoryIds: PersistentSet<String> = persistentSetOf(),
    val sensorData: ToolkitSensorData = ToolkitSensorData(),
    val breathingState: BreathingState = BreathingState(),
    val memoryInfo: MemoryInfo? = null,
    val networkTraffic: NetworkTraffic? = null,
)

@Immutable
data class ToolkitSensorData(
    val compassAzimuth: Float = 0f,
    val levelPitch: Float = 0f,
    val levelRoll: Float = 0f,
    val luxLevel: Float = 0f,
)

enum class ToolkitTilesFilter {
    All,
    Added,
    NeedsSetup,
    Unsupported;

    companion object {}
}
