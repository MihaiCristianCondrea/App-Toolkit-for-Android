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
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(onDateSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val selectedDatePickerState: DatePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    DatePickerDialog(onDismissRequest = {
        onDismiss()
    }, confirmButton = {
        Button(modifier = Modifier.bounceClick(), onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
            onDismiss()
            selectedDatePickerState.selectedDateMillis?.let {
                onDateSelected(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it)))
            }
        }) {
            Text(text = stringResource(id = android.R.string.ok))
        }
    }, dismissButton = {
        OutlinedButton(modifier = Modifier.bounceClick(), onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
            onDismiss()
        }) {
            Text(text = stringResource(id = android.R.string.cancel))
        }
    }) {
        DatePicker(state = selectedDatePickerState)
    }
}