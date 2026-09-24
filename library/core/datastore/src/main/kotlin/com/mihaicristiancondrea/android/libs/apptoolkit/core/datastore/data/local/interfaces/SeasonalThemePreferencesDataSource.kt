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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces

import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.models.HolidayThemeSnapshot
import kotlinx.coroutines.flow.Flow

/** Persisted seasonal theme state: the easter egg unlock and holiday bookkeeping. */
interface SeasonalThemePreferencesDataSource {

    /** Emits whether the About screen easter egg has been found. */
    val seasonalThemesUnlocked: Flow<Boolean>

    /** Emits the occurrence key of the last holiday greeting answered, or null if none was. */
    val lastHolidayGreeting: Flow<String?>

    /** Emits the appearance saved before a holiday theme was applied, or null if none is in use. */
    val holidayThemeSnapshot: Flow<HolidayThemeSnapshot?>

    /** Persists the easter egg unlock. */
    suspend fun saveSeasonalThemesUnlocked(unlocked: Boolean)

    /** Persists the occurrence key of the holiday greeting just answered. */
    suspend fun saveLastHolidayGreeting(occurrenceKey: String)

    /** Persists the appearance to restore after the holiday, or clears it when [snapshot] is null. */
    suspend fun saveHolidayThemeSnapshot(snapshot: HolidayThemeSnapshot?)
}
