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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.providers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.AboutScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.AdvancedSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.UsageAndDiagnosticsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.DisplaySettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.PrivacyScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.constants.SettingsContent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.ThemeSettingsScreen

/**
 * Provider class that handles rendering of different settings sections.
 */
@Stable
class GeneralSettingsContentProvider(
    private val customScreens: Map<String, @Composable (PaddingValues) -> Unit> = emptyMap(),
    private val aboutContent: (@Composable (PaddingValues, SnackbarHostState) -> Unit)? = null,
) {
    @Composable
    fun ProvideContent(
        contentKey: String?,
        paddingValues: PaddingValues,
        snackbarHostState: SnackbarHostState
    ) {
        when (contentKey) {
            SettingsContent.ABOUT -> {
                val aboutScreen = aboutContent
                if (aboutScreen == null) {
                    AboutScreen(
                        paddingValues = paddingValues,
                        snackbarHostState = snackbarHostState
                    )
                } else {
                    aboutScreen(paddingValues, snackbarHostState)
                }
            }

            SettingsContent.ADVANCED -> AdvancedSettingsScreen(paddingValues = paddingValues)
            SettingsContent.DISPLAY -> DisplaySettingsScreen(paddingValues = paddingValues)
            SettingsContent.SECURITY_AND_PRIVACY -> PrivacyScreen(paddingValues = paddingValues)
            SettingsContent.THEME -> ThemeSettingsScreen(paddingValues = paddingValues)
            SettingsContent.USAGE_AND_DIAGNOSTICS -> UsageAndDiagnosticsScreen(paddingValues = paddingValues)
            else -> customScreens[contentKey]?.invoke(paddingValues)
        }
    }
}
