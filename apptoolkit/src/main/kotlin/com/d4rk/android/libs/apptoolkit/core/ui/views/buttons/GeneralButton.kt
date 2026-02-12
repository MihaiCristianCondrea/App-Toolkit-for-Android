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

package com.d4rk.android.libs.apptoolkit.core.ui.views.buttons

import android.view.View
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ButtonIconSpacer

/**
 * A Material Design [Button] that supports text-only, icon+text, or icon-only rendering.
 * When only an icon is provided, this composable uses a filled icon button.
 *
 * @param modifier The [Modifier] to be applied to this button.
 * @param onClick A lambda function to be invoked when the button is clicked.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be clickable.
 * @param iconContentDescription Text used by accessibility services to describe the icon.
 * @param label The text to be displayed on the button, or `null` for icon-only usage.
 * @param vectorIcon The [ImageVector] to be displayed as the leading icon.
 * @param painterIcon The [Painter] to be displayed when no vector icon is provided.
 * @param feedback The feedback configuration for sound and haptics.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4Event Optional GA4 event data to log on click.
 */
@Composable
fun GeneralButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    label: String? = null,
    vectorIcon: ImageVector? = null,
    painterIcon: Painter? = null,
    feedback: ButtonFeedback = ButtonFeedback(),
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    val hasIcon: Boolean = vectorIcon != null || painterIcon != null
    val hasLabel: Boolean = !label.isNullOrEmpty()

    require(hasIcon || hasLabel) { "GeneralButton requires a label, an icon, or both." }

    if (hasIcon && !hasLabel) {
        IconOnlyButton(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            iconContentDescription = iconContentDescription,
            vectorIcon = vectorIcon,
            painterIcon = painterIcon,
            feedback = feedback,
            firebaseController = firebaseController,
            ga4Event = ga4Event,
            style = IconOnlyButtonStyle.Filled,
        )
        return
    }

    Button(
        onClick = {
            feedback.performClick(view = view, hapticFeedback = hapticFeedback)
            firebaseController.logGa4Event(ga4Event)
            onClick()
        },
        enabled = enabled,
        modifier = modifier.bounceClick(),
    ) {
        if (hasIcon) {
            IconContent(
                icon = vectorIcon,
                painter = painterIcon,
                contentDescription = iconContentDescription,
            )
            ButtonIconSpacer()
        }
        if (hasLabel) {
            Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
