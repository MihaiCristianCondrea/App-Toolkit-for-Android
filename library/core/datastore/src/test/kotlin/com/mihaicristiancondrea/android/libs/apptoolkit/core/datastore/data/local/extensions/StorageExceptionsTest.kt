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

import androidx.datastore.core.CorruptionException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.io.IOException

// The SQLite exceptions are android.jar stubs that throw when constructed in a JVM test, so the
// mapping is covered here through the DataStore and I/O cases.
class StorageExceptionsTest {

    @Test
    fun `translates storage failures and keeps them as the cause`() {
        val corruption = CorruptionException("unreadable")
        val translated = corruption.toStorageException()

        assertEquals(StorageException.Reason.CORRUPT, translated?.reason)
        assertSame(corruption, translated?.cause)
        assertEquals(StorageException.Reason.FAILED, IOException().toStorageException()?.reason)
    }

    @Test
    fun `leaves cancellation and other failures alone`() {
        assertNull(CancellationException().toStorageException())
        assertNull(IllegalArgumentException().toStorageException())
    }

    @Test
    fun `storage call rethrows a storage failure translated`() = runTest {
        val thrown = runCatching { storageCall<Unit> { throw IOException("disk") } }.exceptionOrNull()

        assertEquals(StorageException.Reason.FAILED, (thrown as? StorageException)?.reason)
    }
}
