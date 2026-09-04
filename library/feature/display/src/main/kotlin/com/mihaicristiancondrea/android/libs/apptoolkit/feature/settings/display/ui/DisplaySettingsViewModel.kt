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

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.contracts.DisplaySettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.states.DisplaySettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.updateData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/** Owns display-preference observation and persistence for [DisplaySettingsList]. */
class DisplaySettingsViewModel(
    private val displayPreferences: DisplayPreferencesRepository,
    private val themePreferences: ThemePreferencesRepository,
) : ScreenViewModel<DisplaySettingsUiState, DisplaySettingsEvent, ActionEvent>(
    initialState = UiStateScreen(
        screenState = ScreenState.Success(),
        data = DisplaySettingsUiState(),
    ),
) {
    private var observationJob: Job? = null

    init {
        onEvent(DisplaySettingsEvent.Initialize)
    }

    private fun observePreferences() {
        observationJob?.cancel()
        observationJob = combine(
            themePreferences.themeMode,
            themePreferences.dynamicColors,
            displayPreferences.bouncyButtons,
            displayPreferences.showBottomBarLabels,
            displayPreferences.language,
        ) { themeMode, dynamicColors, bouncyButtons, showBottomBarLabels, language ->
            DisplaySettingsUiState(
                themeMode = themeMode,
                dynamicColors = dynamicColors,
                bouncyButtons = bouncyButtons,
                showBottomBarLabels = showBottomBarLabels,
                language = language,
            )
        }.onEach { state ->
            updateStateThreadSafe {
                screenState.updateData(newState = ScreenState.Success()) { state }
            }
        }.launchIn(viewModelScope)
    }

    fun startupRoute(defaultRoute: String): Flow<String> =
        displayPreferences.startupPage(default = defaultRoute)

    override fun onEvent(event: DisplaySettingsEvent) {
        when (event) {
            DisplaySettingsEvent.Initialize -> observePreferences()
            is DisplaySettingsEvent.ThemeModeChanged ->
                persist { themePreferences.selectThemeMode(event.mode) }
            is DisplaySettingsEvent.DynamicColorsChanged ->
                persist { themePreferences.setDynamicColors(event.enabled) }
            is DisplaySettingsEvent.BouncyButtonsChanged ->
                persist { displayPreferences.setBouncyButtons(event.enabled) }
            is DisplaySettingsEvent.BottomBarLabelsChanged ->
                persist { displayPreferences.setShowBottomBarLabels(event.show) }
            is DisplaySettingsEvent.LanguageChanged ->
                persist { displayPreferences.setLanguage(event.language) }
            is DisplaySettingsEvent.StartupRouteChanged ->
                persist { displayPreferences.setStartupPage(event.route) }
        }
    }

    private fun persist(block: suspend () -> Unit) {
        viewModelScope.launch { runCatching { block() } }
    }
}
