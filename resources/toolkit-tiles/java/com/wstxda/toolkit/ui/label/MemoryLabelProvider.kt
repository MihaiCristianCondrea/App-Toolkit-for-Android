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
import com.wstxda.toolkit.manager.memory.MemoryState

class MemoryLabelProvider(private val context: Context) {

    fun getLabel(state: MemoryState, detail: String): CharSequence {
        if (detail.isBlank()) return context.getString(R.string.memory_tile)

        return when (state) {
            MemoryState.RAM -> context.getString(R.string.memory_tile_ram, detail)
            MemoryState.STORAGE -> context.getString(R.string.memory_tile_storage, detail)
        }
    }

    fun getSubtitle(used: String, total: String): CharSequence? {
        if (used.isBlank() || total.isBlank()) return null
        return context.getString(R.string.memory_tile_format, used, total)
    }
}