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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle

/**
 * Full-screen dialog shell with a top app bar, close action, confirm action, and scrollable body.
 *
 * State hoisting contract:
 * - The caller owns visibility state and must close the dialog from [onDismiss]/[onConfirm].
 * - [content] should receive already-prepared UI state and avoid business logic.
 *
 * Dismissal contract:
 * - Back press, outside click, and close button dispatch [onDismiss].
 * - Confirm action dispatches [onConfirm] when [confirmEnabled] is true.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BasicFullScreenDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmEnabled: Boolean = true,
    confirmButtonText: String = stringResource(id = android.R.string.ok),
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true
        )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(), topBar = {
                CenterAlignedTopAppBar(navigationIcon = {
                    GeneralButton(
                        style = GeneralButtonStyle.Text,
                        onClick = onDismiss,
                        icon = ToolkitIcon.Vector(imageVector = Icons.Filled.Close),
                        contentDescription = title
                    )
                }, title = { Text(text = title) }, actions = {
                    GeneralButton(
                        style = GeneralButtonStyle.Text,
                        onClick = onConfirm,
                        enabled = confirmEnabled,
                        label = confirmButtonText,
                    )
                })
            }) { innerPadding: PaddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding)
                    .padding(
                        horizontal = SizeConstants.LargeSize,
                        vertical = SizeConstants.SmallSize
                    )
                    .verticalScroll(state = rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(space = SizeConstants.MediumSize)
            ) {
                content()
            }
        }
    }
}
