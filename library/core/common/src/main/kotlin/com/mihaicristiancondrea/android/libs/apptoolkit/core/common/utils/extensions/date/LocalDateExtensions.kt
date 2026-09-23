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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.date

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason
import java.time.LocalDate
import java.time.Month
import java.time.MonthDay

/**
 * Returns whether the current date falls within the Christmas season.
 *
 * The season starts on December 24th and continues through January 7th to
 * accommodate regions that celebrate Christmas later (e.g., Georgian Christmas
 * on January 7th).
 */
val LocalDate.isChristmasSeason: Boolean
    get() = isWithinSeason(
        start = MonthDay.of(Month.DECEMBER, 24),
        end = MonthDay.of(Month.JANUARY, 7),
    )

/**
 * Returns whether the current date falls within the Halloween season.
 *
 * The season covers October 31st through November 2nd.
 */
val LocalDate.isHalloweenSeason: Boolean
    get() = isWithinSeason(
        start = MonthDay.of(Month.OCTOBER, 31),
        end = MonthDay.of(Month.NOVEMBER, 2),
    )

/** The holiday this date falls in, or null on an ordinary day. */
val LocalDate.holidaySeason: HolidaySeason?
    get() = when {
        isChristmasSeason -> HolidaySeason.CHRISTMAS
        isHalloweenSeason -> HolidaySeason.HALLOWEEN
        else -> null
    }

/**
 * Names the occurrence of [season] this date belongs to, such as `christmas-2026`.
 *
 * The Christmas season runs into January, so its January days are named after the December the
 * season started in. That keeps one season one occurrence: something done "once per Christmas"
 * does not happen again on New Year's Day.
 */
fun LocalDate.holidayOccurrenceKey(season: HolidaySeason): String {
    val startYear = if (season == HolidaySeason.CHRISTMAS && month == Month.JANUARY) year - 1 else year
    return "${season.name.lowercase()}-$startYear"
}

private fun LocalDate.isWithinSeason(start: MonthDay, end: MonthDay): Boolean {
    val today: MonthDay = MonthDay.from(this)
    val seasonWrapsYear = end.isBefore(start)
    return if (seasonWrapsYear) {
        today >= start || today <= end
    } else {
        today in start..end
    }
}
