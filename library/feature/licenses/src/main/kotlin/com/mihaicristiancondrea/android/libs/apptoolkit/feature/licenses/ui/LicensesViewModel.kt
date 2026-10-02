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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.contracts.LicensesEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.states.LicensesUiState

/**
 * Owns the licenses screen state.
 *
 * The bundled library metadata is parsed by a Compose producer, so the screen reports when that
 * finishes, and this ViewModel turns it into the state the screen tracks and the `loadLibraries`
 * report. Until then the count is loading.
 */
class LicensesViewModel(
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<LicensesUiState, LicensesEvent>(
    initialState = LicensesUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "Licenses",
    viewModelName = "LicensesViewModel",
) {

    override fun handleEvent(event: LicensesEvent) {
        when (event) {
            is LicensesEvent.LibrariesLoaded -> onLibrariesLoaded(libraryCount = event.libraryCount)
        }
    }

    private fun onLibrariesLoaded(libraryCount: Int) {
        startOperation(
            action = Actions.LOAD_LIBRARIES,
            extra = mapOf(ExtraKeys.LIBRARY_COUNT to libraryCount.toString()),
        )
        setState { copy(libraryCount = Loadable.Ready(libraryCount)) }
    }

    private object Actions {
        const val LOAD_LIBRARIES: String = "loadLibraries"
    }

    private object ExtraKeys {
        const val LIBRARY_COUNT: String = "libraryCount"
    }
}
