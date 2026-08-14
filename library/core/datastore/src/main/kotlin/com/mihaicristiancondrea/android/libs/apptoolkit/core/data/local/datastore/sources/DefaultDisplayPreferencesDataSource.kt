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
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.DisplayPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/** Display preferences stored in the shared `settings` Preferences DataStore. */
class DefaultDisplayPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : DisplayPreferencesDataSource {

    private val showBottomBarLabelsKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_SHOW_BOTTOM_BAR_LABELS)
    private val bouncyButtonsKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_BOUNCY_BUTTONS)
    private val languageKey =
        stringPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_LANGUAGE)
    private val startupPageKey =
        stringPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_STARTUP_PAGE)

    override val showBottomBarLabels: Flow<Boolean> =
        dataStore.data.map { preferences: Preferences ->
            preferences[showBottomBarLabelsKey] != false
        }.distinctUntilChanged()

    override val bouncyButtons: Flow<Boolean> = dataStore.data.map { preferences: Preferences ->
        preferences[bouncyButtonsKey] != false
    }.distinctUntilChanged()

    override val language: Flow<String> = dataStore.data.map { preferences: Preferences ->
        preferences[languageKey] ?: DEFAULT_LANGUAGE
    }.distinctUntilChanged()

    override fun startupPage(default: String): Flow<String> =
        dataStore.data.map { preferences: Preferences ->
            preferences[startupPageKey] ?: default
        }.distinctUntilChanged()

    override suspend fun saveShowBottomBarLabels(isChecked: Boolean) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[showBottomBarLabelsKey] = isChecked
        }
    }

    override suspend fun saveBouncyButtons(isChecked: Boolean) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[bouncyButtonsKey] = isChecked
        }
    }

    override suspend fun saveLanguage(language: String) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[languageKey] = language
        }
    }

    override suspend fun saveStartupPage(route: String) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[startupPageKey] = route
        }
    }

    private companion object {
        const val DEFAULT_LANGUAGE: String = "en"
    }
}
