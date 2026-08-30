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

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.MorseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.TorchRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/** Quick Settings control that cycles through the distinct levels supported by the device. */
class FlashDimmerTileService : TileService(), KoinComponent {

    private val torchRepository: TorchRepository by inject()
    private val morseRepository: MorseRepository by inject()

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    override fun onClick() {
        super.onClick()
        morseRepository.stop()
        torchRepository.cyclePreset()
        updateTileState()
    }

    private fun updateTileState() {
        val state = torchRepository.state.value
        qsTile?.apply {
            label = getString(R.string.tile_flash_dimmer_title)
            this.state = when {
                !state.capabilities.isAvailable -> Tile.STATE_UNAVAILABLE
                state.isEnabled -> Tile.STATE_ACTIVE
                else -> Tile.STATE_INACTIVE
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                subtitle = when {
                    !state.capabilities.isAvailable ->
                        getString(R.string.flash_dimmer_unavailable)
                    !state.isEnabled -> getString(R.string.flash_dimmer_off)
                    state.capabilities.supportsDimming -> getString(
                        R.string.flash_dimmer_tile_level,
                        state.currentLevel,
                        state.capabilities.maximumLevel,
                    )
                    else -> getString(R.string.flash_dimmer_on)
                }
            }
            updateTile()
        }
    }
}
