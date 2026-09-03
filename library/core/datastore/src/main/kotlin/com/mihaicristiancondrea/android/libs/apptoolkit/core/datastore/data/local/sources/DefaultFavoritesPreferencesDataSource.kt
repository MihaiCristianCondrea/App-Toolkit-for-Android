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
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.FavoritesPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/** Favorited package names stored in the shared `settings` Preferences DataStore. */
class DefaultFavoritesPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : FavoritesPreferencesDataSource {

    private val favoriteAppsKey =
        stringSetPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_FAVORITE_APPS)

    override val favoriteApps: Flow<Set<String>> = dataStore.data.map { preferences: Preferences ->
        preferences[favoriteAppsKey] ?: emptySet()
    }.distinctUntilChanged()

    override suspend fun toggleFavoriteApp(packageName: String) {
        dataStore.edit { preferences: MutablePreferences ->
            val current = preferences[favoriteAppsKey]?.toMutableSet() ?: mutableSetOf()
            if (!current.add(packageName)) {
                current.remove(packageName)
            }
            preferences[favoriteAppsKey] = current
        }
    }
}
