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

package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.views.cards

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.switches.CustomSwitch
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * A card component that displays a toggle switch for user consent.
 *
 * This card is designed to present a specific consent option to the user,
 * such as enabling analytics or crash reporting. It includes an icon, a title, a description,
 * and a switch to toggle the consent state. The entire card is clickable to toggle the switch,
 * providing a larger touch target and better user experience. Haptic and sound feedback are
 * triggered on click.
 *
 * @param title The main text displayed on the card, representing the consent category.
 * @param description A detailed explanation of what the consent option entails.
 * @param switchState The current state of the consent (true for granted, false for denied).
 * @param icon An [ImageVector] to be displayed alongside the title and description,
 *             visually representing the consent category.
 * @param onCheckedChange A lambda function that is invoked when the user toggles the switch or
 *                        clicks the card. It receives the new boolean state of the consent.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4EventProvider Optional provider that builds GA4 event payload from the updated checked state.
 */
@Composable
fun ConsentToggleCard(
    title: String,
    description: String,
    switchState: Boolean,
    icon: ImageVector,
    onCheckedChange: (Boolean) -> Unit,
    firebaseController: FirebaseController? = null,
    ga4EventProvider: ((Boolean) -> Ga4EventData?)? = null,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(),
        onClick = {
            val updatedValue = !switchState
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
            firebaseController.logGa4Event(ga4EventProvider?.invoke(updatedValue))
            onCheckedChange(updatedValue)
        },
        shape = MaterialTheme.shapes.large, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = SizeConstants.LargeSize,
                        vertical = SizeConstants.MediumSize
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(R.string.icon_desc_consent_category),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(SizeConstants.LargeIncreasedSize + SizeConstants.ExtraSmallSize)
                )
                LargeHorizontalSpacer()
                Column(modifier = Modifier.weight(weight = 1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        modifier = Modifier.animateContentSize(),
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
                LargeHorizontalSpacer()
                CustomSwitch(
                    checked = switchState,
                    onCheckedChange = { isChecked ->
                        firebaseController.logGa4Event(ga4EventProvider?.invoke(isChecked))
                        onCheckedChange(isChecked)
                    },
                )
            }
        }
    }
}
