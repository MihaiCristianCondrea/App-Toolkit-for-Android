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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason
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
 * Decides what the app-wide seasonal overlay shows in one activity: snow, and the holiday greeting.
 *
 * On creation it first takes off a holiday theme whose holiday has ended, then asks whether a
 * greeting is due. Doing both before anything is shown means the first screen after a holiday is
 * already back in the person's own colors, and a greeting is never offered for a holiday that just
 * finished.
 *
 * Snow falls only while the Christmas palette is actually on screen and snowfall is on. Outside the
 * Christmas season it also needs the easter egg: a person without it who kept the Christmas palette
 * gets the colors, not snow in July.
 *
 * @param firebaseController Reports how the holiday greeting was answered.
 * @param today Supplies the local date, so tests can pick the season.
 */
class SeasonalThemeOverlayViewModel(
    private val seasonal: SeasonalThemeRepository,
    theme: ThemePreferencesRepository,
    private val firebaseController: FirebaseController,
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
            runCatching { seasonal.restoreThemeAfterHoliday(date) }
            val due = runCatching { seasonal.pendingHolidayGreeting(date) }.getOrNull()
            if (due != null && HolidayGreetingPresence.claim()) {
                greetingClaimed = true
                update { it.copy(greeting = due) }
            }
        }

        combine(seasonal.state, theme.preferencesState) { seasonalState, themeState ->
            val showSnowfall = !themeState.dynamicColors &&
                themeState.staticPaletteId == StaticPaletteIds.CHRISTMAS &&
                (seasonalState.unlocked || today().isChristmasSeason)
            showSnowfall to themeState.themeMode
        }.distinctUntilChanged().onEach { (showSnowfall, themeMode) ->
            update { it.copy(showSnowfall = showSnowfall, themeMode = themeMode) }
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
            firebaseController.logEvent(
                holidayGreetingAnsweredEvent(season = season, useHolidayTheme = useHolidayTheme),
            )
            runCatching {
                seasonal.answerHolidayGreeting(
                    season = season,
                    today = today(),
                    useHolidayTheme = useHolidayTheme,
                )
            }
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

    private suspend fun update(transform: (SeasonalThemeOverlayUiState) -> SeasonalThemeOverlayUiState) {
        updateStateThreadSafe {
            screenState.updateData(newState = ScreenState.Success()) { transform(it) }
        }
    }
}
