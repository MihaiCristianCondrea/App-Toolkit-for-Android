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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItemsIndexed
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.analytics.DEVELOPER_OPTIONS_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.contracts.DeveloperOptionsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.extensions.groupedDeveloperPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.describeNavigationStack
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters.layoutModeLabel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperOption
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperStateLine
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.transformers.developerOptionCategories
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.views.DeveloperOptionItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.views.DeveloperStateItem
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.LocalShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalShellLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

/**
 * The shell's laboratory: a live readout of the state it is in, and every [DeveloperOption]
 * override of its layout, bars, navigation, motion and accessories that applies to this app.
 * Changes apply at once and persist, so the app can be restarted into a variation, and Reset puts
 * every override back as the app declares it.
 *
 * It reads the shell's own locals, so it only works as a page of a `ShellHost`. The page is
 * registered by `developerOptionsPage()`, which `toolkitPages()` in `:library:apptoolkit` calls.
 * The settings search does not list these options because the page is reached once unlocked.
 */
@Composable
fun DeveloperOptionsScreen() {
    val preferences = LocalShellPreferences.current
    val layout = LocalShellLayout.current
    val navigator = LocalShellNavigator.current
    val graph = LocalShellGraph.current
    val capabilities = LocalShellCapabilities.current
    val viewModel: DeveloperOptionsViewModel = koinViewModel { parametersOf(preferences) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    val windowSummary = stringResource(
        id = R.string.shell_dev_window_summary,
        layout.windowWidth.value.roundToInt(),
        layout.windowHeight.value.roundToInt(),
        layoutModeLabel(mode = layout.mode),
    )
    val tabStacks = graph.tabs.mapIndexed { index, tab ->
        DeveloperStateLine(
            title = stringResource(id = R.string.shell_dev_tab_stack, stringResource(id = tab.label)),
            summary = navigator.tabStack(index = index).describeNavigationStack(),
            selected = index == navigator.currentTabIndex,
        )
    }
    val liveState = buildList {
        add(
            DeveloperStateLine(
                title = stringResource(id = R.string.shell_dev_window),
                summary = windowSummary,
            ),
        )
        add(
            DeveloperStateLine(
                title = stringResource(id = R.string.shell_dev_page_stack),
                summary = navigator.pages.describeNavigationStack(),
            ),
        )
        addAll(tabStacks)
    }.toImmutableList()

    ScreenHost(
        screen = DEVELOPER_OPTIONS_SCREEN,
        viewModel = viewModel,
        onMessageAction = { message ->
            if (message.actionLabel == UiText.StringResource(resourceId = CoreUiR.string.try_again)) {
                viewModel.onEvent(event = DeveloperOptionsEvent.Load)
            }
        },
    ) {
        DeveloperOptionsScreenContent(
            settings = state.settings,
            graph = graph,
            capabilities = capabilities,
            liveState = liveState,
            contentPadding = contentPadding(extra = PaddingValues(bottom = SizeConstants.LargeSize)),
            onReset = { viewModel.onEvent(event = DeveloperOptionsEvent.Reset) },
            onChange = viewModel::onEvent,
        )
    }
}

@Composable
internal fun DeveloperOptionsScreenContent(
    settings: ShellSettings,
    graph: ShellGraph,
    capabilities: ShellCapabilities,
    liveState: ImmutableList<DeveloperStateLine>,
    onChange: (DeveloperOptionsEvent) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val categories: List<Pair<DeveloperCategory, List<DeveloperOption>>> = remember(key1 = capabilities) {
        developerOptionCategories(capabilities = capabilities)
    }

    val categoryLiveState = stringResource(id = R.string.shell_dev_live_state)
    val resetTitle = stringResource(id = R.string.shell_dev_reset)
    val resetSummary = stringResource(id = R.string.shell_dev_reset_summary)

    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        animatedItem { PreferenceCategoryItem(title = categoryLiveState) }
        animatedItemsIndexed(items = liveState) { index, line ->
            DeveloperStateItem(
                line = line,
                modifier = Modifier.groupedDeveloperPreference(
                    position = groupedItemPosition(index = index, size = liveState.size),
                ),
            )
        }
        categories.forEach { (category, options) ->
            animatedItem(key = "category_${category.name}") {
                PreferenceCategoryItem(title = stringResource(id = category.title))
            }
            animatedItemsIndexed(
                items = options,
                key = { _, option -> "option_${option.name}" },
            ) { index, option ->
                DeveloperOptionItem(
                    option = option,
                    settings = settings,
                    onChange = onChange,
                    graph = graph,
                    capabilities = capabilities,
                    modifier = Modifier.groupedDeveloperPreference(
                        position = groupedItemPosition(index = index, size = options.size),
                    ),
                )
            }
        }
        animatedItem {
            SettingsPreferenceItem(
                modifier = Modifier
                    .padding(top = SizeConstants.LargeSize)
                    .groupedDeveloperPreference(position = GroupedItemPosition.SINGLE),
                title = resetTitle,
                summary = resetSummary,
                onClick = onReset,
            )
        }
    }
}
