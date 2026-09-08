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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Animation
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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconReplayMode
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.switches.CustomSwitch
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
)

/** Finite, click-driven previews. Changing replay mode resets the preview to its initial frame. */
@Composable
fun AnimationShowcase() {
    var reverse by rememberSaveable { mutableStateOf(false) }
    val replayMode = if (reverse) ToolkitIconReplayMode.Reverse else ToolkitIconReplayMode.Restart
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
            onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(
                    if (reverse) HapticFeedbackType.ToggleOff else HapticFeedbackType.ToggleOn,
                )
                reverse = !reverse
            },
        ) {
            AnimationInteractionRow(
                reverse = reverse,
                onReverseChange = { reverse = it },
            )
        }
        animationSamples.forEachIndexed { index, (name, resource) ->
            val position = if (index == animationSamples.lastIndex) {
                GroupedItemPosition.LAST
            } else {
                GroupedItemPosition.MIDDLE
            }
            key(name, replayMode) {
                AnimationPreviewCard(
                    name = name,
                    resource = resource,
                    position = position,
                    replayMode = replayMode,
                )
            }
        }
    }
}

@Composable
private fun AnimationInteractionRow(
    reverse: Boolean,
    onReverseChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.components_animation_reverse),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        CustomSwitch(
            checked = reverse,
            onCheckedChange = onReverseChange,
        )
    }
}

@Composable
private fun AnimationPreviewCard(
    name: String,
    @DrawableRes resource: Int,
    position: GroupedItemPosition,
    replayMode: ToolkitIconReplayMode,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val view = LocalView.current
    var clickCount by rememberSaveable(replayMode) { mutableIntStateOf(0) }

    val icon = remember(resource, replayMode) {
        ToolkitIcon.AnimatedVector(
            resId = resource,
            atEnd = resource == DesignSystemR.drawable.anim_check,
            replayMode = replayMode,
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
