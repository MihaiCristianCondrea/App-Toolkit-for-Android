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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val formatter : SimpleDateFormat = remember { SimpleDateFormat("dd.MM.yyyy" , Locale.getDefault()) }
    var showDialog : Boolean by remember { mutableStateOf(value = false) }
    val hapticFeedback : HapticFeedback = LocalHapticFeedback.current
    val view : View = LocalView.current
    if (showDialog) {
        DatePickerDialog(onDateSelected = { dateString : String ->
            val parsedDate: Date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(dateString)
                ?: Date(dateMillis)
            onDateSelected(parsedDate.time)
            showDialog = false
        }, onDismiss = { showDialog = false })
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
                hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                showDialog = true
            },
        trailingIcon = {
        Icon(imageVector = Icons.Default.CalendarToday , contentDescription = null)
    } , colors = OutlinedTextFieldDefaults.colors(
        disabledTextColor = MaterialTheme.colorScheme.onSurface , disabledBorderColor = MaterialTheme.colorScheme.outline , disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant , disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
    ))
}