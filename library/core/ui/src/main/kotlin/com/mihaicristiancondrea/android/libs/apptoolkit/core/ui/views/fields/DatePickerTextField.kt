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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs.DatePickerDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import java.text.DateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerTextField(
    modifier: Modifier = Modifier,
    dateMillis: Long,
    onDateSelected: (Long) -> Unit,
    textFieldIcon: ImageVector = Icons.Default.CalendarToday,
    textFieldReadOnly: Boolean = true,
    textFieldEnabled: Boolean = false,
    ga4Event: Ga4EventData? = null,
) {
    val telemetry = LocalTelemetry.current
    val configuration = LocalConfiguration.current
    val locale = remember(configuration) { resolveDisplayLocale(configuration = configuration) }
    val formatter = remember(locale) { DateFormat.getDateInstance(DateFormat.MEDIUM, locale) }
    val formattedDate = remember(dateMillis, formatter) {
        formatter.format(Date(dateMillis))
    }

    var showDialog by rememberSaveable { mutableStateOf(false) }

    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val latestOnDateSelected by rememberUpdatedState(newValue = onDateSelected)

    if (showDialog) {
        DatePickerDialog(
            onDateSelected = { dateString ->
                val parsedMillis = parseIsoDateToLocalMillis(dateString = dateString) ?: dateMillis

                latestOnDateSelected(parsedMillis)
                showDialog = false
            },
            onDismiss = { showDialog = false },
        )
    }

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .bounceClick()
            .clip(MaterialTheme.shapes.large)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
            ) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                telemetry.logGa4Event(ga4Event)
                showDialog = true
            }
    ) {
        OutlinedTextField(
            value = formattedDate,
            onValueChange = {},
            readOnly = textFieldReadOnly,
            enabled = textFieldEnabled,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            trailingIcon = {
                Icon(imageVector = textFieldIcon, contentDescription = null)
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )
    }
}

internal fun resolveDisplayLocale(configuration: Configuration): Locale =
    configuration.locales.takeUnless { it.isEmpty }?.get(0) ?: Locale.getDefault()

internal fun parseIsoDateToLocalMillis(
    dateString: String,
    zone: ZoneId = ZoneId.systemDefault(),
): Long? = runCatching {
    LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
        .atStartOfDay(zone)
        .toInstant()
        .toEpochMilli()
}.getOrNull()
