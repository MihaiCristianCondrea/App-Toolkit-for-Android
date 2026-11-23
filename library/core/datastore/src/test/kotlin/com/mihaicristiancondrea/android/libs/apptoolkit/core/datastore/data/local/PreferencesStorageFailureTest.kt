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

import androidx.datastore.core.CorruptionException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultDisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class PreferencesStorageFailureTest {

    private val store = FakePreferencesDataStore()
    private val themeSource = DefaultThemePreferencesDataSource(dataStore = store)
    private val displaySource = DefaultDisplayPreferencesDataSource(dataStore = store)

    @Test
    fun `a failed read of the theme becomes a storage failure`() = runTest {
        store.readFailure = IOException("disk")

        val failure = assertFailsWith<StorageException> { themeSource.storedPreferences.first() }

        assertEquals(StorageException.Reason.FAILED, failure.reason)
    }

    @Test
    fun `corrupt theme data is reported as corrupt`() = runTest {
        store.readFailure = CorruptionException("bad bytes")

        val failure = assertFailsWith<StorageException> { themeSource.themeMode.first() }

        assertEquals(StorageException.Reason.CORRUPT, failure.reason)
    }

    @Test
    fun `a failed read of the display preferences becomes a storage failure`() = runTest {
        store.readFailure = IOException("disk")

        assertFailsWith<StorageException> { displaySource.bouncyButtons.first() }
        assertFailsWith<StorageException> { displaySource.startupPage(default = "home").first() }
    }

    @Test
    fun `a read failure that is not a storage failure passes through`() = runTest {
        store.readFailure = IllegalStateException("bug")

        assertFailsWith<IllegalStateException> { themeSource.themeMode.first() }
    }

    @Test
    fun `reads work again once the store recovers`() = runTest {
        store.readFailure = IOException("disk")
        assertFailsWith<StorageException> { displaySource.language.first() }

        store.readFailure = null

        assertEquals("en", displaySource.language.first())
    }

    @Test
    fun `a failed theme write is a storage failure and changes nothing`() = runTest {
        val repository = DefaultThemePreferencesRepository(themeSource)
        store.writeFailure = IOException("disk full")

        val failure = assertFailsWith<StorageException> { repository.setAmoledMode(true) }

        assertEquals(StorageException.Reason.FAILED, failure.reason)
        store.writeFailure = null
        assertFalse(themeSource.amoledMode.first())
    }

    @Test
    fun `a failed display write is a storage failure`() = runTest {
        val repository = DefaultDisplayPreferencesRepository(displaySource)
        store.writeFailure = IOException("disk full")

        assertFailsWith<StorageException> { repository.setLanguage("ro") }
        assertFailsWith<StorageException> { repository.setStartupPage("home") }
    }
}
