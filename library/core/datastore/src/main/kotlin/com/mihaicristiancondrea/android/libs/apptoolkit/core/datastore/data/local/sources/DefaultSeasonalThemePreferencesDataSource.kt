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
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.SeasonalThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.models.HolidayThemeSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/** Seasonal theme preferences stored in the shared `settings` Preferences DataStore. */
class DefaultSeasonalThemePreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : SeasonalThemePreferencesDataSource {

    private val unlockedKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_SEASONAL_THEMES_UNLOCKED)
    private val allYearKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_SEASONAL_THEMES_ALL_YEAR)
    private val snowfallKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_SNOWFALL_ENABLED)
    private val lastGreetingKey =
        stringPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_LAST_HOLIDAY_GREETING)
    private val holidaySeasonKey =
        stringPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_HOLIDAY_THEME_SEASON)
    private val previousPaletteKey =
        stringPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_HOLIDAY_PREVIOUS_PALETTE_ID)
    private val previousDynamicColorsKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_HOLIDAY_PREVIOUS_DYNAMIC_COLORS)

    override val seasonalThemesUnlocked: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[unlockedKey] == true
    }.distinctUntilChanged()

    override val seasonalThemesAllYear: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[allYearKey] == true
    }.distinctUntilChanged()

    override val snowfallEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[snowfallKey] != false
    }.distinctUntilChanged()

    override val lastHolidayGreeting: Flow<String?> = dataStore.data.map { preferences ->
        preferences[lastGreetingKey]
    }.distinctUntilChanged()

    override val holidayThemeSnapshot: Flow<HolidayThemeSnapshot?> =
        dataStore.data.map { preferences ->
            // A season name this build no longer knows is treated as no holiday theme at all.
            val season = preferences[holidaySeasonKey]
                ?.let { name -> HolidaySeason.entries.firstOrNull { it.name == name } }
                ?: return@map null
            HolidayThemeSnapshot(
                season = season,
                previousPaletteId = StaticPaletteIds.sanitize(
                    preferences[previousPaletteKey] ?: StaticPaletteIds.DEFAULT,
                ),
                previousDynamicColors = preferences[previousDynamicColorsKey] != false,
            )
        }.distinctUntilChanged()

    override suspend fun saveSeasonalThemesUnlocked(unlocked: Boolean) {
        dataStore.edit { preferences: MutablePreferences -> preferences[unlockedKey] = unlocked }
    }

    override suspend fun saveSeasonalThemesAllYear(enabled: Boolean) {
        dataStore.edit { preferences: MutablePreferences -> preferences[allYearKey] = enabled }
    }

    override suspend fun saveSnowfallEnabled(enabled: Boolean) {
        dataStore.edit { preferences: MutablePreferences -> preferences[snowfallKey] = enabled }
    }

    override suspend fun saveLastHolidayGreeting(occurrenceKey: String) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[lastGreetingKey] = occurrenceKey
        }
    }

    override suspend fun saveHolidayThemeSnapshot(snapshot: HolidayThemeSnapshot?) {
        // One transaction, so a snapshot is never half written or half cleared.
        dataStore.edit { preferences: MutablePreferences ->
            if (snapshot == null) {
                preferences.remove(holidaySeasonKey)
                preferences.remove(previousPaletteKey)
                preferences.remove(previousDynamicColorsKey)
            } else {
                preferences[holidaySeasonKey] = snapshot.season.name
                preferences[previousPaletteKey] = snapshot.previousPaletteId
                preferences[previousDynamicColorsKey] = snapshot.previousDynamicColors
            }
        }
    }
}
