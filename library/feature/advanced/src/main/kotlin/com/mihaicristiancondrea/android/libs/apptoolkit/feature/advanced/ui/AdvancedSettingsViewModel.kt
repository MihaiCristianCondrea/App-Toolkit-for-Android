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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.data.repositories.CacheRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.contracts.AdvancedSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.states.AdvancedSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.states.CacheClearStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * ViewModel for the advanced settings page: clearing the cache, and whether the developer options
 * are offered.
 *
 * [CacheRepository] moves its own file work off the main thread, so this needs no dispatcher. A
 * clear confirms with a message, and a failed one reports with an error message while the rows stay.
 *
 * @param developerOptionsUnlocked Whether the About screen's version easter egg has been found,
 * which offers the developer options here.
 */
class AdvancedSettingsViewModel(
    private val repository: CacheRepository,
    telemetryRepository: TelemetryRepository,
    developerOptionsUnlocked: Flow<Boolean> = flowOf(false),
) : LoggedScreenViewModel<AdvancedSettingsUiState, AdvancedSettingsEvent>(
    initialState = AdvancedSettingsUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "AdvancedSettings",
    viewModelName = "AdvancedSettingsViewModel",
) {
    private var clearJob: Job? = null

    init {
        observeDeveloperOptions(unlocked = developerOptionsUnlocked)
    }

    override fun handleEvent(event: AdvancedSettingsEvent) {
        when (event) {
            AdvancedSettingsEvent.ClearCache -> clearCache()
        }
    }

    /** A failure leaves the row hidden; the unlock is read again the next time the page opens. */
    private fun observeDeveloperOptions(unlocked: Flow<Boolean>) {
        unlocked.collectReport(action = Actions.OBSERVE_DEVELOPER_OPTIONS) { isUnlocked ->
            setState { copy(developerOptionsUnlocked = isUnlocked) }
        }
    }

    /** A second tap restarts the clear rather than queueing another, so only one result shows. */
    private fun clearCache() {
        clearJob = clearJob.restart {
            launchReport(
                action = Actions.CLEAR_CACHE,
                onError = { error ->
                    setState { copy(cacheClear = CacheClearStatus.Failed) }
                    showMessage(error.toErrorMessage(fallback = ClearFailedText))
                },
            ) {
                setState { copy(cacheClear = CacheClearStatus.Clearing) }
                repository.clearCache()
                setState { copy(cacheClear = CacheClearStatus.Idle) }
                showMessage(UiMessage(ClearedText))
            }
        }
    }

    private object Actions {
        const val CLEAR_CACHE: String = "clearCache"
        const val OBSERVE_DEVELOPER_OPTIONS: String = "observeDeveloperOptions"
    }

    private companion object {
        val ClearedText = UiTextHelper.StringResource(R.string.cache_cleared_success)

        /** Shown for a failure with no text of its own; see `toUiText` for the ones that have one. */
        val ClearFailedText = UiTextHelper.StringResource(R.string.cache_cleared_error)
    }
}
