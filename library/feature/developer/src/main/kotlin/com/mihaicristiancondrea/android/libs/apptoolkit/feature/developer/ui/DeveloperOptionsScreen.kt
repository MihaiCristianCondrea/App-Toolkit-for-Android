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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItemsIndexed
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperOption
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.utils.availableDeveloperOptions
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.views.DeveloperOptionItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.utils.layoutModeLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.LocalShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalShellLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.contracts.DeveloperOptionChange
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperStateLine
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.views.DeveloperStateItem
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.navigation.developerOptionsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DeveloperOptionsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
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
 * search does not list these options: the page is reached once unlocked. Like every screen, it is a
 * `ScreenHost`, so it enters with the app's entrance once its transition settles.
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
        DeveloperStateLine(
            title = stringResource(R.string.shell_dev_tab_stack, stringResource(tab.label)),
            summary = navigator.tabStack(index).describeNavigationStack(),
            selected = index == navigator.currentTabIndex,
        )
    }
    val liveState = listOf(
        DeveloperStateLine(stringResource(R.string.shell_dev_window), windowSummary),
        DeveloperStateLine(stringResource(R.string.shell_dev_page_stack), navigator.pages.describeNavigationStack()),
    ) + tabStacks
    ScreenHost(screen = DEVELOPER_OPTIONS_SCREEN) {
        DeveloperOptionsScreenContent(
            settings = settings,
            graph = graph,
            capabilities = capabilities,
            liveState = liveState,
            contentPadding = contentPadding(PaddingValues(bottom = SizeConstants.LargeSize)),
            onReset = { scope.launch { preferences.resetDeveloperOptions() } },
            onChange = { change ->
                scope.launch {
                    when (change) {
                        is DeveloperOptionChange.SetLayout -> preferences.setLayoutMode(change.value)
                        is DeveloperOptionChange.SetTopBarStyle -> preferences.setTopBarOverride(change.value)
                        is DeveloperOptionChange.SetHideTopBarOnScroll -> preferences.setHideTopBarOnScroll(change.value)
                        is DeveloperOptionChange.SetNavigationBarStyle -> preferences.setNavigationBarStyle(change.value)
                        is DeveloperOptionChange.SetHideBottomBarOnScroll -> preferences.setHideBottomBarOnScroll(change.value)
                        is DeveloperOptionChange.SetNavigationTint -> preferences.setNavigationTint(change.value)
                        is DeveloperOptionChange.SetContentWidth -> preferences.setLimitContentWidth(change.value)
                        is DeveloperOptionChange.SetStartOverride -> preferences.setStartOverride(change.value)
                        is DeveloperOptionChange.SetTabTransition -> preferences.setTabTransition(change.value)
                        is DeveloperOptionChange.SetBackEdge -> preferences.setBackEdgeStyle(change.value)
                        is DeveloperOptionChange.SetAnimationSpeed -> preferences.setAnimationSpeed(change.value)
                        is DeveloperOptionChange.SetEntranceStyle -> preferences.setEntranceStyle(change.value)
                        is DeveloperOptionChange.SetMotionTempo -> preferences.setMotionTempo(change.value)
                        is DeveloperOptionChange.SetEntranceReplay -> preferences.setEntranceReplay(change.value)
                        is DeveloperOptionChange.SetBottomAccessory -> preferences.setAccessoryMode(change.value)
                    }
                }
            },
        )
    }
}

@Composable
internal fun DeveloperOptionsScreenContent(
    settings: ShellSettings,
    graph: ShellGraph,
    capabilities: ShellCapabilities,
    liveState: List<DeveloperStateLine>,
    onChange: (DeveloperOptionChange) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val categories: List<Pair<DeveloperCategory, List<DeveloperOption>>> = remember(capabilities) {
        availableDeveloperOptions(capabilities).groupBy { it.category }.toList()
    }

    val categoryLiveState = stringResource(R.string.shell_dev_live_state)
    val resetTitle = stringResource(R.string.shell_dev_reset)
    val resetSummary = stringResource(R.string.shell_dev_reset_summary)

    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        animatedItem { PreferenceCategoryItem(title = categoryLiveState) }
        animatedItemsIndexed(liveState) { index, line ->
            DeveloperStateItem(line, Modifier.grouped(groupedItemPosition(index, liveState.size)))
        }
        categories.forEach { (category, options) ->
            animatedItem(key = "category_${category.name}") { PreferenceCategoryItem(title = stringResource(category.title)) }
            animatedItemsIndexed(options, key = { _, option -> "option_${option.name}" }) { index, option ->
                DeveloperOptionItem(
                    option = option,
                    settings = settings,
                    onChange = onChange,
                    graph = graph,
                    capabilities = capabilities,
                    modifier = Modifier.grouped(groupedItemPosition(index, options.size)),
                )
            }
        }
        animatedItem {
            SettingsPreferenceItem(
                modifier = Modifier
                    .padding(top = SizeConstants.LargeSize)
                    .grouped(GroupedItemPosition.SINGLE),
                title = resetTitle,
                summary = resetSummary,
                onClick = onReset,
            )
        }
    }
}

private val DEVELOPER_OPTIONS_SCREEN = TrackedScreen(name = "DeveloperOptions", className = "DeveloperOptionsScreen")

private fun Modifier.grouped(position: GroupedItemPosition): Modifier =
    groupedPreferenceItem(position = position, outerRadius = SizeConstants.LargeMediumSize)

private fun List<NavKey>.describeNavigationStack(): String = joinToString(separator = "  \u203A  ") { key ->
    key.toString().substringAfterLast('.').substringBefore('@').substringBefore('(')
}

@Preview(showBackground = true)
@Composable
private fun DeveloperOptionsScreenContentPreview() {
    val graph = remember {
        ShellGraphBuilder(appTitle = R.string.shell_developer_options).apply {
            developerOptionsPage()
            start(DeveloperOptionsRoute)
        }.build()
    }
    MaterialTheme {
        DeveloperOptionsScreenContent(
            settings = ShellSettings(),
            graph = graph,
            capabilities = ShellCapabilities(
                tabCount = 0,
                startOptionCount = 1,
                declaresBanner = false,
                declaresPlayer = false,
                hasFramedPages = true,
                hasBackNavigation = false,
                layoutReachesBottomBar = false,
                layoutReachesWideNavigation = false,
                hasContentWidthLimit = false,
            ),
            liveState = listOf(DeveloperStateLine(title = "Window", summary = "Phone")),
            onChange = {},
            onReset = {},
        )
    }
}
