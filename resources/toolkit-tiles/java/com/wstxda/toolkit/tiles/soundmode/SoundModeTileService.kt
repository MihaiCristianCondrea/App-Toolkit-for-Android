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

package com.wstxda.toolkit.tiles.soundmode

import android.service.quicksettings.Tile
import com.wstxda.toolkit.activity.NotificationPolicyPermissionActivity
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.soundmode.SoundModeModule
import com.wstxda.toolkit.ui.icon.SoundModeIconProvider
import com.wstxda.toolkit.ui.label.SoundModeLabelProvider
import kotlinx.coroutines.flow.Flow

class SoundModeTileService : BaseTileService() {

    private val soundModeManager by lazy { SoundModeModule.getInstance(applicationContext) }
    private val labelProvider by lazy { SoundModeLabelProvider(applicationContext) }
    private val iconProvider by lazy { SoundModeIconProvider(applicationContext) }

    override fun onClick() {
        if (!soundModeManager.hasPermission()) {
            startActivityAndCollapse(NotificationPolicyPermissionActivity::class.java)
            return
        }
        soundModeManager.cycleMode()
        updateTile()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        soundModeManager.currentMode,
    )

    override fun updateTile() {
        val hasPermission = soundModeManager.hasPermission()
        val currentMode = soundModeManager.currentMode.value

        setTileState(
            state = if (hasPermission) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE,
            label = labelProvider.getLabel(currentMode, hasPermission),
            subtitle = labelProvider.getSubtitle(hasPermission),
            icon = iconProvider.getIcon(currentMode, hasPermission),
        )
    }
}