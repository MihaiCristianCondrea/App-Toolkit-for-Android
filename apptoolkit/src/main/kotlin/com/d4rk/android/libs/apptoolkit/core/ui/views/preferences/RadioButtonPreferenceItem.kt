package com.d4rk.android.libs.apptoolkit.core.ui.views.preferences

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * A composable function that creates a radio button preference item.
 *
 * This item displays a text label and a radio button. Clicking on the item toggles the radio button's state.
 *
 * @param text The text to display next to the radio button.
 * @param isChecked Whether the radio button is currently checked.
 * @param onCheckedChange A callback that is invoked when the radio button's state changes.
 *                        It provides the new checked state as a Boolean parameter.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4Event Optional GA4 event data to log on click.
 */
@Composable
fun RadioButtonPreferenceItem(
    modifier: Modifier = Modifier,
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .let { base ->
                if (enabled) {
                    base
                        .clickable {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                            firebaseController.logGa4Event(ga4Event)
                            onCheckedChange(!isChecked)
                        }
                } else base
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.bounceClick(),
            selected = isChecked, enabled = enabled, onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                firebaseController.logGa4Event(ga4Event)
                onCheckedChange(!isChecked)
            })
        Text(
            text = text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier
                .weight(weight = 1f)
                .padding(end = SizeConstants.LargeSize, start = SizeConstants.LargeSize)
        )
    }
}
