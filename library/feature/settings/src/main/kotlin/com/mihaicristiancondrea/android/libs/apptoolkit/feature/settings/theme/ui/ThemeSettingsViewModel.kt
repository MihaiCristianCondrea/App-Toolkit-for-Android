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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.theme.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.theme.ui.contracts.ThemeSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
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

/** Owns theme-settings preference observation and mutations. */
class ThemeSettingsViewModel(
    private val preferences: ThemePreferencesRepository,
) : ScreenViewModel<ThemePreferencesState, ThemeSettingsEvent, ActionEvent>(
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
        observationJob = preferences.preferencesState.onEach { state ->
            updateStateThreadSafe {
                screenState.updateData(newState = ScreenState.Success()) { state }
            }
        }.launchIn(viewModelScope)
    }

    private fun selectThemeMode(mode: String) = persist {
        preferences.selectThemeMode(mode)
    }

    private fun persist(block: suspend () -> Unit) {
        viewModelScope.launch { runCatching { block() } }
    }
}
