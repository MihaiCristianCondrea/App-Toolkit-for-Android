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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.WeatherEffect
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.models.HolidayThemePreferences
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
        dataStore.data.map { it.readHolidaySnapshot() }.distinctUntilChanged()

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
        dataStore.edit { it.writeHolidaySnapshot(snapshot) }
    }

    override suspend fun updateHolidayTheme(transform: (HolidayThemePreferences) -> HolidayThemePreferences) {
        dataStore.edit { preferences ->
            val current = HolidayThemePreferences(
                paletteId = StaticPaletteIds.sanitize(preferences[ThemePreferenceKeys.staticPaletteId] ?: StaticPaletteIds.DEFAULT),
                dynamicColors = preferences[ThemePreferenceKeys.dynamicColors] != false,
                snapshot = preferences.readHolidaySnapshot(),
            )
            val next = transform(current)
            if (next.paletteId != current.paletteId) {
                preferences[ThemePreferenceKeys.staticPaletteId] = StaticPaletteIds.sanitize(next.paletteId)
            }
            if (next.dynamicColors != current.dynamicColors) {
                preferences[ThemePreferenceKeys.dynamicColors] = next.dynamicColors
            }
            if (next.snapshot != current.snapshot) preferences.writeHolidaySnapshot(next.snapshot)
        }
    }

    private fun Preferences.readHolidaySnapshot(): HolidayThemeSnapshot? {
        val season = this[holidaySeasonKey]
            ?.let { name -> HolidaySeason.entries.firstOrNull { it.name == name } }
            ?: return null
        return HolidayThemeSnapshot(
            season = season,
            previousPaletteId = StaticPaletteIds.sanitize(this[previousPaletteKey] ?: StaticPaletteIds.DEFAULT),
            previousDynamicColors = this[previousDynamicColorsKey] != false,
        )
    }

    private fun MutablePreferences.writeHolidaySnapshot(snapshot: HolidayThemeSnapshot?) {
        if (snapshot == null) {
            remove(holidaySeasonKey)
            remove(previousPaletteKey)
            remove(previousDynamicColorsKey)
        } else {
            this[holidaySeasonKey] = snapshot.season.name
            this[previousPaletteKey] = snapshot.previousPaletteId
            this[previousDynamicColorsKey] = snapshot.previousDynamicColors
        }
    }
}
