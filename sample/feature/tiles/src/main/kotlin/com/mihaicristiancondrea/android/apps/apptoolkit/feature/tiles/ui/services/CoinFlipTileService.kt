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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.services

import android.service.quicksettings.Tile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import kotlin.random.Random

/** Quick Settings tile that flips a virtual coin. */
class CoinFlipTileService : TrackedTileService() {
    override fun onStartListening() {
        super.onStartListening()
        publishTile(Tile.STATE_INACTIVE, coinFlipText(result = null))
    }

    override fun onClick() {
        super.onClick()
        val result = getString(
            if (Random.nextBoolean()) R.string.tile_service_heads else R.string.tile_service_tails
        )
        publishTile(Tile.STATE_ACTIVE, coinFlipText(result))
    }

    private fun coinFlipText(result: String?) = TileText(
        title = getString(R.string.tile_coin_flip_title),
        subtitle = getString(R.string.tile_coin_flip_summary),
        result = result,
    )
}
