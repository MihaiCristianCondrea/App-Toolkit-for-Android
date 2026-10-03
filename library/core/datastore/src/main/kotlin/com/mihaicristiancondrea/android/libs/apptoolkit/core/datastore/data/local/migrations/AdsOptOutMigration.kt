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

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants

/**
 * Migrates the legacy ads opt-out to the release-visible reduced-ads preference.
 *
 * Only a stored `ads = false` is removed; explicit opt-ins remain unchanged. The removed value
 * falls back to the configured ads default, while `reduce_ads = true` preserves the opt-out as
 * suppression of App Open ads. Subsequent reads no longer match [shouldMigrate].
 *
 * Runs before the first `settings` read, so SDK initialization and ad slots cannot observe the
 * legacy opt-out.
 */
class AdsOptOutMigration : DataMigration<Preferences> {

    private val adsKey = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_ADS)

    private val reduceAdsKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_REDUCE_ADS)

    override suspend fun shouldMigrate(currentData: Preferences): Boolean =
        currentData[adsKey] == false

    override suspend fun migrate(currentData: Preferences): Preferences {
        val migrated: MutablePreferences = currentData.toMutablePreferences()

        migrated[reduceAdsKey] = true
        migrated.remove(adsKey)
        return migrated
    }

    override suspend fun cleanUp() = Unit
}

/** The migrations every AppToolkit host must apply to the shared `settings` DataStore. */
internal fun commonDataStoreMigrations(): List<DataMigration<Preferences>> =
    listOf(AdsOptOutMigration())
