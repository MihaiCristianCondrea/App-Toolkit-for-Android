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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason

/** GA4 vocabulary for the seasonal themes: the holiday greeting. */
internal object SeasonalThemeAnalytics {

    object Events {
        /** How the person answered the holiday greeting's offer of the holiday theme. */
        const val HOLIDAY_GREETING_ANSWERED: String = "holiday_greeting_answered"
    }

    object Params {
        const val SEASON: String = "season"
        const val CHOICE: String = "choice"
    }

    object Choices {
        const val USE_HOLIDAY_THEME: String = "use_holiday_theme"
        const val KEEP_CURRENT_THEME: String = "keep_current_theme"
    }
}

/** `holiday_greeting_answered` for [season], with whether the holiday theme was taken. */
internal fun holidayGreetingAnsweredEvent(
    season: HolidaySeason,
    useHolidayTheme: Boolean,
): AnalyticsEvent = AnalyticsEvent(
    name = SeasonalThemeAnalytics.Events.HOLIDAY_GREETING_ANSWERED,
    params = mapOf(
        SeasonalThemeAnalytics.Params.SEASON to AnalyticsValue.Str(season.name.lowercase()),
        SeasonalThemeAnalytics.Params.CHOICE to AnalyticsValue.Str(
            if (useHolidayTheme) {
                SeasonalThemeAnalytics.Choices.USE_HOLIDAY_THEME
            } else {
                SeasonalThemeAnalytics.Choices.KEEP_CURRENT_THEME
            },
        ),
    ),
)

