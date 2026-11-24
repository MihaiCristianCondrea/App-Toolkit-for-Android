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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.analytics.PRIVACY_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.constants.PrivacyOperationNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts.PrivacyEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.mappers.toPrivacyItems
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.providers.PrivacySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.states.PrivacyUiState
import kotlinx.coroutines.Job

/**
 * ViewModel for the privacy page: the rows built from [PrivacySettingsProvider].
 *
 * The provider only returns URLs, so this needs no dispatcher. A provider that throws is
 * [Loadable.Failed], reported to telemetry, with a retry. Opening a row's link or page needs a
 * `Context` or the navigator, so the screen does it.
 */
class PrivacyViewModel(
    private val provider: PrivacySettingsProvider,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<PrivacyUiState, PrivacyEvent>(
    initialState = PrivacyUiState(),
    telemetryRepository = telemetryRepository,
    screenName = PRIVACY_SCREEN.name,
    viewModelName = "PrivacyViewModel",
) {
    private var loadJob: Job? = null

    init {
        onEvent(event = PrivacyEvent.Load)
    }

    override fun handleEvent(event: PrivacyEvent) {
        when (event) {
            is PrivacyEvent.Load -> loadItems()
            is PrivacyEvent.ItemClicked -> startOperation(action = PrivacyOperationNames.OPEN_PRIVACY_ITEM)
        }
    }

    /** Builds the privacy rows from the host provider. Duplicate requests share an active load. */
    private fun loadItems() {
        loadJob = loadJob.ifIsActive {
            launchReport(
                action = PrivacyOperationNames.LOAD_PRIVACY_ITEMS,
                onError = { error -> setState { copy(items = error.toFailed()) } },
            ) {
                setState { copy(items = Loadable.Loading) }
                val items = provider.toPrivacyItems()
                setState { copy(items = Loadable.Ready(value = items)) }
            }
        }
    }
}
