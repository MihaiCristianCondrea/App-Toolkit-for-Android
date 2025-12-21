package com.d4rk.android.libs.apptoolkit.core.ui.components.fields

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.ui.components.dialogs.DatePickerDialog
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerTextField(
    dateMillis: Long,
    onDateSelected: (Long) -> Unit
) {
    val formatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val showDialog = rememberSaveable { mutableStateOf(false) }

    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    if (showDialog.value) {
        DatePickerDialog(
            onDateSelected = { dateString: String ->
                val parsedMillis: Long = runCatching {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .parse(dateString)
                        ?.time
                }.getOrNull() ?: dateMillis

                onDateSelected(parsedMillis)
                showDialog.value = false
            },
            onDismiss = { showDialog.value = false }
        )
    }

    OutlinedTextField(
        value = formatter.format(Date(dateMillis)),
        onValueChange = {},
        readOnly = true,
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick()
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                showDialog.value = true
            },
        trailingIcon = {
            Icon(Icons.Default.CalendarToday, contentDescription = null)
        },
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    )
}