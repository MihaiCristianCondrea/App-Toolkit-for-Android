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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.migrations

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

class ReduceAdsMigrationTest {

    private val adsKey = booleanPreferencesKey(DataStoreNamesConstants.DATA_STORE_ADS)
    private val reduceAdsKey = booleanPreferencesKey(DataStoreNamesConstants.DATA_STORE_REDUCE_ADS)
    private val migratedKey =
        booleanPreferencesKey(DataStoreNamesConstants.DATA_STORE_REDUCE_ADS_MIGRATED)

    private val migration = ReduceAdsMigration()

    @Test
    fun `install that had turned ads off opts into reduced ads`() = runTest {
        val before = mutablePreferencesOf(adsKey to false)

        assertTrue(migration.shouldMigrate(before))
        val after: Preferences = migration.migrate(before)

        assertEquals(true, after[reduceAdsKey])
        // Cleared rather than flipped, so enablement falls back to the host default exactly as it
        // does on a fresh install.
        assertNull(after[adsKey])
    }

    @Test
    fun `install with ads on is left alone`() = runTest {
        val after = migration.migrate(mutablePreferencesOf(adsKey to true))

        assertEquals(true, after[adsKey])
        assertNull(after[reduceAdsKey])
    }

    @Test
    fun `fresh install is left alone`() = runTest {
        val after = migration.migrate(emptyPreferences())

        assertNull(after[adsKey])
        assertNull(after[reduceAdsKey])
    }

    @Test
    fun `unrelated preferences survive`() = runTest {
        val other = booleanPreferencesKey(DataStoreNamesConstants.DATA_STORE_AMOLED_MODE)

        val after = migration.migrate(mutablePreferencesOf(adsKey to false, other to true))

        assertEquals(true, after[other])
    }

    // Hosts may still call `setAdsEnabled(false)` themselves; a second launch must not quietly
    // convert that into a reduced-ads opt-in.
    @Test
    fun `does not run twice`() = runTest {
        val migrated = migration.migrate(mutablePreferencesOf(adsKey to false))
        assertTrue(migrated[migratedKey] == true)
        assertFalse(migration.shouldMigrate(migrated))

        val hostDisabledAdsLater = migrated.toMutablePreferences().apply { set(adsKey, false) }
        assertFalse(migration.shouldMigrate(hostDisabledAdsLater))
    }
}
