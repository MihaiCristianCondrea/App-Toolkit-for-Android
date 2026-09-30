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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.views.preferences

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CallToAction
import androidx.compose.material.icons.outlined.FormatColorFill
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Swipe
import androidx.compose.material.icons.outlined.ViewDay
import androidx.compose.material.icons.outlined.Web
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.ChoicePreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.BackEdgeStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.BannerStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationTint
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.TopBarOverride
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The shell's layout choices as display settings rows: each reads [settings] and writes through
 * [preferences], so the change applies to the whole app at once and persists. The screen decides
 * which to show from the app's graph.
 */
@Stable
internal class ShellDisplayRows(
    private val settings: ShellSettings,
    private val preferences: ShellPreferences,
    private val scope: CoroutineScope,
) {

    @Composable
    fun TopBarStyle(modifier: Modifier) = ChoicePreferenceItem(
        title = stringResource(R.string.shell_top_bar),
        options = TopBarOverride.entries,
        selected = settings.topBarOverride,
        optionLabel = { topBarLabel(it) },
        onSelect = { scope.launch { preferences.setTopBarOverride(it) } },
        dialogIcon = Icons.Outlined.Web,
        modifier = modifier,
    )

    @Composable
    fun HideTopBarOnScroll(modifier: Modifier) = SwitchPreferenceItem(
        modifier = modifier,
        title = stringResource(R.string.shell_hide_top_bar),
        summary = stringResource(R.string.shell_hide_top_bar_summary),
        checked = settings.hideTopBarOnScroll,
        onCheckedChange = { scope.launch { preferences.setHideTopBarOnScroll(it) } },
    )

    @Composable
    fun NavigationTint(modifier: Modifier) = ChoicePreferenceItem(
        title = stringResource(R.string.shell_navigation_tint),
        options = NavigationTint.entries,
        selected = settings.navigationTint,
        optionLabel = { navigationTintLabel(it) },
        onSelect = { scope.launch { preferences.setNavigationTint(it) } },
        dialogIcon = Icons.Outlined.FormatColorFill,
        modifier = modifier,
    )

    @Composable
    fun ContentWidth(modifier: Modifier) = SwitchPreferenceItem(
        modifier = modifier,
        title = stringResource(R.string.shell_limit_width),
        summary = stringResource(R.string.shell_limit_width_summary),
        checked = settings.limitContentWidth,
        onCheckedChange = { scope.launch { preferences.setLimitContentWidth(it) } },
    )

    @Composable
    fun BannerStyle(modifier: Modifier) = ChoicePreferenceItem(
        title = stringResource(R.string.shell_banner_style),
        options = BannerStyle.entries,
        selected = settings.bannerStyle,
        optionLabel = { bannerStyleLabel(it) },
        onSelect = { scope.launch { preferences.setBannerStyle(it) } },
        dialogIcon = Icons.Outlined.ViewDay,
        modifier = modifier,
    )

    @Composable
    fun NavigationBarStyle(modifier: Modifier) = ChoicePreferenceItem(
        title = stringResource(R.string.shell_navigation_bar),
        options = NavigationBarStyle.entries,
        selected = settings.navigationBarStyle,
        optionLabel = { navigationBarLabel(it) },
        onSelect = { scope.launch { preferences.setNavigationBarStyle(it) } },
        dialogIcon = Icons.Outlined.CallToAction,
        modifier = modifier,
    )

    @Composable
    fun HideBottomBarOnScroll(modifier: Modifier) = SwitchPreferenceItem(
        modifier = modifier,
        title = stringResource(R.string.shell_hide_bottom_bar),
        summary = stringResource(R.string.shell_hide_bottom_bar_summary),
        checked = settings.hideBottomBarOnScroll,
        onCheckedChange = { scope.launch { preferences.setHideBottomBarOnScroll(it) } },
    )

    @Composable
    fun TabTransition(modifier: Modifier) = ChoicePreferenceItem(
        title = stringResource(R.string.shell_tab_transition),
        options = TabTransitionStyle.entries,
        selected = settings.tabTransition,
        optionLabel = { tabTransitionLabel(it) },
        onSelect = { scope.launch { preferences.setTabTransition(it) } },
        dialogIcon = Icons.Outlined.SwapHoriz,
        modifier = modifier,
    )

    @Composable
    fun BackEdge(modifier: Modifier) = ChoicePreferenceItem(
        title = stringResource(R.string.shell_back_edge),
        options = BackEdgeStyle.entries,
        selected = settings.backEdgeStyle,
        optionLabel = { backEdgeLabel(it) },
        onSelect = { scope.launch { preferences.setBackEdgeStyle(it) } },
        dialogIcon = Icons.Outlined.Swipe,
        modifier = modifier,
    )
}

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
private fun bannerStyleLabel(value: BannerStyle): String = stringResource(
    when (value) {
        BannerStyle.Automatic -> R.string.shell_banner_automatic
        BannerStyle.Floating -> R.string.shell_banner_floating
        BannerStyle.Docked -> R.string.shell_banner_docked
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
