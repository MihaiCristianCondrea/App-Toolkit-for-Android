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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories

import android.content.Context
import android.os.Build
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.copyTextToClipboard

/**
 * [ClipboardRepository] backed by the system clipboard service. It keeps only the application
 * [Context], so it is safe to hold for the lifetime of the process.
 */
class DefaultClipboardRepository(context: Context) : ClipboardRepository {

    private val appContext: Context = context.applicationContext

    override val confirmsCopies: Boolean
        get() = Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2

    override fun copyText(label: String, text: String, isSensitive: Boolean) {
        val copied: Boolean =
            appContext.copyTextToClipboard(label = label, text = text, isSensitive = isSensitive)
        check(copied) { "Clipboard rejected the copy for \"$label\"" }
    }
}
