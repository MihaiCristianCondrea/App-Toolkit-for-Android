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
    private val migration = ReduceAdsMigration()

    @Test
    fun `install that had turned ads off opts into reduced ads`() = runTest {
        val before = mutablePreferencesOf(adsKey to false)

        assertTrue(migration.shouldMigrate(before))
        val after: Preferences = migration.migrate(before)

        assertEquals(true, after[reduceAdsKey])
        assertNull(after[adsKey])
    }

    // The preference is gone, so the key is dropped whichever value it held; only an install that
    // had switched ads off carries anything forward.
    @Test
    fun `install with ads on only loses the stale key`() = runTest {
        val after = migration.migrate(mutablePreferencesOf(adsKey to true))

        assertNull(after[adsKey])
        assertNull(after[reduceAdsKey])
    }

    @Test
    fun `fresh install has nothing to migrate`() = runTest {
        assertFalse(migration.shouldMigrate(emptyPreferences()))
    }

    @Test
    fun `unrelated preferences survive`() = runTest {
        val other = booleanPreferencesKey(DataStoreNamesConstants.DATA_STORE_AMOLED_MODE)

        val after = migration.migrate(mutablePreferencesOf(adsKey to false, other to true))

        assertEquals(true, after[other])
    }

    // Nothing writes the key any more, so its absence is what stops this running a second time.
    @Test
    fun `does not run twice`() = runTest {
        val migrated = migration.migrate(mutablePreferencesOf(adsKey to false))

        assertFalse(migration.shouldMigrate(migrated))
    }
}
