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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ReviewPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/** In-app review counters stored in the shared `settings` Preferences DataStore. */
class DefaultReviewPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : ReviewPreferencesDataSource {

    private val sessionCountKey =
        longPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_SESSION_COUNT)
    private val reviewPromptedKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_REVIEW_PROMPTED)

    override val sessionCount: Flow<Int> = dataStore.data.map { preferences: Preferences ->
        (preferences[sessionCountKey] ?: 0L).toInt()
    }.distinctUntilChanged()

    override val hasPromptedReview: Flow<Boolean> = dataStore.data.map { preferences: Preferences ->
        preferences[reviewPromptedKey] == true
    }.distinctUntilChanged()

    override suspend fun incrementSessionCount() {
        dataStore.edit { preferences: MutablePreferences ->
            val current: Long = preferences[sessionCountKey] ?: 0L
            preferences[sessionCountKey] = current + 1L
        }
    }

    override suspend fun setHasPromptedReview(value: Boolean) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[reviewPromptedKey] = value
        }
    }
}
