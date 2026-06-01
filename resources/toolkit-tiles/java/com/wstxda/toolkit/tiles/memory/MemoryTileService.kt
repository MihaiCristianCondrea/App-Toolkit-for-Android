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

package com.wstxda.toolkit.tiles.memory

import android.service.quicksettings.Tile
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.memory.MemoryModule
import com.wstxda.toolkit.ui.icon.MemoryIconProvider
import com.wstxda.toolkit.ui.label.MemoryLabelProvider
import kotlinx.coroutines.flow.Flow

class MemoryTileService : BaseTileService() {

    private val memoryManager by lazy { MemoryModule.getInstance(applicationContext) }
    private val labelProvider by lazy { MemoryLabelProvider(applicationContext) }
    private val iconProvider by lazy { MemoryIconProvider(applicationContext) }

    override fun onStartListening() {
        memoryManager.setListening(true)
        super.onStartListening()
    }

    override fun onStopListening() {
        super.onStopListening()
        memoryManager.setListening(false)
    }

    override fun onClick() {
        memoryManager.toggle()
        updateTile()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        memoryManager.currentState,
        memoryManager.usedValue,
        memoryManager.totalValue,
        memoryManager.detailValue,
    )

    override fun updateTile() {
        val state = memoryManager.currentState.value
        val used = memoryManager.usedValue.value
        val total = memoryManager.totalValue.value
        val detail = memoryManager.detailValue.value

        setTileState(
            state = Tile.STATE_INACTIVE,
            label = labelProvider.getLabel(state, detail),
            subtitle = labelProvider.getSubtitle(used, total),
            icon = iconProvider.getIcon(state),
        )
    }
}