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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle

/**
 * Alert dialog with caller-owned visibility. Back and outside taps call [onDismiss]; the
 * secondary button calls [onCancel], which defaults to [onDismiss]. Action callbacks must
 * update the caller's visibility state.
 */
@Composable
fun BasicAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit = onDismiss,
    icon: ImageVector? = null,
    iconTint: Color? = null,
    title: String? = null,
    content: @Composable () -> Unit = {},
    confirmButtonText: String? = null,
    dismissButtonText: String? = null,
    confirmEnabled: Boolean = true,
    dismissEnabled: Boolean = true,
    showDismissButton: Boolean = true
) {
    AlertDialog(onDismissRequest = onDismiss, icon = {
        icon?.let { vector ->
            Icon(
                imageVector = vector,
                contentDescription = null,
                tint = iconTint ?: LocalContentColor.current
            )
        }
    }, title = {
        if (!title.isNullOrEmpty()) {
            Text(text = title)
        }
    }, text = {
        AnimatedContent(
            targetState = content,
            transitionSpec = {
                expandVertically() togetherWith shrinkVertically() using SizeTransform(
                    clip = false
                )
            },
            label = "DialogContentAnimation"
        ) { targetContent ->
            targetContent()
        }
    }, confirmButton = {
        GeneralButton(
            onClick = onConfirm,
            enabled = confirmEnabled,
            label = confirmButtonText ?: stringResource(id = android.R.string.ok),
        )
    }, dismissButton = {
        if (showDismissButton) {
            GeneralButton(
                style = GeneralButtonStyle.Outlined,
                onClick = onCancel,
                enabled = dismissEnabled,
                label = dismissButtonText ?: stringResource(id = android.R.string.cancel),
            )
        }
    })
}
