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

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.logging.THEME_SETTINGS_LOG_TAG
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openDisplaySettings
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.drawable.rememberPaletteImageVector
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.ThemeModePicker
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.ThemePalettePicker
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.WALLPAPER_PALETTE_PAGE
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.isAmoledAllowed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.contracts.ThemeSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.states.ThemeSettingsUiState
import org.koin.compose.viewmodel.koinViewModel

private const val THEME_SCREEN_NAME = "Theme"
private const val THEME_SCREEN_CLASS = "ThemeSettingsScreen"

/**
 * The theme settings page: the color palette, the theme mode, AMOLED, and a link to the system
 * display settings.
 *
 * Owns [ThemeSettingsViewModel], tracking and messages. Each tap logs its GA4 event before it
 * reaches the ViewModel.
 */
@Composable
fun ThemeSettingsScreen() {
    val viewModel: ThemeSettingsViewModel = koinViewModel()
    val state: ThemeSettingsUiState by viewModel.state.collectAsStateWithLifecycle()
    val telemetryRepository = LocalTelemetry.current
    val context: Context = LocalContext.current

    TrackScreenView(
        screenName = THEME_SCREEN_NAME,
        screenClass = THEME_SCREEN_CLASS,
    )

    TrackScreenState(
        screenName = THEME_SCREEN_NAME,
        state = state.preferences,
    )

    ThemeSettingsScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onPaletteTabSelected = { page ->
            telemetryRepository.logEvent(paletteTabSelectEvent(page = page))
        },
        onDynamicPaletteSelected = { variant ->
            telemetryRepository.logEvent(dynamicPaletteSelectEvent(variant = variant))
            viewModel.onEvent(ThemeSettingsEvent.SelectDynamicPalette(variant))
        },
        onStaticPaletteSelected = { id, seasonal ->
            telemetryRepository.logEvent(staticPaletteSelectEvent(id = id, seasonal = seasonal))
            viewModel.onEvent(ThemeSettingsEvent.SelectStaticPalette(id))
        },
        onThemeModeSelected = { mode ->
            telemetryRepository.logEvent(themeModeSelectEvent(mode = mode))
            viewModel.onEvent(ThemeSettingsEvent.SelectThemeMode(mode))
        },
        onAmoledModeChanged = { enabled ->
            telemetryRepository.logEvent(amoledToggleEvent(enabled = enabled))
            viewModel.onEvent(ThemeSettingsEvent.SetAmoledMode(enabled))
        },
        onOpenDisplaySettings = {
            val opened: Boolean = context.openDisplaySettings()
            telemetryRepository.logEvent(openDisplaySettingsEvent(opened = opened))
            if (!opened) {
                Log.w(THEME_SETTINGS_LOG_TAG, "Failed to open display settings from theme page")
            }
        },
        contentPadding = contentPadding(),
    )

    MessageHost(viewModel = viewModel)
}

/**
 * Renders [state] and reports each tap through its own callback, so the caller can log it.
 *
 * @param onEvent Receives [ThemeSettingsEvent.Load] from the failure screen's retry.
 * @param onPaletteTabSelected Called with the pager page a palette tab opens.
 * @param onStaticPaletteSelected Called with the palette id and whether it is a holiday palette in
 * its season.
 * @param contentPadding Padding from the shell, applied inside the list and the state screens.
 * @param supportsDynamicColors Whether the device offers wallpaper colors, which adds the palette
 * tabs.
 */
@Composable
internal fun ThemeSettingsScreenContent(
    state: ThemeSettingsUiState,
    onEvent: (ThemeSettingsEvent) -> Unit,
    onPaletteTabSelected: (page: Int) -> Unit,
    onDynamicPaletteSelected: (variant: Int) -> Unit,
    onStaticPaletteSelected: (id: String, seasonal: Boolean) -> Unit,
    onThemeModeSelected: (mode: String) -> Unit,
    onAmoledModeChanged: (enabled: Boolean) -> Unit,
    onOpenDisplaySettings: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    supportsDynamicColors: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
) {
    ScreenStateHandler(
        state = state.preferences,
        modifier = modifier,
        contentPadding = contentPadding,
        onRetry = { onEvent(ThemeSettingsEvent.Load) },
    ) { ready ->
        ThemeSettingsList(
            preferences = ready.value,
            seasonalThemesUnlocked = state.seasonalThemesUnlocked,
            supportsDynamicColors = supportsDynamicColors,
            contentPadding = contentPadding,
            onPaletteTabSelected = onPaletteTabSelected,
            onDynamicPaletteSelected = onDynamicPaletteSelected,
            onStaticPaletteSelected = onStaticPaletteSelected,
            onThemeModeSelected = onThemeModeSelected,
            onAmoledModeChanged = onAmoledModeChanged,
            onOpenDisplaySettings = onOpenDisplaySettings,
        )
    }
}

/**
 * The page's list once [preferences] have loaded, so the palette rows open on the stored
 * selection. The palette and theme mode choices are the shared [ThemePalettePicker] and
 * [ThemeModePicker], which the onboarding theme page shows too.
 */
@Composable
private fun ThemeSettingsList(
    preferences: ThemePreferencesState,
    seasonalThemesUnlocked: Boolean,
    supportsDynamicColors: Boolean,
    contentPadding: PaddingValues,
    onPaletteTabSelected: (page: Int) -> Unit,
    onDynamicPaletteSelected: (variant: Int) -> Unit,
    onStaticPaletteSelected: (id: String, seasonal: Boolean) -> Unit,
    onThemeModeSelected: (mode: String) -> Unit,
    onAmoledModeChanged: (enabled: Boolean) -> Unit,
    onOpenDisplaySettings: () -> Unit,
) {
    LazyColumn(
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Image(
                imageVector = rememberPaletteImageVector(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SizeConstants.TwoHundredTwentySize)
                    .clip(RoundedCornerShape(size = SizeConstants.LargeSize + SizeConstants.SmallSize)),
            )
        }

        item { SectionDivider() }

        item { SectionTitle(title = R.string.color_palette) }

        item {
            ThemePalettePicker(
                preferences = preferences,
                seasonalThemesUnlocked = seasonalThemesUnlocked,
                onDynamicPaletteSelected = onDynamicPaletteSelected,
                onStaticPaletteSelected = onStaticPaletteSelected,
                onPaletteTabSelected = onPaletteTabSelected,
                supportsDynamicColors = supportsDynamicColors,
            )
        }

        item { SectionDivider() }

        item { SectionTitle(title = R.string.theme_mode) }

        item {
            ThemeModePicker(
                selectedMode = preferences.themeMode,
                onSelected = onThemeModeSelected,
                modifier = Modifier.padding(horizontal = SizeConstants.LargeSize),
            )
        }

        item {
            SwitchCardItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SizeConstants.MediumSize * 2),
                title = stringResource(id = R.string.amoled_mode),
                enabled = isAmoledAllowed(preferences.themeMode),
                switchState = rememberUpdatedState(preferences.amoledMode),
                onSwitchToggled = onAmoledModeChanged,
                checkIcon = Icons.Filled.Contrast,
            )
        }

        item {
            InfoMessageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = SizeConstants.MediumSize * 2),
                message = stringResource(id = R.string.summary_dark_theme),
                newLine = false,
                learnMoreText = stringResource(id = R.string.screen_and_display_settings),
                learnMoreAction = onOpenDisplaySettings,
            )
        }
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(modifier = Modifier.padding(all = SizeConstants.SmallSize))
}

@Composable
private fun SectionTitle(@StringRes title: Int) {
    Text(
        modifier = Modifier.padding(horizontal = SizeConstants.LargeSize),
        text = stringResource(id = title),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
    )
}

private fun themeEvent(name: String, vararg params: Pair<String, String>): AnalyticsEvent = AnalyticsEvent(
    name = name,
    params = buildMap<String, AnalyticsValue> {
        put(SettingsAnalytics.Params.SCREEN, AnalyticsValue.Str(THEME_SCREEN_NAME))
        params.forEach { (key, value) -> put(key, AnalyticsValue.Str(value)) }
    },
)

private fun paletteTabSelectEvent(page: Int): AnalyticsEvent = themeEvent(
    "theme_tab_select",
    "tab" to (if (page == WALLPAPER_PALETTE_PAGE) "wallpaper" else "other"),
)

private fun dynamicPaletteSelectEvent(variant: Int): AnalyticsEvent = themeEvent(
    "theme_palette_select",
    "palette_type" to "dynamic",
    "variant" to variant.toString(),
)

private fun staticPaletteSelectEvent(id: String, seasonal: Boolean): AnalyticsEvent = themeEvent(
    "theme_palette_select",
    "palette_type" to "static",
    "palette_id" to id,
    "seasonal" to seasonal.toString(),
)

private fun themeModeSelectEvent(mode: String): AnalyticsEvent = themeEvent(
    SettingsAnalytics.Events.THEME_SWITCH,
    SettingsAnalytics.Params.THEME_MODE to mode,
)

private fun amoledToggleEvent(enabled: Boolean): AnalyticsEvent = themeEvent(
    "theme_toggle_amoled",
    "enabled" to enabled.toString(),
)

private fun openDisplaySettingsEvent(opened: Boolean): AnalyticsEvent = themeEvent(
    "theme_open_display_settings",
    "opened" to opened.toString(),
)

private val PreviewPreferences = ThemePreferencesState(
    themeMode = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
    dynamicColors = false,
    amoledMode = false,
    dynamicPaletteVariant = 0,
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
