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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openDisplaySettings
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.drawables.rememberPaletteImageVector
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.ThemeModePicker
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.ThemePalettePicker
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.isAmoledAllowed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.analytics.THEME_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.analytics.amoledToggleEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.analytics.dynamicPaletteSelectEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.analytics.openDisplaySettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.analytics.paletteTabSelectEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.analytics.staticPaletteSelectEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.analytics.themeModeSelectEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.constants.ThemeLogTags
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.contracts.ThemeSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.states.ThemeSettingsUiState
import org.koin.compose.viewmodel.koinViewModel

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
    val telemetry = LocalTelemetry.current
    val context: Context = LocalContext.current

    ScreenHost(screen = THEME_SCREEN, status = state.preferences, viewModel = viewModel) {
        ThemeSettingsScreenContent(
            state = state,
            onEvent = viewModel::onEvent,
            onPaletteTabSelected = { page ->
                telemetry.logEvent(paletteTabSelectEvent(page = page))
            },
            onDynamicPaletteSelected = { variant ->
                telemetry.logEvent(dynamicPaletteSelectEvent(variant = variant))
                viewModel.onEvent(ThemeSettingsEvent.SelectDynamicPalette(variant = variant))
            },
            onStaticPaletteSelected = { id, seasonal ->
                telemetry.logEvent(staticPaletteSelectEvent(id = id, seasonal = seasonal))
                viewModel.onEvent(ThemeSettingsEvent.SelectStaticPalette(id = id))
            },
            onThemeModeSelected = { mode ->
                telemetry.logEvent(themeModeSelectEvent(mode = mode))
                viewModel.onEvent(ThemeSettingsEvent.SelectThemeMode(mode = mode))
            },
            onAmoledModeChanged = { enabled ->
                telemetry.logEvent(amoledToggleEvent(enabled = enabled))
                viewModel.onEvent(ThemeSettingsEvent.SetAmoledMode(enabled = enabled))
            },
            onOpenDisplaySettings = {
                val opened: Boolean = context.openDisplaySettings()
                telemetry.logEvent(openDisplaySettingsEvent(opened = opened))
                if (!opened) {
                    Log.w(ThemeLogTags.THEME_SETTINGS_SCREEN, "Failed to open display settings from theme page")
                }
            },
            contentPadding = contentPadding(),
        )
    }
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
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
        modifier = modifier.fillMaxSize(),
    ) {
        animatedItem(key = "theme_preview_illustration") {
            Image(
                imageVector = rememberPaletteImageVector(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(size = 24.dp)),
            )
        }

        animatedItem(key = "theme_palette_divider") { SectionDivider() }

        animatedItem(key = "theme_palette_title") { SectionTitle(title = R.string.color_palette) }

        animatedItem(key = "theme_palette_picker") {
            ThemePalettePicker(
                preferences = preferences,
                seasonalThemesUnlocked = seasonalThemesUnlocked,
                onDynamicPaletteSelected = onDynamicPaletteSelected,
                onStaticPaletteSelected = onStaticPaletteSelected,
                onPaletteTabSelected = onPaletteTabSelected,
                supportsDynamicColors = supportsDynamicColors,
            )
        }

        animatedItem(key = "theme_mode_divider") { SectionDivider() }

        animatedItem(key = "theme_mode_title") { SectionTitle(title = R.string.theme_mode) }

        animatedItem(key = "theme_mode_picker") {
            ThemeModePicker(
                selectedMode = preferences.themeMode,
                onSelected = onThemeModeSelected,
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
            )
        }

        animatedItem(key = "theme_amoled_card") {
            SwitchCardItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.extraLargeCompact),
                title = stringResource(id = R.string.amoled_mode),
                enabled = isAmoledAllowed(preferences.themeMode),
                switchState = rememberUpdatedState(preferences.amoledMode),
                onSwitchToggled = onAmoledModeChanged,
                checkIcon = Icons.Filled.Contrast,
            )
        }

        animatedItem(key = "theme_display_settings_info") {
            InfoMessageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = MaterialTheme.spacing.extraLargeCompact),
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
    HorizontalDivider(modifier = Modifier.padding(all = MaterialTheme.spacing.small))
}

@Composable
private fun SectionTitle(@StringRes title: Int) {
    Text(
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
        text = stringResource(id = title),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
    )
}
