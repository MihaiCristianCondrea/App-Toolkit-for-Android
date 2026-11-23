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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models

/** The value of one [AnalyticsEvent] parameter, kept typed so a backend can report it as such. */
sealed interface AnalyticsValue {
    /** A text value. */
    data class Str(val value: String) : AnalyticsValue

    /** A whole number. */
    data class LongVal(val value: Long) : AnalyticsValue

    /** A decimal number. */
    data class DoubleVal(val value: Double) : AnalyticsValue

    /** A true or false value. */
    data class Bool(val value: Boolean) : AnalyticsValue
}
