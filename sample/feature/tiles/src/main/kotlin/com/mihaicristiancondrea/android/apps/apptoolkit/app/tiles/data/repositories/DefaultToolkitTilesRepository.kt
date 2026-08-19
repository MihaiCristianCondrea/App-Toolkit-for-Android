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

import android.content.Context
import android.provider.Settings
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitQuickTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitToolKind
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.service.getTileServiceRequests
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** Serves the supported Toolkit Tiles catalog and reads active Quick Settings tiles. */
class DefaultToolkitTilesRepository(
    private val context: Context,
    private val torchRepository: TorchRepository,
) : ToolkitTilesRepository {

    override fun tileCategories(): Flow<ImmutableList<ToolkitTileCategory>> =
        flowOf(withCurrentStatuses(catalogue).toImmutableList())

    override fun withCurrentStatuses(
        categories: List<ToolkitTileCategory>,
    ): List<ToolkitTileCategory> {
        val activeTiles = activeQuickSettingsTiles()
        return filterUnavailableTorchTools(
            categories = categories,
            isTorchAvailable = torchRepository.state.value.capabilities.isAvailable,
        ).map { category ->
            category.copy(
                tiles = category.tiles.map { tile ->
                    val componentName = tile.requestKey?.let(::componentFlattenedName)
                    if (componentName != null && componentName in activeTiles) {
                        tile.copy(status = ToolkitTileStatus.Added)
                    } else {
                        tile
                    }
                }.toImmutableList()
            )
        }
    }

    private fun activeQuickSettingsTiles(): Set<String> = try {
        val tiles = Settings.Secure.getString(context.contentResolver, SYSUI_QS_TILES) ?: ""
        tiles.split(",").toSet()
    } catch (_: SecurityException) {
        emptySet()
    }

    private fun componentFlattenedName(requestKey: String): String? =
        getTileServiceRequests()[requestKey]?.componentName(context)?.flattenToString()

    private val catalogue: ImmutableList<ToolkitTileCategory> = persistentListOf(
        ToolkitTileCategory(
            id = CATEGORY_SENSORS,
            titleResId = R.string.tiles_category_sensors,
            icon = ToolkitTileIcon.Compass,
            initiallyExpanded = true,
            tiles = persistentListOf(
                ToolkitTile(
                    id = "bubble_level",
                    titleResId = R.string.tile_bubble_level_title,
                    summaryResId = R.string.tile_bubble_level_summary,
                    icon = ToolkitTileIcon.Level,
                    status = ToolkitTileStatus.NeedsSetup,
                ),
                ToolkitTile(
                    id = "compass",
                    titleResId = R.string.tile_compass_title,
                    summaryResId = R.string.tile_compass_summary,
                    icon = ToolkitTileIcon.Compass,
                    status = ToolkitTileStatus.NeedsSetup,
                ),
                ToolkitTile(
                    id = "lux_meter",
                    titleResId = R.string.tile_lux_meter_title,
                    summaryResId = R.string.tile_lux_meter_summary,
                    icon = ToolkitTileIcon.Lux,
                    status = ToolkitTileStatus.NeedsSetup,
                ),
            ),
        ),
        ToolkitTileCategory(
            id = CATEGORY_UTILITIES,
            titleResId = R.string.tiles_category_utilities,
            icon = ToolkitTileIcon.Dice,
            tiles = persistentListOf(
                ToolkitTile(
                    id = "coin_flip",
                    titleResId = R.string.tile_coin_flip_title,
                    summaryResId = R.string.tile_coin_flip_summary,
                    icon = ToolkitTileIcon.Coin,
                    status = ToolkitTileStatus.Available,
                    requestKey = "coin_flip",
                ),
                ToolkitTile(
                    id = "dice_roll",
                    titleResId = R.string.tile_dice_roll_title,
                    summaryResId = R.string.tile_dice_roll_summary,
                    icon = ToolkitTileIcon.Dice,
                    status = ToolkitTileStatus.Available,
                    requestKey = "dice_roll",
                ),
                ToolkitTile(
                    id = "counter",
                    titleResId = R.string.tile_counter_title,
                    summaryResId = R.string.tile_counter_summary,
                    icon = ToolkitTileIcon.Counter,
                    status = ToolkitTileStatus.Available,
                    requestKey = "counter",
                ),
                ToolkitTile(
                    id = "material_colors",
                    titleResId = R.string.tool_material_colors_title,
                    summaryResId = R.string.tool_material_colors_summary,
                    icon = ToolkitTileIcon.Palette,
                    status = ToolkitTileStatus.Available,
                    kind = ToolkitToolKind.Expanded,
                    quickTool = ToolkitQuickTool.MaterialColors,
                ),
                ToolkitTile(
                    id = "music_search",
                    titleResId = R.string.tile_music_search_title,
                    summaryResId = R.string.tile_music_search_summary,
                    icon = ToolkitTileIcon.Music,
                    status = ToolkitTileStatus.Available,
                ),
                ToolkitTile(
                    id = "morse",
                    titleResId = R.string.tile_morse_title,
                    summaryResId = R.string.tile_morse_summary,
                    icon = ToolkitTileIcon.Morse,
                    status = ToolkitTileStatus.Available,
                ),
            ),
        ),
        ToolkitTileCategory(
            id = CATEGORY_SYSTEM,
            titleResId = R.string.tiles_category_system,
            icon = ToolkitTileIcon.Sound,
            tiles = persistentListOf(
                ToolkitTile(
                    id = "caffeine",
                    titleResId = R.string.tile_caffeine_title,
                    summaryResId = R.string.tile_caffeine_summary,
                    icon = ToolkitTileIcon.Caffeine,
                    status = ToolkitTileStatus.Available,
                ),
                ToolkitTile(
                    id = "sound_mode",
                    titleResId = R.string.tile_sound_mode_title,
                    summaryResId = R.string.tile_sound_mode_summary,
                    icon = ToolkitTileIcon.Sound,
                    status = ToolkitTileStatus.Available,
                ),
                ToolkitTile(
                    id = "flash_dimmer",
                    titleResId = R.string.tile_flash_dimmer_title,
                    summaryResId = R.string.tile_flash_dimmer_summary,
                    icon = ToolkitTileIcon.FlashDimmer,
                    status = ToolkitTileStatus.Available,
                    requestKey = "flash_dimmer",
                ),
            ),
        ),
        ToolkitTileCategory(
            id = CATEGORY_WELLBEING,
            titleResId = R.string.tiles_category_wellbeing,
            icon = ToolkitTileIcon.Breathing,
            tiles = persistentListOf(
                ToolkitTile(
                    id = "breathing",
                    titleResId = R.string.tile_breathing_title,
                    summaryResId = R.string.tile_breathing_summary,
                    icon = ToolkitTileIcon.Breathing,
                    status = ToolkitTileStatus.Available,
                ),
                ToolkitTile(
                    id = "sos",
                    titleResId = R.string.tile_sos_title,
                    summaryResId = R.string.tile_sos_summary,
                    icon = ToolkitTileIcon.Sos,
                    status = ToolkitTileStatus.Available,
                ),
            ),
        ),
    )

    private companion object {
        const val CATEGORY_SENSORS: String = "sensors"
        const val CATEGORY_UTILITIES: String = "utilities"
        const val CATEGORY_SYSTEM: String = "system"
        const val CATEGORY_WELLBEING: String = "wellbeing"
        const val SYSUI_QS_TILES: String = "sysui_qs_tiles"
    }
}

internal fun filterUnavailableTorchTools(
    categories: List<ToolkitTileCategory>,
    isTorchAvailable: Boolean,
): List<ToolkitTileCategory> {
    if (isTorchAvailable) return categories
    return categories.mapNotNull { category ->
        val visibleTiles = category.tiles.filterNot { it.id in TORCH_TOOL_IDS }.toImmutableList()
        category.copy(tiles = visibleTiles).takeIf { visibleTiles.isNotEmpty() }
    }
}

private val TORCH_TOOL_IDS: Set<String> = setOf("flash_dimmer", "morse", "sos")
