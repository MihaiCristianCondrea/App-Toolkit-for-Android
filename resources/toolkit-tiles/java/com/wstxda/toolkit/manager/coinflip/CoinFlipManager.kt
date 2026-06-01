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

package com.wstxda.toolkit.manager.coinflip

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class CoinFlipManager(@Suppress("unused") context: Context) {

    private val _headsCount = MutableStateFlow(0)
    val headsCount = _headsCount.asStateFlow()

    private val _tailsCount = MutableStateFlow(0)
    val tailsCount = _tailsCount.asStateFlow()

    private val _lastFlip = MutableStateFlow<CoinFlipSide?>(null)
    val lastFlip = _lastFlip.asStateFlow()

    fun flip() {
        val side = if (Random.nextBoolean()) CoinFlipSide.HEADS else CoinFlipSide.TAILS
        _lastFlip.value = side
        if (side == CoinFlipSide.HEADS) _headsCount.value += 1 else _tailsCount.value += 1
    }

    fun reset() {
        _headsCount.value = 0
        _tailsCount.value = 0
        _lastFlip.value = null
    }
}
