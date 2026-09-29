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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.LargeExtendedFloatingActionButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumExtendedFloatingActionButton
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.SmallExtendedFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.AnimatedToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.FabColor
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.FabSize
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonFeedback

/**
 * Draws [fabs] as a column at the bottom end of the screen, top to bottom in list order, so the
 * last one, normally the screen's main action, sits in the corner. Each scales in and out on its
 * own as it appears, disappears or changes [ToolkitFab.visible].
 *
 * The Toolkit's scaffolds call this for the buttons a destination or a screen declares; call it
 * directly only in a scaffold of your own.
 */
@Composable
fun ToolkitFabColumn(
    fabs: List<ToolkitFab>,
    modifier: Modifier = Modifier,
    spacing: Dp = 16.dp,
    feedback: ButtonFeedback = ButtonFeedback(),
) {
    if (fabs.isEmpty()) return
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(spacing, Alignment.Bottom),
    ) {
        fabs.forEach { fab ->
            key(fab.id) {
                AnimatedVisibility(
                    visible = fab.visible,
                    enter = scaleIn(transformOrigin = FabOrigin) + fadeIn(),
                    exit = scaleOut(transformOrigin = FabOrigin) + fadeOut(),
                ) {
                    ToolkitFloatingActionButton(fab, feedback)
                }
            }
        }
    }
}

/** One [ToolkitFab], in the Material 3 button its size and label call for. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ToolkitFloatingActionButton(fab: ToolkitFab, feedback: ButtonFeedback = ButtonFeedback()) {
    val view = LocalView.current
    val haptics = LocalHapticFeedback.current
    var clickCount by remember { mutableIntStateOf(0) }
    val onClick = {
        clickCount++
        feedback.performClick(view = view, hapticFeedback = haptics)
        fab.onClick()
    }
    val containerColor = fab.color.containerColor()
    val contentColor = contentColorFor(containerColor)
    val modifier = Modifier.bounceClick()
    val iconSize = when (fab.size) {
        FabSize.Small, FabSize.Regular -> null
        FabSize.Medium -> FloatingActionButtonDefaults.MediumIconSize
        FabSize.Large -> FloatingActionButtonDefaults.LargeIconSize
    }
    val icon: @Composable () -> Unit = {
        AnimatedToolkitIcon(
            icon = fab.icon,
            clickCount = clickCount,
            // An extended button's label already names it.
            contentDescription = if (fab.label != null) null else fab.contentDescription,
            modifier = if (iconSize != null) Modifier.size(iconSize) else Modifier,
        )
    }
    val label = fab.label
    if (label == null) {
        when (fab.size) {
            FabSize.Small -> SmallFloatingActionButton(onClick, modifier, containerColor = containerColor, contentColor = contentColor, content = icon)
            FabSize.Regular -> FloatingActionButton(onClick, modifier, containerColor = containerColor, contentColor = contentColor, content = icon)
            FabSize.Medium -> MediumFloatingActionButton(onClick, modifier, containerColor = containerColor, contentColor = contentColor, content = icon)
            FabSize.Large -> LargeFloatingActionButton(onClick, modifier, containerColor = containerColor, contentColor = contentColor, content = icon)
        }
    } else {
        val text: @Composable () -> Unit = { Text(label) }
        when (fab.size) {
            FabSize.Small -> SmallExtendedFloatingActionButton(text, icon, onClick, modifier, fab.expanded, containerColor = containerColor, contentColor = contentColor)
            FabSize.Regular -> ExtendedFloatingActionButton(text, icon, onClick, modifier, fab.expanded, containerColor = containerColor, contentColor = contentColor)
            FabSize.Medium -> MediumExtendedFloatingActionButton(text, icon, onClick, modifier, fab.expanded, containerColor = containerColor, contentColor = contentColor)
            FabSize.Large -> LargeExtendedFloatingActionButton(text, icon, onClick, modifier, fab.expanded, containerColor = containerColor, contentColor = contentColor)
        }
    }
}

@Composable
private fun FabColor.containerColor(): Color = when (this) {
    FabColor.Primary -> MaterialTheme.colorScheme.primaryContainer
    FabColor.Secondary -> MaterialTheme.colorScheme.secondaryContainer
    FabColor.Tertiary -> MaterialTheme.colorScheme.tertiaryContainer
    FabColor.Surface -> MaterialTheme.colorScheme.surfaceContainerHigh
}

/** Buttons grow from, and shrink into, the corner they sit in. */
private val FabOrigin = TransformOrigin(pivotFractionX = 1f, pivotFractionY = 1f)
