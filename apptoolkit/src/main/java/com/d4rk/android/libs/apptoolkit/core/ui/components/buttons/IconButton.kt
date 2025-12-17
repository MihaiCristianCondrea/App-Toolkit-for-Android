package com.d4rk.android.libs.apptoolkit.core.ui.components.buttons

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ButtonIconSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * A custom [IconButton] composable that adds a bounce click effect, sound feedback,
 * and haptic feedback to the standard Material Design icon button.
 *
 * This button displays an icon provided as an [ImageVector].
 * It wraps the `androidx.compose.material3.IconButton` and enhances the user experience
 * on interaction.
 *
 * @param modifier The [Modifier] to be applied to this icon button. Defaults to [Modifier].
 * @param onClick The lambda to be executed when this icon button is clicked.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not
 * be clickable. Defaults to `true`.
 * @param iconContentDescription Text used by accessibility services to describe what the icon
 * represents. This is recommended for usability.
 * @param icon The [ImageVector] to be displayed inside the button.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    icon: ImageVector,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    IconButton(
        onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
            onClick()
        },
        enabled = enabled,
        modifier = modifier.bounceClick(),
        shapes = IconButtonDefaults.shapes()
    ) {
        Icon(
            modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
            imageVector = icon,
            contentDescription = iconContentDescription
        )
    }
}

/**
 * An icon button with a filled background, providing a high-emphasis way to trigger an action.
 * This composable is a wrapper around [androidx.compose.material3.FilledIconButton] that
 * adds haptic feedback, a click sound, and a bounce click effect.
 *
 * @param modifier The [Modifier] to be applied to this button.
 * @param onClick Will be called when the user clicks the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not
 * be clickable.
 * @param iconContentDescription Text used by accessibility services to describe what the icon
 * represents. This text should be provided if the icon is used for an action, but not if it is
 * purely decorative.
 * @param icon The icon to be displayed inside the button, as an [ImageVector].
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilledIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    icon: ImageVector,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    androidx.compose.material3.FilledIconButton(
        onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
            onClick()
        },
        enabled = enabled,
        modifier = modifier.bounceClick(),
        shapes = IconButtonDefaults.shapes()
    ) {
        Icon(
            modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
            imageVector = icon,
            contentDescription = iconContentDescription
        )
    }
}

/**
 * A composable that displays a filled tonal icon button. This is a wrapper around Material 3's
 * `FilledTonalIconButton` that adds haptic feedback, a click sound effect, and a bounce animation on click.
 *
 * Tonal icon buttons are a medium-emphasis alternative to standard icon buttons. They use a
 * secondary tonal color for the container.
 *
 * @param modifier The [Modifier] to be applied to this button.
 * @param onClick The lambda to be executed when the button is clicked.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be clickable.
 * @param iconContentDescription Text used by accessibility services to describe the icon's action.
 * @param icon The [ImageVector] to be displayed as the icon.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilledTonalIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    icon: ImageVector,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    androidx.compose.material3.FilledTonalIconButton(
        onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
            onClick()
        },
        enabled = enabled,
        modifier = modifier.bounceClick(),
        shapes = IconButtonDefaults.shapes()
    ) {
        Icon(
            modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
            imageVector = icon,
            contentDescription = iconContentDescription
        )
    }
}

/**
 * A Material Design [Button] with an icon and a text label. This composable enhances the standard
 * button by integrating click sound effects, haptic feedback, and a bounce click animation.
 * The button content consists of an optional leading icon followed by a text label.
 *
 * @param modifier The [Modifier] to be applied to this button.
 * @param onClick A lambda function to be invoked when the button is clicked.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be clickable.
 * @param iconContentDescription Text used by accessibility services to describe the icon.
 * @param label The text to be displayed on the button.
 * @param icon The [ImageVector] to be displayed as the leading icon.
 */
@Composable
fun IconButtonWithText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    label: String,
    icon: ImageVector
) {
    val hapticFeedback : HapticFeedback = LocalHapticFeedback.current
    val view : View = LocalView.current

    Button(onClick = {
        view.playSoundEffect(SoundEffectConstants.CLICK)
        hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
        onClick()
    }, enabled = enabled, modifier = modifier.bounceClick()) {
        Icon(
            modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
            imageVector = icon,
            contentDescription = iconContentDescription
        )
        ButtonIconSpacer()
        Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

/**
 * A filled tonal button with an icon and a text label. This button is a lower-emphasis
 * alternative to a filled button.
 *
 * This composable wraps the Material 3 `FilledTonalButton` and adds custom click feedback,
 * including a sound effect and haptic feedback. It also includes a `bounceClick` modifier
 * for a visual press effect. The button content consists of an optional icon followed by
 * a text label.
 *
 * @param modifier The [Modifier] to be applied to this button.
 * @param onClick A lambda function to be invoked when this button is clicked.
 * @param enabled A boolean indicating whether this button is enabled and can be interacted with.
 * @param iconContentDescription A textual description of the icon for accessibility purposes.
 * @param label The text to be displayed on the button.
 * @param icon The [ImageVector] to be displayed as an icon at the start of the button.
 */
@Composable
fun TonalIconButtonWithText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    label: String,
    icon: ImageVector
) {
    val hapticFeedback : HapticFeedback = LocalHapticFeedback.current
    val view : View = LocalView.current

    FilledTonalButton(onClick = {
        view.playSoundEffect(SoundEffectConstants.CLICK)
        hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
        onClick()
    }, enabled = enabled, modifier = modifier.bounceClick()) {
        Icon(
            modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
            imageVector = icon,
            contentDescription = iconContentDescription
        )
        ButtonIconSpacer()
        Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

/**
 * A composable for a Material Design outlined icon button. Outlined icon buttons are a
 * medium-emphasis alternative to filled or standard icon buttons, with a thin border.
 *
 * This function wraps the `androidx.compose.material3.OutlinedIconButton` and enhances it by adding:
 * - A click sound effect.
 * - Haptic feedback on click.
 * - A visual bounce effect on interaction via the `bounceClick` modifier.
 *
 * The icon is provided as an [ImageVector].
 *
 * @param modifier The [Modifier] to be applied to this button.
 * @param onClick The lambda to be executed when the button is clicked. Defaults to an empty lambda.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be clickable.
 * @param iconContentDescription Text used by accessibility services to describe what the icon represents.
 * @param icon The [ImageVector] to be displayed inside the button.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OutlinedIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    icon: ImageVector,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    OutlinedIconButton(
        onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
            onClick()
        },
        enabled = enabled,
        modifier = modifier.bounceClick(),
        shapes = IconButtonDefaults.shapes()
    ) {
        Icon(
            modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
            imageVector = icon,
            contentDescription = iconContentDescription
        )
    }
}

@Composable
fun OutlinedIconButtonWithText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    label: String,
    icon: ImageVector
) {
    val hapticFeedback : HapticFeedback = LocalHapticFeedback.current
    val view : View = LocalView.current

    OutlinedButton(onClick = {
        view.playSoundEffect(SoundEffectConstants.CLICK)
        hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
        onClick()
    }, enabled = enabled, modifier = modifier.bounceClick()) {
        Icon(
            modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
            imageVector = icon,
            contentDescription = iconContentDescription
        )
        ButtonIconSpacer()
        Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}
