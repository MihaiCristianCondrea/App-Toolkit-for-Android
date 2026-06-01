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

package com.wstxda.toolkit.tiles.networktraffic

import android.service.quicksettings.Tile
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.networktraffic.NetworkTrafficModule
import com.wstxda.toolkit.ui.icon.NetworkTrafficIconProvider
import com.wstxda.toolkit.ui.label.NetworkTrafficLabelProvider
import kotlinx.coroutines.flow.Flow

class NetworkTrafficTileService : BaseTileService() {

    private val networkTrafficManager by lazy { NetworkTrafficModule.getInstance(applicationContext) }
    private val labelProvider by lazy { NetworkTrafficLabelProvider(applicationContext) }
    private val iconProvider by lazy { NetworkTrafficIconProvider(applicationContext) }

    override fun onStartListening() {
        networkTrafficManager.setListening(true)
        super.onStartListening()
    }

    override fun onStopListening() {
        super.onStopListening()
        networkTrafficManager.setListening(false)
    }

    override fun onClick() {
        networkTrafficManager.toggle()
        updateTile()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        networkTrafficManager.currentState,
        networkTrafficManager.speedValue,
    )

    override fun updateTile() {
        val state = networkTrafficManager.currentState.value
        val speed = networkTrafficManager.speedValue.value

        setTileState(
            state = Tile.STATE_INACTIVE,
            label = labelProvider.getLabel(speed),
            subtitle = labelProvider.getSubtitle(state),
            icon = iconProvider.getIcon(state),
        )
    }
}