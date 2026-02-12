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

package com.d4rk.android.libs.apptoolkit.core.utils.extensions.date

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

private fun LocalDate.isWithinSeason(start: MonthDay, end: MonthDay): Boolean {
    val today: MonthDay = MonthDay.from(this)
    val seasonWrapsYear = end.isBefore(start)
    return if (seasonWrapsYear) {
        today >= start || today <= end
    } else {
        today in start..end
    }
}
