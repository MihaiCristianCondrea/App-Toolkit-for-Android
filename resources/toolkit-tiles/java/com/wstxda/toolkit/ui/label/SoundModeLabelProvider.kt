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
import com.wstxda.toolkit.manager.soundmode.SoundMode

class SoundModeLabelProvider(private val context: Context) {

    fun getLabel(currentMode: SoundMode, hasPermission: Boolean): CharSequence {
        return if (hasPermission) {
            when (currentMode) {
                SoundMode.NORMAL -> context.getString(R.string.sound_tile_normal)
                SoundMode.VIBRATE -> context.getString(R.string.sound_tile_vibrate)
                SoundMode.SILENT -> context.getString(R.string.sound_tile_silent)
            }
        } else {
            context.getString(R.string.sound_mode_tile)
        }
    }

    fun getSubtitle(hasPermission: Boolean): CharSequence {
        return if (hasPermission) {
            context.getString(R.string.tile_switch)
        } else {
            context.getString(R.string.tile_setup)
        }
    }
}