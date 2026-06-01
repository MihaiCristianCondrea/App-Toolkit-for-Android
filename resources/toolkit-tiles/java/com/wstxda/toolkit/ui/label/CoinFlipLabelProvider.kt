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

package com.wstxda.toolkit.ui.label

import android.content.Context
import com.wstxda.toolkit.R
import com.wstxda.toolkit.manager.coinflip.CoinFlipSide

class CoinFlipLabelProvider(private val context: Context) {

    fun getLabel(lastFlip: CoinFlipSide?): CharSequence {
        return when (lastFlip) {
            CoinFlipSide.HEADS -> context.getString(R.string.coin_tile_heads)
            CoinFlipSide.TAILS -> context.getString(R.string.coin_tile_tails)
            null -> context.getString(R.string.coin_flip_tile)
        }
    }

    fun getSubtitle(lastFlip: CoinFlipSide?, heads: Int, tails: Int): CharSequence {
        return if (lastFlip == null) {
            context.getString(R.string.coin_flip_tile_tap)
        } else {
            context.getString(R.string.coin_flip_tile_count, heads, tails)
        }
    }
}