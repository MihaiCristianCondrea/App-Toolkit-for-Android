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

package com.wstxda.toolkit.tiles.usbdebugging

import android.service.quicksettings.Tile
import com.wstxda.toolkit.activity.WriteSecureSettingsActivity
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.usbdebugging.UsbDebuggingModule
import com.wstxda.toolkit.ui.icon.UsbDebuggingIconProvider
import com.wstxda.toolkit.ui.label.UsbDebuggingLabelProvider
import kotlinx.coroutines.flow.Flow

class UsbDebuggingTileService : BaseTileService() {

    private val usbDebuggingManager by lazy { UsbDebuggingModule.getInstance(applicationContext) }
    private val labelProvider by lazy { UsbDebuggingLabelProvider(applicationContext) }
    private val iconProvider by lazy { UsbDebuggingIconProvider(applicationContext) }

    override fun onStartListening() {
        usbDebuggingManager.start()
        super.onStartListening()
    }

    override fun onStopListening() {
        super.onStopListening()
        usbDebuggingManager.stop()
    }

    override fun onClick() {
        if (!usbDebuggingManager.hasPermission()) {
            startActivityAndCollapse(WriteSecureSettingsActivity::class.java)
            return
        }
        usbDebuggingManager.toggle()
        updateTile()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        usbDebuggingManager.isEnabled,
        usbDebuggingManager.isDeveloperOptionsEnabled,
    )

    override fun updateTile() {
        val hasPermission = usbDebuggingManager.hasPermission()
        val isEnabled = usbDebuggingManager.isEnabled.value
        val isDeveloperOptionsEnabled = usbDebuggingManager.isDeveloperOptionsEnabled.value

        setTileState(
            state = when {
                !isDeveloperOptionsEnabled -> Tile.STATE_UNAVAILABLE
                !hasPermission -> Tile.STATE_INACTIVE
                isEnabled -> Tile.STATE_ACTIVE
                else -> Tile.STATE_INACTIVE
            },
            label = labelProvider.getLabel(),
            subtitle = labelProvider.getSubtitle(
                isEnabled, hasPermission, isDeveloperOptionsEnabled
            ),
            icon = iconProvider.getIcon(isEnabled),
        )
    }
}