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
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.startActivitySafely
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItemWithDivider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.contracts.DisplaySettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.DisplaySettings
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.DisplayCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.DisplayRow
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.displayRows
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.providers.DisplaySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.states.DisplaySettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.views.dialogs.SelectLanguageAlertDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.LocalShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.ThemeSettingsRoute
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val DISPLAY_SETTINGS_SCREEN_NAME = "DisplaySettings"
private const val DISPLAY_SETTINGS_SCREEN_CLASS = "DisplaySettingsScreen"

private object DisplayPreferenceKeys {
    const val THEME_SETTINGS: String = "theme_settings"
    const val STARTUP_PAGE: String = "startup_page"
    const val LANGUAGE: String = "language"
}

private object DisplayActionNames {
    const val OPEN_THEME_SETTINGS: String = "open_theme_settings"
    const val THEME_REDIRECT: String = "theme_redirect"
    const val OPEN_STARTUP_DIALOG: String = "open_startup_dialog"
    const val CHANGE_STARTUP_DESTINATION: String = "change_startup_destination"
    const val OPEN_LANGUAGE_SETTINGS: String = "open_language_settings"
    const val CHANGE_LANGUAGE: String = "change_language"
}

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
    val provider: DisplaySettingsProvider = koinInject()
    val navigator = LocalShellNavigator.current
    val telemetryRepository = LocalTelemetry.current
    val viewModel: DisplaySettingsViewModel = koinViewModel()
    val state: DisplaySettingsUiState by viewModel.state.collectAsStateWithLifecycle()
    val settings: DisplaySettings? = (state.settings as? Loadable.Ready)?.value
    val context: Context = LocalContext.current

    TrackScreenView(
        screenName = DISPLAY_SETTINGS_SCREEN_NAME,
        screenClass = DISPLAY_SETTINGS_SCREEN_CLASS,
    )

    TrackScreenState(
        screenName = DISPLAY_SETTINGS_SCREEN_NAME,
        state = state.settings,
    )

    var showLanguageDialog: Boolean by rememberSaveable { mutableStateOf(false) }
    var showStartupDialog: Boolean by rememberSaveable { mutableStateOf(false) }

    val capabilities = LocalShellCapabilities.current
    val rows = remember(capabilities, provider) { displayRows(capabilities, provider) }

    DisplaySettingsScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        rows = rows,
        onDarkThemeChanged = { isChecked ->
            val targetMode =
                if (isChecked) DataStoreNamesConstants.THEME_MODE_DARK
                else DataStoreNamesConstants.THEME_MODE_LIGHT
            telemetryRepository.logEvent(
                AnalyticsEvent(
                    name = SettingsAnalytics.Events.THEME_SWITCH,
                    params = mapOf(
                        SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(DISPLAY_SETTINGS_SCREEN_NAME),
                        SettingsAnalytics.Params.THEME_MODE to AnalyticsValue.Str(targetMode),
                    ),
                )
            )
            viewModel.onEvent(DisplaySettingsEvent.ThemeModeChanged(targetMode))
        },
        onOpenThemeSettings = {
            telemetryRepository.logEvent(
                displayActionEvent(
                    actionName = DisplayActionNames.OPEN_THEME_SETTINGS,
                    preferenceKey = DisplayPreferenceKeys.THEME_SETTINGS,
                    value = DisplayActionNames.THEME_REDIRECT,
                )
            )
            navigator.navigate(ThemeSettingsRoute)
        },
        onOpenStartupPage = { showStartupDialog = true },
        onOpenLanguage = {
            val destination: String = context.openLanguageSettings()
            telemetryRepository.logEvent(
                displayActionEvent(
                    actionName = DisplayActionNames.OPEN_LANGUAGE_SETTINGS,
                    preferenceKey = DisplayPreferenceKeys.LANGUAGE,
                    value = destination,
                )
            )
            if (destination == LanguageDestinations.IN_APP_DIALOG) {
                showLanguageDialog = true
            }
        },
        contentPadding = contentPadding(),
    )

    if (showStartupDialog) {
        provider.StartupPageDialog(
            currentRoute = settings?.startupRoute.orEmpty(),
            onDismiss = { showStartupDialog = false },
        ) { selectedDestination: String ->
            viewModel.onEvent(DisplaySettingsEvent.StartupRouteChanged(selectedDestination))
            telemetryRepository.logEvent(
                displayActionEvent(
                    actionName = DisplayActionNames.CHANGE_STARTUP_DESTINATION,
                    preferenceKey = DisplayPreferenceKeys.STARTUP_PAGE,
                    value = selectedDestination,
                )
            )
        }
    }

    if (showLanguageDialog) {
        SelectLanguageAlertDialog(
            currentLanguage = settings?.language.orEmpty(),
            onDismiss = { showLanguageDialog = false },
            onLanguageSelected = { newLanguageCode: String ->
                viewModel.onEvent(DisplaySettingsEvent.LanguageChanged(newLanguageCode))
                showLanguageDialog = false
                telemetryRepository.logEvent(
                    displayActionEvent(
                        actionName = DisplayActionNames.CHANGE_LANGUAGE,
                        preferenceKey = DisplayPreferenceKeys.LANGUAGE,
                        value = newLanguageCode,
                    )
                )
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(newLanguageCode)
                )
            }
        )
    }

    MessageHost(viewModel = viewModel)
}

/** The `value` the `open_language_settings` action reports: where the language row led. */
private object LanguageDestinations {
    const val SYSTEM_LOCALE: String = "system_locale"
    const val APP_DETAILS: String = "app_details"
    const val IN_APP_DIALOG: String = "in_app_dialog"
}

/**
 * Opens the system's per-app language settings, or the app's details page where those are
 * missing, and returns which [LanguageDestinations] opened. Before Android 13 there are none, so
 * the in-app dialog is used.
 */
private fun Context.openLanguageSettings(): String {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return LanguageDestinations.IN_APP_DIALOG
    val packageUri: Uri = Uri.fromParts("package", packageName, null)
    return when {
        startActivitySafely(intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS).setData(packageUri)) ->
            LanguageDestinations.SYSTEM_LOCALE

        startActivitySafely(intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(packageUri)) ->
            LanguageDestinations.APP_DETAILS

        else -> LanguageDestinations.IN_APP_DIALOG
    }
}

private fun displayActionGa4Event(actionName: String, preferenceKey: String): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.ACTION,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(DISPLAY_SETTINGS_SCREEN_NAME),
            SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(actionName),
            SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(preferenceKey),
        ),
    )
}

private fun displayActionEvent(
    actionName: String,
    preferenceKey: String,
    value: String? = null,
): AnalyticsEvent {
    val base = mutableMapOf<String, AnalyticsValue>(
        SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(DISPLAY_SETTINGS_SCREEN_NAME),
        SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(actionName),
        SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(preferenceKey),
    )
    if (value != null) {
        base["value"] = AnalyticsValue.Str(value)
    }
    return AnalyticsEvent(
        name = SettingsAnalytics.Events.ACTION,
        params = base,
    )
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
    rows: List<DisplayRow>,
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
    ) { ready ->
        DisplaySettingsList(
            settings = ready.value,
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

@Composable
private fun DisplaySettingsList(
    settings: DisplaySettings,
    rows: List<DisplayRow>,
    onEvent: (DisplaySettingsEvent) -> Unit,
    onDarkThemeChanged: (Boolean) -> Unit,
    onOpenThemeSettings: () -> Unit,
    onOpenStartupPage: () -> Unit,
    onOpenLanguage: () -> Unit,
    contentPadding: PaddingValues,
) {
    val isSystemDarkTheme: Boolean = isSystemInDarkTheme()
    val isDarkThemeActive: Boolean = when (settings.themeMode) {
        DataStoreNamesConstants.THEME_MODE_DARK -> true
        DataStoreNamesConstants.THEME_MODE_LIGHT -> false
        else -> isSystemDarkTheme
    }
    val themeSummary: String = when (settings.themeMode) {
        DataStoreNamesConstants.THEME_MODE_DARK, DataStoreNamesConstants.THEME_MODE_LIGHT -> stringResource(
            id = R.string.will_never_turn_on_automatically
        )

        else -> stringResource(id = R.string.will_turn_on_automatically_by_system)
    }
    val categories: List<Pair<DisplayCategory, List<DisplayRow>>> = remember(rows) {
        rows.groupBy { it.category }.toList()
    }

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        categories.forEach { (category, categoryRows) ->
            item(key = "category_${category.name}") {
                PreferenceCategoryItem(title = stringResource(id = category.title))
            }
            itemsIndexed(categoryRows, key = { _, row -> "row_${row.name}" }) { index, row ->
                val rowModifier = Modifier.groupedPreferenceItem(
                    position = groupedItemPosition(index, categoryRows.size),
                    outerRadius = SizeConstants.LargeMediumSize,
                )
                when (row) {
                    DisplayRow.DarkTheme -> SwitchPreferenceItemWithDivider(
                        title = stringResource(id = row.title),
                        summary = themeSummary,
                        checked = isDarkThemeActive,
                        onCheckedChange = onDarkThemeChanged,
                        onSwitchClick = onDarkThemeChanged,
                        onClick = onOpenThemeSettings,
                        modifier = rowModifier,
                    )

                    DisplayRow.DynamicColors -> SwitchPreferenceItem(
                        title = stringResource(id = row.title),
                        summary = row.summaryText(),
                        checked = settings.dynamicColors,
                        onCheckedChange = { isChecked -> onEvent(DisplaySettingsEvent.DynamicColorsChanged(isChecked)) },
                        modifier = rowModifier,
                    )

                    DisplayRow.BounceButtons -> SwitchPreferenceItem(
                        title = stringResource(id = row.title),
                        summary = row.summaryText(),
                        checked = settings.bouncyButtons,
                        onCheckedChange = { isChecked -> onEvent(DisplaySettingsEvent.BouncyButtonsChanged(isChecked)) },
                        modifier = rowModifier,
                    )

                    DisplayRow.StartupPage -> SettingsPreferenceItem(
                        title = stringResource(id = row.title),
                        summary = row.summaryText(),
                        onClick = onOpenStartupPage,
                        ga4Event = displayActionGa4Event(
                            actionName = DisplayActionNames.OPEN_STARTUP_DIALOG,
                            preferenceKey = DisplayPreferenceKeys.STARTUP_PAGE,
                        ),
                        modifier = rowModifier,
                    )

                    DisplayRow.NavigationLabels -> SwitchPreferenceItem(
                        title = stringResource(id = row.title),
                        summary = row.summaryText(),
                        checked = settings.showBottomBarLabels,
                        onCheckedChange = { isChecked -> onEvent(DisplaySettingsEvent.BottomBarLabelsChanged(isChecked)) },
                        modifier = rowModifier,
                    )

                    DisplayRow.Language -> SettingsPreferenceItem(
                        title = stringResource(id = row.title),
                        summary = row.summaryText(),
                        onClick = onOpenLanguage,
                        modifier = rowModifier,
                    )
                }
            }
        }
    }
}

@Composable
private fun DisplayRow.summaryText(): String? = summary?.let { stringResource(id = it) }

private val PreviewSettings = DisplaySettings(
    themeMode = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
    dynamicColors = true,
    bouncyButtons = true,
    showBottomBarLabels = true,
    language = "en",
    startupRoute = "",
)

/** Every row, as in an app with several tabs that offers a startup page. */
@Preview(showBackground = true)
@Composable
private fun DisplaySettingsScreenContentPreview() {
    MaterialTheme {
        DisplaySettingsScreenContent(
            state = DisplaySettingsUiState(settings = Loadable.Ready(PreviewSettings)),
            onEvent = {},
            rows = DisplayRow.entries,
            onDarkThemeChanged = {},
            onOpenThemeSettings = {},
            onOpenStartupPage = {},
            onOpenLanguage = {},
        )
    }
}

/** An app without tabs: no navigation heading. */
@Preview(showBackground = true)
@Composable
private fun DisplaySettingsScreenContentWithoutTabsPreview() {
    MaterialTheme {
        DisplaySettingsScreenContent(
            state = DisplaySettingsUiState(settings = Loadable.Ready(PreviewSettings)),
            onEvent = {},
            rows = DisplayRow.entries.filter { it.category != DisplayCategory.Navigation },
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
            onEvent = {},
            rows = DisplayRow.entries,
            onDarkThemeChanged = {},
            onOpenThemeSettings = {},
            onOpenStartupPage = {},
            onOpenLanguage = {},
        )
    }
}
