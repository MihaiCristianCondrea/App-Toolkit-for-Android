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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.CallToAction
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.FormatColorFill
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Swipe
import androidx.compose.material.icons.outlined.Web
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.MotionTempo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.EntranceReplay
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.EntranceStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.ChoicePreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperOption
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.transformers.accessoryModes
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.layoutModeLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.topBarLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.navigationBarLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.navigationTintLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.backEdgeLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.tabTransitionLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.accessoryLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.animationSpeedLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.startLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.entranceStyleLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.motionTempoLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.entranceReplayLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.contracts.DeveloperOptionsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AnimationSpeed
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.BackEdgeStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationTint
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.TopBarOverride

/**
 * One shell override: it renders [settings] and reports changes through [onChange], so the change applies
 * to the whole app at once and persists.
 */
@Composable
internal fun DeveloperOptionItem(
    option: DeveloperOption,
    settings: ShellSettings,
    onChange: (DeveloperOptionsEvent) -> Unit,
    graph: ShellGraph,
    capabilities: ShellCapabilities,
    modifier: Modifier,
) {
    when (option) {
        DeveloperOption.Layout -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_dev_layout),
            options = ShellLayoutMode.entries,
            selected = settings.layoutMode,
            optionLabel = { layoutModeLabel(it) },
            onSelect = { onChange(DeveloperOptionsEvent.SetLayout(it)) },
            dialogIcon = Icons.Outlined.Dashboard,
            modifier = modifier,
        )

        DeveloperOption.TopBarStyle -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_top_bar),
            options = TopBarOverride.entries,
            selected = settings.topBarOverride,
            optionLabel = { topBarLabel(it) },
            onSelect = { onChange(DeveloperOptionsEvent.SetTopBarStyle(it)) },
            dialogIcon = Icons.Outlined.Web,
            modifier = modifier,
        )

        DeveloperOption.HideTopBarOnScroll -> SwitchPreferenceItem(
            modifier = modifier,
            title = stringResource(R.string.shell_hide_top_bar),
            summary = stringResource(R.string.shell_hide_top_bar_summary),
            checked = settings.hideTopBarOnScroll,
            onCheckedChange = { onChange(DeveloperOptionsEvent.SetHideTopBarOnScroll(it)) },
        )

        DeveloperOption.NavigationBarStyle -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_navigation_bar),
            options = NavigationBarStyle.entries,
            selected = settings.navigationBarStyle,
            optionLabel = { navigationBarLabel(it) },
            onSelect = { onChange(DeveloperOptionsEvent.SetNavigationBarStyle(it)) },
            dialogIcon = Icons.Outlined.CallToAction,
            modifier = modifier,
        )

        DeveloperOption.HideBottomBarOnScroll -> SwitchPreferenceItem(
            modifier = modifier,
            title = stringResource(R.string.shell_hide_bottom_bar),
            summary = stringResource(R.string.shell_hide_bottom_bar_summary),
            checked = settings.hideBottomBarOnScroll,
            onCheckedChange = { onChange(DeveloperOptionsEvent.SetHideBottomBarOnScroll(it)) },
        )

        DeveloperOption.NavigationTint -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_navigation_tint),
            options = NavigationTint.entries,
            selected = settings.navigationTint,
            optionLabel = { navigationTintLabel(it) },
            onSelect = { onChange(DeveloperOptionsEvent.SetNavigationTint(it)) },
            dialogIcon = Icons.Outlined.FormatColorFill,
            modifier = modifier,
        )

        DeveloperOption.ContentWidth -> SwitchPreferenceItem(
            modifier = modifier,
            title = stringResource(R.string.shell_limit_width),
            summary = stringResource(R.string.shell_limit_width_summary),
            checked = settings.limitContentWidth,
            onCheckedChange = { onChange(DeveloperOptionsEvent.SetContentWidth(it)) },
        )

        DeveloperOption.StartOverride -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_dev_start),
            options = listOf(-1) + graph.startOptions.indices,
            selected = settings.startOverride.takeIf { it in graph.startOptions.indices } ?: -1,
            optionLabel = { index -> startLabel(graph, index) },
            onSelect = { onChange(DeveloperOptionsEvent.SetStartOverride(it)) },
            dialogIcon = Icons.Outlined.RocketLaunch,
            modifier = modifier,
        )

        DeveloperOption.TabTransition -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_tab_transition),
            options = TabTransitionStyle.entries,
            selected = settings.tabTransition,
            optionLabel = { tabTransitionLabel(it) },
            onSelect = { onChange(DeveloperOptionsEvent.SetTabTransition(it)) },
            dialogIcon = Icons.Outlined.SwapHoriz,
            modifier = modifier,
        )

        DeveloperOption.BackEdge -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_back_edge),
            options = BackEdgeStyle.entries,
            selected = settings.backEdgeStyle,
            optionLabel = { backEdgeLabel(it) },
            onSelect = { onChange(DeveloperOptionsEvent.SetBackEdge(it)) },
            dialogIcon = Icons.Outlined.Swipe,
            modifier = modifier,
        )

        DeveloperOption.AnimationSpeed -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_dev_animation_speed),
            options = AnimationSpeed.entries,
            selected = settings.animationSpeed,
            optionLabel = { animationSpeedLabel(it) },
            onSelect = { onChange(DeveloperOptionsEvent.SetAnimationSpeed(it)) },
            dialogIcon = Icons.Outlined.Animation,
            modifier = modifier,
        )

        DeveloperOption.EntranceStyle -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_dev_entrance_style),
            options = EntranceStyle.entries,
            selected = settings.entranceStyle,
            optionLabel = { entranceStyleLabel(it) },
            onSelect = { onChange(DeveloperOptionsEvent.SetEntranceStyle(it)) },
            dialogIcon = Icons.Outlined.Animation,
            modifier = modifier,
        )

        DeveloperOption.MotionTempo -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_dev_motion_tempo),
            options = MotionTempo.entries,
            selected = settings.motionTempo,
            optionLabel = { motionTempoLabel(it) },
            onSelect = { onChange(DeveloperOptionsEvent.SetMotionTempo(it)) },
            dialogIcon = Icons.Outlined.Animation,
            modifier = modifier,
        )

        DeveloperOption.EntranceReplay -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_dev_entrance_replay),
            options = EntranceReplay.entries,
            selected = settings.entranceReplay,
            optionLabel = { entranceReplayLabel(it) },
            onSelect = { onChange(DeveloperOptionsEvent.SetEntranceReplay(it)) },
            dialogIcon = Icons.Outlined.Animation,
            modifier = modifier,
        )

        DeveloperOption.BottomAccessory -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_dev_bottom_accessory),
            options = accessoryModes(capabilities),
            selected = settings.accessoryMode,
            optionLabel = { accessoryLabel(it, capabilities) },
            onSelect = { onChange(DeveloperOptionsEvent.SetBottomAccessory(it)) },
            dialogIcon = Icons.Outlined.SmartDisplay,
            modifier = modifier,
        )
    }
}
