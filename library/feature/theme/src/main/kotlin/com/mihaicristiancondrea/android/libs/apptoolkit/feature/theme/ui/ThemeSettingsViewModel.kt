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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.contracts.ThemeSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.states.ThemeSettingsUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Owns theme-settings preference observation and mutations.
 *
 * The state holds no data until both the stored preferences and the seasonal themes unlock arrive.
 * Starting from made-up defaults drew the page with the wrong palette selected for a frame, and
 * anything keyed to the first selection, such as scrolling the palette row to it, acted on that
 * wrong one. The unlock can add palettes to the row, so it is part of the same first state.
 */
class ThemeSettingsViewModel(
    private val preferences: ThemePreferencesRepository,
    private val seasonal: SeasonalThemeRepository,
) : ScreenViewModel<ThemeSettingsUiState, ThemeSettingsEvent, ActionEvent>(
    initialState = UiStateScreen(screenState = ScreenState.IsLoading(), data = null),
) {
    private var observationJob: Job? = null

    init {
        onEvent(ThemeSettingsEvent.Initialize)
    }

    override fun onEvent(event: ThemeSettingsEvent) {
        when (event) {
            ThemeSettingsEvent.Initialize -> observePreferences()
            is ThemeSettingsEvent.SelectThemeMode -> selectThemeMode(event.mode)
            is ThemeSettingsEvent.SetAmoledMode -> persist {
                preferences.setAmoledMode(event.enabled)
            }
            is ThemeSettingsEvent.SelectDynamicPalette -> persist {
                preferences.selectDynamicPalette(event.variant)
            }
            is ThemeSettingsEvent.SelectStaticPalette -> persist {
                preferences.selectStaticPalette(event.id)
            }
        }
    }

    private fun observePreferences() {
        observationJob?.cancel()
        observationJob = combine(
            preferences.preferencesState,
            seasonal.state.map { it.unlocked }.distinctUntilChanged(),
        ) { preferencesState, unlocked ->
            ThemeSettingsUiState(preferences = preferencesState, seasonalThemesUnlocked = unlocked)
        }.onEach { state ->
            updateStateThreadSafe { screenState.setSuccess(data = state) }
        }.launchIn(viewModelScope)
    }

    private fun selectThemeMode(mode: String) = persist {
        preferences.selectThemeMode(mode)
    }

    private fun persist(block: suspend () -> Unit) {
        viewModelScope.launch { runCatching { block() } }
    }
}
