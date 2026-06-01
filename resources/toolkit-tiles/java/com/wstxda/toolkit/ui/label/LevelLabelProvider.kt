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

class LevelLabelProvider(private val context: Context) {

    fun getLabel(isActive: Boolean, degrees: Int): CharSequence {
        return if (isActive) {
            if (degrees == 0) {
                context.getString(R.string.level_tile_zero)
            } else {
                context.getString(R.string.level_tile_degrees, degrees)
            }
        } else {
            context.getString(R.string.level_tile)
        }
    }

    fun getSubtitle(isActive: Boolean): CharSequence {
        return if (isActive) {
            context.getString(R.string.tile_on)
        } else {
            context.getString(R.string.tile_off)
        }
    }
}