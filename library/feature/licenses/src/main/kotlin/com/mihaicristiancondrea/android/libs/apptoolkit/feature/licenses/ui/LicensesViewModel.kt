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

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.contracts.LicensesAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.contracts.LicensesEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.states.LicensesUiState
import kotlinx.coroutines.launch

/**
 * Owns the licenses screen state.
 *
 * The bundled library metadata is parsed by a Compose producer, so the screen reports when that
 * finishes and this state holder turns it into the loading and success states the rest of the
 * toolkit renders and tracks.
 */
class LicensesViewModel(
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<LicensesUiState, LicensesEvent, LicensesAction>(
    initialState = UiStateScreen(data = LicensesUiState()),
    firebaseController = firebaseController,
    screenName = "Licenses",
) {

    init {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.setLoading()
            }
        }
    }

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
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.setSuccess(data = LicensesUiState(libraryCount = libraryCount))
            }
        }
    }

    private object Actions {
        const val LOAD_LIBRARIES: String = "loadLibraries"
    }

    private object ExtraKeys {
        const val LIBRARY_COUNT: String = "libraryCount"
    }
}
