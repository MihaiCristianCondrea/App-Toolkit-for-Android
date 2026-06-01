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

package com.wstxda.toolkit.tiles.screenshot

import android.service.quicksettings.Tile
import com.wstxda.toolkit.activity.AccessibilityPermissionActivity
import com.wstxda.toolkit.activity.ScreenshotActivity
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.screenshot.ScreenshotModule
import com.wstxda.toolkit.ui.icon.ScreenshotIconProvider
import com.wstxda.toolkit.ui.label.ScreenshotLabelProvider
import kotlinx.coroutines.flow.Flow

class ScreenshotTileService : BaseTileService() {

    private val screenshotManager by lazy { ScreenshotModule.getInstance(applicationContext) }
    private val labelProvider by lazy { ScreenshotLabelProvider(applicationContext) }
    private val iconProvider by lazy { ScreenshotIconProvider(applicationContext) }

    override fun onClick() {
        if (screenshotManager.isPermissionGranted.value) {
            startActivityAndCollapse(ScreenshotActivity::class.java)
        } else {
            startActivityAndCollapse(AccessibilityPermissionActivity::class.java)
        }
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        screenshotManager.isPermissionGranted,
    )

    override fun updateTile() {
        val hasPermission = screenshotManager.isPermissionGranted.value

        setTileState(
            state = Tile.STATE_INACTIVE,
            label = labelProvider.getLabel(),
            subtitle = labelProvider.getSubtitle(hasPermission),
            icon = iconProvider.getIcon(),
        )
    }
}