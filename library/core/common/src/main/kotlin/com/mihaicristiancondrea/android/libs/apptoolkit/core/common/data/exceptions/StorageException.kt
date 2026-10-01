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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions

import java.io.IOException

/**
 * Reading or writing local storage failed, for [reason].
 *
 * The data layer throws it in place of the storage library's own exception, so a caller can tell
 * the failures apart without depending on DataStore or SQLite. `:library:core:datastore`
 * translates those exceptions into this one, and the UI layer turns it into text. The original
 * exception stays as [cause] for reporting.
 *
 * It is an [IOException], so code that already catches I/O failures still catches it.
 */
class StorageException(
    val reason: Reason,
    cause: Throwable? = null,
) : IOException("Storage operation failed: $reason", cause) {

    enum class Reason {
        /** The device has no space left for the write. */
        FULL,

        /** Another operation holds the storage; the same call can succeed a moment later. */
        BUSY,

        /** The stored data could not be read back. */
        CORRUPT,

        /** The storage could not be opened at all. */
        UNAVAILABLE,

        /** Any other failure of the storage operation. */
        FAILED,
    }
}
