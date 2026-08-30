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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.mappers

import androidx.annotation.StringRes
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.ToolkitTileCategoryData
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.ToolkitTileData
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.models.ToolkitTileCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal fun List<ToolkitTileCategoryData>.toUiModels(): ImmutableList<ToolkitTileCategory> =
    map { category ->
        val categoryVisuals = category.id.categoryVisuals()
        ToolkitTileCategory(
            id = category.id,
            titleResId = categoryVisuals.titleResId,
            icon = categoryVisuals.icon,
            tiles = category.tiles.map(ToolkitTileData::toUiModel).toImmutableList(),
        )
    }.toImmutableList()

private fun ToolkitTileData.toUiModel(): ToolkitTile {
    val visuals = id.tileVisuals()
    return ToolkitTile(
        id = id,
        titleResId = visuals.titleResId,
        summaryResId = visuals.summaryResId,
        icon = visuals.icon,
        status = status,
        kind = kind,
        quickTool = quickTool,
        requestKey = requestKey,
    )
}

private fun String.categoryVisuals(): CategoryVisuals = when (this) {
    "sensors" -> CategoryVisuals(R.string.tiles_category_sensors, ToolkitTileIcon.Compass)
    "utilities" -> CategoryVisuals(R.string.tiles_category_utilities, ToolkitTileIcon.Dice)
    "system" -> CategoryVisuals(R.string.tiles_category_system, ToolkitTileIcon.Sound)
    "wellbeing" -> CategoryVisuals(R.string.tiles_category_wellbeing, ToolkitTileIcon.Breathing)
    else -> error("Unknown Toolkit Tiles category: $this")
}

private fun String.tileVisuals(): TileVisuals = when (this) {
    "bubble_level" -> TileVisuals(R.string.tile_bubble_level_title, R.string.tile_bubble_level_summary, ToolkitTileIcon.Level)
    "compass" -> TileVisuals(R.string.tile_compass_title, R.string.tile_compass_summary, ToolkitTileIcon.Compass)
    "lux_meter" -> TileVisuals(R.string.tile_lux_meter_title, R.string.tile_lux_meter_summary, ToolkitTileIcon.Lux)
    "coin_flip" -> TileVisuals(R.string.tile_coin_flip_title, R.string.tile_coin_flip_summary, ToolkitTileIcon.Coin)
    "dice_roll" -> TileVisuals(R.string.tile_dice_roll_title, R.string.tile_dice_roll_summary, ToolkitTileIcon.Dice)
    "counter" -> TileVisuals(R.string.tile_counter_title, R.string.tile_counter_summary, ToolkitTileIcon.Counter)
    "material_colors" -> TileVisuals(R.string.tool_material_colors_title, R.string.tool_material_colors_summary, ToolkitTileIcon.Palette)
    "music_search" -> TileVisuals(R.string.tile_music_search_title, R.string.tile_music_search_summary, ToolkitTileIcon.Music)
    "morse" -> TileVisuals(R.string.tile_morse_title, R.string.tile_morse_summary, ToolkitTileIcon.Morse)
    "caffeine" -> TileVisuals(R.string.tile_caffeine_title, R.string.tile_caffeine_summary, ToolkitTileIcon.Caffeine)
    "sound_mode" -> TileVisuals(R.string.tile_sound_mode_title, R.string.tile_sound_mode_summary, ToolkitTileIcon.Sound)
    "flash_dimmer" -> TileVisuals(R.string.tile_flash_dimmer_title, R.string.tile_flash_dimmer_summary, ToolkitTileIcon.FlashDimmer)
    "breathing" -> TileVisuals(R.string.tile_breathing_title, R.string.tile_breathing_summary, ToolkitTileIcon.Breathing)
    "sos" -> TileVisuals(R.string.tile_sos_title, R.string.tile_sos_summary, ToolkitTileIcon.Sos)
    else -> error("Unknown Toolkit Tile: $this")
}

@StringRes
internal fun ToolkitTileStatus.helperTitleResId(): Int = when (this) {
    ToolkitTileStatus.Added -> R.string.tool_status_added_title
    ToolkitTileStatus.Available -> R.string.tool_status_available_title
    ToolkitTileStatus.NeedsSetup -> R.string.tool_status_needs_setup_title
    ToolkitTileStatus.Unsupported -> R.string.tool_status_unsupported_title
}

@StringRes
internal fun ToolkitTileStatus.helperSummaryResId(): Int = when (this) {
    ToolkitTileStatus.Added -> R.string.tool_status_added_summary
    ToolkitTileStatus.Available -> R.string.tool_status_available_summary
    ToolkitTileStatus.NeedsSetup -> R.string.tool_status_needs_setup_summary
    ToolkitTileStatus.Unsupported -> R.string.tool_status_unsupported_summary
}

private data class CategoryVisuals(@StringRes val titleResId: Int, val icon: ToolkitTileIcon)
private data class TileVisuals(
    @StringRes val titleResId: Int,
    @StringRes val summaryResId: Int,
    val icon: ToolkitTileIcon,
)
