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

package com.d4rk.android.apps.apptoolkit.app.tiles.domain.usecase

import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTile
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileCategory
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileIcon
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** Builds the curated Toolkit Tiles catalog inspired by the preview-only resources project. */
class GetToolkitTilesUseCase {
    operator fun invoke(): Flow<ImmutableList<ToolkitTileCategory>> = flowOf(
        persistentListOf(
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
                    ToolkitTile(
                        id = "network_traffic",
                        titleResId = R.string.tile_network_traffic_title,
                        summaryResId = R.string.tile_network_traffic_summary,
                        icon = ToolkitTileIcon.Network,
                        status = ToolkitTileStatus.Available,
                    ),
                    ToolkitTile(
                        id = "temperature",
                        titleResId = R.string.tile_temperature_title,
                        summaryResId = R.string.tile_temperature_summary,
                        icon = ToolkitTileIcon.Temperature,
                        status = ToolkitTileStatus.Available,
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
                        id = "clipboard",
                        titleResId = R.string.tile_clipboard_title,
                        summaryResId = R.string.tile_clipboard_summary,
                        icon = ToolkitTileIcon.Clipboard,
                        status = ToolkitTileStatus.Available,
                        requestKey = "clipboard",
                    ),
                    ToolkitTile(
                        id = "music_search",
                        titleResId = R.string.tile_music_search_title,
                        summaryResId = R.string.tile_music_search_summary,
                        icon = ToolkitTileIcon.Music,
                        status = ToolkitTileStatus.Unsupported,
                    ),
                ),
            ),
            ToolkitTileCategory(
                id = CATEGORY_SYSTEM,
                titleResId = R.string.tiles_category_system,
                icon = ToolkitTileIcon.Battery,
                tiles = persistentListOf(
                    ToolkitTile(
                        id = "battery",
                        titleResId = R.string.tile_battery_title,
                        summaryResId = R.string.tile_battery_summary,
                        icon = ToolkitTileIcon.Battery,
                        status = ToolkitTileStatus.Available,
                        requestKey = "battery",
                    ),
                    ToolkitTile(
                        id = "memory",
                        titleResId = R.string.tile_memory_title,
                        summaryResId = R.string.tile_memory_summary,
                        icon = ToolkitTileIcon.Memory,
                        status = ToolkitTileStatus.Available,
                    ),
                    ToolkitTile(
                        id = "caffeine",
                        titleResId = R.string.tile_caffeine_title,
                        summaryResId = R.string.tile_caffeine_summary,
                        icon = ToolkitTileIcon.Caffeine,
                        status = ToolkitTileStatus.NeedsSetup,
                    ),
                    ToolkitTile(
                        id = "sound_mode",
                        titleResId = R.string.tile_sound_mode_title,
                        summaryResId = R.string.tile_sound_mode_summary,
                        icon = ToolkitTileIcon.Sound,
                        status = ToolkitTileStatus.NeedsSetup,
                    ),
                    ToolkitTile(
                        id = "volume_panel",
                        titleResId = R.string.tile_volume_panel_title,
                        summaryResId = R.string.tile_volume_panel_summary,
                        icon = ToolkitTileIcon.Volume,
                        status = ToolkitTileStatus.Available,
                    ),
                    ToolkitTile(
                        id = "screenshot",
                        titleResId = R.string.tile_screenshot_title,
                        summaryResId = R.string.tile_screenshot_summary,
                        icon = ToolkitTileIcon.Screenshot,
                        status = ToolkitTileStatus.NeedsSetup,
                    ),
                    ToolkitTile(
                        id = "lock_screen",
                        titleResId = R.string.tile_lock_screen_title,
                        summaryResId = R.string.tile_lock_screen_summary,
                        icon = ToolkitTileIcon.Lock,
                        status = ToolkitTileStatus.NeedsSetup,
                    ),
                    ToolkitTile(
                        id = "power_menu",
                        titleResId = R.string.tile_power_menu_title,
                        summaryResId = R.string.tile_power_menu_summary,
                        icon = ToolkitTileIcon.Power,
                        status = ToolkitTileStatus.Unsupported,
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
                        status = ToolkitTileStatus.NeedsSetup,
                    ),
                ),
            ),
        )
    )

    private companion object {
        const val CATEGORY_SENSORS: String = "sensors"
        const val CATEGORY_UTILITIES: String = "utilities"
        const val CATEGORY_SYSTEM: String = "system"
        const val CATEGORY_WELLBEING: String = "wellbeing"
    }
}
