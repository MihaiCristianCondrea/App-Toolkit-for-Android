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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.preferences.ToolkitTilesPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.quicksettings.QuickSettingsTilesLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.ToolkitQuickTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.ToolkitTileCategoryData
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.ToolkitTileData
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.ToolkitTileStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class DefaultToolkitTilesRepository(
    private val torchRepository: TorchRepository,
    private val preferencesDataSource: ToolkitTilesPreferencesDataSource,
    private val quickSettingsDataSource: QuickSettingsTilesLocalDataSource,
) : ToolkitTilesRepository {

    override fun tileCategories(): Flow<ImmutableList<ToolkitTileCategoryData>> =
        flowOf(currentTileCategories())

    override val expandedCategoryIds: Flow<Set<String>> =
        preferencesDataSource.expandedCategoryIds.map { persistedIds ->
            persistedIds ?: catalogue
                .filter(ToolkitTileCategoryData::initiallyExpanded)
                .mapTo(mutableSetOf(), ToolkitTileCategoryData::id)
        }

    override suspend fun saveExpandedCategoryIds(categoryIds: Set<String>) {
        preferencesDataSource.saveExpandedCategoryIds(categoryIds)
    }

    override fun currentTileCategories(): ImmutableList<ToolkitTileCategoryData> {
        val activeTiles = quickSettingsDataSource.activeTileComponents()
        return filterUnavailableTorchTools(
            categories = catalogue,
            isTorchAvailable = torchRepository.state.value.capabilities.isAvailable,
        ).map { category ->
            category.copy(
                tiles = category.tiles.map { tile ->
                    val componentName = tile.requestKey?.let(quickSettingsDataSource::componentName)
                    if (componentName != null && componentName in activeTiles) {
                        tile.copy(status = ToolkitTileStatus.Added)
                    } else tile
                }.toImmutableList(),
            )
        }.toImmutableList()
    }

    private val catalogue: ImmutableList<ToolkitTileCategoryData> = persistentListOf(
        ToolkitTileCategoryData(
            id = "sensors",
            initiallyExpanded = true,
            tiles = persistentListOf(
                tile("bubble_level", ToolkitTileStatus.NeedsSetup),
                tile("compass", ToolkitTileStatus.NeedsSetup),
                tile("lux_meter", ToolkitTileStatus.NeedsSetup),
            ),
        ),
        ToolkitTileCategoryData(
            id = "utilities",
            tiles = persistentListOf(
                tile("coin_flip", requestKey = "coin_flip"),
                tile("dice_roll", requestKey = "dice_roll"),
                tile("counter", requestKey = "counter"),
                tile("material_colors", quickTool = ToolkitQuickTool.MaterialColors),
                tile("music_search"),
                tile("morse"),
            ),
        ),
        ToolkitTileCategoryData(
            id = "system",
            tiles = persistentListOf(
                tile("caffeine"),
                tile("sound_mode"),
                tile("flash_dimmer", requestKey = "flash_dimmer"),
            ),
        ),
        ToolkitTileCategoryData(
            id = "wellbeing",
            tiles = persistentListOf(tile("breathing"), tile("sos")),
        ),
    )
}

private fun tile(
    id: String,
    status: ToolkitTileStatus = ToolkitTileStatus.Available,
    quickTool: ToolkitQuickTool? = null,
    requestKey: String? = null,
) = ToolkitTileData(id = id, status = status, quickTool = quickTool, requestKey = requestKey)

internal fun filterUnavailableTorchTools(
    categories: List<ToolkitTileCategoryData>,
    isTorchAvailable: Boolean,
): List<ToolkitTileCategoryData> {
    if (isTorchAvailable) return categories
    return categories.mapNotNull { category ->
        val visibleTiles = category.tiles.filterNot { it.id in TORCH_TOOL_IDS }.toImmutableList()
        category.copy(tiles = visibleTiles).takeIf { visibleTiles.isNotEmpty() }
    }
}

private val TORCH_TOOL_IDS = setOf("flash_dimmer", "morse", "sos")
