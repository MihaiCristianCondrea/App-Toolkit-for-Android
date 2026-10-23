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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.WeatherEffect
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.SeasonalThemeState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.date.holidayOccurrenceKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.date.holidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.SeasonalThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.models.HolidayThemeSnapshot
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

/**
 * Coordinates holiday palette changes and restoration. An existing snapshot retains the
 * original appearance when switching between holiday themes.
 */
class DefaultSeasonalThemeRepository(
    private val seasonal: SeasonalThemePreferencesDataSource,
) : SeasonalThemeRepository {

    override val state: Flow<SeasonalThemeState> = combine(
        seasonal.seasonalThemesUnlocked,
        seasonal.holidayThemeSnapshot,
        seasonal.weatherEffect,
    ) { unlocked, snapshot, weatherEffect ->
        SeasonalThemeState(unlocked = unlocked, holidayThemeInUse = snapshot?.season, weatherEffect = weatherEffect)
    }

    override suspend fun setWeatherEffect(effect: WeatherEffect) = seasonal.saveWeatherEffect(effect)

    override suspend fun unlockSeasonalThemes(): Boolean = seasonal.unlockSeasonalThemes()

    override suspend fun pendingHolidayGreeting(today: LocalDate): HolidaySeason? {
        val season = today.holidaySeason ?: return null
        val answered = seasonal.lastHolidayGreeting.first()
        return season.takeIf { answered != today.holidayOccurrenceKey(season) }
    }

    override suspend fun answerHolidayGreeting(
        season: HolidaySeason,
        today: LocalDate,
        useHolidayTheme: Boolean,
    ) {
        if (useHolidayTheme) applyHolidayTheme(season)
        seasonal.saveLastHolidayGreeting(today.holidayOccurrenceKey(season))
    }

    override suspend fun restoreThemeAfterHoliday(today: LocalDate) {
        seasonal.updateHolidayTheme { current ->
            val snapshot = current.snapshot ?: return@updateHolidayTheme current
            if (today.holidaySeason == snapshot.season) return@updateHolidayTheme current
            if (!current.dynamicColors && current.paletteId == snapshot.season.paletteId) {
                current.copy(
                    paletteId = snapshot.previousPaletteId,
                    dynamicColors = snapshot.previousDynamicColors,
                    snapshot = null,
                )
            } else {
                current.copy(snapshot = null)
            }
        }
    }

    private suspend fun applyHolidayTheme(season: HolidaySeason) {
        seasonal.updateHolidayTheme { current ->
            if (!current.dynamicColors && current.paletteId == season.paletteId) return@updateHolidayTheme current
            val snapshot = current.snapshot?.copy(season = season) ?: HolidayThemeSnapshot(
                season = season,
                previousPaletteId = current.paletteId,
                previousDynamicColors = current.dynamicColors,
            )
            current.copy(paletteId = season.paletteId, dynamicColors = false, snapshot = snapshot)
        }
    }
}
