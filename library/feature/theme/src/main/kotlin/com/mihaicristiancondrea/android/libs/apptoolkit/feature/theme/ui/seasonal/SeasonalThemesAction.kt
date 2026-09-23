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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.views.SeasonalThemesDialog
import org.koin.compose.viewmodel.koinViewModel

/**
 * Top app bar action of the theme screen that opens the seasonal themes controls.
 *
 * It renders nothing until the About screen easter egg has been found: the controls are the
 * reward, so a person who never tapped the version five times sees the theme screen as before.
 */
@Composable
fun SeasonalThemesAction() {
    val viewModel: SeasonalThemesViewModel = koinViewModel()
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()
    val state = screenState.data ?: return
    if (!state.seasonal.unlocked) return

    var showDialog: Boolean by rememberSaveable { mutableStateOf(false) }

    AnimatedIconButtonDirection(
        fromRight = true,
        icon = ToolkitIcon.Vector(Icons.Outlined.Celebration),
        contentDescription = stringResource(id = R.string.seasonal_themes_title),
        onClick = { showDialog = true },
    )

    if (showDialog) {
        SeasonalThemesDialog(
            state = state,
            onEvent = viewModel::onEvent,
            onDismiss = { showDialog = false },
        )
    }
}
