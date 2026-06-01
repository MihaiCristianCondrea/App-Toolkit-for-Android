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
import com.wstxda.toolkit.manager.soundmode.SoundMode

class SoundModeIconProvider(private val context: Context) {

    fun getIcon(currentMode: SoundMode, hasPermission: Boolean): Icon {
        if (!hasPermission) {
            return Icon.createWithResource(context, R.drawable.ic_sound)
        }

        val iconRes = when (currentMode) {
            SoundMode.NORMAL -> R.drawable.ic_sound_normal
            SoundMode.VIBRATE -> R.drawable.ic_sound_vibrate
            SoundMode.SILENT -> R.drawable.ic_sound_silent
        }

        return Icon.createWithResource(context, iconRes)
    }
}