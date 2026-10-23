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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.local

import app.cash.turbine.test
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ChangelogPreferencesDataSourceTest {

    @Test
    fun `last seen version defaults to empty and persists`() = runTest {
        val dataStore = createDataStore()
        val default = ""
        val expected = "1.0.0"

        assertEquals(default, dataStore.lastSeenVersion(default).first())

        dataStore.saveLastSeenVersion(expected)

        assertEquals(expected, dataStore.lastSeenVersion(default).first())
    }

    @Test
    fun `cached changelog defaults to empty and persists`() = runTest {
        val dataStore = createDataStore()
        val default = ""
        val expected = "New release"

        assertEquals(default, dataStore.cachedChangelog(default).first())

        dataStore.saveCachedChangelog(expected)

        assertEquals(expected, dataStore.cachedChangelog(default).first())
    }

    private fun createDataStore(): DefaultChangelogPreferencesDataSource =
        DefaultChangelogPreferencesDataSource(dataStore = FakePreferencesDataStore())
}
