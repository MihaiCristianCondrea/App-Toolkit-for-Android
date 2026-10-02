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
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.DynamicPaletteVariant
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.logging.THEME_SETTINGS_LOG_TAG
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.colorscheme.applyDynamicVariant
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openDisplaySettings
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.date.isChristmasSeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.date.isHalloweenSeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.filterSeasonalStaticPalettes
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.WallpaperSwatchColors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.toSwatchColors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ThemePaletteProvider.paletteById
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.views.WallpaperColorOptionCard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.cards.ThemeChoicePreviewCard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.drawable.rememberPaletteImageVector
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.ThemePalettePager
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.dedupeStaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.isAmoledAllowed
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.previews.DarkModePreview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.previews.LightModePreview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.previews.SystemModePreview
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.contracts.ThemeSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.states.ThemeSettingsUiState
import java.time.LocalDate
import java.time.ZoneId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

private const val DARK_SURFACE_LUMINANCE: Float = 0.5f

private const val WALLPAPER_PAGE: Int = 0
private const val OTHER_PAGE: Int = 1

/** The selection index of a row that has nothing selected. */
private const val NO_SELECTION: Int = -1

private val PaletteTabTitles: ImmutableList<Int> = persistentListOf(R.string.wallpaper_colors, R.string.other_colors)

private val ThemeModes: ImmutableList<String> = persistentListOf(
    DataStoreNamesConstants.THEME_MODE_LIGHT,
    DataStoreNamesConstants.THEME_MODE_DARK,
    DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
)

private val PaletteRowPadding: PaddingValues = PaddingValues(horizontal = SizeConstants.LargeSize)

private val PaletteRowArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
    space = SizeConstants.MediumSize,
    alignment = Alignment.CenterHorizontally,
)

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
 * selection. Swatches follow the theme the app is drawn in, which can differ from the system's.
 * The palette pages are remembered, so the pager only gets new pages when what they show changes.
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
    val context: Context = LocalContext.current
    val isDynamicColors: Boolean = preferences.dynamicColors
    val staticPaletteId: String = preferences.staticPaletteId
    val isAppInDarkTheme: Boolean = MaterialTheme.colorScheme.surface.luminance() < DARK_SURFACE_LUMINANCE

    val variantSwatches: ImmutableList<WallpaperSwatchColors> =
        remember(context, supportsDynamicColors, isAppInDarkTheme) {
            if (supportsDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val base = if (isAppInDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
                DynamicPaletteVariant.indices.map { variant -> base.applyDynamicVariant(variant).toSwatchColors() }
                    .toImmutableList()
            } else {
                persistentListOf()
            }
        }

    val isChristmasSeason: Boolean = remember { LocalDate.now(ZoneId.systemDefault()).isChristmasSeason }
    val isHalloweenSeason: Boolean = remember { LocalDate.now(ZoneId.systemDefault()).isHalloweenSeason }

    val staticOptions: ImmutableList<String> =
        remember(isChristmasSeason, isHalloweenSeason, staticPaletteId, seasonalThemesUnlocked) {
            dedupeStaticPaletteIds(
                options = filterSeasonalStaticPalettes(
                    baseOptions = StaticPaletteIds.withDefault,
                    isChristmasSeason = isChristmasSeason,
                    isHalloweenSeason = isHalloweenSeason,
                    selectedPaletteId = staticPaletteId,
                    showAllYear = seasonalThemesUnlocked,
                ),
                selectedPaletteId = staticPaletteId,
            ).toImmutableList()
        }

    val staticSwatches: ImmutableList<WallpaperSwatchColors> = remember(staticOptions, isAppInDarkTheme) {
        staticOptions.map { id ->
            val palette = paletteById(id)
            (if (isAppInDarkTheme) palette.darkColorScheme else palette.lightColorScheme).toSwatchColors()
        }.toImmutableList()
    }

    val selectedVariant: Int = if (isDynamicColors) preferences.dynamicPaletteVariant else NO_SELECTION
    val selectedStaticId: String? = if (!supportsDynamicColors || !isDynamicColors) staticPaletteId else null
    val variantRowState: LazyListState = rememberScrolledToSelected(selectedIndex = selectedVariant)
    val staticRowState: LazyListState = rememberScrolledToSelected(
        selectedIndex = selectedStaticId?.let(staticOptions::indexOf) ?: NO_SELECTION,
    )

    val initialPage: Int = if (supportsDynamicColors && isDynamicColors) WALLPAPER_PAGE else OTHER_PAGE
    val pagerState: PagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { PaletteTabTitles.size },
    )
    LaunchedEffect(initialPage) {
        if (pagerState.currentPage != initialPage) {
            pagerState.scrollToPage(initialPage)
        }
    }

    val palettePages: ImmutableList<@Composable () -> Unit> = remember(
        variantSwatches,
        selectedVariant,
        variantRowState,
        staticOptions,
        staticSwatches,
        selectedStaticId,
        staticRowState,
        isChristmasSeason,
        isHalloweenSeason,
        onDynamicPaletteSelected,
        onStaticPaletteSelected,
    ) {
        persistentListOf<@Composable () -> Unit>(
            {
                DynamicPaletteRow(
                    swatches = variantSwatches,
                    selectedVariant = selectedVariant,
                    state = variantRowState,
                    onSelected = onDynamicPaletteSelected,
                )
            },
            {
                StaticPaletteRow(
                    options = staticOptions,
                    swatches = staticSwatches,
                    selectedId = selectedStaticId,
                    isChristmasSeason = isChristmasSeason,
                    isHalloweenSeason = isHalloweenSeason,
                    state = staticRowState,
                    onSelected = onStaticPaletteSelected,
                )
            },
        )
    }

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

        if (supportsDynamicColors) {
            item {
                PaletteTabs(pagerState = pagerState, onTabSelected = onPaletteTabSelected)
            }

            item {
                ThemePalettePager(
                    pagerState = pagerState,
                    pages = palettePages,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        } else {
            item {
                StaticPaletteRow(
                    options = staticOptions,
                    swatches = staticSwatches,
                    selectedId = selectedStaticId,
                    isChristmasSeason = isChristmasSeason,
                    isHalloweenSeason = isHalloweenSeason,
                    state = staticRowState,
                    onSelected = onStaticPaletteSelected,
                )
            }
        }

        item { SectionDivider() }

        item { SectionTitle(title = R.string.theme_mode) }

        item {
            ThemeModeRow(selectedMode = preferences.themeMode, onSelected = onThemeModeSelected)
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

/** The wallpaper and other colors tabs above the palette pager, which animate it to their page. */
@Composable
private fun PaletteTabs(
    pagerState: PagerState,
    onTabSelected: (page: Int) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SizeConstants.LargeSize),
    ) {
        PaletteTabTitles.forEachIndexed { page, title ->
            SegmentedButton(
                selected = pagerState.currentPage == page,
                onClick = {
                    onTabSelected(page)
                    coroutineScope.launch { pagerState.animateScrollToPage(page) }
                },
                shape = SegmentedButtonDefaults.itemShape(index = page, count = PaletteTabTitles.size),
            ) {
                Text(
                    text = stringResource(id = title),
                    modifier = Modifier.padding(vertical = SizeConstants.LargeSize),
                )
            }
        }
    }
}

/** The wallpaper color variants; [selectedVariant] is [NO_SELECTION] while a static palette is on. */
@Composable
private fun DynamicPaletteRow(
    swatches: ImmutableList<WallpaperSwatchColors>,
    selectedVariant: Int,
    state: LazyListState,
    onSelected: (variant: Int) -> Unit,
) {
    LazyRow(
        state = state,
        contentPadding = PaletteRowPadding,
        horizontalArrangement = PaletteRowArrangement,
    ) {
        itemsIndexed(items = swatches, key = { index, _ -> index }) { index, colors ->
            WallpaperColorOptionCard(
                colors = colors,
                selected = index == selectedVariant,
                onClick = { onSelected(index) },
            )
        }
    }
}

/**
 * The static palettes, with a badge on a holiday palette during its season. [selectedId] is null
 * while wallpaper colors are on.
 */
@Composable
private fun StaticPaletteRow(
    options: ImmutableList<String>,
    swatches: ImmutableList<WallpaperSwatchColors>,
    selectedId: String?,
    isChristmasSeason: Boolean,
    isHalloweenSeason: Boolean,
    state: LazyListState,
    onSelected: (id: String, seasonal: Boolean) -> Unit,
) {
    LazyRow(
        state = state,
        contentPadding = PaletteRowPadding,
        horizontalArrangement = PaletteRowArrangement,
    ) {
        itemsIndexed(items = options, key = { _, id -> id }) { index, id ->
            val seasonal: Boolean = (isChristmasSeason && id == StaticPaletteIds.CHRISTMAS) ||
                (isHalloweenSeason && id == StaticPaletteIds.HALLOWEEN)
            WallpaperColorOptionCard(
                colors = swatches[index],
                selected = id == selectedId,
                showSeasonalBadge = seasonal,
                onClick = { onSelected(id, seasonal) },
            )
        }
    }
}

@Composable
private fun ThemeModeRow(
    selectedMode: String,
    onSelected: (mode: String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SizeConstants.LargeSize)
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {
        ThemeModes.forEach { mode ->
            ThemeModeCard(
                mode = mode,
                selected = selectedMode == mode,
                onClick = { onSelected(mode) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/** One theme mode card. Any mode other than light and dark is drawn as follow system. */
@Composable
private fun ThemeModeCard(
    mode: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title: Int
    val description: Int
    val icon: ImageVector
    when (mode) {
        DataStoreNamesConstants.THEME_MODE_LIGHT -> {
            title = R.string.light_mode
            description = R.string.onboarding_theme_light_desc
            icon = Icons.Filled.LightMode
        }

        DataStoreNamesConstants.THEME_MODE_DARK -> {
            title = R.string.dark_mode
            description = R.string.onboarding_theme_dark_desc
            icon = Icons.Filled.DarkMode
        }

        else -> {
            title = R.string.follow_system
            description = R.string.onboarding_theme_system_desc
            icon = Icons.Filled.BrightnessAuto
        }
    }
    ThemeChoicePreviewCard(
        title = stringResource(id = title),
        description = stringResource(id = description),
        icon = icon,
        isSelected = selected,
        onClick = onClick,
        modifier = modifier,
        preview = {
            when (mode) {
                DataStoreNamesConstants.THEME_MODE_LIGHT -> LightModePreview(Modifier.fillMaxWidth())
                DataStoreNamesConstants.THEME_MODE_DARK -> DarkModePreview(Modifier.fillMaxWidth())
                else -> SystemModePreview(Modifier.fillMaxWidth())
            }
        },
    )
}

/**
 * A row state that opens with [selectedIndex] in view and then centers it, once, so a later pick
 * never scrolls the row under the finger. It waits for the row's first layout, which may be on a
 * pager page that is not composed yet. A negative [selectedIndex] leaves the row at its start.
 */
@Composable
private fun rememberScrolledToSelected(selectedIndex: Int): LazyListState {
    val state: LazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = selectedIndex.coerceAtLeast(0),
    )
    var centered: Boolean by rememberSaveable { mutableStateOf(selectedIndex < 0) }
    LaunchedEffect(state) {
        if (centered) return@LaunchedEffect
        val item = snapshotFlow {
            state.layoutInfo.visibleItemsInfo.firstOrNull { it.index == selectedIndex }
        }.filterNotNull().first()
        val layoutInfo = state.layoutInfo
        val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2f
        state.scrollBy(item.offset + item.size / 2f - center)
        centered = true
    }
    return state
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
    "tab" to (if (page == WALLPAPER_PAGE) "wallpaper" else "other"),
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
