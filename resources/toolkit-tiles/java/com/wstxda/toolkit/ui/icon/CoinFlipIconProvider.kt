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

package com.wstxda.toolkit.ui.icon

import android.content.Context
import android.graphics.drawable.Icon
import com.wstxda.toolkit.R
import com.wstxda.toolkit.manager.coinflip.CoinFlipSide

class CoinFlipIconProvider(private val context: Context) {

    fun getIcon(lastFlip: CoinFlipSide?): Icon {
        val iconRes = when (lastFlip) {
            CoinFlipSide.HEADS -> R.drawable.ic_coin_heads
            CoinFlipSide.TAILS -> R.drawable.ic_coin_tails
            null -> R.drawable.ic_coin
        }
        return Icon.createWithResource(context, iconRes)
    }
}