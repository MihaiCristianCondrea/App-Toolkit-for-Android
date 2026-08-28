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
 * Releases installs that are stuck ad-free by a preference no release build can reach.
 *
 * Release builds no longer show the "Display ads" switch, but `ads` still gates SDK initialization
 * and every ad slot. An install that had switched ads off therefore had no way back: not only did
 * no ad render, but `AdsCoreManager` skipped `ensureAdsSdkInitialized()` entirely, so
 * `AdsSdkState.isReady` never turned true and every native and banner slot waited on it forever.
 * The switch that could have undone it only exists in debug builds.
 *
 * So the stored value is dropped and enablement falls back to the build's default — on for release,
 * off for debug, exactly as a fresh install. The intent behind an explicit opt-out is not thrown
 * away: it moves to `reduce_ads`, the preference release builds *do* expose, which suppresses the
 * app-open ad. Those installs see ordinary in-app ads again, which is the deliberate trade for
 * giving them a working control.
 *
 * The key is dropped whichever value it held, so that after this runs the stored state matches a
 * fresh install in every build. Its absence is also what stops this running twice; the debug switch
 * writing `ads` again is a deliberate choice by a developer, and gets migrated the same way on the
 * next upgrade.
 *
 * Runs as a [DataMigration] on the `settings` store, so it completes before the first read is
 * served and no consumer — least of all `AdsCoreManager`, which samples the preference once at
 * startup — can observe the pre-migration value.
 */
class AdsOptOutMigration : DataMigration<Preferences> {

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
    listOf(AdsOptOutMigration())
