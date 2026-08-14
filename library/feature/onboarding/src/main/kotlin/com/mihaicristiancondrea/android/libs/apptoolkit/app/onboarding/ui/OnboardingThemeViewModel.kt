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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui.contracts.OnboardingThemeEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.extensions.themePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.updateData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/** Owns theme preference state used by the onboarding theme page. */
class OnboardingThemeViewModel(
    private val preferences: ThemePreferencesDataSource,
) : ScreenViewModel<ThemePreferencesState, OnboardingThemeEvent, ActionEvent>(
    initialState = UiStateScreen(
        screenState = ScreenState.Success(),
        data = ThemePreferencesState(
            themeMode = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
            dynamicColors = true,
            amoledMode = false,
            dynamicPaletteVariant = 0,
            staticPaletteId = StaticPaletteIds.DEFAULT,
        ),
    ),
) {
    private var observationJob: Job? = null

    init {
        onEvent(OnboardingThemeEvent.Initialize)
    }

    override fun handleEvent(event: OnboardingThemeEvent) {
        when (event) {
            OnboardingThemeEvent.Initialize -> observePreferences()
            is OnboardingThemeEvent.SelectThemeMode -> selectThemeMode(event.mode)
            is OnboardingThemeEvent.SetAmoledMode -> persist {
                preferences.saveAmoledMode(event.enabled)
            }
            is OnboardingThemeEvent.SelectDynamicPalette -> persist {
                preferences.saveDynamicColors(true)
                preferences.saveDynamicPaletteVariant(event.variant)
            }
            is OnboardingThemeEvent.SelectStaticPalette -> persist {
                preferences.saveDynamicColors(false)
                preferences.saveStaticPaletteId(event.id)
            }
        }
    }

    private fun observePreferences() {
        observationJob?.cancel()
        observationJob = preferences.themePreferencesState().onEach { state ->
            updateStateThreadSafe {
                screenState.updateData(newState = ScreenState.Success()) { state }
            }
        }.launchIn(viewModelScope)
    }

    private fun selectThemeMode(mode: String) = persist {
        preferences.saveThemeMode(mode)
        if (mode == DataStoreNamesConstants.THEME_MODE_LIGHT && currentState.data?.amoledMode == true) {
            preferences.saveAmoledMode(false)
        }
    }

    private fun persist(block: suspend () -> Unit) {
        viewModelScope.launch { runCatching { block() } }
    }
}
