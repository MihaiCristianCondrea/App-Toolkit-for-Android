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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.states.AdvancedSettingsUiState

@Preview(showBackground = true)
@Composable
private fun AdvancedSettingsScreenContentPreview() {
    MaterialTheme {
        AdvancedSettingsScreenContent(
            state = AdvancedSettingsUiState(developerOptionsUnlocked = true),
            onEvent = {},
            onReportBug = {},
            onOpenDeveloperOptions = {},
            showBugReport = true,
        )
    }
}

/** Without an issue reporter and before the easter egg, only the cache row shows. */
@Preview(showBackground = true)
@Composable
private fun AdvancedSettingsScreenContentMinimalPreview() {
    MaterialTheme {
        AdvancedSettingsScreenContent(
            state = AdvancedSettingsUiState(),
            onEvent = {},
            onReportBug = {},
            onOpenDeveloperOptions = {},
        )
    }
}
