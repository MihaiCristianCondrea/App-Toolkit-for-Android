package com.d4rk.android.libs.apptoolkit.core.ui.views.buttons

import android.view.View
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ButtonIconSpacer

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
 * @param vectorIcon The [ImageVector] to be displayed inside the button.
 * @param painterIcon The [Painter] to be displayed if an [ImageVector] is not provided.
 * @param feedback The feedback configuration for sound and haptics.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    vectorIcon: ImageVector? = null,
    painterIcon: Painter? = null,
    feedback: ButtonFeedback = ButtonFeedback(),
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    androidx.compose.material3.IconButton(
        onClick = {
            feedback.performClick(view = view, hapticFeedback = hapticFeedback)
            onClick()
        },
        enabled = enabled,
        modifier = modifier.bounceClick(),
        shapes = IconButtonDefaults.shapes()
    ) {
        IconContent(
            icon = vectorIcon,
            painter = painterIcon,
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
 * @param vectorIcon The icon to be displayed inside the button, as an [ImageVector].
 * @param painterIcon The [Painter] to be displayed when no vector icon is provided.
 * @param feedback The feedback configuration for sound and haptics.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilledIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    vectorIcon: ImageVector? = null,
    painterIcon: Painter? = null,
    feedback: ButtonFeedback = ButtonFeedback(),
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    androidx.compose.material3.FilledIconButton(
        onClick = {
            feedback.performClick(view = view, hapticFeedback = hapticFeedback)
            onClick()
        },
        enabled = enabled,
        modifier = modifier.bounceClick(),
        shapes = IconButtonDefaults.shapes()
    ) {
        IconContent(
            icon = vectorIcon,
            painter = painterIcon,
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
 * @param vectorIcon The [ImageVector] to be displayed as the icon.
 * @param painterIcon The [Painter] to be displayed if no vector icon is provided.
 * @param feedback The feedback configuration for sound and haptics.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilledTonalIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    vectorIcon: ImageVector? = null,
    painterIcon: Painter? = null,
    feedback: ButtonFeedback = ButtonFeedback(),
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    androidx.compose.material3.FilledTonalIconButton(
        onClick = {
            feedback.performClick(view = view, hapticFeedback = hapticFeedback)
            onClick()
        },
        enabled = enabled,
        modifier = modifier.bounceClick(),
        shapes = IconButtonDefaults.shapes()
    ) {
        IconContent(
            icon = vectorIcon,
            painter = painterIcon,
            contentDescription = iconContentDescription
        )
    }
}

/**
 * A Material Design [Button] that supports text-only, icon+text, or icon-only rendering.
 * When only an icon is provided, this composable falls back to [FilledIconButton].
 *
 * @param modifier The [Modifier] to be applied to this button.
 * @param onClick A lambda function to be invoked when the button is clicked.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be clickable.
 * @param iconContentDescription Text used by accessibility services to describe the icon.
 * @param label The text to be displayed on the button, or `null` for icon-only usage.
 * @param vectorIcon The [ImageVector] to be displayed as the leading icon.
 * @param painterIcon The [Painter] to be displayed when no vector icon is provided.
 * @param feedback The feedback configuration for sound and haptics.
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
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    val hasIcon: Boolean = vectorIcon != null || painterIcon != null
    val hasLabel: Boolean = !label.isNullOrEmpty()

    require(hasIcon || hasLabel) { "GeneralButton requires a label, an icon, or both." }

    if (hasIcon && !hasLabel) {
        FilledIconButton(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            iconContentDescription = iconContentDescription,
            vectorIcon = vectorIcon,
            painterIcon = painterIcon,
            feedback = feedback,
        )
        return
    }

    Button(
        onClick = {
            feedback.performClick(view = view, hapticFeedback = hapticFeedback)
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

/**
 * A filled tonal button that supports text-only, icon+text, or icon-only rendering.
 * When only an icon is provided, this composable falls back to [FilledTonalIconButton].
 *
 * @param modifier The [Modifier] to be applied to this button.
 * @param onClick A lambda function to be invoked when this button is clicked.
 * @param enabled A boolean indicating whether this button is enabled and can be interacted with.
 * @param iconContentDescription A textual description of the icon for accessibility purposes.
 * @param label The text to be displayed on the button, or `null` for icon-only usage.
 * @param vectorIcon The [ImageVector] to be displayed as an icon at the start of the button.
 * @param painterIcon The [Painter] to be displayed when a vector icon is not provided.
 * @param feedback The feedback configuration for sound and haptics.
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
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    val hasIcon: Boolean = vectorIcon != null || painterIcon != null
    val hasLabel: Boolean = !label.isNullOrEmpty()

    require(hasIcon || hasLabel) { "GeneralTonalButton requires a label, an icon, or both." }

    if (hasIcon && !hasLabel) {
        FilledTonalIconButton(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            iconContentDescription = iconContentDescription,
            vectorIcon = vectorIcon,
            painterIcon = painterIcon,
            feedback = feedback,
        )
        return
    }

    FilledTonalButton(
        onClick = {
            feedback.performClick(view = view, hapticFeedback = hapticFeedback)
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
 * @param vectorIcon The [ImageVector] to be displayed inside the button.
 * @param painterIcon The [Painter] to be displayed when no vector icon is provided.
 * @param feedback The feedback configuration for sound and haptics.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OutlinedIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    vectorIcon: ImageVector? = null,
    painterIcon: Painter? = null,
    feedback: ButtonFeedback = ButtonFeedback(),
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    androidx.compose.material3.OutlinedIconButton(
        onClick = {
            feedback.performClick(view = view, hapticFeedback = hapticFeedback)
            onClick()
        },
        enabled = enabled,
        modifier = modifier.bounceClick(),
        shapes = IconButtonDefaults.shapes()
    ) {
        IconContent(
            icon = vectorIcon,
            painter = painterIcon,
            contentDescription = iconContentDescription
        )
    }
}

/**
 * An outlined button that supports text-only, icon+text, or icon-only rendering.
 * When only an icon is provided, this composable falls back to [OutlinedIconButton].
 *
 * @param modifier The [Modifier] to be applied to this button.
 * @param onClick The lambda to be executed when the button is clicked.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be clickable.
 * @param iconContentDescription Text used by accessibility services to describe what the icon represents.
 * @param label The text to be displayed on the button, or `null` for icon-only usage.
 * @param vectorIcon The [ImageVector] to be displayed inside the button.
 * @param painterIcon The [Painter] to be displayed when no vector icon is provided.
 * @param feedback The feedback configuration for sound and haptics.
 */
@Composable
fun GeneralOutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    label: String? = null,
    vectorIcon: ImageVector? = null,
    painterIcon: Painter? = null,
    feedback: ButtonFeedback = ButtonFeedback(),
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    val hasIcon: Boolean = vectorIcon != null || painterIcon != null
    val hasLabel: Boolean = !label.isNullOrEmpty()

    require(hasIcon || hasLabel) { "GeneralOutlinedButton requires a label, an icon, or both." }

    if (hasIcon && !hasLabel) {
        OutlinedIconButton(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            iconContentDescription = iconContentDescription,
            vectorIcon = vectorIcon,
            painterIcon = painterIcon,
            feedback = feedback,
        )
        return
    }

    OutlinedButton(
        onClick = {
            feedback.performClick(view = view, hapticFeedback = hapticFeedback)
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

/**
 * A Material Design [TextButton] that supports text-only, icon+text, or icon-only rendering.
 * When only an icon is provided, this composable falls back to [IconButton].
 *
 * @param modifier The [Modifier] to be applied to this button.
 * @param onClick A lambda function to be invoked when the button is clicked.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be clickable.
 * @param iconContentDescription Text used by accessibility services to describe the icon.
 * @param label The text to be displayed on the button, or `null` for icon-only usage.
 * @param vectorIcon The [ImageVector] to be displayed as the leading icon.
 * @param painterIcon The [Painter] to be displayed when no vector icon is provided.
 * @param feedback The feedback configuration for sound and haptics.
 */
@Composable
fun GeneralTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    label: String? = null,
    vectorIcon: ImageVector? = null,
    painterIcon: Painter? = null,
    feedback: ButtonFeedback = ButtonFeedback(),
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    val hasIcon: Boolean = vectorIcon != null || painterIcon != null
    val hasLabel: Boolean = !label.isNullOrEmpty()

    require(hasIcon || hasLabel) { "GeneralTextButton requires a label, an icon, or both." }

    if (hasIcon && !hasLabel) {
        IconButton(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            iconContentDescription = iconContentDescription,
            vectorIcon = vectorIcon,
            painterIcon = painterIcon,
            feedback = feedback,
        )
        return
    }

    TextButton(
        onClick = {
            feedback.performClick(view = view, hapticFeedback = hapticFeedback)
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
