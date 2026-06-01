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

package com.wstxda.toolkit.tiles.brightness

import android.service.quicksettings.Tile
import com.wstxda.toolkit.activity.WriteSettingsPermissionActivity
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.brightness.AutoBrightnessModule
import com.wstxda.toolkit.ui.icon.AutoBrightnessIconProvider
import com.wstxda.toolkit.ui.label.AutoBrightnessLabelProvider
import kotlinx.coroutines.flow.Flow

class AutoBrightnessTileService : BaseTileService() {

    private val brightnessManager by lazy { AutoBrightnessModule.getInstance(applicationContext) }
    private val labelProvider by lazy { AutoBrightnessLabelProvider(applicationContext) }
    private val iconProvider by lazy { AutoBrightnessIconProvider(applicationContext) }

    override fun onStartListening() {
        brightnessManager.start()
        super.onStartListening()
    }

    override fun onStopListening() {
        super.onStopListening()
        brightnessManager.stop()
    }

    override fun onClick() {
        if (!brightnessManager.isPermissionGranted()) {
            startActivityAndCollapse(WriteSettingsPermissionActivity::class.java)
            return
        }
        brightnessManager.toggle()
        updateTile()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        brightnessManager.isEnabled,
    )

    override fun updateTile() {
        val isEnabled = brightnessManager.isEnabled.value
        val hasPermission = brightnessManager.isPermissionGranted()

        setTileState(
            state = if (isEnabled && hasPermission) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE,
            label = labelProvider.getLabel(),
            subtitle = labelProvider.getSubtitle(isEnabled, hasPermission),
            icon = iconProvider.getIcon(isEnabled),
        )
    }
}