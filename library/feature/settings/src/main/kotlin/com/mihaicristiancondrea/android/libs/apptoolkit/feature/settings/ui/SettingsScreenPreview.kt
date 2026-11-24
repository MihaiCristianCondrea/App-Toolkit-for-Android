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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.states.SettingsUiState

// Only the loaded and loading states have previews: the empty and failure states draw
// `NoDataScreen`, which resolves its ad unit through Koin.
@Preview(showBackground = true)
@Composable
private fun SettingsScreenContentPreview() {
    MaterialTheme {
        SettingsScreenContent(
            state = SettingsUiState(
                config = Loadable.Ready(
                    value = SettingsConfig(
                        title = "Settings",
                        categories = listOf(
                            SettingsCategory(
                                preferences = listOf(
                                    SettingsPreference(
                                        key = "notifications",
                                        icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Notifications),
                                        title = "Notifications",
                                        summary = "Alerts and reminders",
                                    ),
                                    SettingsPreference(
                                        key = "display",
                                        icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Settings),
                                        title = "Display",
                                        summary = "Layout and text size",
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
            contentPadding = PaddingValues(),
            query = "",
            onQueryChange = {},
            results = null,
            onPreferenceClick = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenContentLoadingPreview() {
    MaterialTheme {
        SettingsScreenContent(
            state = SettingsUiState(config = Loadable.Loading),
            contentPadding = PaddingValues(),
            query = "",
            onQueryChange = {},
            results = null,
            onPreferenceClick = {},
            onRetry = {},
        )
    }
}
