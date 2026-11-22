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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.DisplayRow
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.DisplaySettings
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.states.DisplaySettingsUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

private val PreviewSettings = DisplaySettings(
    themeMode = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
    dynamicColors = true,
    bouncyButtons = true,
    showBottomBarLabels = true,
    language = "en",
    startupRoute = "home",
)

@Preview(showBackground = true)
@Composable
private fun DisplaySettingsScreenContentPreview() {
    MaterialTheme {
        DisplaySettingsScreenContent(
            state = DisplaySettingsUiState(settings = Loadable.Ready(value = PreviewSettings)),
            rows = DisplayRow.entries.toImmutableList(),
            onEvent = {},
            onDarkThemeChanged = {},
            onOpenThemeSettings = {},
            onOpenStartupPage = {},
            onOpenLanguage = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DisplaySettingsScreenContentNoTabsPreview() {
    MaterialTheme {
        DisplaySettingsScreenContent(
            state = DisplaySettingsUiState(settings = Loadable.Ready(value = PreviewSettings)),
            rows = persistentListOf(
                DisplayRow.DarkTheme,
                DisplayRow.DynamicColors,
                DisplayRow.BounceButtons,
                DisplayRow.Language,
            ),
            onEvent = {},
            onDarkThemeChanged = {},
            onOpenThemeSettings = {},
            onOpenStartupPage = {},
            onOpenLanguage = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DisplaySettingsScreenContentLoadingPreview() {
    MaterialTheme {
        DisplaySettingsScreenContent(
            state = DisplaySettingsUiState(settings = Loadable.Loading),
            rows = DisplayRow.entries.toImmutableList(),
            onEvent = {},
            onDarkThemeChanged = {},
            onOpenThemeSettings = {},
            onOpenStartupPage = {},
            onOpenLanguage = {},
        )
    }
}
