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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.SeasonalThemeState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.date.holidayOccurrenceKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.date.holidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.SeasonalThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.models.HolidayThemeSnapshot
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

/** Reads and writes the seasonal themes through the preference store. */
class DefaultSeasonalThemeRepository(
    private val seasonal: SeasonalThemePreferencesDataSource,
    private val theme: ThemePreferencesDataSource,
) : SeasonalThemeRepository {

    override val state: Flow<SeasonalThemeState> = combine(
        seasonal.seasonalThemesUnlocked,
        seasonal.seasonalThemesAllYear,
        seasonal.snowfallEnabled,
        seasonal.holidayThemeSnapshot,
    ) { unlocked, allYear, snowfall, snapshot ->
        SeasonalThemeState(
            unlocked = unlocked,
            allYear = allYear,
            snowfallEnabled = snowfall,
            holidayThemeInUse = snapshot?.season,
        )
    }

    override suspend fun unlockSeasonalThemes(): Boolean {
        if (seasonal.seasonalThemesUnlocked.first()) return false
        seasonal.saveSeasonalThemesUnlocked(true)
        return true
    }

    override suspend fun setSeasonalThemesAllYear(enabled: Boolean) {
        seasonal.saveSeasonalThemesAllYear(enabled)
    }

    override suspend fun setSnowfallEnabled(enabled: Boolean) {
        seasonal.saveSnowfallEnabled(enabled)
    }

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
        val snapshot = seasonal.holidayThemeSnapshot.first() ?: return
        if (today.holidaySeason == snapshot.season) return

        if (isWearing(snapshot.season)) {
            theme.saveStaticPaletteId(snapshot.previousPaletteId)
            theme.saveDynamicColors(snapshot.previousDynamicColors)
        }
        seasonal.saveHolidayThemeSnapshot(null)
    }

    private suspend fun applyHolidayTheme(season: HolidaySeason) {
        if (isWearing(season)) return
        // Keep the oldest appearance. If a snapshot exists the person is already wearing a holiday
        // theme, and the snapshot holds their everyday appearance, which is what they expect back.
        val snapshot = seasonal.holidayThemeSnapshot.first()?.copy(season = season)
            ?: HolidayThemeSnapshot(
                season = season,
                previousPaletteId = theme.staticPaletteId.first(),
                previousDynamicColors = theme.dynamicColors.first(),
            )
        seasonal.saveHolidayThemeSnapshot(snapshot)
        theme.saveDynamicColors(false)
        theme.saveStaticPaletteId(season.paletteId)
    }

    private suspend fun isWearing(season: HolidaySeason): Boolean =
        !theme.dynamicColors.first() && theme.staticPaletteId.first() == season.paletteId
}
