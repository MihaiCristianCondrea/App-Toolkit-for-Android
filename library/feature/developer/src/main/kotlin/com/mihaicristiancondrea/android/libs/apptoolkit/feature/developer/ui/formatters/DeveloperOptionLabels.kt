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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.MotionTempo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.EntranceReplay
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.EntranceStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AccessoryMode
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AnimationSpeed
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.BackEdgeStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationTint
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.TopBarOverride

@Composable
internal fun layoutModeLabel(mode: ShellLayoutMode): String = stringResource(
    id = when (mode) {
        ShellLayoutMode.Auto -> R.string.shell_layout_auto
        ShellLayoutMode.BottomBar -> R.string.shell_layout_bottom_bar
        ShellLayoutMode.Rail -> R.string.shell_layout_rail
        ShellLayoutMode.ExpandedRail -> R.string.shell_layout_expanded_rail
        ShellLayoutMode.PermanentDrawer -> R.string.shell_layout_permanent_drawer
    },
)

@Composable
internal fun topBarLabel(value: TopBarOverride): String = stringResource(
    id = when (value) {
        TopBarOverride.AsDeclared -> R.string.shell_as_declared
        TopBarOverride.Small -> R.string.shell_top_bar_small
        TopBarOverride.CenterAligned -> R.string.shell_top_bar_center
        TopBarOverride.Large -> R.string.shell_top_bar_large
        TopBarOverride.LargeCollapsed -> R.string.shell_top_bar_large_collapsed
        TopBarOverride.Hidden -> R.string.shell_top_bar_hidden
    },
)

@Composable
internal fun navigationBarLabel(value: NavigationBarStyle): String = stringResource(
    id = when (value) {
        NavigationBarStyle.Standard -> R.string.shell_navigation_bar_standard
        NavigationBarStyle.Short -> R.string.shell_navigation_bar_short
    },
)

@Composable
internal fun navigationTintLabel(value: NavigationTint): String = stringResource(
    id = when (value) {
        NavigationTint.None -> R.string.shell_navigation_tint_none
        NavigationTint.Always -> R.string.shell_navigation_tint_always
        NavigationTint.OnScroll -> R.string.shell_navigation_tint_on_scroll
    },
)

@Composable
internal fun backEdgeLabel(value: BackEdgeStyle): String = stringResource(
    id = when (value) {
        BackEdgeStyle.System -> R.string.shell_back_edge_system
        BackEdgeStyle.FollowFinger -> R.string.shell_back_edge_follow
    },
)

@Composable
internal fun tabTransitionLabel(value: TabTransitionStyle): String = stringResource(
    id = when (value) {
        TabTransitionStyle.Directional -> R.string.shell_transition_directional
        TabTransitionStyle.FadeThrough -> R.string.shell_transition_fade_through
        TabTransitionStyle.Fade -> R.string.shell_transition_fade
        TabTransitionStyle.None -> R.string.shell_transition_none
    },
)

@Composable
internal fun accessoryLabel(value: AccessoryMode, capabilities: ShellCapabilities): String = stringResource(
    id = when (value) {
        AccessoryMode.AsDeclared -> when {
            capabilities.hasBanner && capabilities.hasPlayer -> R.string.shell_accessory_as_declared
            capabilities.hasPlayer -> R.string.shell_accessory_player
            else -> R.string.shell_accessory_banner
        }
        AccessoryMode.None -> R.string.shell_accessory_none
        AccessoryMode.BannerOnly -> R.string.shell_accessory_banner
        AccessoryMode.PlayerOnly -> R.string.shell_accessory_player
    },
)

@Composable
internal fun animationSpeedLabel(value: AnimationSpeed): String = stringResource(
    id = when (value) {
        AnimationSpeed.Normal -> R.string.shell_speed_normal
        AnimationSpeed.Slow -> R.string.shell_speed_slow
        AnimationSpeed.VerySlow -> R.string.shell_speed_very_slow
    },
)

/** A start option: a tab by its label, a start screen by its title, or the app's own choice. */
@Composable
internal fun startLabel(graph: ShellGraph, index: Int): String {
    val key = graph.startOptions.getOrNull(index = index)
        ?: return stringResource(
            id = R.string.shell_start_as_declared,
            startLabelOf(graph = graph, key = graph.start),
        )
    return startLabelOf(graph = graph, key = key)
}

@Composable
private fun startLabelOf(graph: ShellGraph, key: NavKey): String {
    val tab = graph.tabs.firstOrNull { it.key == key }
    return if (tab != null) {
        stringResource(id = tab.label)
    } else {
        val title = graph.destination(key = key).title?.invoke(key) ?: key.toString().substringAfterLast('.')
        stringResource(id = R.string.shell_start_screen, title)
    }
}

@Composable
internal fun entranceStyleLabel(value: EntranceStyle): String = stringResource(
    id = when (value) {
        EntranceStyle.Slide -> R.string.shell_entrance_slide
        EntranceStyle.Scale -> R.string.shell_entrance_scale
        EntranceStyle.Fade -> R.string.shell_entrance_fade
        EntranceStyle.None -> R.string.shell_entrance_none
    },
)

@Composable
internal fun motionTempoLabel(value: MotionTempo): String = stringResource(
    id = when (value) {
        MotionTempo.Calm -> R.string.shell_tempo_calm
        MotionTempo.Standard -> R.string.shell_tempo_standard
        MotionTempo.Brisk -> R.string.shell_tempo_brisk
    },
)

@Composable
internal fun entranceReplayLabel(value: EntranceReplay): String = stringResource(
    id = when (value) {
        EntranceReplay.EveryVisit -> R.string.shell_replay_every_visit
        EntranceReplay.OnOpen -> R.string.shell_replay_on_open
        EntranceReplay.FirstVisit -> R.string.shell_replay_first_visit
    },
)
