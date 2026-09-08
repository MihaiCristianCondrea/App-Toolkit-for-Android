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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.chip

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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.AnimatedToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event

/**
 * A [FilterChip] with the toolkit's click feedback, bounce, and GA4 logging.
 *
 * Leading icon, in order of precedence:
 * - [leadingIcon], when given, renders as-is and this composable stays out of the way.
 * - Otherwise the chip shows a checkmark while [selected], and [icon] while it is not. Passing no
 *   [icon] leaves the unselected chip label-only, which is the Material default.
 *
 * [hasAnimation] chooses whether that swap crossfades or happens at once. Turn it off for rows that
 * rebuild often enough that the animation reads as noise, or where a caller animates the row itself.
 *
 * @param selected Whether the chip is currently selected.
 * @param onClick Invoked after feedback and analytics, on every click.
 * @param label Text displayed on the chip.
 * @param modifier The [Modifier] applied to the chip.
 * @param icon Icon shown while the chip is not selected.
 * @param hasAnimation Whether the leading icon crossfades between its states.
 * @param leadingIcon Full override of the leading icon slot.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4Event Optional GA4 event data to log on click.
 */
@Composable
fun CommonFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    icon: ToolkitIcon? = null,
    hasAnimation: Boolean = true,
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
            when {
                leadingIcon != null -> leadingIcon()

                hasAnimation -> AnimatedContent(
                    targetState = selected,
                    transitionSpec = { SelectAllTransitions.fadeScale },
                    label = "Filter chip leading icon",
                ) { isSelected ->
                    FilterChipLeadingIcon(selected = isSelected, icon = icon)
                }

                else -> FilterChipLeadingIcon(selected = selected, icon = icon)
            }
        },
        modifier = modifier.bounceClick(),
        interactionSource = interactionSource,
    )
}

/** Checkmark while selected, the caller's [icon] otherwise, nothing when there is neither. */
@Composable
private fun FilterChipLeadingIcon(
    selected: Boolean,
    icon: ToolkitIcon?,
) {
    when {
        selected -> Icon(imageVector = Icons.Filled.Check, contentDescription = null)

        icon != null -> AnimatedToolkitIcon(
            icon = icon,
            clickCount = 0,
            contentDescription = null,
            modifier = Modifier.size(size = FilterChipDefaults.IconSize),
        )
    }
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
