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

package com.d4rk.android.apps.apptoolkit.app.tiles.domain.model

import android.content.ComponentName
import android.content.Context
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.tiles.service.BatteryTileService
import com.d4rk.android.apps.apptoolkit.app.tiles.service.ClipboardTileService
import com.d4rk.android.apps.apptoolkit.app.tiles.service.CoinFlipTileService
import com.d4rk.android.apps.apptoolkit.app.tiles.service.CounterTileService
import com.d4rk.android.apps.apptoolkit.app.tiles.service.DiceRollTileService

data class TileServiceRequest(
    val serviceClass: Class<*>,
    val labelResId: Int,
    val iconResId: Int,
) {
    fun componentName(context: Context): ComponentName = ComponentName(context, serviceClass)
}

fun getTileServiceRequests(): Map<String, TileServiceRequest> = mapOf(
    "battery" to TileServiceRequest(
        BatteryTileService::class.java,
        R.string.tile_battery_title,
        R.drawable.ic_tile_battery
    ),
    "clipboard" to TileServiceRequest(
        ClipboardTileService::class.java,
        R.string.tile_clipboard_title,
        R.drawable.ic_tile_clipboard
    ),
    "coin_flip" to TileServiceRequest(
        CoinFlipTileService::class.java,
        R.string.tile_coin_flip_title,
        R.drawable.ic_tile_coin
    ),
    "counter" to TileServiceRequest(
        CounterTileService::class.java,
        R.string.tile_counter_title,
        R.drawable.ic_tile_counter
    ),
    "dice_roll" to TileServiceRequest(
        DiceRollTileService::class.java,
        R.string.tile_dice_roll_title,
        R.drawable.ic_tile_dice
    ),
)
