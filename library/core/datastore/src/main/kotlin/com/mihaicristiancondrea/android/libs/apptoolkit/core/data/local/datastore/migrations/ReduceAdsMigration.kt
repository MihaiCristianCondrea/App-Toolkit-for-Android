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

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants

/**
 * Carries an install that turned ads off under the old switch over to the reduced-ads preference.
 *
 * The ads screen now offers a single "Reduce ads" toggle. Leaving a grandfathered install on
 * `adsEnabled = false` would show that toggle off while the app displayed nothing, and turning it
 * off again would change nothing — the screen would be lying about the only control it has. So the
 * intent moves to where the switch can act on it: `reduceAds` becomes `true` and the old key is
 * cleared, which returns ads enablement to the host-configured default exactly as on a fresh
 * install.
 *
 * These users keep the part of the promise the new preference can express — no ad when the app is
 * opened — and see ordinary in-app ads again.
 *
 * A marker key, rather than the absence of `ads`, is what makes this run once: hosts may still call
 * `setAdsEnabled(false)` for their own reasons, and a later launch must not silently undo that.
 *
 * This runs as a [DataMigration] on the `settings` DataStore, so it completes before the first read
 * is served and no consumer ever observes the pre-migration values.
 */
class ReduceAdsMigration : DataMigration<Preferences> {

    private val adsKey = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_ADS)

    private val reduceAdsKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_REDUCE_ADS)

    private val migratedKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_REDUCE_ADS_MIGRATED)

    override suspend fun shouldMigrate(currentData: Preferences): Boolean =
        currentData[migratedKey] != true

    override suspend fun migrate(currentData: Preferences): Preferences {
        val migrated: MutablePreferences = currentData.toMutablePreferences()

        if (currentData[adsKey] == false) {
            migrated[reduceAdsKey] = true
            migrated.remove(adsKey)
        }

        migrated[migratedKey] = true
        return migrated
    }

    override suspend fun cleanUp() = Unit
}

/** The migrations every AppToolkit host must apply to the shared `settings` DataStore. */
internal fun commonDataStoreMigrations(): List<DataMigration<Preferences>> =
    listOf(ReduceAdsMigration())
