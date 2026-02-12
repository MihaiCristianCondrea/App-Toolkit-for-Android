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

package com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.fab

import android.view.View
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.ButtonFeedback
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick

/**
 * An animated extended floating action button that scales in and out based on its visibility.
 *
 * @param visible Controls the visibility of the button. When true, the button is fully visible; when false, it's scaled down to nothing.
 * @param onClick The action to perform when the button is clicked.
 * @param icon The icon to display within the button.
 * @param text Optional text to display alongside the icon in the button.
 * @param expanded Determines whether the button is in its expanded state (with text) or collapsed (icon only).
 * @param modifier Modifier to apply to the button.
 * @param feedback The feedback configuration for sound and haptics.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4Event Optional GA4 event data to log on click.
 */
@Composable
fun AnimatedExtendedFloatingActionButton(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    text: (@Composable () -> Unit)? = null,
    expanded: Boolean = true,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    feedback: ButtonFeedback = ButtonFeedback(),
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    val animatedScale: Float by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "FAB Scale"
    )

    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    if (animatedScale > 0f) {
        ExtendedFloatingActionButton(
            onClick = {
                feedback.performClick(view = view, hapticFeedback = hapticFeedback)
                firebaseController.logGa4Event(ga4Event)
                onClick()
            }, icon = icon, text = text ?: {}, expanded = expanded, modifier = modifier
                .bounceClick()
                .graphicsLayer {
                    scaleX = animatedScale
                    scaleY = animatedScale
                    transformOrigin = TransformOrigin(pivotFractionX = 1f, pivotFractionY = 1f)
                }
                .bounceClick(), containerColor = containerColor)
    }
}
