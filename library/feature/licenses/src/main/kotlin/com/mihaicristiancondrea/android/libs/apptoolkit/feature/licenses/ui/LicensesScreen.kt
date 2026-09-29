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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.contracts.LicensesEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.states.LicensesUiState
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.variant.LibraryBadges
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val LICENSES_SCREEN_NAME = "Licenses"
private const val LICENSES_SCREEN_CLASS = "LicensesScreen"

/**
 * Lists the open-source libraries bundled with the host application.
 *
 * The body of the licenses page, which `licensesPage()` registers; the page frame draws the app bar.
 */
@Composable
fun LicensesScreen() {
    val firebaseController: FirebaseController = koinInject()
    val viewModel: LicensesViewModel = koinViewModel()
    val screenState: UiStateScreen<LicensesUiState> by
    viewModel.uiState.collectAsStateWithLifecycle()

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = LICENSES_SCREEN_NAME,
        screenClass = LICENSES_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = LICENSES_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    val libraries: Libs? by produceLibraries(resId = R.raw.aboutlibraries)

    LaunchedEffect(libraries) {
        libraries?.let { loaded ->
            viewModel.onEvent(
                event = LicensesEvent.LibrariesLoaded(libraryCount = loaded.libraries.size),
            )
        }
    }

    LibrariesContainer(
        libraries = libraries,
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding(),
        badges = LibraryBadges(
            description = true,
            funding = true,
        ),
    )
}
