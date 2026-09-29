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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.ChoicePreferenceItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalShellLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AccessoryMode
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AnimationSpeed
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellSettings
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Every variation of the shell, switchable while the app runs, and a live readout of the state it
 * is in. Changes apply at once and persist, so the app can be restarted into a variation.
 *
 * It reads the shell's own locals, so it only works as a page of a `ShellHost`. The page comes with
 * `developerOptionsPage()`, which `toolkitGraph { }` in `:library:apptoolkit` calls.
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

    // Only what the app's graph gives the options something to act on.
    val navigation: List<@Composable (Modifier) -> Unit> = buildList {
        if (graph.startOptions.size > 1) {
            add { modifier ->
                ChoicePreferenceItem(
                    title = stringResource(R.string.shell_dev_start),
                    options = listOf(-1) + graph.startOptions.indices,
                    selected = settings.startOverride.takeIf { it in graph.startOptions.indices } ?: -1,
                    optionLabel = { index -> startLabel(graph, index) },
                    onSelect = { scope.launch { preferences.setStartOverride(it) } },
                    modifier = modifier,
                )
            }
        }
        add { modifier ->
            ChoicePreferenceItem(
                title = stringResource(R.string.shell_dev_layout),
                options = ShellLayoutMode.entries,
                selected = settings.layoutMode,
                optionLabel = { layoutModeLabel(it) },
                onSelect = { scope.launch { preferences.setLayoutMode(it) } },
                modifier = modifier,
            )
        }
    }

    val accessories: List<@Composable (Modifier) -> Unit> = buildList {
        if (graph.banner != null || graph.player != null) {
            // Only the accessories the app declares: with one of the two, showing it or not is
            // the whole choice, and a player option in an app without a player does nothing.
            val options = if (graph.banner != null && graph.player != null) {
                AccessoryMode.entries
            } else {
                listOf(AccessoryMode.AsDeclared, AccessoryMode.None)
            }
            add { modifier ->
                ChoicePreferenceItem(
                    title = stringResource(R.string.shell_dev_bottom_accessory),
                    options = options,
                    selected = settings.accessoryMode,
                    optionLabel = { accessoryLabel(it) },
                    onSelect = { scope.launch { preferences.setAccessoryMode(it) } },
                    modifier = modifier,
                )
            }
        }
    }

    val motion: List<@Composable (Modifier) -> Unit> = listOf(
        { modifier ->
            ChoicePreferenceItem(
                title = stringResource(R.string.shell_dev_animation_speed),
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
                title = resetTitle,
                summary = resetSummary,
                onClick = { scope.launch { preferences.resetDeveloperOptions() } },
            )
        }
    }
}

private fun LazyListScope.group(title: String, rows: List<@Composable (Modifier) -> Unit>) {
    if (rows.isEmpty()) return
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
private fun accessoryLabel(value: AccessoryMode): String = stringResource(
    when (value) {
        AccessoryMode.AsDeclared -> R.string.shell_accessory_as_declared
        AccessoryMode.None -> R.string.shell_accessory_none
        AccessoryMode.BannerOnly -> R.string.shell_accessory_banner
        AccessoryMode.PlayerOnly -> R.string.shell_accessory_player
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
private fun animationSpeedLabel(value: AnimationSpeed): String = stringResource(
    when (value) {
        AnimationSpeed.Normal -> R.string.shell_speed_normal
        AnimationSpeed.Slow -> R.string.shell_speed_slow
        AnimationSpeed.VerySlow -> R.string.shell_speed_very_slow
    },
)
