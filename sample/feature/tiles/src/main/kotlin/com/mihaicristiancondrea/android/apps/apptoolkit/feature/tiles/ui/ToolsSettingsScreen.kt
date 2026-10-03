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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolsSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import org.koin.compose.viewmodel.koinViewModel

/**
 * The tools' own settings page, owned by the sample rather than the Toolkit. The settings search
 * finds its rows through `toolsSettingsSearch`, although the root settings list does not link it.
 *
 * Owns [ToolsSettingsViewModel] and tracking.
 */
@Composable
fun ToolsSettingsScreen() {
    val viewModel: ToolsSettingsViewModel = koinViewModel()
    val screen = AppScreenTracking.Screens.TOOLS_SETTINGS

    TrackScreenView(screenName = screen.name, screenClass = screen.className)

    ToolsSettingsScreenContent(onEvent = viewModel::onEvent, contentPadding = contentPadding())
}

/**
 * The page's rows: resetting the count, then opening or closing every category of the tools list.
 *
 * @param onEvent Receives the events [ToolsSettingsViewModel] handles.
 * @param contentPadding Padding from the shell, applied inside the list.
 */
@Composable
internal fun ToolsSettingsScreenContent(
    onEvent: (ToolsSettingsEvent) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        item { PreferenceCategoryItem(title = stringResource(id = R.string.tools_settings_counter)) }
        item {
            SettingsPreferenceItem(
                title = stringResource(id = R.string.tools_settings_reset_counter),
                summary = stringResource(id = R.string.tools_settings_reset_counter_summary),
                onClick = { onEvent(ToolsSettingsEvent.ResetCounter) },
                modifier = Modifier.groupedPreferenceItem(
                    position = GroupedItemPosition.SINGLE,
                    outerRadius = SizeConstants.LargeMediumSize,
                ),
            )
        }
        item { PreferenceCategoryItem(title = stringResource(id = R.string.tools_settings_list)) }
        item {
            SettingsPreferenceItem(
                title = stringResource(id = R.string.tools_settings_expand_all),
                summary = stringResource(id = R.string.tools_settings_expand_all_summary),
                onClick = { onEvent(ToolsSettingsEvent.ExpandAllCategories) },
                modifier = Modifier.groupedPreferenceItem(
                    position = GroupedItemPosition.FIRST,
                    outerRadius = SizeConstants.LargeMediumSize,
                ),
            )
        }
        item {
            SettingsPreferenceItem(
                title = stringResource(id = R.string.tools_settings_collapse_all),
                summary = stringResource(id = R.string.tools_settings_collapse_all_summary),
                onClick = { onEvent(ToolsSettingsEvent.CollapseAllCategories) },
                modifier = Modifier.groupedPreferenceItem(
                    position = GroupedItemPosition.LAST,
                    outerRadius = SizeConstants.LargeMediumSize,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ToolsSettingsScreenContentPreview() {
    MaterialTheme {
        ToolsSettingsScreenContent(onEvent = {})
    }
}
