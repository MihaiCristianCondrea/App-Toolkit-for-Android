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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.states.AboutUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LicensesRoute
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

internal const val ABOUT_SCREEN_NAME = "About"
private const val ABOUT_SCREEN_CLASS = "AboutScreen"

/**
 * The "About" screen: information about the application and the device.
 *
 * This is the stateful half. It owns the [AboutViewModel], collects its state, tracks the screen,
 * shows the ViewModel's messages and navigates, then hands the rendering to [AboutScreenContent].
 *
 * The list includes:
 * - App information: full name, version, and a link to the open-source licenses screen.
 * - Device information: a summary of the device's details, which can be copied to the clipboard.
 *
 * It also features an easter egg: tapping the app version five times triggers a konfetti animation.
 *
 * @param onVersionTap Callback invoked with the cumulative number of taps on the app version item.
 */
@Composable
fun AboutScreen(
    onVersionTap: (Int) -> Unit = {},
) {
    val viewModel: AboutViewModel = koinViewModel()
    val state: AboutUiState by viewModel.state.collectAsStateWithLifecycle()
    val firebaseController: FirebaseController = koinInject()
    val navigator = LocalShellNavigator.current

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = ABOUT_SCREEN_NAME,
        screenClass = ABOUT_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = ABOUT_SCREEN_NAME,
        state = state.items,
    )

    AboutScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onOpenLicenses = { navigator.navigate(LicensesRoute) },
        contentPadding = contentPadding(),
        onVersionTap = onVersionTap,
        firebaseController = firebaseController,
    )

    MessageHost(viewModel = viewModel)
}
