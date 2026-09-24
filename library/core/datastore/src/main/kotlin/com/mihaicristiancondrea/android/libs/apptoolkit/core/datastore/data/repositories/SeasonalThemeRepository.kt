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
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

/**
 * The way to read and change the seasonal themes: the holiday greeting, the holiday palette that
 * comes with it, and the easter egg unlock.
 *
 * It is separate from [ThemePreferencesRepository] because it owns rules that span both stores:
 * applying a holiday palette records the appearance it replaces, and the end of the holiday puts
 * that appearance back. Keeping both halves here means no screen can apply one without the other.
 */
interface SeasonalThemeRepository {

    /** The stored seasonal state, with defaults filled in for anything never set. */
    val state: Flow<SeasonalThemeState>

    /**
     * Records that the About screen easter egg was found. Unlocking is permanent.
     *
     * @return true only for the call that unlocked them, so the unlock can be announced once.
     */
    suspend fun unlockSeasonalThemes(): Boolean

    /**
     * The holiday whose greeting should be shown on [today], or null.
     *
     * Each holiday is greeted once per occurrence: answering the greeting for Christmas 2026 keeps it
     * from coming back on another day of the same season, but Christmas 2027 greets again.
     */
    suspend fun pendingHolidayGreeting(today: LocalDate): HolidaySeason?

    /**
     * Records the answer to the [season] greeting shown on [today].
     *
     * With [useHolidayTheme] the holiday palette is applied and the appearance it replaces is kept,
     * so [restoreThemeAfterHoliday] can put it back. Nothing is saved when the holiday palette is
     * already the one in use, since there would be nothing to go back to.
     */
    suspend fun answerHolidayGreeting(
        season: HolidaySeason,
        today: LocalDate,
        useHolidayTheme: Boolean,
    )

    /**
     * Puts back the appearance from before a holiday theme once that holiday is over on [today].
     *
     * The holiday palette is only swapped out if it is still the one in use. A person who picked
     * something else during the holiday made a newer choice, and that choice is kept.
     */
    suspend fun restoreThemeAfterHoliday(today: LocalDate)
}
