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

package com.d4rk.android.apps.apptoolkit.app.tiles.service

import android.os.Build
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.d4rk.android.apps.apptoolkit.R

/** Quick Settings tile that refreshes the current battery percentage. */
class BatteryTileService : TileService() {
    override fun onStartListening() {
        super.onStartListening()
        updateBatteryTile()
    }

    override fun onClick() {
        super.onClick()
        updateBatteryTile()
    }

    private fun updateBatteryTile() {
        val batteryIntent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, UNKNOWN_LEVEL) ?: UNKNOWN_LEVEL
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, DEFAULT_SCALE) ?: DEFAULT_SCALE
        val percent = if (level >= 0 && scale > 0) level * 100 / scale else UNKNOWN_LEVEL
        qsTile?.apply {
            label = getString(R.string.tile_battery_title)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                subtitle = if (percent >= 0) getString(R.string.tile_service_percent, percent) else null
            }
            state = Tile.STATE_ACTIVE
            updateTile()
        }
    }

    private companion object {
        const val UNKNOWN_LEVEL: Int = -1
        const val DEFAULT_SCALE: Int = 100
    }
}
