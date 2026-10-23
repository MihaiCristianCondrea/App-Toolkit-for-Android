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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.datastore.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Keeps the components showcase flag in [dataStore] under the sample's own key.
 */
class DefaultAppPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : AppPreferencesDataSource {

    override val componentsShowcaseUnlocked: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[Keys.componentsShowcaseUnlocked] == true }
        .distinctUntilChanged()

    override suspend fun saveComponentsShowcaseUnlocked(isUnlocked: Boolean) {
        dataStore.edit { preferences -> preferences[Keys.componentsShowcaseUnlocked] = isUnlocked }
    }

    private object Keys {
        val componentsShowcaseUnlocked = booleanPreferencesKey("components_showcase_unlocked")
    }
}
