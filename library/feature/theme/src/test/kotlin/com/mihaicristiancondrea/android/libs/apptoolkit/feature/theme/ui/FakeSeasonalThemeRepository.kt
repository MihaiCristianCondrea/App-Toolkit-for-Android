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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.SeasonalThemeState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.WeatherEffect
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * In-memory [SeasonalThemeRepository] for the theme ViewModel tests. Every suspend call is recorded
 * in [calls], with its date where it takes one, before [failure] is thrown.
 */
class FakeSeasonalThemeRepository : SeasonalThemeRepository {

    /** The stored seasonal state. */
    val stored: MutableStateFlow<SeasonalThemeState> = MutableStateFlow(SeasonalThemeState())

    /** What [pendingHolidayGreeting] answers. */
    var pendingGreeting: HolidaySeason? = null

    /** Thrown by every suspend call, while set. */
    var failure: Throwable? = null

    /** The suspend calls made, in order, such as `restoreThemeAfterHoliday(2026-07-14)`. */
    val calls: MutableList<String> = mutableListOf()

    /** The greeting answers saved, as season, date and whether the holiday theme was taken. */
    val answers: MutableList<Triple<HolidaySeason, LocalDate, Boolean>> = mutableListOf()

    override val state: Flow<SeasonalThemeState> = stored

    override suspend fun unlockSeasonalThemes(): Boolean {
        record("unlockSeasonalThemes")
        val first = !stored.value.unlocked
        stored.update { it.copy(unlocked = true) }
        return first
    }

    override suspend fun setWeatherEffect(effect: WeatherEffect) {
        record("setWeatherEffect")
        stored.update { it.copy(weatherEffect = effect) }
    }

    override suspend fun pendingHolidayGreeting(today: LocalDate): HolidaySeason? {
        record("pendingHolidayGreeting($today)")
        return pendingGreeting
    }

    override suspend fun answerHolidayGreeting(
        season: HolidaySeason,
        today: LocalDate,
        useHolidayTheme: Boolean,
    ) {
        record("answerHolidayGreeting($today)")
        answers += Triple(season, today, useHolidayTheme)
    }

    override suspend fun restoreThemeAfterHoliday(today: LocalDate) {
        record("restoreThemeAfterHoliday($today)")
    }

    private fun record(call: String) {
        calls += call
        failure?.let { throw it }
    }
}
