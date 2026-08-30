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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Stores Toolkit Tiles catalogue preferences in the app's shared Preferences DataStore. */
class DefaultToolkitTilesPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : ToolkitTilesPreferencesDataSource {

    override val expandedCategoryIds: Flow<Set<String>?> = dataStore.data.map { preferences ->
        preferences[EXPANDED_CATEGORY_IDS]
    }

    override suspend fun saveExpandedCategoryIds(categoryIds: Set<String>) {
        dataStore.edit { preferences ->
            preferences[EXPANDED_CATEGORY_IDS] = categoryIds
        }
    }

    private companion object {
        val EXPANDED_CATEGORY_IDS = stringSetPreferencesKey("expanded_toolkit_tile_category_ids")
    }
}
