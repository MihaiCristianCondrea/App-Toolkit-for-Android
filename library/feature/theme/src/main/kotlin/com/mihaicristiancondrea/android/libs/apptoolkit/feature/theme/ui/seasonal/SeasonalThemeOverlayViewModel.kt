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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.WeatherEffect
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.date.isChristmasSeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.contracts.SeasonalThemeOverlayEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.states.SeasonalThemeOverlayUiState
import java.time.LocalDate
import java.time.ZoneId
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Decides what the app-wide seasonal overlay shows in one activity: snow or rain, and the holiday
 * greeting.
 *
 * On creation it first takes off a holiday theme whose holiday has ended, then asks whether a
 * greeting is due. Doing both before anything is shown means the first screen after a holiday is
 * already back in the person's own colors, and a greeting is never offered for a holiday that just
 * finished.
 *
 * With the [WeatherEffect.Automatic] weather effect, the default, snow falls only while the
 * Christmas palette is actually on screen. Outside the Christmas season it also needs the easter
 * egg: a person without it who kept the Christmas palette gets the colors, not snow in July.
 * [WeatherEffect.Snow] and [WeatherEffect.Rain] fall over every palette, and [WeatherEffect.Off]
 * lets nothing fall. They are picked from the theme settings' app bar, which only the easter egg
 * opens, so snow and rain on any palette need it too. Rain gives way to snow while the Christmas
 * palette is worn during the Christmas season, and comes back once the season is over.
 *
 * Failures are reported and never shown: the overlay has nothing to show them on, and each one
 * only means a greeting or a theme change waits for the next activity.
 *
 * @param telemetryRepository Reports how the holiday greeting was answered, and every failure.
 * @param today Supplies the local date, so tests can pick the season.
 */
class SeasonalThemeOverlayViewModel(
    private val seasonal: SeasonalThemeRepository,
    private val theme: ThemePreferencesRepository,
    telemetryRepository: TelemetryRepository,
    private val today: () -> LocalDate = { LocalDate.now(ZoneId.systemDefault()) },
) : LoggedScreenViewModel<SeasonalThemeOverlayUiState, SeasonalThemeOverlayEvent>(
    initialState = SeasonalThemeOverlayUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "SeasonalThemeOverlay",
    viewModelName = "SeasonalThemeOverlayViewModel",
) {
    private var greetingClaimed: Boolean = false

    init {
        restoreThemeAfterHoliday(date = today())
        observeWeather()
    }

    override fun handleEvent(event: SeasonalThemeOverlayEvent) {
        when (event) {
            is SeasonalThemeOverlayEvent.AnswerGreeting -> answerGreeting(event.useHolidayTheme)
        }
    }

    /** Looks up the greeting once the restore is done, and also after it failed. */
    private fun restoreThemeAfterHoliday(date: LocalDate) {
        launchReport(
            action = Actions.RESTORE_THEME_AFTER_HOLIDAY,
            onError = { lookUpGreeting(date = date) },
        ) {
            seasonal.restoreThemeAfterHoliday(date)
            lookUpGreeting(date = date)
        }
    }

    /** Shows a due greeting unless another activity's overlay already shows one. */
    private fun lookUpGreeting(date: LocalDate) {
        launchReport(action = Actions.PENDING_HOLIDAY_GREETING) {
            val due: HolidaySeason? = seasonal.pendingHolidayGreeting(date)
            if (due != null && HolidayGreetingPresence.claim()) {
                greetingClaimed = true
                setState { copy(greeting = due) }
            }
        }
    }

    /** The Christmas theme brings its snow in season even to someone who picked rain. */
    private fun observeWeather() {
        combine(seasonal.state, theme.preferencesState) { seasonalState, themeState ->
            val unlocked = seasonalState.unlocked
            val christmasOnScreen = !themeState.dynamicColors &&
                themeState.staticPaletteId == StaticPaletteIds.CHRISTMAS
            val holidaySnow = christmasOnScreen && today().isChristmasSeason
            val showSnowfall = when (seasonalState.weatherEffect) {
                WeatherEffect.Automatic -> holidaySnow || (christmasOnScreen && unlocked)
                WeatherEffect.Snow -> unlocked
                WeatherEffect.Rain -> holidaySnow
                WeatherEffect.Off -> false
            }
            val showRain = seasonalState.weatherEffect == WeatherEffect.Rain && unlocked && !holidaySnow
            Triple(showSnowfall, showRain, themeState.themeMode)
        }.distinctUntilChanged()
            .collectReport(action = Actions.OBSERVE_WEATHER) { (showSnowfall, showRain, themeMode) ->
                setState { copy(showSnowfall = showSnowfall, showRain = showRain, themeMode = themeMode) }
            }
    }

    /** Frees the greeting slot once the answer is saved, or failed to save. */
    private fun answerGreeting(useHolidayTheme: Boolean) {
        val season: HolidaySeason = currentState.greeting ?: return
        setState { copy(greeting = null) }
        telemetryRepository.logEvent(
            holidayGreetingAnsweredEvent(season = season, useHolidayTheme = useHolidayTheme),
        )
        launchReport(
            action = Actions.ANSWER_HOLIDAY_GREETING,
            onError = { releaseGreeting() },
        ) {
            seasonal.answerHolidayGreeting(
                season = season,
                today = today(),
                useHolidayTheme = useHolidayTheme,
            )
            releaseGreeting()
        }
    }

    override fun onCleared() {
        releaseGreeting()
        super.onCleared()
    }

    private fun releaseGreeting() {
        if (greetingClaimed) {
            greetingClaimed = false
            HolidayGreetingPresence.release()
        }
    }

    private object Actions {
        const val RESTORE_THEME_AFTER_HOLIDAY: String = "restoreThemeAfterHoliday"
        const val PENDING_HOLIDAY_GREETING: String = "pendingHolidayGreeting"
        const val OBSERVE_WEATHER: String = "observeWeather"
        const val ANSWER_HOLIDAY_GREETING: String = "answerHolidayGreeting"
    }
}
