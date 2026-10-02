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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.contracts.LicensesEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.states.LicensesUiState
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.variant.LibraryBadges
import org.koin.compose.viewmodel.koinViewModel

private const val LICENSES_SCREEN_NAME = "Licenses"
private const val LICENSES_SCREEN_CLASS = "LicensesScreen"

/**
 * Lists the open-source libraries bundled with the host application.
 *
 * The body of the licenses page, which `licensesPage()` registers; the page frame draws the app bar.
 * This is the stateful half. It parses the bundled metadata with the library's own producer, tells
 * the [LicensesViewModel] when that is done, tracks the screen, and hands the list to
 * [LicensesScreenContent].
 */
@Composable
fun LicensesScreen() {
    val viewModel: LicensesViewModel = koinViewModel()
    val state: LicensesUiState by viewModel.state.collectAsStateWithLifecycle()
    val libraries: Libs? by produceLibraries(resId = R.raw.aboutlibraries)

    TrackScreenView(
        screenName = LICENSES_SCREEN_NAME,
        screenClass = LICENSES_SCREEN_CLASS,
    )

    TrackScreenState(
        screenName = LICENSES_SCREEN_NAME,
        state = state.libraryCount,
    )

    LaunchedEffect(libraries) {
        libraries?.let { loaded ->
            viewModel.onEvent(LicensesEvent.LibrariesLoaded(libraryCount = loaded.libraries.size))
        }
    }

    LicensesScreenContent(
        libraries = libraries,
        contentPadding = contentPadding(),
    )
}

/**
 * Draws [libraries], the parsed metadata of the bundled open-source libraries.
 *
 * The stateless half of [LicensesScreen]. While [libraries] is `null` the container shows its own
 * progress indicator, so there is no separate loading state to draw.
 */
@Composable
internal fun LicensesScreenContent(
    libraries: Libs?,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LibrariesContainer(
        libraries = libraries,
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        badges = LibraryBadges(
            description = true,
            funding = true,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun LicensesScreenContentLoadingPreview() {
    MaterialTheme {
        LicensesScreenContent(libraries = null)
    }
}
