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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.analytics.SETTINGS_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.constants.SettingsOperationNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.contracts.SettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.providers.SettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.states.SettingsUiState
import kotlinx.coroutines.Job

/**
 * Loads the host's settings for the settings list.
 *
 * The [SettingsProvider] builds its config from resources, which is main-safe, so it needs no
 * dispatcher. A config with no categories or no preferences is [Loadable.Empty]; a provider that
 * throws is [Loadable.Failed], reported to telemetry. Reloading when already [Loadable.Ready]
 * refreshes rows in place without flashing [Loadable.Loading].
 */
class SettingsViewModel(
    private val settingsProvider: SettingsProvider,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<SettingsUiState, SettingsEvent>(
    initialState = SettingsUiState(),
    telemetryRepository = telemetryRepository,
    screenName = SETTINGS_SCREEN.name,
    viewModelName = "SettingsViewModel",
) {
    private var loadJob: Job? = null

    override fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.Load -> loadSettings()
        }
    }

    /** Loads or refreshes the settings config from [settingsProvider]. */
    private fun loadSettings() {
        loadJob = loadJob.restart {
            launchReport(
                action = SettingsOperationNames.LOAD_SETTINGS,
                onError = { error -> setState { copy(config = error.toFailed()) } },
            ) {
                if (state.value.config !is Loadable.Ready) {
                    setState { copy(config = Loadable.Loading) }
                }
                val loaded = settingsProvider.provideSettingsConfig()
                val content = if (loaded.categories.none { it.preferences.isNotEmpty() }) {
                    Loadable.Empty(
                        message = UiText.StringResource(resourceId = R.string.error_no_settings_found),
                    )
                } else {
                    Loadable.Ready(value = loaded)
                }
                setState { copy(config = content) }
            }
        }
    }
}
