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

package com.wstxda.toolkit.tiles.coinflip

import android.service.quicksettings.Tile
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.coinflip.CoinFlipModule
import com.wstxda.toolkit.ui.icon.CoinFlipIconProvider
import com.wstxda.toolkit.ui.label.CoinFlipLabelProvider
import kotlinx.coroutines.flow.Flow

class CoinFlipTileService : BaseTileService() {

    private val coinFlipManager by lazy { CoinFlipModule.getInstance(applicationContext) }
    private val labelProvider by lazy { CoinFlipLabelProvider(applicationContext) }
    private val iconProvider by lazy { CoinFlipIconProvider(applicationContext) }

    override fun onStopListening() {
        super.onStopListening()
        coinFlipManager.reset()
    }

    override fun onClick() {
        coinFlipManager.flip()
        updateTile()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        coinFlipManager.lastFlip,
        coinFlipManager.headsCount,
        coinFlipManager.tailsCount,
    )

    override fun updateTile() {
        val lastFlip = coinFlipManager.lastFlip.value
        val heads = coinFlipManager.headsCount.value
        val tails = coinFlipManager.tailsCount.value

        setTileState(
            state = if (lastFlip != null) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE,
            label = labelProvider.getLabel(lastFlip),
            subtitle = labelProvider.getSubtitle(lastFlip, heads, tails),
            icon = iconProvider.getIcon(lastFlip),
        )
    }
}