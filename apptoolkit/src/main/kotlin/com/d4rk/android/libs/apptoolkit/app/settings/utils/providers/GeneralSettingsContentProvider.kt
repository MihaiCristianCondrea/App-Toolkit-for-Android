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
