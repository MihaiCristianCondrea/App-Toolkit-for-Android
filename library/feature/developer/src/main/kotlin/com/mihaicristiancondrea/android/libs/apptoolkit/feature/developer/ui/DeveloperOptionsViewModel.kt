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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.analytics.developerOptionKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.analytics.developerOptionSavedEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.analytics.developerOptionsResetEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.contracts.DeveloperOptionsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperOption
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.states.DeveloperOptionsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.data.repositories.ShellPreferencesRepository
import kotlinx.coroutines.Job
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

internal class DeveloperOptionsViewModel(
    private val preferences: ShellPreferencesRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<DeveloperOptionsUiState, DeveloperOptionsEvent>(
    initialState = DeveloperOptionsUiState(preferences.lastKnown ?: preferences.defaults),
    telemetryRepository = telemetryRepository,
    screenName = "DeveloperOptions",
    viewModelName = "DeveloperOptionsViewModel",
) {
    private var observeJob: Job? = null

    init {
        onEvent(event = DeveloperOptionsEvent.Load)
    }

    override fun handleEvent(event: DeveloperOptionsEvent) {
        when (event) {
            DeveloperOptionsEvent.Load -> observeSettings()
            DeveloperOptionsEvent.Reset -> resetOptions()
            is DeveloperOptionsEvent.SetLayout -> saveOption(DeveloperOption.Layout) { preferences.setLayoutMode(event.value) }
            is DeveloperOptionsEvent.SetTopBarStyle -> saveOption(DeveloperOption.TopBarStyle) { preferences.setTopBarOverride(event.value) }
            is DeveloperOptionsEvent.SetHideTopBarOnScroll -> saveOption(DeveloperOption.HideTopBarOnScroll) { preferences.setHideTopBarOnScroll(event.value) }
            is DeveloperOptionsEvent.SetNavigationBarStyle -> saveOption(DeveloperOption.NavigationBarStyle) { preferences.setNavigationBarStyle(event.value) }
            is DeveloperOptionsEvent.SetHideBottomBarOnScroll -> saveOption(DeveloperOption.HideBottomBarOnScroll) { preferences.setHideBottomBarOnScroll(event.value) }
            is DeveloperOptionsEvent.SetNavigationTint -> saveOption(DeveloperOption.NavigationTint) { preferences.setNavigationTint(event.value) }
            is DeveloperOptionsEvent.SetContentWidth -> saveOption(DeveloperOption.ContentWidth) { preferences.setLimitContentWidth(event.value) }
            is DeveloperOptionsEvent.SetStartOverride -> saveOption(DeveloperOption.StartOverride) { preferences.setStartOverride(event.value) }
            is DeveloperOptionsEvent.SetTabTransition -> saveOption(DeveloperOption.TabTransition) { preferences.setTabTransition(event.value) }
            is DeveloperOptionsEvent.SetBackEdge -> saveOption(DeveloperOption.BackEdge) { preferences.setBackEdgeStyle(event.value) }
            is DeveloperOptionsEvent.SetAnimationSpeed -> saveOption(DeveloperOption.AnimationSpeed) { preferences.setAnimationSpeed(event.value) }
            is DeveloperOptionsEvent.SetEntranceStyle -> saveOption(DeveloperOption.EntranceStyle) { preferences.setEntranceStyle(event.value) }
            is DeveloperOptionsEvent.SetMotionTempo -> saveOption(DeveloperOption.MotionTempo) { preferences.setMotionTempo(event.value) }
            is DeveloperOptionsEvent.SetEntranceReplay -> saveOption(DeveloperOption.EntranceReplay) { preferences.setEntranceReplay(event.value) }
            is DeveloperOptionsEvent.SetBottomAccessory -> saveOption(DeveloperOption.BottomAccessory) { preferences.setAccessoryMode(event.value) }
        }
    }

    private fun observeSettings() {
        observeJob = observeJob.restart {
            preferences.settings.collectReport(
                action = Actions.OBSERVE_SETTINGS,
                onError = { error ->
                    showMessage(
                        error.toErrorMessage().copy(
                            actionLabel = UiText.StringResource(CoreUiR.string.try_again),
                        ),
                    )
                },
            ) { settings -> setState { copy(settings = settings) } }
        }
    }

    private fun saveOption(option: DeveloperOption, write: suspend () -> Unit) {
        launchReport(
            action = Actions.SAVE_OPTION,
            extra = mapOf(SettingsAnalytics.Params.PREFERENCE_KEY to developerOptionKey(option)),
            onError = { error -> showMessage(error.toErrorMessage()) },
        ) {
            write()
            telemetryRepository.logEvent(developerOptionSavedEvent(option))
        }
    }

    private fun resetOptions() {
        launchReport(
            action = Actions.RESET,
            onError = { error -> showMessage(error.toErrorMessage()) },
        ) {
            preferences.resetDeveloperOptions()
            telemetryRepository.logEvent(developerOptionsResetEvent())
        }
    }

    private object Actions {
        const val OBSERVE_SETTINGS = "observeDeveloperSettings"
        const val SAVE_OPTION = "saveDeveloperOption"
        const val RESET = "resetDeveloperOptions"
    }
}
