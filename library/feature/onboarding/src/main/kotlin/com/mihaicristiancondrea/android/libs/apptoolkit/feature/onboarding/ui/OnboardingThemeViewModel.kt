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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts.OnboardingThemeEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states.OnboardingThemeUiState

/**
 * ViewModel for the onboarding theme page. It reads and writes the same
 * [ThemePreferencesRepository] as the theme settings page, so a choice made here is the app's theme
 * from then on.
 *
 * A failed write shows an error message. A failed read is reported and the page keeps what it
 * showed last.
 */
class OnboardingThemeViewModel(
    private val preferences: ThemePreferencesRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<OnboardingThemeUiState, OnboardingThemeEvent>(
    initialState = OnboardingThemeUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "Onboarding",
    viewModelName = "OnboardingThemeViewModel",
) {

    init {
        observePreferences()
    }

    override fun handleEvent(event: OnboardingThemeEvent) {
        when (event) {
            is OnboardingThemeEvent.SelectThemeMode -> persist(setting = Settings.THEME_MODE) {
                preferences.selectThemeMode(event.mode)
            }

            is OnboardingThemeEvent.SetAmoledMode -> persist(setting = Settings.AMOLED_MODE) {
                preferences.setAmoledMode(event.enabled)
            }

            is OnboardingThemeEvent.SelectDynamicPalette -> persist(setting = Settings.DYNAMIC_PALETTE) {
                preferences.selectDynamicPalette(event.variant)
            }

            is OnboardingThemeEvent.SelectStaticPalette -> persist(setting = Settings.STATIC_PALETTE) {
                preferences.selectStaticPalette(event.id)
            }
        }
    }

    private fun observePreferences() {
        preferences.preferencesState.collectReport(action = Actions.OBSERVE_PREFERENCES) { stored ->
            setState { copy(preferences = stored) }
        }
    }

    /**
     * Saves one theme setting as the [Actions.PERSIST_ONBOARDING_THEME] operation. Every write runs
     * to completion, so a quick second tap never cancels the first.
     */
    private fun persist(setting: String, write: suspend () -> Unit) {
        launchReport(
            action = Actions.PERSIST_ONBOARDING_THEME,
            extra = mapOf(ExtraKeys.SETTING to setting),
            onError = { error -> showMessage(error.toErrorMessage()) },
            block = write,
        )
    }

    private object Actions {
        const val OBSERVE_PREFERENCES: String = "observePreferences"
        const val PERSIST_ONBOARDING_THEME: String = "persistOnboardingTheme"
    }

    private object ExtraKeys {
        const val SETTING: String = "setting"
    }

    private object Settings {
        const val THEME_MODE: String = "theme_mode"
        const val AMOLED_MODE: String = "amoled_mode"
        const val DYNAMIC_PALETTE: String = "dynamic_palette"
        const val STATIC_PALETTE: String = "static_palette"
    }
}
