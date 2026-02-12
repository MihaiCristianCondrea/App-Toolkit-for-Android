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

package com.d4rk.android.apps.apptoolkit.app.settings.about.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.d4rk.android.apps.apptoolkit.app.components.ui.ComponentsUnlockViewModel
import com.d4rk.android.apps.apptoolkit.app.components.ui.contract.ComponentsUnlockEvent
import com.d4rk.android.libs.apptoolkit.app.about.ui.AboutScreen
import org.koin.compose.viewmodel.koinViewModel

/**
 * App-specific About screen content that wires the components showcase unlock behavior.
 */
@Composable
fun AppAboutSettingsContent(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
) {
    val unlockViewModel: ComponentsUnlockViewModel = koinViewModel()

    AboutScreen(
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        onVersionTap = { tapCount ->
            unlockViewModel.onEvent(ComponentsUnlockEvent.VersionTapped(tapCount))
        },
    )
}
