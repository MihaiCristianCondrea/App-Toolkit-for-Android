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

package com.wstxda.toolkit.tiles.lock

import android.service.quicksettings.Tile
import com.wstxda.toolkit.activity.AccessibilityPermissionActivity
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.lock.LockModule
import com.wstxda.toolkit.ui.icon.LockIconProvider
import com.wstxda.toolkit.ui.label.LockLabelProvider
import kotlinx.coroutines.flow.Flow

class LockTileService : BaseTileService() {

    private val lockManager by lazy { LockModule.getInstance(applicationContext) }
    private val labelProvider by lazy { LockLabelProvider(applicationContext) }
    private val iconProvider by lazy { LockIconProvider(applicationContext) }

    override fun onClick() {
        if (!lockManager.isPermissionGranted.value) {
            startActivityAndCollapse(AccessibilityPermissionActivity::class.java)
            return
        }
        lockManager.lockScreen()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        lockManager.isPermissionGranted,
    )

    override fun updateTile() {
        val hasPermission = lockManager.isPermissionGranted.value

        setTileState(
            state = Tile.STATE_INACTIVE,
            label = labelProvider.getLabel(),
            subtitle = labelProvider.getSubtitle(hasPermission),
            icon = iconProvider.getIcon(),
        )
    }
}