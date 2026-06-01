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

class DiceRollLabelProvider(private val context: Context) {

    fun getLabel(roll: Int?): CharSequence {
        return when (roll) {
            1 -> context.getString(R.string.dice_tile_1)
            2 -> context.getString(R.string.dice_tile_2)
            3 -> context.getString(R.string.dice_tile_3)
            4 -> context.getString(R.string.dice_tile_4)
            5 -> context.getString(R.string.dice_tile_5)
            6 -> context.getString(R.string.dice_tile_6)
            else -> context.getString(R.string.dice_roll_tile)
        }
    }

    fun getSubtitle(isRolling: Boolean): CharSequence {
        return if (isRolling) {
            context.getString(R.string.dice_roll_tile_rolling)
        } else {
            context.getString(R.string.dice_roll_tile_tap)
        }
    }
}