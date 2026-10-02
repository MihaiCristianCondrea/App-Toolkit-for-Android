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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.migrations

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdsOptOutMigrationTest {

    private val adsKey = booleanPreferencesKey(DataStoreNamesConstants.DATA_STORE_ADS)
    private val reduceAdsKey = booleanPreferencesKey(DataStoreNamesConstants.DATA_STORE_REDUCE_ADS)

    private val migration = AdsOptOutMigration()

    @Test
    fun `an opted-out install is released and keeps its intent as reduced ads`() = runTest {
        val before = mutablePreferencesOf(adsKey to false)

        assertTrue(migration.shouldMigrate(before))
        val after: Preferences = migration.migrate(before)

        assertNull(after[adsKey])
        assertEquals(true, after[reduceAdsKey])
    }

    // Preserve explicit opt-ins even when the caller supplies a disabled ads default.
    @Test
    fun `an opted-in install is left alone`() = runTest {
        val before = mutablePreferencesOf(adsKey to true)

        assertFalse(migration.shouldMigrate(before))
        assertEquals(true, before[adsKey])
    }

    @Test
    fun `a fresh install has nothing to migrate`() = runTest {
        assertFalse(migration.shouldMigrate(emptyPreferences()))
    }

    @Test
    fun `unrelated preferences survive`() = runTest {
        val other = booleanPreferencesKey(DataStoreNamesConstants.DATA_STORE_AMOLED_MODE)

        val after = migration.migrate(mutablePreferencesOf(adsKey to false, other to true))

        assertEquals(true, after[other])
    }

    @Test
    fun `an existing reduced-ads choice is left alone`() = runTest {
        val after = migration.migrate(
            mutablePreferencesOf(adsKey to false, reduceAdsKey to true)
        )

        assertEquals(true, after[reduceAdsKey])
    }

    @Test
    fun `an opt-in survives repeated launches`() = runTest {
        var data: Preferences = mutablePreferencesOf(adsKey to true)

        repeat(times = 3) {
            if (migration.shouldMigrate(data)) {
                data = migration.migrate(data)
            }
        }

        assertEquals(true, data[adsKey])
    }

    @Test
    fun `the absent key is what stops this running twice`() = runTest {
        val migrated = migration.migrate(mutablePreferencesOf(adsKey to false))

        assertFalse(migration.shouldMigrate(migrated))
    }
}
