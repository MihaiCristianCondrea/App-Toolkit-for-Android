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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.AnimatedToolkitIcon
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonFeedback
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick

/**
 * Small FAB shown only when both [isVisible] and [isExtended] are `true`. The caller owns
 * visibility and actions. Supply [contentDescription] to name the icon for accessibility.
 *
 * Click feedback and [onLogClick] run before [onClick].
 */
@Composable
fun SmallFloatingActionButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isExtended: Boolean,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
    feedback: ButtonFeedback = ButtonFeedback(),
    onLogClick: (() -> Unit)? = null,
) = SmallFloatingActionButton(
    modifier, isVisible, isExtended, ToolkitIcon.Vector(icon), contentDescription, onClick,
    feedback, onLogClick,
)

/** Icon-source overload; playback state is local to the composed button. */
@Composable
fun SmallFloatingActionButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isExtended: Boolean,
    icon: ToolkitIcon,
    contentDescription: String? = null,
    onClick: () -> Unit,
    feedback: ButtonFeedback = ButtonFeedback(),
    onLogClick: (() -> Unit)? = null,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    var clickCount by remember { mutableIntStateOf(0) }

    AnimatedVisibility(
        visible = isVisible && isExtended,
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        SmallFloatingActionButton(onClick = {
            clickCount++
            feedback.performClick(view = view, hapticFeedback = hapticFeedback)
            onLogClick?.invoke()
            onClick()
        }, modifier = modifier.bounceClick()) {
            AnimatedToolkitIcon(icon = icon, clickCount = clickCount, contentDescription = contentDescription)
        }
    }
}
