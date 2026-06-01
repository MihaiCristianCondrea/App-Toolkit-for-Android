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

package com.wstxda.toolkit.tiles.battery

import android.service.quicksettings.Tile
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.battery.BatteryModule
import com.wstxda.toolkit.ui.icon.BatteryIconProvider
import com.wstxda.toolkit.ui.label.BatteryLabelProvider
import kotlinx.coroutines.flow.Flow

class BatteryTileService : BaseTileService() {

    private val batteryManager by lazy { BatteryModule.getInstance(applicationContext) }
    private val labelProvider by lazy { BatteryLabelProvider(applicationContext) }
    private val iconProvider by lazy { BatteryIconProvider(applicationContext) }

    override fun onStartListening() {
        batteryManager.setListening(true)
        super.onStartListening()
    }

    override fun onStopListening() {
        super.onStopListening()
        batteryManager.setListening(false)
    }

    override fun onClick() {
        batteryManager.toggle()
        updateTile()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        batteryManager.batteryInfo,
        batteryManager.displayState,
    )

    override fun updateTile() {
        val batteryInfo = batteryManager.batteryInfo.value
        val displayState = batteryManager.displayState.value

        setTileState(
            state = if (batteryInfo.isCharging) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE,
            label = labelProvider.getLabel(batteryInfo, displayState),
            subtitle = labelProvider.getSubtitle(batteryInfo, displayState),
            icon = iconProvider.getIcon(batteryInfo),
        )
    }
}