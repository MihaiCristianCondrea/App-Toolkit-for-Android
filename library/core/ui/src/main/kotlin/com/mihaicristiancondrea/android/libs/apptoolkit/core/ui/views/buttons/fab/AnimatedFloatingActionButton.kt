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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonFeedback
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry

/**
 * FAB with caller-owned visibility and action, plus local saved toggle state. Clicks perform
 * feedback and optional GA4 logging before [onClick]. Supply [contentDescription] to name the
 * icon for accessibility.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedFloatingActionButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
    feedback: ButtonFeedback = ButtonFeedback(),
    ga4Event: Ga4EventData? = null,
) = AnimatedFloatingActionButton(
    modifier, isVisible, ToolkitIcon.Vector(icon), contentDescription, onClick,
    feedback, ga4Event,
)

/** Icon-source overload; playback state is local to the composed button. */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedFloatingActionButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    icon: ToolkitIcon,
    contentDescription: String? = null,
    onClick: () -> Unit,
    feedback: ButtonFeedback = ButtonFeedback(),
    ga4Event: Ga4EventData? = null,
) {
    val telemetryRepository = LocalTelemetry.current
    val haptics = LocalHapticFeedback.current
    val view = LocalView.current
    val checkedState = rememberSaveable { mutableStateOf(false) }

    var clickCount by remember { mutableIntStateOf(0) }

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        ToggleFloatingActionButton(
            checked = checkedState.value,
            onCheckedChange = { newChecked ->
                clickCount++
                feedback.performClick(view = view, hapticFeedback = haptics)
                telemetryRepository.logGa4Event(ga4Event)
                checkedState.value = newChecked
                onClick()
            },
            modifier = modifier.bounceClick()
        ) {
            AnimatedToolkitIcon(icon = icon, clickCount = clickCount, contentDescription = contentDescription)
        }
    }
}
