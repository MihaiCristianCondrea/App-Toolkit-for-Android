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

class CounterLabelProvider(private val context: Context) {

    fun getAddLabel(isActive: Boolean, count: Int): CharSequence {
        return if (isActive) count.toString() else context.getString(R.string.counter_tile_add)
    }

    fun getRemoveLabel(isActive: Boolean, count: Int): CharSequence {
        return if (isActive) count.toString() else context.getString(R.string.counter_tile_remove)
    }

    fun getResetLabel(): CharSequence {
        return context.getString(R.string.counter_tile_reset)
    }

    fun getAddSubtitle(isActive: Boolean): CharSequence {
        return if (isActive) {
            context.getString(R.string.counter_tile_add)
        } else {
            context.getString(R.string.counter_tile)
        }
    }

    fun getRemoveSubtitle(isActive: Boolean): CharSequence {
        return if (isActive) {
            context.getString(R.string.counter_tile_remove)
        } else {
            context.getString(R.string.counter_tile)
        }
    }

    fun getResetSubtitle(): CharSequence {
        return context.getString(R.string.counter_tile)
    }
}