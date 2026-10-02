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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.contracts.PermissionsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.states.PermissionsUiState
import org.koin.compose.viewmodel.koinViewModel

private const val PERMISSIONS_SCREEN_NAME = "Permissions"
private const val PERMISSIONS_SCREEN_CLASS = "PermissionsScreen"

/**
 * The app's permissions and why it asks for each: the body of the permissions page, which
 * `permissionsPage()` registers, and what Android's permission manager opens for this app.
 *
 * This is the stateful half. It owns the [PermissionsViewModel] and tracks the screen, then hands
 * the rendering to [PermissionsScreenContent].
 */
@Composable
fun PermissionsScreen() {
    val viewModel: PermissionsViewModel = koinViewModel()
    val state: PermissionsUiState by viewModel.state.collectAsStateWithLifecycle()

    TrackScreenView(
        screenName = PERMISSIONS_SCREEN_NAME,
        screenClass = PERMISSIONS_SCREEN_CLASS,
    )
    TrackScreenState(
        screenName = PERMISSIONS_SCREEN_NAME,
        state = state.config,
    )

    LaunchedEffect(Unit) {
        viewModel.onEvent(PermissionsEvent.Load)
    }

    PermissionsScreenContent(
        state = state,
        contentPadding = contentPadding(),
        onRetry = { viewModel.onEvent(PermissionsEvent.Load) },
    )
}
