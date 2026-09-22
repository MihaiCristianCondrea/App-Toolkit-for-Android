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

/** Quick Settings tile that rolls a six-sided die. */
class DiceRollTileService : TrackedTileService() {
    override fun onStartListening() {
        super.onStartListening()
        publishTile(Tile.STATE_INACTIVE, diceRollText(result = null))
    }

    override fun onClick() {
        super.onClick()
        val value = Random.nextInt(from = 1, until = 7)
        publishTile(
            Tile.STATE_ACTIVE,
            diceRollText(getString(R.string.tile_service_dice_value, value)),
        )
    }

    private fun diceRollText(result: String?) = TileText(
        title = getString(R.string.tile_dice_roll_title),
        subtitle = getString(R.string.tile_dice_roll_summary),
        result = result,
    )
}
