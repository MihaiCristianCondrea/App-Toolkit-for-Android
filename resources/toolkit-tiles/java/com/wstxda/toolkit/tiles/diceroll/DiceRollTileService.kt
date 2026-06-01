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

package com.wstxda.toolkit.tiles.diceroll

import android.service.quicksettings.Tile
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.diceroll.DiceRollModule
import com.wstxda.toolkit.ui.icon.DiceRollIconProvider
import com.wstxda.toolkit.ui.label.DiceRollLabelProvider
import kotlinx.coroutines.flow.Flow

class DiceRollTileService : BaseTileService() {

    private val diceRollManager by lazy { DiceRollModule.getInstance(applicationContext) }
    private val labelProvider by lazy { DiceRollLabelProvider(applicationContext) }
    private val iconProvider by lazy { DiceRollIconProvider(applicationContext) }

    override fun onStopListening() {
        super.onStopListening()
        diceRollManager.clearState()
    }

    override fun onClick() {
        diceRollManager.roll()
        updateTile()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        diceRollManager.currentRoll,
        diceRollManager.isRolling,
    )

    override fun updateTile() {
        val currentRoll = diceRollManager.currentRoll.value
        val isRolling = diceRollManager.isRolling.value

        setTileState(
            state = if (currentRoll != null) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE,
            label = labelProvider.getLabel(currentRoll),
            subtitle = labelProvider.getSubtitle(isRolling),
            icon = iconProvider.getIcon(currentRoll),
        )
    }
}