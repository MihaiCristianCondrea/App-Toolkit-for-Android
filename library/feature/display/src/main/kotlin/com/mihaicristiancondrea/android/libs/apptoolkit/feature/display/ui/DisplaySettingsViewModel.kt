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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.contracts.DisplaySettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.DisplaySettings
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.states.DisplaySettingsUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Follows and stores the display preferences the display settings screen shows. A failed read
 * replaces them with a retryable failure; a failed write keeps them on screen and shows an error
 * message. The shell's layout choices are written by the screen itself, not here.
 */
class DisplaySettingsViewModel(
    private val displayPreferences: DisplayPreferencesRepository,
    private val themePreferences: ThemePreferencesRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<DisplaySettingsUiState, DisplaySettingsEvent>(
    initialState = DisplaySettingsUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "DisplaySettings",
    viewModelName = "DisplaySettingsViewModel",
) {
    private var observeJob: Job? = null

    init {
        onEvent(DisplaySettingsEvent.Load)
    }

    override fun handleEvent(event: DisplaySettingsEvent) {
        when (event) {
            DisplaySettingsEvent.Load -> observePreferences()

            is DisplaySettingsEvent.ThemeModeChanged ->
                persist(action = Actions.SET_THEME_MODE) { themePreferences.selectThemeMode(event.mode) }

            is DisplaySettingsEvent.DynamicColorsChanged ->
                persist(action = Actions.SET_DYNAMIC_COLORS) { themePreferences.setDynamicColors(event.enabled) }

            is DisplaySettingsEvent.BouncyButtonsChanged ->
                persist(action = Actions.SET_BOUNCY_BUTTONS) { displayPreferences.setBouncyButtons(event.enabled) }

            is DisplaySettingsEvent.BottomBarLabelsChanged ->
                persist(action = Actions.SET_SHOW_BOTTOM_BAR_LABELS) {
                    displayPreferences.setShowBottomBarLabels(event.show)
                }

            is DisplaySettingsEvent.LanguageChanged ->
                persist(action = Actions.SET_LANGUAGE) { displayPreferences.setLanguage(event.language) }

            is DisplaySettingsEvent.StartupRouteChanged ->
                persist(action = Actions.SET_STARTUP_PAGE) { displayPreferences.setStartupPage(event.route) }
        }
    }

    /** Follows the stored preferences. A retry restarts the collection that failed. */
    private fun observePreferences() {
        observeJob = observeJob.restart {
            setState { copy(settings = Loadable.Loading) }
            storedSettings().collectReport(
                action = Actions.OBSERVE_PREFERENCES,
                onError = { error -> setState { copy(settings = error.toFailed()) } },
            ) { settings ->
                setState { copy(settings = Loadable.Ready(settings)) }
            }
        }
    }

    private fun storedSettings(): Flow<DisplaySettings> = combine(
        themePreferences.themeMode,
        themePreferences.dynamicColors,
        displayPreferences.bouncyButtons,
        displayPreferences.showBottomBarLabels,
        combine(
            displayPreferences.language,
            displayPreferences.startupPage(default = NO_STARTUP_ROUTE),
        ) { language, startupRoute -> language to startupRoute },
    ) { themeMode, dynamicColors, bouncyButtons, showBottomBarLabels, (language, startupRoute) ->
        DisplaySettings(
            themeMode = themeMode,
            dynamicColors = dynamicColors,
            bouncyButtons = bouncyButtons,
            showBottomBarLabels = showBottomBarLabels,
            language = language,
            startupRoute = startupRoute,
        )
    }

    /**
     * Runs one preference write as [action]. Every write completes on its own, since each one is
     * a separate choice; a failure shows an error message and the stored value stays on screen.
     */
    private fun persist(action: String, write: suspend () -> Unit) {
        launchReport(
            action = action,
            onError = { error -> showMessage(error.toErrorMessage()) },
            block = write,
        )
    }

    private object Actions {
        const val OBSERVE_PREFERENCES: String = "observePreferences"
        const val SET_THEME_MODE: String = "setThemeMode"
        const val SET_DYNAMIC_COLORS: String = "setDynamicColors"
        const val SET_BOUNCY_BUTTONS: String = "setBouncyButtons"
        const val SET_SHOW_BOTTOM_BAR_LABELS: String = "setShowBottomBarLabels"
        const val SET_LANGUAGE: String = "setLanguage"
        const val SET_STARTUP_PAGE: String = "setStartupPage"
    }

    private companion object {
        const val NO_STARTUP_ROUTE: String = ""
    }
}
