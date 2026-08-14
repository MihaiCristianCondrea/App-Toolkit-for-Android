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
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.DynamicPaletteVariant
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ThemePreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/** Appearance preferences stored in the shared `settings` Preferences DataStore. */
class DefaultThemePreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : ThemePreferencesDataSource {

    private val themeModeKey =
        stringPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_THEME_MODE)
    private val amoledModeKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_AMOLED_MODE)
    private val dynamicColorsKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_DYNAMIC_COLORS)
    private val dynamicPaletteVariantKey =
        intPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_DYNAMIC_PALETTE_VARIANT)
    private val staticPaletteIdKey =
        stringPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_STATIC_PALETTE_ID)

    override val themeMode: Flow<String> = dataStore.data.map { preferences: Preferences ->
        preferences[themeModeKey] ?: DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM
    }.distinctUntilChanged()

    override val amoledMode: Flow<Boolean> = dataStore.data.map { preferences: Preferences ->
        preferences[amoledModeKey] == true
    }.distinctUntilChanged()

    override val dynamicColors: Flow<Boolean> = dataStore.data.map { preferences: Preferences ->
        preferences[dynamicColorsKey] != false
    }.distinctUntilChanged()

    override val dynamicPaletteVariant: Flow<Int> = dataStore.data.map { preferences: Preferences ->
        DynamicPaletteVariant.clamp(preferences[dynamicPaletteVariantKey] ?: 0)
    }.distinctUntilChanged()

    override val staticPaletteId: Flow<String> = dataStore.data.map { preferences: Preferences ->
        StaticPaletteIds.sanitize(preferences[staticPaletteIdKey] ?: StaticPaletteIds.DEFAULT)
    }.distinctUntilChanged()

    override suspend fun saveThemeMode(mode: String) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[themeModeKey] = mode
        }
    }

    override suspend fun saveAmoledMode(isChecked: Boolean) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[amoledModeKey] = isChecked
        }
    }

    override suspend fun saveDynamicColors(isChecked: Boolean) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[dynamicColorsKey] = isChecked
        }
    }

    override suspend fun saveDynamicPaletteVariant(variant: Int) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[dynamicPaletteVariantKey] = DynamicPaletteVariant.clamp(variant)
        }
    }

    override suspend fun saveStaticPaletteId(id: String) {
        val safe = StaticPaletteIds.sanitize(id)
        dataStore.edit { preferences: MutablePreferences ->
            preferences[staticPaletteIdKey] = safe
        }
    }
}
