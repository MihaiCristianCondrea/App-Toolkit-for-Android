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

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Returns the selected date as `yyyy-MM-dd`. The caller owns visibility, validation, and
 * storage. Cancel calls [onDismiss]; confirm calls [onDismiss] before [onDateSelected], and
 * emits no date if none is selected.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(onDateSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val selectedDatePickerState: DatePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())

    DatePickerDialog(onDismissRequest = {
        onDismiss()
    }, confirmButton = {
        GeneralButton(onClick = {
            onDismiss()
            selectedDatePickerState.selectedDateMillis?.let {
                onDateSelected(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it)))
            }
        }, label = stringResource(id = android.R.string.ok))
    }, dismissButton = {
        GeneralButton(
            style = GeneralButtonStyle.Outlined,
            onClick = onDismiss,
            label = stringResource(id = android.R.string.cancel),
        )
    }) {
        DatePicker(state = selectedDatePickerState)
    }
}
