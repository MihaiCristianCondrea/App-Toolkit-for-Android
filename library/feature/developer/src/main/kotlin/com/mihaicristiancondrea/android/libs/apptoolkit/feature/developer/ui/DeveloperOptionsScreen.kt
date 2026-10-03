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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperOption
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.developerOptions
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.views.DeveloperOptionItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.views.layoutModeLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.LocalShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalShellLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellSettings
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * The shell's laboratory: a live readout of the state it is in, and every override of its layout,
 * bars, navigation, motion and accessories that acts on something in this app, from
 * `developerOptions`. Changes apply at once and persist, so the app can be restarted into a
 * variation, and Reset puts every one back as the app declares it.
 *
 * It reads the shell's own locals, so it only works as a page of a `ShellHost`. The page comes with
 * `developerOptionsPage()`, which `toolkitGraph { }` in `:library:apptoolkit` calls. The settings
 * search does not list these options: the page is reached once unlocked.
 */
@Composable
fun DeveloperOptionsScreen() {
    val settings = LocalShellSettings.current
    val preferences = LocalShellPreferences.current
    val layout = LocalShellLayout.current
    val navigator = LocalShellNavigator.current
    val graph = LocalShellGraph.current
    val capabilities = LocalShellCapabilities.current
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
    val categories: List<Pair<DeveloperCategory, List<DeveloperOption>>> = remember(capabilities) {
        developerOptions(capabilities).groupBy { it.category }.toList()
    }

    val categoryLiveState = stringResource(R.string.shell_dev_live_state)
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
        categories.forEach { (category, options) ->
            item(key = "category_${category.name}") { PreferenceCategoryItem(title = stringResource(category.title)) }
            itemsIndexed(options, key = { _, option -> "option_${option.name}" }) { index, option ->
                DeveloperOptionItem(
                    option = option,
                    settings = settings,
                    preferences = preferences,
                    scope = scope,
                    graph = graph,
                    capabilities = capabilities,
                    modifier = Modifier.grouped(groupedItemPosition(index, options.size)),
                )
            }
        }
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