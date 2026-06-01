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

package com.wstxda.toolkit.tiles.compass

import android.service.quicksettings.Tile
import com.wstxda.toolkit.base.BaseForegroundTileService
import com.wstxda.toolkit.manager.compass.CompassManager
import com.wstxda.toolkit.manager.compass.CompassModule
import com.wstxda.toolkit.ui.icon.CompassIconProvider
import com.wstxda.toolkit.ui.label.CompassLabelProvider
import kotlinx.coroutines.flow.Flow

class CompassTileService : BaseForegroundTileService() {

    private val compassManager by lazy { CompassModule.getInstance(applicationContext) }
    private val labelProvider by lazy { CompassLabelProvider(applicationContext) }
    private val iconProvider by lazy { CompassIconProvider(applicationContext) }

    override fun isFeatureSupported(): Boolean = CompassManager.isSupported(this)
    override fun isFeatureEnabled(): Boolean = compassManager.isEnabled.value
    override fun resumeFeature() = compassManager.resume()
    override fun pauseFeature() = compassManager.pause()
    override fun toggleFeature() = compassManager.toggle()

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        compassManager.isEnabled,
        compassManager.currentDegrees,
    )

    override fun updateTile() {
        val isEnabled = compassManager.isEnabled.value
        val degrees = compassManager.currentDegrees.value

        setTileState(
            state = if (isEnabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE,
            label = labelProvider.getLabel(isEnabled, degrees),
            subtitle = labelProvider.getSubtitle(isEnabled),
            icon = iconProvider.getIcon(isEnabled, degrees),
        )
    }
}