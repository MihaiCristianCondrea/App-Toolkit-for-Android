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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.logging.CLIPBOARD_HELPER_LOG_TAG

/**
 * Copies [text] to the clipboard.
 *
 * - Android 13+ shows system UI confirmation; avoid duplicate in-app snackbars there.
 * - If [isSensitive] is true, the clipboard preview is obfuscated on Android 13+. :contentReference[oaicite:5]{index=5}
 * - [onCopyFallback] is invoked only on API 32 and lower where in-app feedback is still needed.
 *
 * From Android 10 the system only lets the focused window touch the clipboard, and it enforces that
 * by dropping the write in silence: [ClipboardManager.setPrimaryClip] neither throws nor reports
 * anything back. Something as ordinary as the system clipboard preview taking focus is enough for
 * the next copy to disappear, so the write is read back before it is called a success, and a caller
 * that shows a confirmation only shows one for a copy that actually landed.
 *
 * @return true only when the clipboard reports holding the clip that was just written.
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
        val requestedAtMillis: Long = System.currentTimeMillis()
        clipboard.setPrimaryClip(clip)

        if (!clipboard.holdsClipWrittenAt(label = label, requestedAtMillis = requestedAtMillis)) {
            Log.w(
                CLIPBOARD_HELPER_LOG_TAG,
                "Clipboard dropped the write for \"$label\"; the window was most likely not focused"
            )
            return@runCatching false
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            onCopyFallback()
        }
        true
    }.getOrElse { t ->
        Log.w(CLIPBOARD_HELPER_LOG_TAG, "Failed to write clipboard", t)
        false
    }
}

/**
 * True when the clipboard reports holding the clip written at [requestedAtMillis] under [label].
 *
 * Only the clip description is read, never its content, so this never trips the "pasted from your
 * clipboard" notice Android 12 shows for reading another app's clip. A denied write leaves the
 * previous description in place, which fails the timestamp check, and a denied read returns null,
 * which fails outright.
 *
 * [ClipDescription.getTimestamp] is 0 for a clip the platform never stamped; the label alone decides
 * there, which is no weaker than not checking at all.
 */
private fun ClipboardManager.holdsClipWrittenAt(label: String, requestedAtMillis: Long): Boolean {
    val description: ClipDescription = runCatching { primaryClipDescription }.getOrNull() ?: return false
    if (description.label?.toString() != label) return false

    val timestamp: Long = description.timestamp
    return timestamp == UNSTAMPED_CLIP || timestamp >= requestedAtMillis
}

/** [ClipDescription.getTimestamp] for a clip the platform did not stamp. */
private const val UNSTAMPED_CLIP: Long = 0L
