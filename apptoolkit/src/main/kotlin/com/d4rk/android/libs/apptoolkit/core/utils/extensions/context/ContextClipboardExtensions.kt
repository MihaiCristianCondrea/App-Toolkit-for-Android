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

package com.d4rk.android.libs.apptoolkit.core.utils.extensions.context

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import com.d4rk.android.libs.apptoolkit.core.utils.constants.logging.CLIPBOARD_HELPER_LOG_TAG

/**
 * Copies [text] to the clipboard.
 *
 * - Android 13+ shows system UI confirmation; avoid duplicate in-app snackbars there.
 * - If [isSensitive] is true, the clipboard preview is obfuscated on Android 13+. :contentReference[oaicite:5]{index=5}
 * - [onCopyFallback] is invoked only on API 32 and lower where in-app feedback is still needed.
 *
 * @return true if the clipboard was written, false otherwise.
 */
fun Context.copyTextToClipboard(
    label: String,
    text: String,
    isSensitive: Boolean = false,
    onCopyFallback: () -> Unit = {},
): Boolean {
    val clipboard = getSystemService(ClipboardManager::class.java)
    if (clipboard == null) {
        Log.w(CLIPBOARD_HELPER_LOG_TAG, "Clipboard service unavailable")
        return false
    }

    val clip = ClipData.newPlainText(label, text).apply {
        if (isSensitive) {
            description.extras = (description.extras ?: PersistableBundle()).apply {
                putBoolean(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        ClipDescription.EXTRA_IS_SENSITIVE
                    } else {
                        "android.content.extra.IS_SENSITIVE"
                    },
                    true
                )
            }
        }
    }

    return runCatching {
        clipboard.setPrimaryClip(clip)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            onCopyFallback()
        }
        true
    }.getOrElse { t ->
        Log.w(CLIPBOARD_HELPER_LOG_TAG, "Failed to write clipboard", t)
        false
    }
}
