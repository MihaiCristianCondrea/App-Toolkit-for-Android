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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.contracts.SettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.providers.SettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.states.SettingsUiState
import kotlinx.coroutines.Job

/**
 * Loads the host's settings for the settings list.
 *
 * The [SettingsProvider] builds its config from resources, which is main-safe, so it needs no
 * dispatcher. A config with no category is [Loadable.Empty]; a provider that throws is
 * [Loadable.Failed], reported to telemetry.
 */
class SettingsViewModel(
    private val settingsProvider: SettingsProvider,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<SettingsUiState, SettingsEvent>(
    initialState = SettingsUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "Settings",
    viewModelName = "SettingsViewModel",
) {
    private var loadJob: Job? = null

    override fun handleEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.Load -> loadSettings()
        }
    }

    private fun loadSettings() {
        loadJob = loadJob.restart {
            launchReport(
                action = Actions.LOAD_SETTINGS,
                onError = { error -> setState { copy(config = error.toFailed()) } },
            ) {
                setState { copy(config = Loadable.Loading) }
                val loaded = settingsProvider.provideSettingsConfig()
                val content = if (loaded.categories.isEmpty()) {
                    Loadable.Empty(NoSettingsText)
                } else {
                    Loadable.Ready(loaded)
                }
                setState { copy(config = content) }
            }
        }
    }

    private object Actions {
        const val LOAD_SETTINGS: String = "loadSettings"
    }

    private companion object {
        val NoSettingsText = UiTextHelper.StringResource(R.string.error_no_settings_found)
    }
}
