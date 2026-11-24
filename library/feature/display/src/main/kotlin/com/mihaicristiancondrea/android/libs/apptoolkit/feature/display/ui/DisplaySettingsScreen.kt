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

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.analytics.DISPLAY_SETTINGS_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.analytics.DisplayActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.analytics.DisplayPreferenceKeys
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.analytics.LanguageDestinations
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.analytics.displayActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.analytics.displayThemeModeEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.contracts.DisplaySettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.extensions.openLanguageSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.DisplayRow
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.DisplaySettings
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.providers.DisplaySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.states.DisplaySettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.transformers.displayRows
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.views.dialogs.SelectLanguageAlertDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.views.lists.DisplaySettingsList
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.LocalShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.keys.ThemeSettingsScreen
import kotlinx.collections.immutable.ImmutableList
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel

/**
 * Displays the person's appearance, interaction, navigation and language preferences. Works only
 * as a page of `ShellHost`, whose capabilities decide which rows mean something in this app; the
 * settings search lists the same rows. The shell's own variations are developer options.
 *
 * Owns [DisplaySettingsViewModel], tracking, messages, the host's startup page dialog, the
 * language dialog and the system language settings, and logs each of their GA4 events.
 */
@Composable
fun DisplaySettingsScreen() {
    val provider: DisplaySettingsProvider? = getKoin().getOrNull()
    val navigator = LocalShellNavigator.current
    val telemetry = LocalTelemetry.current
    val viewModel: DisplaySettingsViewModel = koinViewModel()
    val state: DisplaySettingsUiState by viewModel.state.collectAsStateWithLifecycle()
    val settings: DisplaySettings? = (state.settings as? Loadable.Ready)?.value
    val context: Context = LocalContext.current

    var showLanguageDialog: Boolean by rememberSaveable { mutableStateOf(value = false) }
    var showStartupDialog: Boolean by rememberSaveable { mutableStateOf(value = false) }

    val capabilities = LocalShellCapabilities.current
    val rows: ImmutableList<DisplayRow> = remember(capabilities, provider) {
        displayRows(capabilities = capabilities, startup = provider)
    }

    ScreenHost(screen = DISPLAY_SETTINGS_SCREEN, status = state.settings, viewModel = viewModel) {
        DisplaySettingsScreenContent(
            state = state,
            onEvent = viewModel::onEvent,
            rows = rows,
            onDarkThemeChanged = { isChecked ->
                val targetMode = if (isChecked) {
                    DataStoreNamesConstants.THEME_MODE_DARK
                } else {
                    DataStoreNamesConstants.THEME_MODE_LIGHT
                }
                telemetry.logEvent(event = displayThemeModeEvent(mode = targetMode))
                viewModel.onEvent(event = DisplaySettingsEvent.ThemeModeChanged(mode = targetMode))
            },
            onOpenThemeSettings = {
                telemetry.logEvent(
                    event = displayActionEvent(
                        actionName = DisplayActionNames.OPEN_THEME_SETTINGS,
                        preferenceKey = DisplayPreferenceKeys.THEME_SETTINGS,
                        value = DisplayActionNames.THEME_REDIRECT,
                    ),
                )
                navigator.navigate(key = ThemeSettingsScreen)
            },
            onOpenStartupPage = { showStartupDialog = true },
            onOpenLanguage = {
                val destination: String = context.openLanguageSettings()
                telemetry.logEvent(
                    event = displayActionEvent(
                        actionName = DisplayActionNames.OPEN_LANGUAGE_SETTINGS,
                        preferenceKey = DisplayPreferenceKeys.LANGUAGE,
                        value = destination,
                    ),
                )
                if (destination == LanguageDestinations.IN_APP_DIALOG) {
                    showLanguageDialog = true
                }
            },
            contentPadding = contentPadding(),
        )
    }

    if (showStartupDialog && (settings != null) && (provider != null)) {
        provider.StartupPageDialog(
            currentRoute = settings.startupRoute,
            onDismiss = { showStartupDialog = false },
        ) { selectedDestination: String ->
            viewModel.onEvent(event = DisplaySettingsEvent.StartupRouteChanged(route = selectedDestination))
            telemetry.logEvent(
                event = displayActionEvent(
                    actionName = DisplayActionNames.CHANGE_STARTUP_DESTINATION,
                    preferenceKey = DisplayPreferenceKeys.STARTUP_PAGE,
                    value = selectedDestination,
                ),
            )
        }
    }

    if (showLanguageDialog && (settings != null)) {
        SelectLanguageAlertDialog(
            currentLanguage = settings.language,
            onDismiss = { showLanguageDialog = false },
            onLanguageSelected = { newLanguageCode: String ->
                viewModel.onEvent(event = DisplaySettingsEvent.LanguageChanged(language = newLanguageCode))
                showLanguageDialog = false
                telemetry.logEvent(
                    event = displayActionEvent(
                        actionName = DisplayActionNames.CHANGE_LANGUAGE,
                        preferenceKey = DisplayPreferenceKeys.LANGUAGE,
                        value = newLanguageCode,
                    ),
                )
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(newLanguageCode),
                )
            },
        )
    }
}

/**
 * The display settings for [state], or their loading or failure state: the [rows] that mean
 * something in this app, under their headings, and no heading without a row.
 *
 * @param onEvent Receives the events [DisplaySettingsViewModel] handles.
 * @param rows The rows to show, from `displayRows`, in the page's order.
 * @param onDarkThemeChanged The dark theme switch was toggled.
 * @param onOpenThemeSettings The dark theme row was tapped.
 * @param onOpenStartupPage The startup page row was tapped.
 * @param onOpenLanguage The language row was tapped.
 * @param contentPadding Padding from the shell, applied inside the list and the state screens.
 */
@Composable
internal fun DisplaySettingsScreenContent(
    state: DisplaySettingsUiState,
    onEvent: (DisplaySettingsEvent) -> Unit,
    rows: ImmutableList<DisplayRow>,
    onDarkThemeChanged: (Boolean) -> Unit,
    onOpenThemeSettings: () -> Unit,
    onOpenStartupPage: () -> Unit,
    onOpenLanguage: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    ScreenStateHandler(
        state = state.settings,
        modifier = modifier,
        contentPadding = contentPadding,
        onRetry = { onEvent(DisplaySettingsEvent.Load) },
    ) { (settings) ->
        DisplaySettingsList(
            settings = settings,
            rows = rows,
            onEvent = onEvent,
            onDarkThemeChanged = onDarkThemeChanged,
            onOpenThemeSettings = onOpenThemeSettings,
            onOpenStartupPage = onOpenStartupPage,
            onOpenLanguage = onOpenLanguage,
            contentPadding = contentPadding,
        )
    }
}
