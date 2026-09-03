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
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ChangelogPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/** Changelog state stored in the shared `settings` Preferences DataStore. */
class DefaultChangelogPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : ChangelogPreferencesDataSource {

    private val lastSeenVersionKey =
        stringPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_LAST_SEEN_VERSION)
    private val cachedChangelogKey =
        stringPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_CACHED_CHANGELOG)

    override fun lastSeenVersion(default: String): Flow<String> =
        dataStore.data.map { preferences: Preferences ->
            preferences[lastSeenVersionKey] ?: default
        }.distinctUntilChanged()

    override fun cachedChangelog(default: String): Flow<String> =
        dataStore.data.map { preferences: Preferences ->
            preferences[cachedChangelogKey] ?: default
        }.distinctUntilChanged()

    override suspend fun saveLastSeenVersion(version: String) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[lastSeenVersionKey] = version
        }
    }

    override suspend fun saveCachedChangelog(changelog: String) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[cachedChangelogKey] = changelog
        }
    }
}
