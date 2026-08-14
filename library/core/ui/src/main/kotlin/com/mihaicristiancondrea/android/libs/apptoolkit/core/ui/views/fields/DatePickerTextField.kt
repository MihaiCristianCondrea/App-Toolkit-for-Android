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

import android.view.SoundEffectConstants
import android.view.View
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
import androidx.compose.ui.platform.LocalView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs.DatePickerDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.bounceClick
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerTextField(
    modifier: Modifier = Modifier,
    dateMillis: Long,
    onDateSelected: (Long) -> Unit,
    textFieldIcon: ImageVector = Icons.Default.CalendarToday,
    textFieldReadOnly: Boolean = true,
    textFieldEnabled: Boolean = false,
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    val configuration = LocalConfiguration.current
    val locale = remember(configuration) { configuration.locales[0] }
    val formatter = remember(locale) { SimpleDateFormat("dd.MM.yyyy", locale) }
    val parser = remember(locale) { SimpleDateFormat("yyyy-MM-dd", locale) }
    val formattedDate = remember(dateMillis, formatter) {
        formatter.format(Date(dateMillis))
    }

    var showDialog by rememberSaveable { mutableStateOf(false) }

    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    val latestOnDateSelected by rememberUpdatedState(onDateSelected)

    if (showDialog) {
        DatePickerDialog(
            onDateSelected = { dateString ->
                val parsedMillis = runCatching { parser.parse(dateString)?.time }
                    .getOrNull() ?: dateMillis

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
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                firebaseController.logGa4Event(ga4Event)
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
