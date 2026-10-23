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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local

import androidx.datastore.preferences.core.intPreferencesKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class FakePreferencesDataStoreTest {
    @Test
    fun `concurrent transforms serialize even when the first suspends`() = runTest {
        val store = FakePreferencesDataStore()
        val count = intPreferencesKey("count")
        val release = CompletableDeferred<Unit>()
        val first = async(start = CoroutineStart.UNDISPATCHED) {
            store.updateData { current ->
                release.await()
                current.toMutablePreferences().apply { this[count] = (current[count] ?: 0) + 1 }
            }
        }
        val second = async(start = CoroutineStart.UNDISPATCHED) {
            store.updateData { current ->
                current.toMutablePreferences().apply { this[count] = (current[count] ?: 0) + 1 }
            }
        }
        assertFalse(second.isCompleted)
        release.complete(Unit)
        first.await()
        second.await()
        assertEquals(2, store.data.first()[count])
    }

    @Test
    fun `a failed transform leaves the stored state and mutex usable`() = runTest {
        val store = FakePreferencesDataStore()
        val count = intPreferencesKey("count")
        assertFailsWith<IllegalStateException> {
            store.updateData { current ->
                current.toMutablePreferences().apply { this[count] = 99 }
                error("write failed")
            }
        }
        assertEquals(null, store.data.first()[count])
        store.updateData { current -> current.toMutablePreferences().apply { this[count] = 1 } }
        assertEquals(1, store.data.first()[count])
    }
}
