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
import androidx.compose.material3.IconButtonShapes
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ButtonIconSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    icon: ImageVector? = null,
    painter: Painter? = null,
    shapes: IconButtonShapes = IconButtonDefaults.shapes()
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
        shapes = shapes
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                imageVector = it,
                contentDescription = iconContentDescription
            )
        } ?: painter?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                painter = it,
                contentDescription = iconContentDescription
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilledIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    icon: ImageVector? = null,
    painter: Painter? = null,
    shapes: IconButtonShapes = IconButtonDefaults.shapes()
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
        shapes = shapes
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                imageVector = it,
                contentDescription = iconContentDescription
            )
        } ?: painter?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                painter = it,
                contentDescription = iconContentDescription
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilledTonalIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    icon: ImageVector? = null,
    painter: Painter? = null,
    shapes: IconButtonShapes = IconButtonDefaults.shapes()
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
        shapes = shapes
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                imageVector = it,
                contentDescription = iconContentDescription
            )
        } ?: painter?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                painter = it,
                contentDescription = iconContentDescription
            )
        }
    }
}

@Composable
fun IconButtonWithText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    label: String,
    icon: ImageVector? = null,
    painter: Painter? = null
) {
    val hapticFeedback : HapticFeedback = LocalHapticFeedback.current
    val view : View = LocalView.current

    Button(onClick = {
        view.playSoundEffect(SoundEffectConstants.CLICK)
        hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
        onClick()
    }, enabled = enabled, modifier = modifier.bounceClick()) {
        icon?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                imageVector = it,
                contentDescription = iconContentDescription
            )
        } ?: painter?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                painter = it,
                contentDescription = iconContentDescription)
        }
        ButtonIconSpacer()
        Text(text = label)
    }
}

@Composable
fun TonalIconButtonWithText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    label: String,
    icon: ImageVector? = null,
    painter: Painter? = null
) {
    val hapticFeedback : HapticFeedback = LocalHapticFeedback.current
    val view : View = LocalView.current

    FilledTonalButton(onClick = {
        view.playSoundEffect(SoundEffectConstants.CLICK)
        hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
        onClick()
    }, enabled = enabled, modifier = modifier.bounceClick()) {
        icon?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                imageVector = it,
                contentDescription = iconContentDescription
            )
        } ?: painter?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                painter = it,
                contentDescription = iconContentDescription)
        }
        ButtonIconSpacer()
        Text(text = label)
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OutlinedIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    icon: ImageVector? = null,
    painter: Painter? = null,
    shapes: IconButtonShapes = IconButtonDefaults.shapes()
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
        shapes = shapes
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                imageVector = it,
                contentDescription = iconContentDescription
            )
        } ?: painter?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                painter = it,
                contentDescription = iconContentDescription
            )
        }
    }
}

@Composable
fun OutlinedIconButtonWithText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconContentDescription: String? = null,
    label: String,
    icon: ImageVector? = null,
    painter: Painter? = null
) {
    val hapticFeedback : HapticFeedback = LocalHapticFeedback.current
    val view : View = LocalView.current

    OutlinedButton(onClick = {
        view.playSoundEffect(SoundEffectConstants.CLICK)
        hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
        onClick()
    }, enabled = enabled, modifier = modifier.bounceClick()) {
        icon?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                imageVector = it,
                contentDescription = iconContentDescription
            )
        } ?: painter?.let {
            Icon(
                modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                painter = it,
                contentDescription = iconContentDescription)
        }
        ButtonIconSpacer()
        Text(text = label)
    }
}
