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

package com.wstxda.toolkit.tiles.mediaoutput

import android.service.quicksettings.Tile
import com.wstxda.toolkit.activity.MediaOutputActivity
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.ui.icon.MediaOutputIconProvider
import com.wstxda.toolkit.ui.label.MediaOutputLabelProvider

class MediaOutputTileService : BaseTileService() {

    private val labelProvider by lazy { MediaOutputLabelProvider(applicationContext) }
    private val iconProvider by lazy { MediaOutputIconProvider(applicationContext) }

    override fun onClick() {
        startActivityAndCollapse(MediaOutputActivity::class.java)
    }

    override fun updateTile() {
        setTileState(
            state = Tile.STATE_INACTIVE,
            label = labelProvider.getLabel(),
            subtitle = labelProvider.getSubtitle(),
            icon = iconProvider.getIcon(),
        )
    }
}