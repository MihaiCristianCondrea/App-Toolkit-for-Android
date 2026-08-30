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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.service

import android.content.ComponentName
import android.content.Context
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R

/**
 * Quick Settings registration details for one [android.service.quicksettings.TileService].
 *
 * This is platform wiring rather than a domain model: it names a concrete service class and the
 * label/icon resources the system shows in the add-tile prompt.
 */
data class TileServiceRequest(
    val serviceClass: Class<*>,
    val labelResId: Int,
    val iconResId: Int,
) {
    fun componentName(context: Context): ComponentName = ComponentName(context, serviceClass)
}

/** Registry of tiles this app can ask the system to add, keyed by `ToolkitTile.requestKey`. */
fun getTileServiceRequests(): Map<String, TileServiceRequest> = mapOf(
    ToolkitTileIds.COIN_FLIP to TileServiceRequest(
        CoinFlipTileService::class.java,
        R.string.tile_coin_flip_title,
        CoreUiR.drawable.ic_tile_coin
    ),
    ToolkitTileIds.COUNTER to TileServiceRequest(
        CounterTileService::class.java,
        R.string.tile_counter_title,
        CoreUiR.drawable.ic_tile_counter
    ),
    ToolkitTileIds.DICE_ROLL to TileServiceRequest(
        DiceRollTileService::class.java,
        R.string.tile_dice_roll_title,
        CoreUiR.drawable.ic_tile_dice
    ),
    ToolkitTileIds.FLASH_DIMMER to TileServiceRequest(
        FlashDimmerTileService::class.java,
        R.string.tile_flash_dimmer_title,
        CoreUiR.drawable.ic_tile_flash_dimmer,
    ),
)
