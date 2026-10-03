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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
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
    screenName = "Theme",
    viewModelName = "ThemeSettingsViewModel",
) {
    private var observeJob: Job? = null

    init {
        onEvent(ThemeSettingsEvent.Load)
    }

    override fun handleEvent(event: ThemeSettingsEvent) {
        when (event) {
            ThemeSettingsEvent.Load -> observePreferences()

            is ThemeSettingsEvent.SelectThemeMode -> persist(setting = Settings.THEME_MODE) {
                preferences.selectThemeMode(event.mode)
            }

            is ThemeSettingsEvent.SetAmoledMode -> persist(setting = Settings.AMOLED_MODE) {
                preferences.setAmoledMode(event.enabled)
            }

            is ThemeSettingsEvent.SetWeatherEffect -> persist(setting = Settings.WEATHER_EFFECT) {
                seasonal.setWeatherEffect(event.effect)
            }

            is ThemeSettingsEvent.SelectDynamicPalette -> persist(setting = Settings.DYNAMIC_PALETTE) {
                preferences.selectDynamicPalette(event.variant)
            }

            is ThemeSettingsEvent.SelectStaticPalette -> persist(setting = Settings.STATIC_PALETTE) {
                preferences.selectStaticPalette(event.id)
            }
        }
    }

    private fun observePreferences() {
        setState { copy(preferences = Loadable.Loading) }
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
                action = Actions.OBSERVE_PREFERENCES,
                onError = { error -> setState { copy(preferences = error.toFailed()) } },
            ) { loaded ->
                setState { loaded }
            }
        }
    }

    /**
     * Saves one theme setting as the [Actions.PERSIST_THEME_SETTING] operation. Every write runs to
     * completion, so a quick second tap never cancels the first.
     */
    private fun persist(setting: String, write: suspend () -> Unit) {
        launchReport(
            action = Actions.PERSIST_THEME_SETTING,
            extra = mapOf(ExtraKeys.SETTING to setting),
            onError = { error -> showMessage(error.toErrorMessage()) },
            block = write,
        )
    }

    private object Actions {
        const val OBSERVE_PREFERENCES: String = "observePreferences"
        const val PERSIST_THEME_SETTING: String = "persistThemeSetting"
    }

    private object ExtraKeys {
        const val SETTING: String = "setting"
    }

    private object Settings {
        const val THEME_MODE: String = "theme_mode"
        const val AMOLED_MODE: String = "amoled_mode"
        const val WEATHER_EFFECT: String = "weather_effect"
        const val DYNAMIC_PALETTE: String = "dynamic_palette"
        const val STATIC_PALETTE: String = "static_palette"
    }
}
