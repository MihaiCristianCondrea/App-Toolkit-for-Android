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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ButtonIconSpacer

/** Visual treatment shared by labelled and icon-only buttons. */
enum class GeneralButtonStyle { Filled, Tonal, Outlined, Elevated, Text }

/** Logical icon position; Start and End follow the layout direction. */
enum class ButtonIconPosition { Start, End }

/**
 * Adaptive action button. A nonblank label selects a content button; otherwise [icon] is required.
 * [contentDescription] is optional; for icon-only actions, null marks the icon as decorative.
 * A labelled action uses its visible text unless an explicit description replaces it. Icons beside labels are decorative.
 *
 * Feedback, analytics and icon replay run once per enabled click, before [onClick]. Do not add
 * click feedback at call sites. Null color overrides retain each Material style's defaults,
 * including disabled colors. [iconTint] follows ToolkitIcon tinting rules (Lottie must be tintable).
 * Elevated icon-only buttons use a compact ElevatedButton with Material elevation and touch target.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GeneralButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: GeneralButtonStyle = GeneralButtonStyle.Filled,
    enabled: Boolean = true,
    label: String? = null,
    icon: ToolkitIcon? = null,
    iconPosition: ButtonIconPosition = ButtonIconPosition.Start,
    contentDescription: String? = label,
    iconSize: Dp = SizeConstants.ButtonIconSize,
    iconTint: Color? = null,
    containerColor: Color? = null,
    contentColor: Color? = null,
    shape: Shape? = null,
    feedback: ButtonFeedback = ButtonFeedback(),
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    val hasLabel = !label.isNullOrBlank()
    require(hasLabel || icon != null) { "GeneralButton requires a nonblank label or an icon." }
    val hapticFeedback = LocalHapticFeedback.current
    val view = LocalView.current
    var clickCount by remember { mutableIntStateOf(0) }
    val click = {
        clickCount++
        feedback.performClick(view = view, hapticFeedback = hapticFeedback)
        firebaseController.logGa4Event(ga4Event)
        onClick()
    }
    val buttonModifier = modifier.bounceClick(animationEnabled = enabled)
    val content: @Composable () -> Unit = {
        val descriptionOverride = contentDescription?.takeIf { hasLabel && it != label }
        val text: @Composable () -> Unit = {
            if (hasLabel) Text(
                text = requireNotNull(label), maxLines = 1, overflow = TextOverflow.Ellipsis,
                modifier = if (descriptionOverride != null) Modifier.clearAndSetSemantics {
                    this.contentDescription = descriptionOverride
                } else Modifier,
            )
        }
        val image: @Composable () -> Unit = {
            if (icon != null) IconContent(
                icon = icon, clickCount = clickCount,
                contentDescription = if (hasLabel) null else contentDescription,
                size = iconSize, tint = iconTint,
            )
        }
        if (iconPosition == ButtonIconPosition.Start) image() else text()
        if (hasLabel && icon != null) ButtonIconSpacer()
        if (iconPosition == ButtonIconPosition.Start) text() else image()
    }
    val container = containerColor ?: Color.Unspecified
    val foreground = contentColor ?: Color.Unspecified
    if (!hasLabel && style != GeneralButtonStyle.Elevated) {
        val colors = when (style) {
            GeneralButtonStyle.Filled -> IconButtonDefaults.filledIconButtonColors()
            GeneralButtonStyle.Tonal -> IconButtonDefaults.filledTonalIconButtonColors()
            GeneralButtonStyle.Outlined -> IconButtonDefaults.outlinedIconButtonColors()
            else -> IconButtonDefaults.iconButtonColors()
        }.copy(containerColor = container, contentColor = foreground)
        when (style) {
            GeneralButtonStyle.Filled -> FilledIconButton(
                onClick = click,
                modifier = buttonModifier,
                enabled = enabled,
                colors = colors,
                shapes = IconButtonDefaults.shapes(),
                content = content,
            )
            GeneralButtonStyle.Tonal -> FilledTonalIconButton(
                onClick = click,
                modifier = buttonModifier,
                enabled = enabled,
                colors = colors,
                shapes = IconButtonDefaults.shapes(),
                content = content,
            )
            GeneralButtonStyle.Outlined -> OutlinedIconButton(
                onClick = click,
                modifier = buttonModifier,
                enabled = enabled,
                colors = colors,
                shapes = IconButtonDefaults.shapes(),
                content = content,
            )
            else -> IconButton(
                onClick = click,
                modifier = buttonModifier,
                enabled = enabled,
                colors = colors,
                shapes = IconButtonDefaults.shapes(),
                content = content,
            )
        }
        return
    }
    val colors = when (style) {
        GeneralButtonStyle.Filled -> ButtonDefaults.buttonColors()
        GeneralButtonStyle.Tonal -> ButtonDefaults.filledTonalButtonColors()
        GeneralButtonStyle.Outlined -> ButtonDefaults.outlinedButtonColors()
        GeneralButtonStyle.Elevated -> ButtonDefaults.elevatedButtonColors()
        GeneralButtonStyle.Text -> ButtonDefaults.textButtonColors()
    }.copy(containerColor = container, contentColor = foreground)
    val rowContent: @Composable RowScope.() -> Unit = { content() }
    val resolvedShape = shape ?: ButtonDefaults.shape
    when (style) {
        GeneralButtonStyle.Filled -> Button(
            onClick = click,
            modifier = buttonModifier,
            enabled = enabled,
            colors = colors,
            shape = resolvedShape,
            content = rowContent,
        )
        GeneralButtonStyle.Tonal -> FilledTonalButton(
            onClick = click,
            modifier = buttonModifier,
            enabled = enabled,
            colors = colors,
            shape = resolvedShape,
            content = rowContent,
        )
        GeneralButtonStyle.Outlined -> OutlinedButton(
            onClick = click,
            modifier = buttonModifier,
            enabled = enabled,
            colors = colors,
            shape = resolvedShape,
            content = rowContent,
        )
        GeneralButtonStyle.Text -> TextButton(
            onClick = click,
            modifier = buttonModifier,
            enabled = enabled,
            colors = colors,
            shape = resolvedShape,
            content = rowContent,
        )
        GeneralButtonStyle.Elevated -> ElevatedButton(
            onClick = click, modifier = if (hasLabel) buttonModifier else buttonModifier.size(40.dp),
            enabled = enabled, colors = colors,
            shape = resolvedShape,
            contentPadding = if (hasLabel) ButtonDefaults.ContentPadding else PaddingValues(0.dp),
            content = rowContent,
        )
    }
}
