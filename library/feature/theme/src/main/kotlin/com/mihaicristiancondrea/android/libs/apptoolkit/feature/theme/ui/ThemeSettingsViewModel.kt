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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.analytics.THEME_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.constants.ThemeOperationNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.contracts.ThemeSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.states.ThemeSettingsUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Owns theme-settings preference observation and mutations.
 *
 * [ThemeSettingsUiState.preferences] stays [Loadable.Loading] until both the stored preferences and
 * the seasonal themes unlock arrive, because the palette rows scroll to the first selection they
 * see and the unlock can add palettes to them. A failed write shows an error message.
 */
class ThemeSettingsViewModel(
    private val preferences: ThemePreferencesRepository,
    private val seasonal: SeasonalThemeRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<ThemeSettingsUiState, ThemeSettingsEvent>(
    initialState = ThemeSettingsUiState(),
    telemetryRepository = telemetryRepository,
    screenName = THEME_SCREEN.name,
    viewModelName = "ThemeSettingsViewModel",
) {
    private var observeJob: Job? = null

    init {
        onEvent(ThemeSettingsEvent.Load)
    }

    override fun handleEvent(event: ThemeSettingsEvent) {
        when (event) {
            is ThemeSettingsEvent.Load -> observePreferences()

            is ThemeSettingsEvent.SelectThemeMode -> persist(setting = ThemeOperationNames.Settings.THEME_MODE) {
                preferences.selectThemeMode(mode = event.mode)
            }

            is ThemeSettingsEvent.SetAmoledMode -> persist(setting = ThemeOperationNames.Settings.AMOLED_MODE) {
                preferences.setAmoledMode(enabled = event.enabled)
            }

            is ThemeSettingsEvent.SetWeatherEffect -> persist(setting = ThemeOperationNames.Settings.WEATHER_EFFECT) {
                seasonal.setWeatherEffect(effect = event.effect)
            }

            is ThemeSettingsEvent.SelectDynamicPalette -> persist(setting = ThemeOperationNames.Settings.DYNAMIC_PALETTE) {
                preferences.selectDynamicPalette(variant = event.variant)
            }

            is ThemeSettingsEvent.SelectStaticPalette -> persist(setting = ThemeOperationNames.Settings.STATIC_PALETTE) {
                preferences.selectStaticPalette(id = event.id)
            }
        }
    }

    private fun observePreferences() {
        setState {
            if (preferences is Loadable.Ready) {
                this
            } else {
                copy(preferences = Loadable.Loading)
            }
        }
        observeJob = observeJob.restart {
            combine(
                preferences.preferencesState,
                seasonal.state.map { it.unlocked to it.weatherEffect }.distinctUntilChanged(),
            ) { preferencesState, (unlocked, weatherEffect) ->
                ThemeSettingsUiState(
                    preferences = Loadable.Ready(preferencesState),
                    seasonalThemesUnlocked = unlocked,
                    weatherEffect = weatherEffect,
                )
            }.collectReport(
                action = ThemeOperationNames.OBSERVE_PREFERENCES,
                onError = { error -> setState { copy(preferences = error.toFailed()) } },
            ) { loaded ->
                setState { loaded }
            }
        }
    }

    /**
     * Saves one theme setting as the [ThemeOperationNames.PERSIST_THEME_SETTING] operation. Every
     * write runs to completion, so a quick second tap never cancels the first.
     */
    private fun persist(setting: String, write: suspend () -> Unit) {
        launchReport(
            action = ThemeOperationNames.PERSIST_THEME_SETTING,
            extra = mapOf(ThemeOperationNames.ExtraKeys.SETTING to setting),
            onError = { error -> showMessage(error.toErrorMessage()) },
            block = write,
        )
    }
}
