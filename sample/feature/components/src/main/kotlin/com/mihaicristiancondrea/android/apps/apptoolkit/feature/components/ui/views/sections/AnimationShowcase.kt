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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.sections

import android.view.SoundEffectConstants
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSection
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSurface
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.AnimatedToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconLoopTrigger
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconReplayMode
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonIconPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dropdown.CommonDropdownMenuItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.R as DesignSystemR

private val animationSamples = listOf(
    "anim_check" to DesignSystemR.drawable.anim_check,
    "anim_grid" to DesignSystemR.drawable.anim_grid,
    "anim_grid_select" to DesignSystemR.drawable.anim_grid_select,
    "anim_settings" to DesignSystemR.drawable.anim_settings,
    "anim_share" to DesignSystemR.drawable.anim_share,
    "anim_edit" to DesignSystemR.drawable.anim_edit,
    "anim_graphic_eq" to DesignSystemR.drawable.anim_graphic_eq,
    "anim_language" to DesignSystemR.drawable.anim_language,
    "anim_alarm" to DesignSystemR.drawable.anim_alarm,
    "anim_stopwatch" to DesignSystemR.drawable.anim_stopwatch,
    "anim_timer" to DesignSystemR.drawable.anim_timer,
    "anim_queue_music" to DesignSystemR.drawable.anim_queue_music,
    "anim_visibility_strike" to DesignSystemR.drawable.anim_visibility_strike,
)

private val replayModes: ImmutableList<ToolkitIconReplayMode> =
    persistentListOf(ToolkitIconReplayMode.Restart, ToolkitIconReplayMode.Reverse)

private val loopTriggers: ImmutableList<ToolkitIconLoopTrigger> =
    persistentListOf(ToolkitIconLoopTrigger.OnInteraction, ToolkitIconLoopTrigger.Immediately)

/**
 * Tap-driven previews of every bundled animation, above one compact block of playback controls.
 *
 * The three controls are the three independent decisions a `ToolkitIcon.Animated` makes, which is
 * why the replay mode is a menu rather than a switch: it names a cycle shape, so a switch would have
 * to be labelled after only one of the two. Loop stays a checkbox because it is a yes/no, and it
 * reveals the trigger menu, which is meaningless while nothing loops.
 *
 * Changing any of them resets the previews to their initial frame.
 */
@Composable
fun AnimationShowcase() {
    var replayMode: ToolkitIconReplayMode by rememberSaveable {
        mutableStateOf(value = ToolkitIconReplayMode.Restart)
    }
    var loop: Boolean by rememberSaveable { mutableStateOf(value = false) }
    var loopTrigger: ToolkitIconLoopTrigger by rememberSaveable {
        mutableStateOf(value = ToolkitIconLoopTrigger.OnInteraction)
    }
    val hapticFeedback = LocalHapticFeedback.current
    val view = LocalView.current

    ShowcaseHeader(
        title = stringResource(R.string.components_section_animations),
        icon = Icons.Outlined.Animation,
    )
    ShowcaseSection {
        Text(
            text = stringResource(R.string.components_animation_preview_helper),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                start = SizeConstants.SmallSize,
                bottom = SizeConstants.ExtraSmallSize,
            ),
        )
        ShowcaseSurface(
            position = GroupedItemPosition.FIRST,
            contentPadding = PaddingValues(
                horizontal = SizeConstants.LargeSize,
                vertical = SizeConstants.SmallSize,
            ),
        ) {
            val replayLabel = stringResource(R.string.components_animation_replay)
            AnimationControlRow(label = replayLabel) {
                AnimationOptionMenu(
                    controlLabel = replayLabel,
                    selected = replayMode,
                    options = replayModes,
                    optionLabel = { mode -> stringResource(id = mode.labelResId()) },
                    onOptionSelected = { replayMode = it },
                )
            }
            AnimationControlRow(
                label = stringResource(R.string.components_animation_loop),
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    hapticFeedback.performHapticFeedback(
                        if (loop) HapticFeedbackType.ToggleOff else HapticFeedbackType.ToggleOn,
                    )
                    loop = !loop
                },
            ) {
                Checkbox(checked = loop, onCheckedChange = { loop = it })
            }
            AnimatedVisibility(visible = loop) {
                val startLabel = stringResource(R.string.components_animation_loop_start)
                AnimationControlRow(label = startLabel) {
                    AnimationOptionMenu(
                        controlLabel = startLabel,
                        selected = loopTrigger,
                        options = loopTriggers,
                        optionLabel = { trigger -> stringResource(id = trigger.labelResId()) },
                        onOptionSelected = { loopTrigger = it },
                    )
                }
            }
        }
        animationSamples.forEachIndexed { index, (name, resource) ->
            val position = if (index == animationSamples.lastIndex) {
                GroupedItemPosition.LAST
            } else {
                GroupedItemPosition.MIDDLE
            }
            key(name, replayMode, loop, loopTrigger) {
                AnimationPreviewCard(
                    name = name,
                    resource = resource,
                    position = position,
                    replayMode = replayMode,
                    loop = loop,
                    loopTrigger = loopTrigger,
                )
            }
        }
    }
}

/** Label of the replay mode as it is offered in the menu. */
private fun ToolkitIconReplayMode.labelResId(): Int = when (this) {
    ToolkitIconReplayMode.Restart -> R.string.components_animation_replay_restart
    ToolkitIconReplayMode.Reverse -> R.string.components_animation_replay_reverse
}

/** Label of the loop trigger as it is offered in the menu. */
private fun ToolkitIconLoopTrigger.labelResId(): Int = when (this) {
    ToolkitIconLoopTrigger.Immediately -> R.string.components_animation_loop_start_immediately
    ToolkitIconLoopTrigger.OnInteraction -> R.string.components_animation_loop_start_on_tap
}

/**
 * One line of the controls block: what the control is on the left, the control itself on the right.
 *
 * [onClick] makes the whole line toggle the control, for the ones a tap anywhere can operate.
 */
@Composable
private fun AnimationControlRow(
    label: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    control: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = SizeConstants.FortyFourSize)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f),
        )
        control()
    }
}

/**
 * The value picker of one control: a button showing the current option, and the menu of all of them.
 *
 * [controlLabel] only names the control for screen readers, which would otherwise hear the value
 * without knowing what it sets.
 */
@Composable
private fun <T> AnimationOptionMenu(
    controlLabel: String,
    selected: T,
    options: ImmutableList<T>,
    optionLabel: @Composable (T) -> String,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded: Boolean by rememberSaveable { mutableStateOf(value = false) }
    val selectedLabel: String = optionLabel(selected)

    Box(modifier = modifier) {
        GeneralButton(
            onClick = { expanded = true },
            style = GeneralButtonStyle.Tonal,
            label = selectedLabel,
            icon = ToolkitIcon.Vector(imageVector = Icons.Rounded.ArrowDropDown),
            iconPosition = ButtonIconPosition.End,
            contentDescription = "$controlLabel: $selectedLabel",
        )
        DropdownMenu(
            expanded = expanded,
            shape = MaterialTheme.shapes.largeIncreased,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                CommonDropdownMenuItem(
                    text = optionLabel(option),
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun AnimationPreviewCard(
    name: String,
    @DrawableRes resource: Int,
    position: GroupedItemPosition,
    replayMode: ToolkitIconReplayMode,
    loop: Boolean,
    loopTrigger: ToolkitIconLoopTrigger,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val view = LocalView.current
    var clickCount by rememberSaveable(replayMode, loop, loopTrigger) { mutableIntStateOf(0) }

    val icon = remember(resource, replayMode, loop, loopTrigger) {
        ToolkitIcon.AnimatedVector(
            resId = resource,
            atEnd = resource == DesignSystemR.drawable.anim_check,
            replayMode = replayMode,
            loop = loop,
            loopTrigger = loopTrigger,
        )
    }

    ShowcaseSurface(
        position = position,
        modifier = modifier,
        onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
            clickCount++
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
            )
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(52.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AnimatedToolkitIcon(
                        icon = icon,
                        clickCount = clickCount,
                        contentDescription = name,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }
    }
}
