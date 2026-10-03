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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.WeatherEffect
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

/**
 * Seasonal preferences in the shared `settings` DataStore. Unknown weather-effect names use
 * [WeatherEffect.Automatic]; unknown season names produce no holiday snapshot. Snapshot fields
 * are written or cleared in one transaction.
 */
class DefaultSeasonalThemePreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : SeasonalThemePreferencesDataSource {

    private val unlockedKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_SEASONAL_THEMES_UNLOCKED)
    private val weatherEffectKey =
        stringPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_SEASONAL_WEATHER_EFFECT)
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

    override val weatherEffect: Flow<WeatherEffect> = dataStore.data.map { preferences ->
        preferences[weatherEffectKey]
            ?.let { name -> WeatherEffect.entries.firstOrNull { it.name == name } }
            ?: WeatherEffect.Automatic
    }.distinctUntilChanged()

    override val lastHolidayGreeting: Flow<String?> = dataStore.data.map { preferences ->
        preferences[lastGreetingKey]
    }.distinctUntilChanged()

    override val holidayThemeSnapshot: Flow<HolidayThemeSnapshot?> =
        dataStore.data.map { preferences ->
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

    override suspend fun saveWeatherEffect(effect: WeatherEffect) {
        dataStore.edit { preferences: MutablePreferences -> preferences[weatherEffectKey] = effect.name }
    }

    override suspend fun saveLastHolidayGreeting(occurrenceKey: String) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[lastGreetingKey] = occurrenceKey
        }
    }

    override suspend fun saveHolidayThemeSnapshot(snapshot: HolidayThemeSnapshot?) {
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
