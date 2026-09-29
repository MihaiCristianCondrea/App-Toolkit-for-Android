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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.CallToAction
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.FormatColorFill
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Swipe
import androidx.compose.material.icons.outlined.VerticalAlignBottom
import androidx.compose.material.icons.outlined.ViewColumn
import androidx.compose.material.icons.outlined.ViewDay
import androidx.compose.material.icons.outlined.Web
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.isSpecified
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs.BasicAlertDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.RadioButtonPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalShellLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AccessoryMode
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AnimationSpeed
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.BackEdgeStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.BannerStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationTint
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.TopBarOverride
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

/**
 * Every variation of the shell, switchable while the app runs, and a live readout of the state it
 * is in. Changes apply at once and persist, so the app can be restarted into a variation.
 *
 * It reads the shell's own locals, so it only works as a page of a `ShellHost`. The page comes with
 * `toolkitGraph { }` from `:library:apptoolkit`, which registers it for `DeveloperOptionsRoute`.
 */
@Composable
fun DeveloperOptionsScreen() {
    val settings = LocalShellSettings.current
    val preferences = LocalShellPreferences.current
    val layout = LocalShellLayout.current
    val navigator = LocalShellNavigator.current
    val graph = LocalShellGraph.current
    val scope = rememberCoroutineScope()

    val windowSummary = stringResource(
        R.string.shell_dev_window_summary,
        layout.windowWidth.value.roundToInt(),
        layout.windowHeight.value.roundToInt(),
        layoutModeLabel(layout.mode),
    )
    val tabStacks = graph.tabs.mapIndexed { index, tab ->
        StateLine(
            title = stringResource(R.string.shell_dev_tab_stack, stringResource(tab.label)),
            summary = navigator.tabStack(index).describe(),
            selected = index == navigator.currentTabIndex,
        )
    }
    val liveState = listOf(
        StateLine(stringResource(R.string.shell_dev_window), windowSummary),
        StateLine(stringResource(R.string.shell_dev_page_stack), navigator.pages.describe()),
    ) + tabStacks

    val navigation: List<@Composable (Modifier) -> Unit> = listOf(
        { modifier ->
            ChoiceItem(
                title = stringResource(R.string.shell_dev_start),
                icon = Icons.Outlined.RocketLaunch,
                options = listOf(-1) + graph.startOptions.indices,
                selected = settings.startOverride.takeIf { it in graph.startOptions.indices } ?: -1,
                optionLabel = { index -> startLabel(graph, index) },
                onSelect = { scope.launch { preferences.setStartOverride(it) } },
                modifier = modifier,
            )
        },
        { modifier ->
            ChoiceItem(
                title = stringResource(R.string.shell_dev_layout),
                icon = Icons.Outlined.Dashboard,
                options = ShellLayoutMode.entries,
                selected = settings.layoutMode,
                optionLabel = { layoutModeLabel(it) },
                onSelect = { scope.launch { preferences.setLayoutMode(it) } },
                modifier = modifier,
            )
        },
        { modifier ->
            ChoiceItem(
                title = stringResource(R.string.shell_dev_top_bar),
                icon = Icons.Outlined.Web,
                options = TopBarOverride.entries,
                selected = settings.topBarOverride,
                optionLabel = { topBarLabel(it) },
                onSelect = { scope.launch { preferences.setTopBarOverride(it) } },
                modifier = modifier,
            )
        },
        { modifier ->
            ChoiceItem(
                title = stringResource(R.string.shell_dev_navigation_bar),
                icon = Icons.Outlined.CallToAction,
                options = NavigationBarStyle.entries,
                selected = settings.navigationBarStyle,
                optionLabel = { navigationBarLabel(it) },
                onSelect = { scope.launch { preferences.setNavigationBarStyle(it) } },
                modifier = modifier,
            )
        },
        { modifier ->
            ChoiceItem(
                title = stringResource(R.string.shell_dev_navigation_tint),
                icon = Icons.Outlined.FormatColorFill,
                options = NavigationTint.entries,
                selected = settings.navigationTint,
                optionLabel = { navigationTintLabel(it) },
                onSelect = { scope.launch { preferences.setNavigationTint(it) } },
                modifier = modifier,
            )
        },
        { modifier ->
            SwitchPreferenceItem(
                modifier = modifier,
                icon = Icons.Outlined.VerticalAlignBottom,
                title = stringResource(R.string.shell_dev_hide_bottom_bar),
                summary = stringResource(R.string.shell_dev_hide_bottom_bar_summary),
                checked = settings.hideBottomBarOnScroll,
                onCheckedChange = { scope.launch { preferences.setHideBottomBarOnScroll(it) } },
            )
        },
        { modifier ->
            SwitchPreferenceItem(
                modifier = modifier,
                icon = Icons.Outlined.ViewColumn,
                title = stringResource(R.string.shell_dev_limit_width),
                summary = if (layout.contentMaxWidth.isSpecified || !settings.limitContentWidth) {
                    stringResource(R.string.shell_dev_limit_width_summary)
                } else {
                    stringResource(R.string.shell_dev_limit_width_unset)
                },
                checked = settings.limitContentWidth,
                onCheckedChange = { scope.launch { preferences.setLimitContentWidth(it) } },
            )
        },
    )

    val accessories: List<@Composable (Modifier) -> Unit> = listOf(
        { modifier ->
            ChoiceItem(
                title = stringResource(R.string.shell_dev_bottom_accessory),
                icon = Icons.Outlined.SmartDisplay,
                options = AccessoryMode.entries,
                selected = settings.accessoryMode,
                optionLabel = { accessoryLabel(it) },
                onSelect = { scope.launch { preferences.setAccessoryMode(it) } },
                modifier = modifier,
            )
        },
        { modifier ->
            ChoiceItem(
                title = stringResource(R.string.shell_dev_banner_style),
                icon = Icons.Outlined.ViewDay,
                options = BannerStyle.entries,
                selected = settings.bannerStyle,
                optionLabel = { bannerStyleLabel(it) },
                onSelect = { scope.launch { preferences.setBannerStyle(it) } },
                modifier = modifier,
            )
        },
    )

    val motion: List<@Composable (Modifier) -> Unit> = listOf(
        { modifier ->
            ChoiceItem(
                title = stringResource(R.string.shell_dev_tab_transition),
                icon = Icons.Outlined.SwapHoriz,
                options = TabTransitionStyle.entries,
                selected = settings.tabTransition,
                optionLabel = { tabTransitionLabel(it) },
                onSelect = { scope.launch { preferences.setTabTransition(it) } },
                modifier = modifier,
            )
        },
        { modifier ->
            ChoiceItem(
                title = stringResource(R.string.shell_dev_back_edge),
                icon = Icons.Outlined.Swipe,
                options = BackEdgeStyle.entries,
                selected = settings.backEdgeStyle,
                optionLabel = { backEdgeLabel(it) },
                onSelect = { scope.launch { preferences.setBackEdgeStyle(it) } },
                modifier = modifier,
            )
        },
        { modifier ->
            ChoiceItem(
                title = stringResource(R.string.shell_dev_animation_speed),
                icon = Icons.Outlined.Animation,
                options = AnimationSpeed.entries,
                selected = settings.animationSpeed,
                optionLabel = { animationSpeedLabel(it) },
                onSelect = { scope.launch { preferences.setAnimationSpeed(it) } },
                modifier = modifier,
            )
        },
    )

    val categoryLiveState = stringResource(R.string.shell_dev_live_state)
    val categoryNavigation = stringResource(R.string.shell_dev_navigation)
    val categoryAccessories = stringResource(R.string.shell_dev_accessories)
    val categoryMotion = stringResource(R.string.shell_dev_motion)
    val resetTitle = stringResource(R.string.shell_dev_reset)
    val resetSummary = stringResource(R.string.shell_dev_reset_summary)

    LazyColumn(
        contentPadding = contentPadding(PaddingValues(bottom = SizeConstants.LargeSize)),
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        item { PreferenceCategoryItem(title = categoryLiveState) }
        itemsIndexed(liveState) { index, line ->
            StateItem(line, Modifier.grouped(groupedItemPosition(index, liveState.size)))
        }
        group(categoryNavigation, navigation)
        group(categoryAccessories, accessories)
        group(categoryMotion, motion)
        item {
            SettingsPreferenceItem(
                modifier = Modifier
                    .padding(top = SizeConstants.LargeSize)
                    .grouped(GroupedItemPosition.SINGLE),
                icon = Icons.Outlined.RestartAlt,
                title = resetTitle,
                summary = resetSummary,
                onClick = { scope.launch { preferences.resetDeveloperOptions() } },
            )
        }
    }
}

private fun LazyListScope.group(title: String, rows: List<@Composable (Modifier) -> Unit>) {
    item { PreferenceCategoryItem(title = title) }
    itemsIndexed(rows) { index, row -> row(Modifier.grouped(groupedItemPosition(index, rows.size))) }
}

private fun Modifier.grouped(position: GroupedItemPosition): Modifier =
    groupedPreferenceItem(position = position, outerRadius = SizeConstants.LargeMediumSize)

/** One read-only line of the live state; [selected] marks the tab on screen. */
private data class StateLine(val title: String, val summary: String, val selected: Boolean = false)

@Composable
private fun StateItem(line: StateLine, modifier: Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = if (line.selected) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        } else {
            CardDefaults.cardColors()
        },
    ) {
        Column(modifier = Modifier.padding(all = SizeConstants.LargeSize)) {
            Text(
                text = line.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(text = line.summary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

/** A row showing the chosen option, which opens a dialog listing all of [options]. */
@Composable
private fun <T> ChoiceItem(
    title: String,
    icon: ImageVector,
    options: List<T>,
    selected: T,
    optionLabel: @Composable (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier,
) {
    var open by rememberSaveable { mutableStateOf(false) }
    SettingsPreferenceItem(
        modifier = modifier,
        icon = icon,
        title = title,
        summary = optionLabel(selected),
        onClick = { open = true },
    )
    if (open) {
        BasicAlertDialog(
            onDismiss = { open = false },
            onConfirm = { open = false },
            icon = icon,
            title = title,
            showDismissButton = false,
            confirmButtonText = stringResource(CoreUiR.string.done_button_content_description),
            content = {
                Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)) {
                    options.forEachIndexed { index, option ->
                        RadioButtonPreferenceItem(
                            modifier = Modifier.groupedPreferenceItem(
                                position = groupedItemPosition(index, options.size),
                                outerRadius = SizeConstants.LargeMediumSize,
                                horizontalPadding = SizeConstants.ZeroSize,
                            ),
                            text = optionLabel(option),
                            isChecked = option == selected,
                            onCheckedChange = {
                                onSelect(option)
                                open = false
                            },
                        )
                    }
                }
            },
        )
    }
}

private fun List<NavKey>.describe(): String = joinToString(separator = "  ›  ") { key ->
    key.toString().substringAfterLast('.').substringBefore('@').substringBefore('(')
}

@Composable
private fun layoutModeLabel(mode: ShellLayoutMode): String = stringResource(
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
private fun accessoryLabel(value: AccessoryMode): String = stringResource(
    when (value) {
        AccessoryMode.AsDeclared -> R.string.shell_accessory_as_declared
        AccessoryMode.None -> R.string.shell_accessory_none
        AccessoryMode.BannerOnly -> R.string.shell_accessory_banner
        AccessoryMode.PlayerOnly -> R.string.shell_accessory_player
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
private fun animationSpeedLabel(value: AnimationSpeed): String = stringResource(
    when (value) {
        AnimationSpeed.Normal -> R.string.shell_speed_normal
        AnimationSpeed.Slow -> R.string.shell_speed_slow
        AnimationSpeed.VerySlow -> R.string.shell_speed_very_slow
    },
)
