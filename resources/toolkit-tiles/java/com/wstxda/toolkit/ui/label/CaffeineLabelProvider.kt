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
import com.wstxda.toolkit.manager.caffeine.CaffeineState
import java.util.concurrent.TimeUnit

class CaffeineLabelProvider(private val context: Context) {

    fun getLabel(state: CaffeineState, hasPermission: Boolean): CharSequence {
        if (!hasPermission || state == CaffeineState.Off) {
            return context.getString(R.string.caffeine_tile)
        }

        return getFormattedTime(state)
    }

    fun getSubtitle(state: CaffeineState, hasPermission: Boolean): CharSequence {
        if (!hasPermission) {
            return context.getString(R.string.tile_setup)
        }

        if (state == CaffeineState.Off) {
            return context.getString(R.string.tile_off)
        }

        return context.getString(R.string.tile_on)
    }

    private fun getFormattedTime(state: CaffeineState): String {
        return when (state) {
            CaffeineState.Infinite -> context.getString(R.string.caffeine_tile_infinite)
            CaffeineState.Off -> ""
            else -> {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(state.timeout.toLong())
                context.getString(R.string.caffeine_tile_minutes, minutes)
            }
        }
    }
}