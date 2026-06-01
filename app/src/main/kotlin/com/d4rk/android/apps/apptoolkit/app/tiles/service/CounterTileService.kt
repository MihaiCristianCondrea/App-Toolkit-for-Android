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
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.d4rk.android.apps.apptoolkit.R

/** Quick Settings tile that increments an in-memory counter while System UI keeps it alive. */
class CounterTileService : TileService() {
    override fun onStartListening() {
        super.onStartListening()
        updateCounterTile()
    }

    override fun onClick() {
        super.onClick()
        counter += 1
        updateCounterTile()
    }

    private fun updateCounterTile() {
        qsTile?.apply {
            label = getString(R.string.tile_counter_title)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                subtitle = counter.toString()
            }
            state = if (counter > 0) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            updateTile()
        }
    }

    private companion object {
        var counter: Int = 0
    }
}
