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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.services

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds

/** Registry of tiles this app can ask the system to add, keyed by `ToolkitTile.requestKey`. */
fun getTileServiceRequests(): Map<String, TileServiceRequest> = mapOf(
    ToolkitTileIds.COIN_FLIP to TileServiceRequest(
        CoinFlipTileService::class.java,
        R.string.tile_coin_flip_title,
        R.drawable.ic_tile_coin
    ),
    ToolkitTileIds.COUNTER to TileServiceRequest(
        CounterTileService::class.java,
        R.string.tile_counter_title,
        R.drawable.ic_tile_counter
    ),
    ToolkitTileIds.DICE_ROLL to TileServiceRequest(
        DiceRollTileService::class.java,
        R.string.tile_dice_roll_title,
        R.drawable.ic_tile_dice
    ),
    ToolkitTileIds.FLASH_DIMMER to TileServiceRequest(
        FlashDimmerTileService::class.java,
        R.string.tile_flash_dimmer_title,
        R.drawable.ic_tile_flash_dimmer,
    ),
)
