package com.d4rk.android.libs.apptoolkit.core.ui.views.fields

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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.d4rk.android.libs.apptoolkit.core.ui.views.dialogs.DatePickerDialog
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val formatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val parser = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

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
            value = formatter.format(Date(dateMillis)),
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
