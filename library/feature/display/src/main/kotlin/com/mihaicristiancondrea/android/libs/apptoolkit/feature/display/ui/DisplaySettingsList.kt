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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.contracts.DisplaySettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.states.DisplaySettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.views.dialogs.SelectLanguageAlertDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.providers.DisplaySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.startActivitySafely
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItemWithDivider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.R
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel


private const val DISPLAY_SETTINGS_SCREEN_NAME = "DisplaySettings"
private const val DISPLAY_SETTINGS_SCREEN_CLASS = "DisplaySettingsList"

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
 * A composable function that displays a comprehensive list of display-related settings.
 *
 * This screen allows users to manage various UI preferences including:
 * - **Appearance:** Dark theme toggle (with system sync detection) and Dynamic Colors (Android 12+).
 * - **App Behavior:** Global settings like "Bouncy Buttons" animations.
 * - **Navigation:** Configuration for the startup page and visibility of bottom bar labels.
 * - **Language:** Access to per-app language settings via system settings (Android 13+) or an internal dialog.
 *
 * Persistence is owned by [DisplaySettingsViewModel]; [DisplaySettingsProvider] supplies host UI.
 *
 * @param paddingValues The padding to be applied to the [LazyColumn] container,
 * typically used to avoid overlap with system bars or scaffolds.
 */
@Composable
fun DisplaySettingsList(
    paddingValues: PaddingValues = PaddingValues(),
) {
    val provider: DisplaySettingsProvider = koinInject()
    val firebaseController: FirebaseController = koinInject()
    val viewModel: DisplaySettingsViewModel = koinViewModel()
    val screenState: UiStateScreen<DisplaySettingsUiState> by
        viewModel.uiState.collectAsStateWithLifecycle()
    val uiState = screenState.data ?: DisplaySettingsUiState()
    val startupRoute: String by viewModel.startupRoute(defaultRoute = "")
        .collectAsStateWithLifecycle(initialValue = "")

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = DISPLAY_SETTINGS_SCREEN_NAME,
        screenClass = DISPLAY_SETTINGS_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = DISPLAY_SETTINGS_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    val context: Context = LocalContext.current

    val showLanguageDialog = rememberSaveable { mutableStateOf(false) }
    val showStartupDialog = rememberSaveable { mutableStateOf(false) }

    val currentThemeModeKey: String = uiState.themeMode

    val isSystemDarkTheme: Boolean = isSystemInDarkTheme()

    val isDarkThemeActive: Boolean = when (currentThemeModeKey) {
        DataStoreNamesConstants.THEME_MODE_DARK -> true
        DataStoreNamesConstants.THEME_MODE_LIGHT -> false
        else -> isSystemDarkTheme
    }

    val themeSummary: String = when (currentThemeModeKey) {
        DataStoreNamesConstants.THEME_MODE_DARK, DataStoreNamesConstants.THEME_MODE_LIGHT -> stringResource(
            id = R.string.will_never_turn_on_automatically
        )

        else -> stringResource(id = R.string.will_turn_on_automatically_by_system)
    }

    val onDarkThemeChanged: (Boolean) -> Unit = remember(viewModel, firebaseController) {
        { isChecked: Boolean ->
            val targetMode =
                if (isChecked) DataStoreNamesConstants.THEME_MODE_DARK
                else DataStoreNamesConstants.THEME_MODE_LIGHT
            firebaseController.logEvent(
                AnalyticsEvent(
                    name = SettingsAnalytics.Events.THEME_SWITCH,
                    params = mapOf(
                        SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(
                            DISPLAY_SETTINGS_SCREEN_NAME
                        ),
                        SettingsAnalytics.Params.THEME_MODE to AnalyticsValue.Str(targetMode),
                    ),
                )
            )
            viewModel.onEvent(DisplaySettingsEvent.ThemeModeChanged(targetMode))
        }
    }

    if (showStartupDialog.value) {
        provider.StartupPageDialog(
            currentRoute = startupRoute,
            onDismiss = { showStartupDialog.value = false }
        ) { selectedDestination: String ->
            viewModel.onEvent(DisplaySettingsEvent.StartupRouteChanged(selectedDestination))
            firebaseController.logEvent(
                displayActionEvent(
                    actionName = DisplayActionNames.CHANGE_STARTUP_DESTINATION,
                    preferenceKey = DisplayPreferenceKeys.STARTUP_PAGE,
                    value = selectedDestination,
                )
            )
        }
    }

    if (showLanguageDialog.value) {
        SelectLanguageAlertDialog(
            currentLanguage = uiState.language,
            onDismiss = { showLanguageDialog.value = false },
            onLanguageSelected = { newLanguageCode: String ->
                viewModel.onEvent(DisplaySettingsEvent.LanguageChanged(newLanguageCode))
                showLanguageDialog.value = false
                firebaseController.logEvent(
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

    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.appearance))
        }

        item {
            SwitchPreferenceItemWithDivider(
                title = stringResource(id = R.string.dark_theme),
                summary = themeSummary,
                checked = isDarkThemeActive,
                onCheckedChange = onDarkThemeChanged,
                onSwitchClick = onDarkThemeChanged,
                onClick = {
                    firebaseController.logEvent(
                        displayActionEvent(
                            actionName = DisplayActionNames.OPEN_THEME_SETTINGS,
                            preferenceKey = DisplayPreferenceKeys.THEME_SETTINGS,
                            value = DisplayActionNames.THEME_REDIRECT,
                        )
                    )
                    provider.openThemeSettings()
                },
                modifier = Modifier.groupedPreferenceItem(
                    position = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) GroupedItemPosition.FIRST else GroupedItemPosition.SINGLE,
                    outerRadius = SizeConstants.LargeMediumSize,
                )
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            item {
                SwitchPreferenceItem(
                    title = stringResource(id = R.string.dynamic_colors),
                    summary = stringResource(id = R.string.summary_preference_settings_dynamic_colors),
                    checked = uiState.dynamicColors,
                    onCheckedChange = { isChecked ->
                        viewModel.onEvent(DisplaySettingsEvent.DynamicColorsChanged(isChecked))
                    },
                    modifier = Modifier.groupedPreferenceItem(
                        position = GroupedItemPosition.LAST,
                        outerRadius = SizeConstants.LargeMediumSize,
                    )
                )
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.app_behavior))
        }

        item {
            SwitchPreferenceItem(
                title = stringResource(id = R.string.bounce_buttons),
                summary = stringResource(id = R.string.summary_preference_settings_bounce_buttons),
                checked = uiState.bouncyButtons,
                onCheckedChange = { isChecked ->
                    viewModel.onEvent(DisplaySettingsEvent.BouncyButtonsChanged(isChecked))
                },
                modifier = Modifier.groupedPreferenceItem(
                    position = GroupedItemPosition.SINGLE,
                    outerRadius = SizeConstants.LargeMediumSize,
                )
            )
        }

        if (provider.supportsStartupPage) {

            item {
                PreferenceCategoryItem(title = stringResource(id = R.string.navigation))
            }

            item {
                SettingsPreferenceItem(
                    title = stringResource(id = R.string.startup_page),
                    summary = stringResource(id = R.string.summary_preference_settings_startup_page),
                    onClick = { showStartupDialog.value = true },
                    firebaseController = firebaseController,
                    ga4Event = displayActionGa4Event(
                        actionName = DisplayActionNames.OPEN_STARTUP_DIALOG,
                        preferenceKey = DisplayPreferenceKeys.STARTUP_PAGE,
                    ),
                    modifier = Modifier.groupedPreferenceItem(
                        position = GroupedItemPosition.FIRST,
                        outerRadius = SizeConstants.LargeMediumSize,
                    )
                )
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(id = R.string.show_labels_on_bottom_bar),
                    summary = stringResource(id = R.string.summary_preference_settings_show_labels_on_bottom_bar),
                    checked = uiState.showBottomBarLabels,
                    onCheckedChange = { isChecked ->
                        viewModel.onEvent(DisplaySettingsEvent.BottomBarLabelsChanged(isChecked))
                    },
                    modifier = Modifier.groupedPreferenceItem(
                        position = GroupedItemPosition.LAST,
                        outerRadius = SizeConstants.LargeMediumSize,
                    )
                )
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.language))
        }

        item {
            SettingsPreferenceItem(
                title = stringResource(id = R.string.language),
                summary = stringResource(id = R.string.summary_preference_settings_language),
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val localeIntent: Intent =
                            Intent(Settings.ACTION_APP_LOCALE_SETTINGS).setData(
                                Uri.fromParts("package", context.packageName, null)
                            )
                        val detailsIntent: Intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                                Uri.fromParts("package", context.packageName, null)
                            )

                        val openedLocaleSettings =
                            context.startActivitySafely(intent = localeIntent)
                        val openedAppDetails = if (!openedLocaleSettings) {
                            context.startActivitySafely(intent = detailsIntent)
                        } else {
                            false
                        }
                        firebaseController.logEvent(
                            displayActionEvent(
                                actionName = DisplayActionNames.OPEN_LANGUAGE_SETTINGS,
                                preferenceKey = DisplayPreferenceKeys.LANGUAGE,
                                value = when {
                                    openedLocaleSettings -> "system_locale"
                                    openedAppDetails -> "app_details"
                                    else -> "in_app_dialog"
                                },
                            )
                        )
                        if (!openedLocaleSettings && !openedAppDetails) {
                            showLanguageDialog.value = true
                        }
                    } else {
                        firebaseController.logEvent(
                            displayActionEvent(
                                actionName = DisplayActionNames.OPEN_LANGUAGE_SETTINGS,
                                preferenceKey = DisplayPreferenceKeys.LANGUAGE,
                                value = "in_app_dialog",
                            )
                        )
                        showLanguageDialog.value = true
                    }
                },
                modifier = Modifier.groupedPreferenceItem(
                    position = GroupedItemPosition.SINGLE,
                    outerRadius = SizeConstants.LargeMediumSize,
                )
            )
        }
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
