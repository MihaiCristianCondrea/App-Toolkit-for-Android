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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSection
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSurface
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconReplayMode
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
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
)

/** Finite, click-driven previews. Changing replay mode resets the preview to its initial frame. */
@Composable
fun AnimationShowcase() {
    var reverse by rememberSaveable { mutableStateOf(false) }
    val replayMode = if (reverse) ToolkitIconReplayMode.Reverse else ToolkitIconReplayMode.Restart
    ShowcaseHeader(
        title = stringResource(R.string.components_section_animations),
        icon = Icons.Outlined.Animation,
    )
    ShowcaseSection {
        ShowcaseSurface(position = GroupedItemPosition.FIRST) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize)) {
                FilterChip(
                    selected = !reverse,
                    onClick = { reverse = false },
                    label = { Text(stringResource(R.string.components_animation_restart)) },
                )
                FilterChip(
                    selected = reverse,
                    onClick = { reverse = true },
                    label = { Text(stringResource(R.string.components_animation_reverse)) },
                )
            }
        }
        animationSamples.forEachIndexed { index, (name, resource) ->
            ShowcaseSurface(
                position = if (index == animationSamples.lastIndex) GroupedItemPosition.LAST else GroupedItemPosition.MIDDLE,
            ) {
                Text(text = name, style = MaterialTheme.typography.titleSmall)
                SmallVerticalSpacer()
                key(replayMode) {
                    // Check starts with an empty path; show its completed frame so icon-only controls remain visible.
                    val icon = ToolkitIcon.AnimatedVector(
                        resource,
                        atEnd = resource == DesignSystemR.drawable.anim_check,
                        replayMode = replayMode,
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
                        verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
                    ) {
                        GeneralButton(onClick = {}, label = name, icon = icon)
                        GeneralButton(onClick = {}, style = GeneralButtonStyle.Tonal, icon = icon,
                            contentDescription = name)
                        GeneralButton(onClick = {}, style = GeneralButtonStyle.Text, icon = icon,
                            contentDescription = name)
                    }
                }
            }
        }
    }
}
