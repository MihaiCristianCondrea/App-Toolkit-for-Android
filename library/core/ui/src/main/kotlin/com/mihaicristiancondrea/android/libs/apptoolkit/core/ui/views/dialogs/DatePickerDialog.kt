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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Returns the selected date as `yyyy-MM-dd`, the calendar day the person picked. The caller owns visibility, validation, and
 * storage. Cancel calls [onDismiss]; confirm calls [onDismiss] before [onDateSelected], and
 * emits no date if none is selected.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(onDateSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val selectedDatePickerState: DatePickerState =
        rememberDatePickerState(initialSelectedDateMillis = todayAsPickerMillis())

    DatePickerDialog(onDismissRequest = {
        onDismiss()
    }, confirmButton = {
        GeneralButton(onClick = {
            onDismiss()
            selectedDatePickerState.selectedDateMillis?.let {
                onDateSelected(pickerMillisToIsoDate(it))
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

/**
 * Formats [pickerMillis] as `yyyy-MM-dd`. Material's date picker reports the picked day as
 * midnight UTC, so it is read in UTC: any other zone would move the date by a day.
 */
internal fun pickerMillisToIsoDate(pickerMillis: Long): String =
    Instant.ofEpochMilli(pickerMillis).atZone(ZoneOffset.UTC).toLocalDate()
        .format(DateTimeFormatter.ISO_LOCAL_DATE.withLocale(Locale.ROOT))

/**
 * Today in [zone] as the midnight UTC value the Material date picker uses for a selected day, so
 * the picker opens on the person's current calendar day whatever their offset from UTC.
 */
internal fun todayAsPickerMillis(zone: ZoneId = ZoneId.systemDefault()): Long =
    LocalDate.now(zone).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
