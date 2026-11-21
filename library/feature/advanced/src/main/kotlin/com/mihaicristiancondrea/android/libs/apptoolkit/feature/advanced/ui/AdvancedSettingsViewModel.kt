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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.isRetryable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.data.repositories.CacheRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.analytics.ADVANCED_SETTINGS_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.constants.AdvancedSettingsOperationNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.contracts.AdvancedSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.states.AdvancedSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.states.CacheClearStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

/**
 * Owns cache-clear status and developer options visibility.
 *
 * @param developerOptionsUnlocked Emits the current developer options unlock state.
 */
class AdvancedSettingsViewModel(
    private val repository: CacheRepository,
    telemetryRepository: TelemetryRepository,
    private val developerOptionsUnlocked: Flow<Boolean> = flowOf(value = false),
) : LoggedScreenViewModel<AdvancedSettingsUiState, AdvancedSettingsEvent>(
    initialState = AdvancedSettingsUiState(),
    telemetryRepository = telemetryRepository,
    screenName = ADVANCED_SETTINGS_SCREEN.name,
    viewModelName = "AdvancedSettingsViewModel",
) {
    private var clearJob: Job? = null
    private var observeJob: Job? = null

    init {
        onEvent(event = AdvancedSettingsEvent.ObserveDeveloperOptions)
    }

    override fun handleEvent(event: AdvancedSettingsEvent) {
        when (event) {
            is AdvancedSettingsEvent.ObserveDeveloperOptions -> observeDeveloperOptions()
            is AdvancedSettingsEvent.ClearCache -> clearCache()
        }
    }

    /** Observes unlock state and allows retry after the observation has failed. */
    private fun observeDeveloperOptions() {
        observeJob = observeJob.ifIsActive {
            developerOptionsUnlocked.collectReport(
                action = AdvancedSettingsOperationNames.OBSERVE_DEVELOPER_OPTIONS,
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
            ) { isUnlocked ->
                setState { copy(developerOptionsUnlocked = isUnlocked) }
            }
        }
    }

    /** Runs one cache clear at a time and reports completion through a snackbar. */
    private fun clearCache() {
        clearJob = clearJob.ifIsActive {
            launchReport(
                action = AdvancedSettingsOperationNames.CLEAR_CACHE,
                onError = { error ->
                    setState { copy(cacheClear = CacheClearStatus.Failed) }
                    showMessage(
                        message = error.toErrorMessage(
                            fallback = UiText.StringResource(resourceId = R.string.cache_cleared_error),
                        ),
                    )
                },
            ) {
                setState { copy(cacheClear = CacheClearStatus.Clearing) }
                repository.clearCache()
                setState { copy(cacheClear = CacheClearStatus.Idle) }
                showMessage(
                    message = UiMessage(
                        text = UiText.StringResource(resourceId = R.string.cache_cleared_success),
                    ),
                )
            }
        }
    }
}
