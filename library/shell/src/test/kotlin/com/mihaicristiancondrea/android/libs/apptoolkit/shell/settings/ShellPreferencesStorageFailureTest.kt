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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesOf
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.data.repositories.DefaultShellPreferencesRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ShellPreferencesStorageFailureTest {
    @Test
    fun `setter update and reset expose storage failures`() = runTest {
        val store = object : DataStore<Preferences> {
            override val data = flowOf(emptyPreferences())
            override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences =
                throw IOException("Write failed")
        }
        val repository = DefaultShellPreferencesRepository(store, ShellSettings())
        assertFailsWith<StorageException> { repository.setHideTopBarOnScroll(true) }
        assertFailsWith<StorageException> { repository.update { it.copy(hideTopBarOnScroll = true) } }
        assertFailsWith<StorageException> { repository.resetDeveloperOptions() }
    }

    @Test
    fun `a failed read falls back to defaults without replacing the last known settings`() = runTest {
        val chosen = booleanPreferencesKey("hide_top_bar_on_scroll")
        val store = object : DataStore<Preferences> {
            override val data = flow {
                emit(preferencesOf(chosen to true))
                throw IOException("Read failed")
            }
            override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences =
                emptyPreferences()
        }
        val repository = DefaultShellPreferencesRepository(store, ShellSettings())

        val emitted = repository.settings.toList()

        assertEquals(listOf(ShellSettings(hideTopBarOnScroll = true), ShellSettings()), emitted)
        assertEquals(ShellSettings(hideTopBarOnScroll = true), repository.lastKnown)
    }

    @Test
    fun `a read that fails first emits defaults and leaves nothing known`() = runTest {
        val store = object : DataStore<Preferences> {
            override val data = flow<Preferences> { throw IOException("Read failed") }
            override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences =
                emptyPreferences()
        }
        val repository = DefaultShellPreferencesRepository(store, ShellSettings())

        assertEquals(listOf(ShellSettings()), repository.settings.toList())
        assertNull(repository.lastKnown)
    }

    @Test
    fun `each repository remembers its own last read`() = runTest {
        val chosen = booleanPreferencesKey("hide_top_bar_on_scroll")
        val first = DefaultShellPreferencesRepository(FakeStore(preferencesOf(chosen to true)), ShellSettings())
        val second = DefaultShellPreferencesRepository(FakeStore(emptyPreferences()), ShellSettings())

        first.settings.toList()

        assertEquals(ShellSettings(hideTopBarOnScroll = true), first.lastKnown)
        assertNull(second.lastKnown)
    }

    private class FakeStore(preferences: Preferences) : DataStore<Preferences> {
        override val data = flowOf(preferences)
        override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences =
            emptyPreferences()
    }
}
