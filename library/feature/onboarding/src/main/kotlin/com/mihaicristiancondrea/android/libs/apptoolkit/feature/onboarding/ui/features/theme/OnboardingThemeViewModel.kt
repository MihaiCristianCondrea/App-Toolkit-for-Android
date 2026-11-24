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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.features.theme

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.analytics.ONBOARDING_SCREEN_NAME
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.analytics.OnboardingThemeSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.analytics.onboardingThemeCustomizedEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.constants.OnboardingOperationNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.features.theme.contracts.OnboardingThemeEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.features.theme.states.OnboardingThemeUiState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * ViewModel for the onboarding theme page. It reads and writes the same
 * [ThemePreferencesRepository] as the theme settings page, so a choice made here is the app's theme
 * from then on.
 *
 * A failed write shows an error message and does not report `theme_customized`. A failed read is
 * reported and the page keeps what it showed last.
 */
class OnboardingThemeViewModel(
    private val preferences: ThemePreferencesRepository,
    private val seasonal: SeasonalThemeRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<OnboardingThemeUiState, OnboardingThemeEvent>(
    initialState = OnboardingThemeUiState(),
    telemetryRepository = telemetryRepository,
    screenName = ONBOARDING_SCREEN_NAME,
    viewModelName = "OnboardingThemeViewModel",
) {

    init {
        observePreferences()
    }

    override fun handleEvent(event: OnboardingThemeEvent) {
        when (event) {
            is OnboardingThemeEvent.SelectThemeMode -> persist(
                setting = OnboardingThemeSettings.THEME_MODE,
            ) {
                preferences.selectThemeMode(mode = event.mode)
            }

            is OnboardingThemeEvent.SetAmoledMode -> persist(
                setting = OnboardingThemeSettings.AMOLED_MODE,
            ) {
                preferences.setAmoledMode(enabled = event.enabled)
            }

            is OnboardingThemeEvent.SelectDynamicPalette -> persist(
                setting = OnboardingThemeSettings.DYNAMIC_PALETTE,
            ) {
                preferences.selectDynamicPalette(variant = event.variant)
            }

            is OnboardingThemeEvent.SelectStaticPalette -> persist(
                setting = OnboardingThemeSettings.STATIC_PALETTE,
            ) {
                preferences.selectStaticPalette(id = event.id)
            }
        }
    }

    /** Follows the stored theme and whether the holiday palettes are offered all year. */
    private fun observePreferences() {
        combine(
            flow = preferences.preferencesState,
            flow2 = seasonal.state.map { state -> state.unlocked }.distinctUntilChanged(),
        ) { stored, unlocked -> stored to unlocked }
            .collectReport(action = OnboardingOperationNames.OBSERVE_PREFERENCES) { (stored, unlocked) ->
                setState { copy(preferences = stored, seasonalThemesUnlocked = unlocked) }
            }
    }

    /**
     * Saves one theme setting as the [OnboardingOperationNames.PERSIST_ONBOARDING_THEME] operation
     * and logs `theme_customized` only after the write completes. Every write runs to completion,
     * so a quick second tap never cancels the first.
     */
    private fun persist(setting: String, write: suspend () -> Unit) {
        launchReport(
            action = OnboardingOperationNames.PERSIST_ONBOARDING_THEME,
            extra = mapOf(OnboardingOperationNames.ExtraKeys.SETTING to setting),
            onError = { error -> showMessage(message = error.toErrorMessage()) },
        ) {
            write()
            telemetryRepository.logEvent(event = onboardingThemeCustomizedEvent(setting = setting))
        }
    }
}
