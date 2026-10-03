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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions

import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteDatabaseLockedException
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import androidx.datastore.core.CorruptionException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import kotlinx.coroutines.CancellationException
import java.io.IOException

/**
 * Runs [block], a read or write of local storage, and rethrows a storage failure as
 * [StorageException].
 *
 * Repositories wrap their DataStore or database calls in it so their callers see one exception
 * type whatever the storage library threw. Cancellation and failures that are not storage failures
 * pass through unchanged.
 */
suspend inline fun <T> storageCall(crossinline block: suspend () -> T): T =
    try {
        block()
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (throwable: Throwable) {
        throw throwable.toStorageException() ?: throwable
    }

/**
 * Translates this into a [StorageException], keeping it as the cause, or returns `null` when it is
 * not a storage failure.
 *
 * Any [IOException] counts: thrown from inside a storage call, it is the storage that failed.
 */
fun Throwable.toStorageException(): StorageException? {
    val reason: StorageException.Reason = when (this) {
        is StorageException -> return this
        is CancellationException -> return null

        is SQLiteFullException -> StorageException.Reason.FULL
        is SQLiteDatabaseLockedException -> StorageException.Reason.BUSY
        is SQLiteDatabaseCorruptException,
        is CorruptionException -> StorageException.Reason.CORRUPT
        is SQLiteCantOpenDatabaseException -> StorageException.Reason.UNAVAILABLE
        is SQLiteException,
        is IOException -> StorageException.Reason.FAILED

        else -> return null
    }
    return StorageException(reason = reason, cause = this)
}
