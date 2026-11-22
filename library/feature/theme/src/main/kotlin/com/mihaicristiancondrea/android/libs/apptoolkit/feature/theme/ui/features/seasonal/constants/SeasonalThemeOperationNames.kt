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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.features.seasonal.constants

/** Operation names reported to telemetry by `SeasonalThemeOverlayViewModel`. */
internal object SeasonalThemeOperationNames {
    const val RESTORE_THEME_AFTER_HOLIDAY: String = "restoreThemeAfterHoliday"
    const val PENDING_HOLIDAY_GREETING: String = "pendingHolidayGreeting"
    const val OBSERVE_WEATHER: String = "observeWeather"
    const val ANSWER_HOLIDAY_GREETING: String = "answerHolidayGreeting"
}
