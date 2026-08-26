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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.sources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.AdsPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Limit-ads preference stored in the shared `settings` Preferences DataStore.
 *
 * The default is a hard `false` with no build input: limiting ads is something the user opts into.
 * Installs that had switched ads off under the preference this one replaced are carried over by
 * `LimitAdsMigration` before any read is served.
 */
class DefaultAdsPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : AdsPreferencesDataSource {

    private val limitAdsKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_LIMIT_ADS)

    override val limitAds: Flow<Boolean> =
        dataStore.data.map { preferences: Preferences ->
            preferences[limitAdsKey] ?: DEFAULT_LIMIT_ADS
        }.distinctUntilChanged()

    override suspend fun saveLimitAds(isChecked: Boolean) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[limitAdsKey] = isChecked
        }
    }

    private companion object {
        /** Limiting ads is an opt-in, so it is never on until the user asks for it. */
        const val DEFAULT_LIMIT_ADS: Boolean = false
    }
}
