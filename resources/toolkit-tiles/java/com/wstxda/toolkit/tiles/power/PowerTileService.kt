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

package com.wstxda.toolkit.tiles.power

import android.service.quicksettings.Tile
import com.wstxda.toolkit.activity.AccessibilityPermissionActivity
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.power.PowerModule
import com.wstxda.toolkit.ui.icon.PowerIconProvider
import com.wstxda.toolkit.ui.label.PowerLabelProvider
import kotlinx.coroutines.flow.Flow

class PowerTileService : BaseTileService() {

    private val powerManager by lazy { PowerModule.getInstance(applicationContext) }
    private val labelProvider by lazy { PowerLabelProvider(applicationContext) }
    private val iconProvider by lazy { PowerIconProvider(applicationContext) }

    override fun onClick() {
        if (!powerManager.isPermissionGranted.value) {
            startActivityAndCollapse(AccessibilityPermissionActivity::class.java)
            return
        }
        powerManager.powerMenu()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        powerManager.isPermissionGranted,
    )

    override fun updateTile() {
        val hasPermission = powerManager.isPermissionGranted.value

        setTileState(
            state = Tile.STATE_INACTIVE,
            label = labelProvider.getLabel(),
            subtitle = labelProvider.getSubtitle(hasPermission),
            icon = iconProvider.getIcon(),
        )
    }
}