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

/**
 * The system clipboard, as the data layer exposes it.
 *
 * The clipboard is a platform data source, so a ViewModel reaches it through this repository
 * rather than through a `Context`. The platform's own rules, such as whether the system confirms a
 * copy, live here too, so callers never check `Build.VERSION`.
 */
interface ClipboardRepository {

    /**
     * Whether the system shows its own confirmation for every copy, as Android 13 and later do.
     * An in-app confirmation would then report the same copy twice.
     */
    val confirmsCopies: Boolean

    /**
     * Writes [text] to the clipboard under [label].
     *
     * Safe to call from the main thread: the write is one quick call to the clipboard service.
     *
     * @param isSensitive Hides the text in the system's clipboard preview.
     * @throws IllegalStateException when the clipboard is unavailable or rejects the write.
     */
    fun copyText(label: String, text: String, isSensitive: Boolean = false)
}
