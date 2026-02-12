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

package com.d4rk.android.libs.apptoolkit.core.ui.views.dialogs

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick

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
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
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
        Button(modifier = Modifier.bounceClick(), onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
            onConfirm()
        }, enabled = confirmEnabled) {
            Text(text = confirmButtonText ?: stringResource(id = android.R.string.ok))
        }
    }, dismissButton = {
        if (showDismissButton) {
            OutlinedButton(modifier = Modifier.bounceClick(), onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                onCancel()
            }, enabled = dismissEnabled) {
                Text(text = dismissButtonText ?: stringResource(id = android.R.string.cancel))
            }
        }
    })
}