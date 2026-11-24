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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.isRetryable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.analytics.DEVELOPER_OPTIONS_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.analytics.developerOptionKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.analytics.developerOptionSavedEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.analytics.developerOptionsResetEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.constants.DeveloperOptionsOperationNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.contracts.DeveloperOptionsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperOption
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.states.DeveloperOptionsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.data.repositories.ShellPreferencesRepository
import kotlinx.coroutines.Job
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

/**
 * Observes and updates the host shell's developer overrides.
 *
 * Starts from [ShellPreferencesRepository.lastKnown] or [ShellPreferencesRepository.defaults] so
 * the screen renders immediately. A failed read retains the last settings and offers a retry
 * action when the failure is retryable. Successful saves and resets report settings analytics only
 * after persistence completes.
 */
class DeveloperOptionsViewModel(
    private val preferences: ShellPreferencesRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<DeveloperOptionsUiState, DeveloperOptionsEvent>(
    initialState = DeveloperOptionsUiState(settings = preferences.lastKnown ?: preferences.defaults),
    telemetryRepository = telemetryRepository,
    screenName = DEVELOPER_OPTIONS_SCREEN.name,
    viewModelName = "DeveloperOptionsViewModel",
) {
    private var observeJob: Job? = null

    init {
        onEvent(event = DeveloperOptionsEvent.Load)
    }

    override fun handleEvent(event: DeveloperOptionsEvent) {
        when (event) {
            is DeveloperOptionsEvent.Load -> observeSettings()
            is DeveloperOptionsEvent.Reset -> resetOptions()
            is DeveloperOptionsEvent.SetLayout -> saveOption(option = DeveloperOption.Layout) { preferences.setLayoutMode(value = event.value) }
            is DeveloperOptionsEvent.SetTopBarStyle -> saveOption(option = DeveloperOption.TopBarStyle) { preferences.setTopBarOverride(value = event.value) }
            is DeveloperOptionsEvent.SetHideTopBarOnScroll -> saveOption(option = DeveloperOption.HideTopBarOnScroll) { preferences.setHideTopBarOnScroll(value = event.value) }
            is DeveloperOptionsEvent.SetNavigationBarStyle -> saveOption(option = DeveloperOption.NavigationBarStyle) { preferences.setNavigationBarStyle(value = event.value) }
            is DeveloperOptionsEvent.SetHideBottomBarOnScroll -> saveOption(option = DeveloperOption.HideBottomBarOnScroll) { preferences.setHideBottomBarOnScroll(value = event.value) }
            is DeveloperOptionsEvent.SetNavigationTint -> saveOption(option = DeveloperOption.NavigationTint) { preferences.setNavigationTint(value = event.value) }
            is DeveloperOptionsEvent.SetContentWidth -> saveOption(option = DeveloperOption.ContentWidth) { preferences.setLimitContentWidth(value = event.value) }
            is DeveloperOptionsEvent.SetStartOverride -> saveOption(option = DeveloperOption.StartOverride) { preferences.setStartOverride(value = event.value) }
            is DeveloperOptionsEvent.SetTabTransition -> saveOption(option = DeveloperOption.TabTransition) { preferences.setTabTransition(value = event.value) }
            is DeveloperOptionsEvent.SetBackEdge -> saveOption(option = DeveloperOption.BackEdge) { preferences.setBackEdgeStyle(value = event.value) }
            is DeveloperOptionsEvent.SetAnimationSpeed -> saveOption(option = DeveloperOption.AnimationSpeed) { preferences.setAnimationSpeed(value = event.value) }
            is DeveloperOptionsEvent.SetEntranceStyle -> saveOption(option = DeveloperOption.EntranceStyle) { preferences.setEntranceStyle(value = event.value) }
            is DeveloperOptionsEvent.SetMotionTempo -> saveOption(option = DeveloperOption.MotionTempo) { preferences.setMotionTempo(value = event.value) }
            is DeveloperOptionsEvent.SetEntranceReplay -> saveOption(option = DeveloperOption.EntranceReplay) { preferences.setEntranceReplay(value = event.value) }
            is DeveloperOptionsEvent.SetBottomAccessory -> saveOption(option = DeveloperOption.BottomAccessory) { preferences.setAccessoryMode(value = event.value) }
        }
    }

    /** Replaces the active settings collector and surfaces read failures as snackbar messages. */
    private fun observeSettings() {
        observeJob = observeJob.restart {
            preferences.settings.collectReport(
                action = DeveloperOptionsOperationNames.OBSERVE_SETTINGS,
                onError = { error ->
                    val message = error.toErrorMessage()
                    showMessage(
                        message = if (error.isRetryable) {
                            message.copy(actionLabel = UiText.StringResource(resourceId = CoreUiR.string.try_again))
                        } else {
                            message
                        },
                    )
                },
            ) { settings -> setState { copy(settings = settings) } }
        }
    }

    private fun saveOption(option: DeveloperOption, write: suspend () -> Unit) {
        launchReport(
            action = DeveloperOptionsOperationNames.SAVE_OPTION,
            extra = mapOf(SettingsAnalytics.Params.PREFERENCE_KEY to developerOptionKey(option = option)),
            onError = { error -> showMessage(message = error.toErrorMessage()) },
        ) {
            write()
            telemetryRepository.logEvent(event = developerOptionSavedEvent(option = option))
        }
    }

    private fun resetOptions() {
        launchReport(
            action = DeveloperOptionsOperationNames.RESET,
            onError = { error -> showMessage(message = error.toErrorMessage()) },
        ) {
            preferences.resetDeveloperOptions()
            telemetryRepository.logEvent(event = developerOptionsResetEvent())
        }
    }
}
