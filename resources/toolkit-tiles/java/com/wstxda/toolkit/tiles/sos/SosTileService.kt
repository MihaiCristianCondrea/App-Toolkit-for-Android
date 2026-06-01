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

package com.wstxda.toolkit.tiles.sos

import android.service.quicksettings.Tile
import android.widget.Toast
import com.wstxda.toolkit.R
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.sos.SosModule
import com.wstxda.toolkit.ui.icon.SosIconProvider
import com.wstxda.toolkit.ui.label.SosLabelProvider
import kotlinx.coroutines.flow.Flow

class SosTileService : BaseTileService() {

    private val sosManager by lazy { SosModule.getInstance(applicationContext) }
    private val labelProvider by lazy { SosLabelProvider(applicationContext) }
    private val iconProvider by lazy { SosIconProvider(applicationContext) }

    override fun onClick() {
        if (!sosManager.hasFlashHardware()) {
            Toast.makeText(this, R.string.not_supported, Toast.LENGTH_SHORT).show()
            return
        }
        if (qsTile?.state == Tile.STATE_UNAVAILABLE) return

        sosManager.toggle()
        updateTile()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        sosManager.isActive,
        sosManager.isFlashAvailable,
    )

    override fun updateTile() {
        val isActive = sosManager.isActive.value
        val isHardwareAvailable = sosManager.hasFlashHardware()
        val isSystemAvailable = sosManager.isFlashAvailable.value
        val isFullyAvailable = isHardwareAvailable && isSystemAvailable

        setTileState(
            state = when {
                !isFullyAvailable -> Tile.STATE_UNAVAILABLE
                isActive -> Tile.STATE_ACTIVE
                else -> Tile.STATE_INACTIVE
            },
            label = labelProvider.getLabel(),
            subtitle = labelProvider.getSubtitle(isActive, isFullyAvailable),
            icon = iconProvider.getIcon(),
        )
    }
}