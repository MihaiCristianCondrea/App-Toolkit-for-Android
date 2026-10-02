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

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.result.runSuspendCatching
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.WeatherEffect
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.date.isChristmasSeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.updateData
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.contracts.SeasonalThemeOverlayEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.states.SeasonalThemeOverlayUiState
import java.time.LocalDate
import java.time.ZoneId
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
 * @param telemetryRepository Reports how the holiday greeting was answered.
 * @param today Supplies the local date, so tests can pick the season.
 */
class SeasonalThemeOverlayViewModel(
    private val seasonal: SeasonalThemeRepository,
    theme: ThemePreferencesRepository,
    private val telemetryRepository: TelemetryRepository,
    private val today: () -> LocalDate = { LocalDate.now(ZoneId.systemDefault()) },
) : ScreenViewModel<SeasonalThemeOverlayUiState, SeasonalThemeOverlayEvent, ActionEvent>(
    initialState = UiStateScreen(
        screenState = ScreenState.Success(),
        data = SeasonalThemeOverlayUiState(),
    ),
) {
    private var greetingClaimed: Boolean = false

    init {
        viewModelScope.launch {
            val date = today()
            runSuspendCatching { seasonal.restoreThemeAfterHoliday(date) }
                .onFailure { throwable -> report(throwable, operation = "restoreThemeAfterHoliday") }
            val due = runSuspendCatching { seasonal.pendingHolidayGreeting(date) }
                .onFailure { throwable -> report(throwable, operation = "pendingHolidayGreeting") }
                .getOrNull()
            if (due != null && HolidayGreetingPresence.claim()) {
                greetingClaimed = true
                update { it.copy(greeting = due) }
            }
        }

        combine(seasonal.state, theme.preferencesState) { seasonalState, themeState ->
            val unlocked = seasonalState.unlocked
            val christmasOnScreen = !themeState.dynamicColors &&
                themeState.staticPaletteId == StaticPaletteIds.CHRISTMAS
            val holidaySnow = christmasOnScreen && today().isChristmasSeason
            val showSnowfall = when (seasonalState.weatherEffect) {
                WeatherEffect.Automatic -> holidaySnow || (christmasOnScreen && unlocked)
                WeatherEffect.Snow -> unlocked
                // The Christmas theme brings its snow even to someone who picked rain.
                WeatherEffect.Rain -> holidaySnow
                WeatherEffect.Off -> false
            }
            val showRain = seasonalState.weatherEffect == WeatherEffect.Rain && unlocked && !holidaySnow
            Triple(showSnowfall, showRain, themeState.themeMode)
        }.distinctUntilChanged().onEach { (showSnowfall, showRain, themeMode) ->
            update {
                it.copy(showSnowfall = showSnowfall, showRain = showRain, themeMode = themeMode)
            }
        }.launchIn(viewModelScope)
    }

    override fun onEvent(event: SeasonalThemeOverlayEvent) {
        when (event) {
            is SeasonalThemeOverlayEvent.AnswerGreeting -> answerGreeting(event.useHolidayTheme)
        }
    }

    private fun answerGreeting(useHolidayTheme: Boolean) {
        val season: HolidaySeason = screenData?.greeting ?: return
        viewModelScope.launch {
            update { it.copy(greeting = null) }
            telemetryRepository.logEvent(
                holidayGreetingAnsweredEvent(season = season, useHolidayTheme = useHolidayTheme),
            )
            runSuspendCatching {
                seasonal.answerHolidayGreeting(
                    season = season,
                    today = today(),
                    useHolidayTheme = useHolidayTheme,
                )
            }.onFailure { throwable -> report(throwable, operation = "answerHolidayGreeting") }
            releaseGreeting()
        }
    }

    private fun report(throwable: Throwable, operation: String) {
        telemetryRepository.recordNonFatal(
            throwable = throwable,
            attributes = mapOf("operation" to operation),
        )
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

    private suspend fun update(transform: (SeasonalThemeOverlayUiState) -> SeasonalThemeOverlayUiState) {
        updateStateThreadSafe {
            screenState.updateData(newState = ScreenState.Success()) { transform(it) }
        }
    }
}
