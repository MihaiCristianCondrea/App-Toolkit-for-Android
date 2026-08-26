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
 * Carries an install that had switched ads off over to the reduced-ads preference.
 *
 * The ads-enabled preference is gone: the SDK initializes and ad slots render unconditionally, and
 * the ads screen offers a single "Reduce ads" toggle. Left alone, an install that had switched ads
 * off would simply start seeing every ad again with the toggle off. So the intent moves to the
 * preference that still exists — `reduceAds` becomes `true`, which is as much of the old promise as
 * the new model can express: no ad when the app is opened.
 *
 * The stale `ads` key is always dropped, whichever value it held. Nothing writes it any more, so
 * its absence is what makes this run once; no marker preference is needed.
 *
 * This runs as a [DataMigration] on the `settings` DataStore, so it completes before the first read
 * is served and no consumer ever observes the pre-migration values.
 */
class ReduceAdsMigration : DataMigration<Preferences> {

    private val adsKey = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_ADS)

    private val reduceAdsKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_REDUCE_ADS)

    override suspend fun shouldMigrate(currentData: Preferences): Boolean =
        currentData.contains(adsKey)

    override suspend fun migrate(currentData: Preferences): Preferences {
        val migrated: MutablePreferences = currentData.toMutablePreferences()

        if (currentData[adsKey] == false) {
            migrated[reduceAdsKey] = true
        }

        migrated.remove(adsKey)
        return migrated
    }

    override suspend fun cleanUp() = Unit
}

/** The migrations every AppToolkit host must apply to the shared `settings` DataStore. */
internal fun commonDataStoreMigrations(): List<DataMigration<Preferences>> =
    listOf(ReduceAdsMigration())
