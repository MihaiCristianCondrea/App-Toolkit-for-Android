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

package com.d4rk.android.libs.apptoolkit.core.ui.views.preferences

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.d4rk.android.libs.apptoolkit.core.ui.views.switches.CustomSwitch
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * Creates a clickable card with a title and a switch for app preference screens.
 *
 * This composable function displays a card with a title and a switch. The entire card is clickable, and clicking it toggles the switch and invokes the `onSwitchToggled` callback.
 * The switch visually indicates its 'on' state by displaying a check icon within the thumb.
 *
 * @param title The text displayed as the card's title.
 * @param switchState A [State] object holding the current on/off state of the switch. Use `true` for the 'on' state and `false` for the 'off' state.
 * @param onSwitchToggled A callback function invoked when the switch is toggled, either by clicking the card or the switch itself.  It receives the new state of the switch (a `Boolean` value) as a parameter.
 *
 * The card has a rounded corner shape and provides a click sound effect upon interaction.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4Event Optional GA4 event data to log on click.
 * @param ga4EventProvider Optional provider for GA4 event data resolved using the toggled state.
 */
@Composable
fun SwitchCardItem(
    modifier: Modifier = Modifier,
    title: String,
    enabled: Boolean = true,
    switchState: State<Boolean>,
    onSwitchToggled: (Boolean) -> Unit,
    checkIcon: ImageVector = Icons.Filled.Check,
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
    ga4EventProvider: ((Boolean) -> Ga4EventData?)? = null,
) {
    val view: View = LocalView.current
    Card(
        enabled = enabled,
        shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = modifier,
        onClick = {
            if (!enabled) return@Card
            val updatedValue = !switchState.value
            view.playSoundEffect(SoundEffectConstants.CLICK)
            firebaseController.logGa4Event(ga4EventProvider?.invoke(updatedValue) ?: ga4Event)
            onSwitchToggled(updatedValue)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = SizeConstants.LargeSize),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )
            CustomSwitch(
                checked = switchState.value,
                enabled = enabled,
                onCheckedChange = { isChecked ->
                    firebaseController.logGa4Event(ga4EventProvider?.invoke(isChecked) ?: ga4Event)
                    onSwitchToggled(isChecked)
                },
                checkIcon = checkIcon
            )
        }
    }
}
