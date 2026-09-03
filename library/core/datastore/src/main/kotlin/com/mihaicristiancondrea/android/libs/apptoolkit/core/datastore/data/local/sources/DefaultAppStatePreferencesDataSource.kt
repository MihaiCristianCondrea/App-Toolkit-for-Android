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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.sources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.AppStatePreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/** App-state flags stored in the shared `settings` Preferences DataStore. */
class DefaultAppStatePreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : AppStatePreferencesDataSource {

    private val lastUsedKey =
        longPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_LAST_USED)
    private val settingsInteractedKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_SETTINGS_INTERACTED)
    private val componentsShowcaseUnlockedKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_COMPONENTS_SHOWCASE_UNLOCKED)

    override val lastUsed: Flow<Long> = dataStore.data.map { preferences: Preferences ->
        preferences[lastUsedKey] ?: 0
    }.distinctUntilChanged()

    override val settingsInteracted: Flow<Boolean> = dataStore.data.map { preferences: Preferences ->
        preferences[settingsInteractedKey] == true
    }.distinctUntilChanged()

    override val componentsShowcaseUnlocked: Flow<Boolean> =
        dataStore.data.map { preferences: Preferences ->
            preferences[componentsShowcaseUnlockedKey] == true
        }.distinctUntilChanged()

    override suspend fun saveLastUsed(timestamp: Long) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[lastUsedKey] = timestamp
        }
    }

    override suspend fun markSettingsInteracted() {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[settingsInteractedKey] = true
        }
    }

    override suspend fun saveComponentsShowcaseUnlocked(isUnlocked: Boolean) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[componentsShowcaseUnlockedKey] = isUnlocked
        }
    }
}
