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

package com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.chip

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick

/**
 * A custom [FilterChip] that provides haptic and sound feedback on click,
 * along with a bounce click effect.
 *
 * This composable automatically displays a checkmark icon with a fade and scale animation
 * when selected, if no custom [leadingIcon] is provided.
 *
 * @param selected Whether the chip is currently selected.
 * @param onClick The callback to be invoked when the chip is clicked.
 * @param label The text to be displayed on the chip.
 * @param modifier The [Modifier] to be applied to the chip.
 * @param leadingIcon An optional composable to be displayed at the start of the chip.
 *                    If null, a checkmark icon will be shown when the chip is selected.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4Event Optional GA4 event data to log on click.
 */
@Composable
fun CommonFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable (() -> Unit))? = null,
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    FilterChip(
        selected = selected,
        onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
            firebaseController.logGa4Event(ga4Event)
            onClick()
        },
        label = { Text(text = label) },
        leadingIcon = {
            if (leadingIcon != null) {
                leadingIcon()
            } else {
                AnimatedContent(
                    targetState = selected,
                    transitionSpec = { SelectAllTransitions.fadeScale },
                    label = "Checkmark Animation"
                ) { targetChecked ->
                    if (targetChecked) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                    }
                }
            }
        },
        modifier = modifier.bounceClick(),
        interactionSource = interactionSource,
    )
}

/**
 * A collection of predefined transitions for animating content changes,
 * specifically designed for selection states like in chips or checkboxes.
 */
object SelectAllTransitions {
    private const val DURATION = 300
    private val fadeScaleSpec = tween<Float>(DURATION)

    val fadeScale: ContentTransform by lazy {
        (fadeIn(animationSpec = fadeScaleSpec) + scaleIn(animationSpec = fadeScaleSpec))
            .togetherWith(fadeOut(animationSpec = fadeScaleSpec) + scaleOut(animationSpec = fadeScaleSpec))
    }
}
