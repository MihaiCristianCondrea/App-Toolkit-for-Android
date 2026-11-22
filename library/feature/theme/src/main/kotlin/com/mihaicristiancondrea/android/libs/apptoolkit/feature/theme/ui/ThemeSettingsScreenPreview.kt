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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.states.ThemeSettingsUiState

private val PreviewPreferences = ThemePreferencesState(
    dynamicColors = false,
    staticPaletteId = StaticPaletteIds.GOOGLE_BLUE,
)

/** Ready and Loading only: the default empty and failure screens inject Koin bindings. */
@Preview(showBackground = true)
@Composable
private fun ThemeSettingsScreenContentPreview() {
    MaterialTheme {
        ThemeSettingsScreenContent(
            state = ThemeSettingsUiState(preferences = Loadable.Ready(PreviewPreferences)),
            onEvent = {},
            onPaletteTabSelected = {},
            onDynamicPaletteSelected = {},
            onStaticPaletteSelected = { _, _ -> },
            onThemeModeSelected = {},
            onAmoledModeChanged = {},
            onOpenDisplaySettings = {},
            supportsDynamicColors = false,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeSettingsScreenContentWallpaperColorsPreview() {
    MaterialTheme {
        ThemeSettingsScreenContent(
            state = ThemeSettingsUiState(
                preferences = Loadable.Ready(PreviewPreferences.copy(dynamicColors = true)),
                seasonalThemesUnlocked = true,
            ),
            onEvent = {},
            onPaletteTabSelected = {},
            onDynamicPaletteSelected = {},
            onStaticPaletteSelected = { _, _ -> },
            onThemeModeSelected = {},
            onAmoledModeChanged = {},
            onOpenDisplaySettings = {},
            supportsDynamicColors = true,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeSettingsScreenContentLoadingPreview() {
    MaterialTheme {
        ThemeSettingsScreenContent(
            state = ThemeSettingsUiState(),
            onEvent = {},
            onPaletteTabSelected = {},
            onDynamicPaletteSelected = {},
            onStaticPaletteSelected = { _, _ -> },
            onThemeModeSelected = {},
            onAmoledModeChanged = {},
            onOpenDisplaySettings = {},
        )
    }
}
