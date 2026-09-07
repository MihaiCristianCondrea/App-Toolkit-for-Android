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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.CustomSnackbarVisuals
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle

/**
 * Material 3 [SnackbarHost] tailored for [CustomSnackbarVisuals].
 *
 * The host renders a dismiss action with sound and haptic feedback and
 * applies custom colors when the snackbar represents an error.
 *
 * @param snackbarState State that holds the current snackbar data.
 * @param modifier Modifier applied to the [SnackbarHost].
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DefaultSnackbarHost(snackbarState: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(hostState = snackbarState, modifier = modifier) { snackbarData: SnackbarData ->
        (snackbarData.visuals as? CustomSnackbarVisuals)?.let { visuals: CustomSnackbarVisuals ->
            val isError: Boolean = visuals.isError
            Snackbar(
                modifier = Modifier.padding(all = SizeConstants.LargeSize),
                containerColor = if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.inverseSurface,
                contentColor = if (isError) MaterialTheme.colorScheme.error else SnackbarDefaults.contentColor,
                action = {
                    GeneralButton(
                        style = GeneralButtonStyle.Text,
                        onClick = snackbarData::dismiss,
                        icon = ToolkitIcon.Vector(Icons.Outlined.Close),
                        contentDescription = stringResource(android.R.string.cancel),
                        contentColor = if (isError) MaterialTheme.colorScheme.error else SnackbarDefaults.contentColor,
                    )
                }) {
                Text(text = visuals.message)
            }
        }
    }
}