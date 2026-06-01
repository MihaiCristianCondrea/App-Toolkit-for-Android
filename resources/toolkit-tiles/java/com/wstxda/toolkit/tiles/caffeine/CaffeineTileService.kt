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

package com.wstxda.toolkit.tiles.caffeine

import android.service.quicksettings.Tile
import com.wstxda.toolkit.activity.WriteSettingsPermissionActivity
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.caffeine.CaffeineModule
import com.wstxda.toolkit.manager.caffeine.CaffeineState
import com.wstxda.toolkit.ui.icon.CaffeineIconProvider
import com.wstxda.toolkit.ui.label.CaffeineLabelProvider
import kotlinx.coroutines.flow.Flow

class CaffeineTileService : BaseTileService() {

    private val caffeineManager by lazy { CaffeineModule.getInstance(applicationContext) }
    private val labelProvider by lazy { CaffeineLabelProvider(applicationContext) }
    private val iconProvider by lazy { CaffeineIconProvider(applicationContext) }

    override fun onStartListening() {
        caffeineManager.synchronizeState()
        super.onStartListening()
    }

    override fun onClick() {
        if (!caffeineManager.isPermissionGranted()) {
            startActivityAndCollapse(WriteSettingsPermissionActivity::class.java)
            return
        }
        caffeineManager.cycleState()
        updateTile()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        caffeineManager.currentState,
    )

    override fun updateTile() {
        val currentState = caffeineManager.currentState.value
        val hasPermission = caffeineManager.isPermissionGranted()

        setTileState(
            state = if (currentState != CaffeineState.Off && hasPermission) {
                Tile.STATE_ACTIVE
            } else {
                Tile.STATE_INACTIVE
            },
            label = labelProvider.getLabel(currentState, hasPermission),
            subtitle = labelProvider.getSubtitle(currentState, hasPermission),
            icon = iconProvider.getIcon(currentState),
        )
    }
}