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

package com.wstxda.toolkit.manager.clipboard

import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.core.content.getSystemService

object ClipboardDispatcher {

    fun clearClipboard(context: Context): Boolean {
        return try {
            val clipboardManager = context.getSystemService<ClipboardManager>() ?: return false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                clipboardManager.clearPrimaryClip()
            } else {
                clipboardManager.setPrimaryClip(
                    android.content.ClipData.newPlainText("", "")
                )
            }
            true
        } catch (_: Exception) {
            false
        }
    }
}