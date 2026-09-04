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
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ColorScheme
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.filterSeasonalStaticPalettes
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.contracts.ThemeSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.WallpaperSwatchColors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ThemePaletteProvider.paletteById
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.views.WallpaperColorOptionCard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.theme.ThemeModeChoice
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.cards.ThemeChoicePreviewCard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.drawable.rememberPaletteImageVector
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.ThemePalettePager
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.dedupeStaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.isAmoledAllowed
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.previews.DarkModePreview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.previews.LightModePreview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.previews.SystemModePreview
import java.time.LocalDate
import java.time.ZoneId
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val THEME_SCREEN_NAME = "Theme"
private const val THEME_SCREEN_CLASS = "ThemeSettingsList"

/**
 * Theme settings content for the app.
 *
 * This composable renders a vertical list of theme-related controls:
 * - Optional header illustration.
 * - Dynamic vs static (wallpaper/other) palette pickers when the device supports dynamic color.
 * - AMOLED toggle.
 * - Theme mode selection (follow system / dark / light).
 * - An informational message with a "Learn more" action that opens system display settings.
 *
 * [ThemeSettingsViewModel] owns persisted state and mutations.
 */
@Composable
fun ThemeSettingsList(paddingValues: PaddingValues) {
    val firebaseController: FirebaseController = koinInject()
    val firebase = rememberUpdatedState(firebaseController)
    val viewModel: ThemeSettingsViewModel = koinViewModel()
    val screenState: UiStateScreen<ThemePreferencesState> by
        viewModel.uiState.collectAsStateWithLifecycle()

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = THEME_SCREEN_NAME,
        screenClass = THEME_SCREEN_CLASS,
    )
    TrackScreenState(
        firebaseController = firebaseController,
        screenName = THEME_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val themePreferences = screenState.data ?: return
    val currentThemeModeKey = themePreferences.themeMode
    val isAmoledMode = themePreferences.amoledMode
    val isDynamicColors: Boolean = themePreferences.dynamicColors
    val dynamicVariantIndex: Int = themePreferences.dynamicPaletteVariant
    val staticPaletteId: String = themePreferences.staticPaletteId
    val amoledAllowed = isAmoledAllowed(currentThemeModeKey)

    val supportsDynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val themeChoices: List<ThemeModeChoice> = listOf(
        ThemeModeChoice(
            key = DataStoreNamesConstants.THEME_MODE_LIGHT,
            title = stringResource(id = R.string.light_mode),
            description = stringResource(R.string.onboarding_theme_light_desc),
            icon = Icons.Filled.LightMode,
        ),
        ThemeModeChoice(
            key = DataStoreNamesConstants.THEME_MODE_DARK,
            title = stringResource(id = R.string.dark_mode),
            description = stringResource(R.string.onboarding_theme_dark_desc),
            icon = Icons.Filled.DarkMode,
        ),
        ThemeModeChoice(
            key = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
            title = stringResource(id = R.string.follow_system),
            description = stringResource(R.string.onboarding_theme_system_desc),
            icon = Icons.Filled.BrightnessAuto,
        ),
    )

    val isSystemInDarkThemeNow: Boolean = isSystemInDarkTheme()

    val wallpaperPreviewScheme: ColorScheme? = remember(supportsDynamic, isSystemInDarkThemeNow) {
        if (!supportsDynamic) null
        else if (isSystemInDarkThemeNow) dynamicDarkColorScheme(context)
        else dynamicLightColorScheme(context)
    }

    val variantSwatches: List<WallpaperSwatchColors> = remember(wallpaperPreviewScheme) {
        val base = wallpaperPreviewScheme ?: return@remember emptyList()
        DynamicPaletteVariant.indices.map { variant ->
            val scheme = base.applyDynamicVariant(variant)
            WallpaperSwatchColors(
                primary = scheme.primary,
                secondary = scheme.secondary,
                tertiary = scheme.tertiaryContainer,
            )
        }
    }

    val isChristmasSeason: Boolean = remember {
        LocalDate.now(ZoneId.systemDefault()).isChristmasSeason
    }
    val isHalloweenSeason: Boolean = remember {
        LocalDate.now(ZoneId.systemDefault()).isHalloweenSeason
    }

    val staticOptions: List<String> = remember(
        isChristmasSeason,
        isHalloweenSeason,
        staticPaletteId
    ) {
        val seasonalOptions = filterSeasonalStaticPalettes(
            baseOptions = StaticPaletteIds.withDefault,
            isChristmasSeason = isChristmasSeason,
            isHalloweenSeason = isHalloweenSeason,
            selectedPaletteId = staticPaletteId
        )
        dedupeStaticPaletteIds(
            options = seasonalOptions,
            selectedPaletteId = staticPaletteId
        )
    }

    val staticSwatches: List<WallpaperSwatchColors> =
        remember(staticOptions, isSystemInDarkThemeNow) {
            staticOptions.map { id ->
                val p = paletteById(id)
                val scheme = if (isSystemInDarkThemeNow) p.darkColorScheme else p.lightColorScheme
                WallpaperSwatchColors(scheme.primary, scheme.secondary, scheme.tertiary)
            }
        }

    val tabTitles = listOf(
        stringResource(id = R.string.wallpaper_colors),
        stringResource(id = R.string.other_colors)
    )

    val initialPagerPage = if (supportsDynamic && isDynamicColors) 0 else 1
    val pagerState = rememberPagerState(
        initialPage = initialPagerPage,
        pageCount = { 2 }
    )

    LaunchedEffect(initialPagerPage) {
        if (pagerState.currentPage != initialPagerPage) {
            pagerState.scrollToPage(initialPagerPage)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = paddingValues,
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
                        .clip(
                            RoundedCornerShape(
                                size = SizeConstants.LargeSize + SizeConstants.SmallSize
                            )
                        )
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(all = SizeConstants.SmallSize))
            }

            item {
                Text(
                    modifier = Modifier.padding(horizontal = SizeConstants.LargeSize),
                    text = stringResource(id = R.string.color_palette),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            if (supportsDynamic) {
                item {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = SizeConstants.LargeSize)
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            SegmentedButton(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    firebase.value.logEvent(
                                        AnalyticsEvent(
                                            name = "theme_tab_select",
                                            params = mapOf(
                                                SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(
                                                    THEME_SCREEN_NAME
                                                ),
                                                "tab" to AnalyticsValue.Str(
                                                    if (index == 0) "wallpaper" else "other"
                                                ),
                                            ),
                                        ),
                                    )
                                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                                },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = tabTitles.size
                                )
                            ) {
                                Text(
                                    text = title,
                                    modifier = Modifier.padding(vertical = SizeConstants.LargeSize)
                                )
                            }
                        }
                    }
                }

                item {
                    ThemePalettePager(
                        pagerState = pagerState,
                        pages = persistentListOf(
                            {
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = SizeConstants.LargeSize),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        space = SizeConstants.MediumSize,
                                        alignment = Alignment.CenterHorizontally
                                    )
                                ) {
                                    itemsIndexed(
                                        items = variantSwatches,
                                        key = { index, _ -> index }
                                    ) { index, palette ->
                                        WallpaperColorOptionCard(
                                            colors = palette,
                                            selected = isDynamicColors && index == dynamicVariantIndex,
                                            onClick = {
                                                firebase.value.logEvent(
                                                    AnalyticsEvent(
                                                        name = "theme_palette_select",
                                                        params = mapOf(
                                                            "screen" to AnalyticsValue.Str(
                                                                THEME_SCREEN_NAME
                                                            ),
                                                            "palette_type" to AnalyticsValue.Str("dynamic"),
                                                            "variant" to AnalyticsValue.Str(index.toString()),
                                                        ),
                                                    ),
                                                )
                                                viewModel.onEvent(
                                                    ThemeSettingsEvent.SelectDynamicPalette(index)
                                                )
                                            }
                                        )
                                    }
                                }
                            },
                            {
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = SizeConstants.LargeSize),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        space = SizeConstants.MediumSize,
                                        alignment = Alignment.CenterHorizontally
                                    )
                                ) {
                                    itemsIndexed(
                                        items = staticOptions,
                                        key = { _, id -> id }
                                    ) { index, id ->
                                        WallpaperColorOptionCard(
                                            colors = staticSwatches[index],
                                            selected = !isDynamicColors && id == staticPaletteId,
                                            showSeasonalBadge = (isChristmasSeason && id == StaticPaletteIds.CHRISTMAS) ||
                                                    (isHalloweenSeason && id == StaticPaletteIds.HALLOWEEN),
                                            onClick = {
                                                firebase.value.logEvent(
                                                    AnalyticsEvent(
                                                        name = "theme_palette_select",
                                                        params = mapOf(
                                                            "screen" to AnalyticsValue.Str(
                                                                THEME_SCREEN_NAME
                                                            ),
                                                            "palette_type" to AnalyticsValue.Str("static"),
                                                            "palette_id" to AnalyticsValue.Str(id),
                                                            "seasonal" to AnalyticsValue.Str(
                                                                ((isChristmasSeason && id == StaticPaletteIds.CHRISTMAS) ||
                                                                        (isHalloweenSeason && id == StaticPaletteIds.HALLOWEEN)).toString()
                                                            ),
                                                        ),
                                                    ),
                                                )
                                                viewModel.onEvent(
                                                    ThemeSettingsEvent.SelectStaticPalette(id)
                                                )
                                            }
                                        )
                                    }
                                }
                            },
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            } else {
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = SizeConstants.LargeSize),
                        horizontalArrangement = Arrangement.spacedBy(
                            space = SizeConstants.MediumSize,
                            alignment = Alignment.CenterHorizontally
                        )
                    ) {
                        itemsIndexed(
                            items = staticOptions,
                            key = { _, id -> id }
                        ) { index, id ->
                            WallpaperColorOptionCard(
                                colors = staticSwatches[index],
                                selected = id == staticPaletteId,
                                showSeasonalBadge = (isChristmasSeason && id == StaticPaletteIds.CHRISTMAS) ||
                                        (isHalloweenSeason && id == StaticPaletteIds.HALLOWEEN),
                                onClick = {
                                    firebase.value.logEvent(
                                        AnalyticsEvent(
                                            name = "theme_palette_select",
                                            params = mapOf(
                                                SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(
                                                    THEME_SCREEN_NAME
                                                ),
                                                "palette_type" to AnalyticsValue.Str("static"),
                                                "palette_id" to AnalyticsValue.Str(id),
                                                "seasonal" to AnalyticsValue.Str(
                                                    ((isChristmasSeason && id == StaticPaletteIds.CHRISTMAS) ||
                                                            (isHalloweenSeason && id == StaticPaletteIds.HALLOWEEN)).toString()
                                                ),
                                            ),
                                        ),
                                    )
                                    viewModel.onEvent(ThemeSettingsEvent.SelectStaticPalette(id))
                                }
                            )
                        }
                    }
                }
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(all = SizeConstants.SmallSize))
            }

            item {
                Text(
                    modifier = Modifier.padding(horizontal = SizeConstants.LargeSize),
                    text = stringResource(id = R.string.theme_mode),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SizeConstants.LargeSize)
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
                ) {
                    themeChoices.forEach { choice ->
                        ThemeChoicePreviewCard(
                            title = choice.title,
                            description = choice.description,
                            icon = choice.icon,
                            isSelected = currentThemeModeKey == choice.key,
                            onClick = {
                                firebase.value.logEvent(
                                    AnalyticsEvent(
                                        name = SettingsAnalytics.Events.THEME_SWITCH,
                                        params = mapOf(
                                            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(
                                                THEME_SCREEN_NAME
                                            ),
                                            SettingsAnalytics.Params.THEME_MODE to AnalyticsValue.Str(
                                                choice.key
                                            ),
                                        ),
                                    ),
                                )
                                viewModel.onEvent(ThemeSettingsEvent.SelectThemeMode(choice.key))
                            },
                            modifier = Modifier.weight(1f),
                            preview = {
                                when (choice.key) {
                                    DataStoreNamesConstants.THEME_MODE_LIGHT -> LightModePreview(
                                        Modifier.fillMaxWidth()
                                    )

                                    DataStoreNamesConstants.THEME_MODE_DARK -> DarkModePreview(
                                        Modifier.fillMaxWidth()
                                    )

                                    else -> SystemModePreview(Modifier.fillMaxWidth())
                                }
                            }
                        )
                    }
                }
            }

            item {
                SwitchCardItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SizeConstants.MediumSize * 2),
                    title = stringResource(id = R.string.amoled_mode),
                    enabled = amoledAllowed,
                    switchState = rememberUpdatedState(isAmoledMode),
                    onSwitchToggled = { isChecked ->
                        firebase.value.logEvent(
                            AnalyticsEvent(
                                name = "theme_toggle_amoled",
                                params = mapOf(
                                    SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(
                                        THEME_SCREEN_NAME
                                    ),
                                    "enabled" to AnalyticsValue.Str(isChecked.toString()),
                                ),
                            ),
                        )
                        viewModel.onEvent(ThemeSettingsEvent.SetAmoledMode(isChecked))
                    },
                    checkIcon = Icons.Filled.Contrast
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
                    learnMoreAction = {
                        val opened = context.openDisplaySettings()
                        firebase.value.logEvent(
                            AnalyticsEvent(
                                name = "theme_open_display_settings",
                                params = mapOf(
                                    SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(
                                        THEME_SCREEN_NAME
                                    ),
                                    "opened" to AnalyticsValue.Str(opened.toString()),
                                ),
                            ),
                        )
                        if (!opened) {
                            Log.w(
                                THEME_SETTINGS_LOG_TAG,
                                "Failed to open display settings from theme page"
                            )
                        }
                    }
                )
            }
        }
    }
}
