package com.d4rk.android.libs.apptoolkit.app.display.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.os.LocaleListCompat
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.display.ui.views.dialogs.SelectLanguageAlertDialog
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.DisplaySettingsProvider
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.rememberCommonDataStore
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.effects.collectDataStoreState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItemWithDivider
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraTinyVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.logging.DISPLAY_SETTINGS_LOG_TAG
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.startActivitySafely
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel


private const val DISPLAY_SETTINGS_SCREEN_NAME = "DisplaySettings"
private const val DISPLAY_SETTINGS_SCREEN_CLASS = "DisplaySettingsScreen"

/**
 * A composable function that displays a comprehensive list of display-related settings.
 *
 * This screen allows users to manage various UI preferences including:
 * - **Appearance:** Dark theme toggle (with system sync detection) and Dynamic Colors (Android 12+).
 * - **App Behavior:** Global settings like "Bouncy Buttons" animations.
 * - **Navigation:** Configuration for the startup page and visibility of bottom bar labels.
 * - **Language:** Access to per-app language settings via system settings (Android 13+) or an internal dialog.
 *
 * The function utilizes [CommonDataStore] for persistence and [DisplaySettingsProvider]
 * for navigation and custom dialog implementations.
 *
 * @param paddingValues The padding to be applied to the [LazyColumn] container,
 * typically used to avoid overlap with system bars or scaffolds.
 */
@Composable
fun DisplaySettingsScreen(
    paddingValues: PaddingValues = PaddingValues(),
) {
    val viewModel: DisplaySettingsViewModel = koinViewModel()
    val screenState: UiStateScreen<*> by viewModel.uiState.collectAsStateWithLifecycle()
    val provider: DisplaySettingsProvider = koinInject()
    val firebaseController: FirebaseController = koinInject()

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

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val dataStore: CommonDataStore = rememberCommonDataStore()

    val showLanguageDialog = rememberSaveable { mutableStateOf(false) }
    val showStartupDialog = rememberSaveable { mutableStateOf(false) }

    val currentThemeModeState = dataStore.themeMode.collectDataStoreState(
        initial = { DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM },
        logTag = DISPLAY_SETTINGS_LOG_TAG,
        onErrorReset = { mutableState ->
            mutableState.value = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM
            dataStore.themeModeState.value = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM
        },
    )
    val currentThemeModeKey: String by currentThemeModeState

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

    val isDynamicColorsState = dataStore.dynamicColors.collectDataStoreState(
        initial = { true },
        logTag = DISPLAY_SETTINGS_LOG_TAG,
    )
    val isDynamicColors: Boolean by isDynamicColorsState

    val bouncyButtonsState = dataStore.bouncyButtons.collectDataStoreState(
        initial = { true },
        logTag = DISPLAY_SETTINGS_LOG_TAG,
    )
    val bouncyButtons: Boolean by bouncyButtonsState

    val showLabelsOnBottomBarState = dataStore.getShowBottomBarLabels()
        .collectDataStoreState(initial = { true }, logTag = DISPLAY_SETTINGS_LOG_TAG)
    val showLabelsOnBottomBar: Boolean by showLabelsOnBottomBarState

    val setThemeMode: (String) -> Unit = remember(coroutineScope, dataStore) {
        { mode: String ->
            coroutineScope.launch {
                dataStore.saveThemeMode(mode = mode)
                dataStore.themeModeState.value = mode
            }
        }
    }

    val onDarkThemeChanged: (Boolean) -> Unit = remember(setThemeMode) {
        { isChecked: Boolean ->
            setThemeMode(
                if (isChecked) DataStoreNamesConstants.THEME_MODE_DARK
                else DataStoreNamesConstants.THEME_MODE_LIGHT
            )
        }
    }

    ScreenStateHandler(
        screenState = screenState,
        onLoading = { LoadingScreen() },
        onEmpty = { NoDataScreen(paddingValues = paddingValues) },
        onError = { NoDataScreen(isError = true, paddingValues = paddingValues) },
        onSuccess = {
            LazyColumn(contentPadding = paddingValues, modifier = Modifier.fillMaxHeight()) {
        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.appearance))
            SmallVerticalSpacer()

            Column(
                modifier = Modifier
                    .padding(horizontal = SizeConstants.LargeSize)
                    .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            ) {
                SwitchPreferenceItemWithDivider(
                    title = stringResource(id = R.string.dark_theme),
                    summary = themeSummary,
                    checked = isDarkThemeActive,
                    onCheckedChange = onDarkThemeChanged,
                    onSwitchClick = onDarkThemeChanged,
                    onClick = { provider.openThemeSettings() }
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ExtraTinyVerticalSpacer()

                    SwitchPreferenceItem(
                        title = stringResource(id = R.string.dynamic_colors),
                        summary = stringResource(id = R.string.summary_preference_settings_dynamic_colors),
                        checked = isDynamicColors,
                        onCheckedChange = { isChecked ->
                            coroutineScope.launch { dataStore.saveDynamicColors(isChecked = isChecked) }
                        }
                    )
                }
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.app_behavior))
            SmallVerticalSpacer()

            Column(
                modifier = Modifier
                    .padding(horizontal = SizeConstants.LargeSize)
                    .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            ) {
                SwitchPreferenceItem(
                    title = stringResource(id = R.string.bounce_buttons),
                    summary = stringResource(id = R.string.summary_preference_settings_bounce_buttons),
                    checked = bouncyButtons,
                    onCheckedChange = { isChecked ->
                        coroutineScope.launch { dataStore.saveBouncyButtons(isChecked = isChecked) }
                    }
                )
            }
        }

        if (provider.supportsStartupPage) {
            item {
                PreferenceCategoryItem(title = stringResource(id = R.string.navigation))
                SmallVerticalSpacer()

                Column(
                    modifier = Modifier
                        .padding(horizontal = SizeConstants.LargeSize)
                        .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
                ) {
                    SettingsPreferenceItem(
                        title = stringResource(id = R.string.startup_page),
                        summary = stringResource(id = R.string.summary_preference_settings_startup_page),
                        onClick = { showStartupDialog.value = true }
                    )

                    if (showStartupDialog.value) {
                        provider.StartupPageDialog(
                            onDismiss = { showStartupDialog.value = false }
                        ) { }
                    }

                    ExtraTinyVerticalSpacer()

                    SwitchPreferenceItem(
                        title = stringResource(id = R.string.show_labels_on_bottom_bar),
                        summary = stringResource(id = R.string.summary_preference_settings_show_labels_on_bottom_bar),
                        checked = showLabelsOnBottomBar,
                        onCheckedChange = { isChecked ->
                            coroutineScope.launch { dataStore.saveShowLabelsOnBottomBar(isChecked = isChecked) }
                        }
                    )
                }
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.language))
            SmallVerticalSpacer()

            Column(
                modifier = Modifier
                    .padding(horizontal = SizeConstants.LargeSize)
                    .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            ) {
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
                            if (!openedLocaleSettings && !openedAppDetails) {
                                showLanguageDialog.value = true
                            }
                        } else {
                            showLanguageDialog.value = true
                        }
                    }
                )
            }

            if (showLanguageDialog.value) {
                SelectLanguageAlertDialog(
                    onDismiss = { showLanguageDialog.value = false },
                    onLanguageSelected = { newLanguageCode: String ->
                        showLanguageDialog.value = false
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.forLanguageTags(newLanguageCode)
                        )
                    }
                )
            }
        }
    }
            }
        },
    )
}
