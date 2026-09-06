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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons

import android.view.View
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ButtonIconSpacer

/**
 * An outlined button that supports text-only, icon+text, or icon-only rendering.
 * When only an icon is provided, this composable uses an outlined icon button.
 *
 * State ownership:
 * - The caller owns button enabled state and click side effects.
 * - This composable is render-only and forwards interaction callbacks.
 *
 * Accessibility:
 * - Provide [iconContentDescription] for icon-only usage.
 * - Keep [label] short because it is constrained to one line.
 *
 * @param modifier The [Modifier] to be applied to this button.
 * @param onClick The lambda to be executed when the button is clicked.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be clickable.
 * @param iconContentDescription Text used by accessibility services to describe what the icon represents.
 * @param label The text to be displayed on the button, or `null` for icon-only usage.
 * @param icon The icon rendered before the label, or on its own when [label] is null. An
 *   animated icon plays every time the button is clicked.
 * @param iconSize The icon size used when this composable renders icon + text content.
 * @param feedback The feedback configuration for sound and haptics.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4Event Optional GA4 event data to log on click.
 */
@Composable
fun GeneralOutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    label: String? = null,
    icon: ToolkitIcon? = null,
    iconSize: Dp = SizeConstants.ButtonIconSize,
    feedback: ButtonFeedback = ButtonFeedback(),
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    var clickCount: Int by remember { mutableIntStateOf(value = 0) }
    val hasIcon: Boolean = icon != null
    val hasLabel: Boolean = !label.isNullOrEmpty()

    require(hasIcon || hasLabel) { "GeneralOutlinedButton requires a label, an icon, or both." }

    if (hasIcon && !hasLabel) {
        IconOnlyButton(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            iconContentDescription = iconContentDescription,
            icon = icon,
            feedback = feedback,
            firebaseController = firebaseController,
            ga4Event = ga4Event,
            style = IconOnlyButtonStyle.Outlined,
            iconSize = iconSize,
        )
        return
    }

    OutlinedButton(
        onClick = {
            clickCount++
            feedback.performClick(view = view, hapticFeedback = hapticFeedback)
            firebaseController.logGa4Event(ga4Event)
            onClick()
        },
        enabled = enabled,
        modifier = modifier.bounceClick(),
    ) {
        if (hasIcon) {
            IconContent(
                icon = icon,
                clickCount = clickCount,
                contentDescription = iconContentDescription,
                size = iconSize,
            )
            ButtonIconSpacer()
        }
        if (hasLabel) {
            Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
