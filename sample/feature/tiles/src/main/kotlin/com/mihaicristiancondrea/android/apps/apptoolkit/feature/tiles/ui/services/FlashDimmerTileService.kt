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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.MorseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.TorchRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Quick Settings control that cycles through the distinct levels supported by the device.
 *
 * It follows the shared torch state while visible, so the system flashlight, the in-app dimmer and
 * SOS/Morse playback are reflected, and a torch that was already on when the process started is
 * shown once the camera reports it.
 */
class FlashDimmerTileService : TrackedTileService(), KoinComponent {

    private val torchRepository: TorchRepository by inject()
    private val morseRepository: MorseRepository by inject()

    override fun onStartListening() {
        super.onStartListening()
        renderWhileListening(torchRepository.state, ::renderTorchState)
    }

    override fun onClick() {
        super.onClick()
        morseRepository.stop()
        torchRepository.cyclePreset()
        renderTorchState(torchRepository.state.value)
    }

    private fun renderTorchState(state: TorchState) {
        val capabilities = state.capabilities
        val tileState = when {
            !capabilities.isAvailable -> Tile.STATE_UNAVAILABLE
            state.isEnabled -> Tile.STATE_ACTIVE
            else -> Tile.STATE_INACTIVE
        }
        val subtitle = when {
            !capabilities.isAvailable || state.error != null ->
                getString(R.string.flash_dimmer_unavailable)

            !state.isEnabled -> getString(R.string.flash_dimmer_off)
            capabilities.supportsDimming -> getString(
                R.string.flash_dimmer_tile_level,
                state.currentLevel,
                capabilities.maximumLevel,
            )

            else -> getString(R.string.flash_dimmer_on)
        }
        publishTile(
            tileState,
            TileText(title = getString(R.string.tile_flash_dimmer_title), subtitle = subtitle),
        )
    }
}
