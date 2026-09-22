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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.CounterRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/** Quick Settings tile that increments the count shared with the in-app Counter tool. */
class CounterTileService : TrackedTileService(), KoinComponent {

    private val counterRepository: CounterRepository by inject()

    override fun onStartListening() {
        super.onStartListening()
        renderWhileListening(counterRepository.count, ::renderCount)
    }

    override fun onClick() {
        super.onClick()
        counterRepository.increment()
    }

    private fun renderCount(count: Int) {
        publishTile(
            if (count > 0) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE,
            TileText(
                title = getString(R.string.tile_counter_title),
                subtitle = count.toString(),
                result = count.takeIf { it != 0 }?.toString(),
            ),
        )
    }
}
