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

package com.d4rk.android.libs.apptoolkit.app.settings.utils.providers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.d4rk.android.libs.apptoolkit.app.about.ui.AboutScreen
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.AdvancedSettingsList
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.UsageAndDiagnosticsList
import com.d4rk.android.libs.apptoolkit.app.display.ui.DisplaySettingsList
import com.d4rk.android.libs.apptoolkit.app.privacy.ui.PrivacySettingsList
import com.d4rk.android.libs.apptoolkit.app.settings.utils.constants.SettingsContent
import com.d4rk.android.libs.apptoolkit.app.theme.ui.ThemeSettingsList

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

            SettingsContent.ADVANCED -> AdvancedSettingsList(paddingValues = paddingValues)
            SettingsContent.DISPLAY -> DisplaySettingsList(paddingValues = paddingValues)
            SettingsContent.SECURITY_AND_PRIVACY -> PrivacySettingsList(paddingValues = paddingValues)
            SettingsContent.THEME -> ThemeSettingsList(paddingValues = paddingValues)
            SettingsContent.USAGE_AND_DIAGNOSTICS -> UsageAndDiagnosticsList(paddingValues = paddingValues)
            else -> customScreens[contentKey]?.invoke(paddingValues)
        }
    }
}
