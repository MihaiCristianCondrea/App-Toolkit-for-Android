package com.d4rk.android.libs.apptoolkit.core.ui.views.buttons

import android.view.View
import androidx.compose.material3.FilledTonalButton
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
 * A filled tonal button that supports text-only, icon+text, or icon-only rendering.
 * When only an icon is provided, this composable uses a filled tonal icon button.
 *
 * @param modifier The [Modifier] to be applied to this button.
 * @param onClick A lambda function to be invoked when this button is clicked.
 * @param enabled A boolean indicating whether this button is enabled and can be interacted with.
 * @param iconContentDescription A textual description of the icon for accessibility purposes.
 * @param label The text to be displayed on the button, or `null` for icon-only usage.
 * @param vectorIcon The [ImageVector] to be displayed as an icon at the start of the button.
 * @param painterIcon The [Painter] to be displayed when a vector icon is not provided.
 * @param feedback The feedback configuration for sound and haptics.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4Event Optional GA4 event data to log on click.
 */
@Composable
fun GeneralTonalButton(
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

    require(hasIcon || hasLabel) { "GeneralTonalButton requires a label, an icon, or both." }

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
            style = IconOnlyButtonStyle.FilledTonal,
        )
        return
    }

    FilledTonalButton(
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
            Text(text = label.orEmpty(), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
