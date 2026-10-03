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
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.ChoicePreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperOption
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.accessoryModes
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AccessoryMode
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AnimationSpeed
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.BackEdgeStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationTint
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.TopBarOverride
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * One shell override: it reads [settings] and writes through [preferences], so the change applies
 * to the whole app at once and persists.
 */
@Composable
internal fun DeveloperOptionItem(
    option: DeveloperOption,
    settings: ShellSettings,
    preferences: ShellPreferences,
    scope: CoroutineScope,
    graph: ShellGraph,
    capabilities: ShellCapabilities,
    modifier: Modifier,
) {
    fun write(change: suspend ShellPreferences.() -> Unit) {
        scope.launch { preferences.change() }
    }
    when (option) {
        DeveloperOption.Layout -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_dev_layout),
            options = ShellLayoutMode.entries,
            selected = settings.layoutMode,
            optionLabel = { layoutModeLabel(it) },
            onSelect = { write { setLayoutMode(it) } },
            dialogIcon = Icons.Outlined.Dashboard,
            modifier = modifier,
        )

        DeveloperOption.TopBarStyle -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_top_bar),
            options = TopBarOverride.entries,
            selected = settings.topBarOverride,
            optionLabel = { topBarLabel(it) },
            onSelect = { write { setTopBarOverride(it) } },
            dialogIcon = Icons.Outlined.Web,
            modifier = modifier,
        )

        DeveloperOption.HideTopBarOnScroll -> SwitchPreferenceItem(
            modifier = modifier,
            title = stringResource(R.string.shell_hide_top_bar),
            summary = stringResource(R.string.shell_hide_top_bar_summary),
            checked = settings.hideTopBarOnScroll,
            onCheckedChange = { write { setHideTopBarOnScroll(it) } },
        )

        DeveloperOption.NavigationBarStyle -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_navigation_bar),
            options = NavigationBarStyle.entries,
            selected = settings.navigationBarStyle,
            optionLabel = { navigationBarLabel(it) },
            onSelect = { write { setNavigationBarStyle(it) } },
            dialogIcon = Icons.Outlined.CallToAction,
            modifier = modifier,
        )

        DeveloperOption.HideBottomBarOnScroll -> SwitchPreferenceItem(
            modifier = modifier,
            title = stringResource(R.string.shell_hide_bottom_bar),
            summary = stringResource(R.string.shell_hide_bottom_bar_summary),
            checked = settings.hideBottomBarOnScroll,
            onCheckedChange = { write { setHideBottomBarOnScroll(it) } },
        )

        DeveloperOption.NavigationTint -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_navigation_tint),
            options = NavigationTint.entries,
            selected = settings.navigationTint,
            optionLabel = { navigationTintLabel(it) },
            onSelect = { write { setNavigationTint(it) } },
            dialogIcon = Icons.Outlined.FormatColorFill,
            modifier = modifier,
        )

        DeveloperOption.ContentWidth -> SwitchPreferenceItem(
            modifier = modifier,
            title = stringResource(R.string.shell_limit_width),
            summary = stringResource(R.string.shell_limit_width_summary),
            checked = settings.limitContentWidth,
            onCheckedChange = { write { setLimitContentWidth(it) } },
        )

        DeveloperOption.StartOverride -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_dev_start),
            options = listOf(-1) + graph.startOptions.indices,
            selected = settings.startOverride.takeIf { it in graph.startOptions.indices } ?: -1,
            optionLabel = { index -> startLabel(graph, index) },
            onSelect = { write { setStartOverride(it) } },
            dialogIcon = Icons.Outlined.RocketLaunch,
            modifier = modifier,
        )

        DeveloperOption.TabTransition -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_tab_transition),
            options = TabTransitionStyle.entries,
            selected = settings.tabTransition,
            optionLabel = { tabTransitionLabel(it) },
            onSelect = { write { setTabTransition(it) } },
            dialogIcon = Icons.Outlined.SwapHoriz,
            modifier = modifier,
        )

        DeveloperOption.BackEdge -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_back_edge),
            options = BackEdgeStyle.entries,
            selected = settings.backEdgeStyle,
            optionLabel = { backEdgeLabel(it) },
            onSelect = { write { setBackEdgeStyle(it) } },
            dialogIcon = Icons.Outlined.Swipe,
            modifier = modifier,
        )

        DeveloperOption.AnimationSpeed -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_dev_animation_speed),
            options = AnimationSpeed.entries,
            selected = settings.animationSpeed,
            optionLabel = { animationSpeedLabel(it) },
            onSelect = { write { setAnimationSpeed(it) } },
            dialogIcon = Icons.Outlined.Animation,
            modifier = modifier,
        )

        DeveloperOption.BottomAccessory -> ChoicePreferenceItem(
            title = stringResource(R.string.shell_dev_bottom_accessory),
            options = accessoryModes(capabilities),
            selected = settings.accessoryMode,
            optionLabel = { accessoryLabel(it) },
            onSelect = { write { setAccessoryMode(it) } },
            dialogIcon = Icons.Outlined.SmartDisplay,
            modifier = modifier,
        )
    }
}

@Composable
internal fun layoutModeLabel(mode: ShellLayoutMode): String = stringResource(
    when (mode) {
        ShellLayoutMode.Auto -> R.string.shell_layout_auto
        ShellLayoutMode.BottomBar -> R.string.shell_layout_bottom_bar
        ShellLayoutMode.Rail -> R.string.shell_layout_rail
        ShellLayoutMode.ExpandedRail -> R.string.shell_layout_expanded_rail
        ShellLayoutMode.PermanentDrawer -> R.string.shell_layout_permanent_drawer
    },
)

@Composable
private fun topBarLabel(value: TopBarOverride): String = stringResource(
    when (value) {
        TopBarOverride.AsDeclared -> R.string.shell_as_declared
        TopBarOverride.Small -> R.string.shell_top_bar_small
        TopBarOverride.CenterAligned -> R.string.shell_top_bar_center
        TopBarOverride.Large -> R.string.shell_top_bar_large
        TopBarOverride.LargeCollapsed -> R.string.shell_top_bar_large_collapsed
        TopBarOverride.Hidden -> R.string.shell_top_bar_hidden
    },
)

@Composable
private fun navigationBarLabel(value: NavigationBarStyle): String = stringResource(
    when (value) {
        NavigationBarStyle.Standard -> R.string.shell_navigation_bar_standard
        NavigationBarStyle.Short -> R.string.shell_navigation_bar_short
    },
)

@Composable
private fun navigationTintLabel(value: NavigationTint): String = stringResource(
    when (value) {
        NavigationTint.None -> R.string.shell_navigation_tint_none
        NavigationTint.Always -> R.string.shell_navigation_tint_always
        NavigationTint.OnScroll -> R.string.shell_navigation_tint_on_scroll
    },
)

@Composable
private fun backEdgeLabel(value: BackEdgeStyle): String = stringResource(
    when (value) {
        BackEdgeStyle.System -> R.string.shell_back_edge_system
        BackEdgeStyle.FollowFinger -> R.string.shell_back_edge_follow
    },
)

@Composable
private fun tabTransitionLabel(value: TabTransitionStyle): String = stringResource(
    when (value) {
        TabTransitionStyle.Directional -> R.string.shell_transition_directional
        TabTransitionStyle.FadeThrough -> R.string.shell_transition_fade_through
        TabTransitionStyle.Fade -> R.string.shell_transition_fade
        TabTransitionStyle.None -> R.string.shell_transition_none
    },
)

@Composable
private fun accessoryLabel(value: AccessoryMode): String = stringResource(
    when (value) {
        AccessoryMode.AsDeclared -> R.string.shell_accessory_as_declared
        AccessoryMode.None -> R.string.shell_accessory_none
        AccessoryMode.BannerOnly -> R.string.shell_accessory_banner
        AccessoryMode.PlayerOnly -> R.string.shell_accessory_player
    },
)

@Composable
private fun animationSpeedLabel(value: AnimationSpeed): String = stringResource(
    when (value) {
        AnimationSpeed.Normal -> R.string.shell_speed_normal
        AnimationSpeed.Slow -> R.string.shell_speed_slow
        AnimationSpeed.VerySlow -> R.string.shell_speed_very_slow
    },
)

/** A start option: a tab by its label, a start screen by its title, or the app's own choice. */
@Composable
private fun startLabel(graph: ShellGraph, index: Int): String {
    val key = graph.startOptions.getOrNull(index)
        ?: return stringResource(R.string.shell_start_as_declared, startLabelOf(graph, graph.start))
    return startLabelOf(graph, key)
}

@Composable
private fun startLabelOf(graph: ShellGraph, key: NavKey): String {
    val tab = graph.tabs.firstOrNull { it.key == key }
    if (tab != null) return stringResource(tab.label)
    val title = graph.destination(key).title?.invoke(key) ?: key.toString().substringAfterLast('.')
    return stringResource(R.string.shell_start_screen, title)
}
