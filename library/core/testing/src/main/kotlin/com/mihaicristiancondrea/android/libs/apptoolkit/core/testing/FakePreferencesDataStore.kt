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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.testing

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * An in-memory Preferences DataStore for testing the data sources written over one.
 *
 * Set [readFailure] or [writeFailure] to make the store fail the way a disk error does. A read
 * failure is thrown to each collector of [data] before it receives a value, as DataStore does; a
 * write failure is thrown from [updateData] before the transform runs and leaves the state alone.
 * Set either back to `null` to recover.
 */
class FakePreferencesDataStore(initial: Preferences = emptyPreferences()) : DataStore<Preferences> {
    private val state = MutableStateFlow(initial)
    private val updates = Mutex()

    /** Thrown to every collector of [data] while it is not `null`. */
    @Volatile
    var readFailure: Throwable? = null

    /** Thrown from [updateData] while it is not `null`. */
    @Volatile
    var writeFailure: Throwable? = null

    override val data: Flow<Preferences> = flow {
        readFailure?.let { throw it }
        emitAll(state)
    }

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences = updates.withLock {
        writeFailure?.let { throw it }
        val updated = transform(state.value)
        state.value = updated
        updated
    }
}
