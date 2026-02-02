package com.d4rk.android.libs.apptoolkit.app.theme.ui

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
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews.DarkModePreview
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews.LightModePreview
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews.SystemModePreview
import com.d4rk.android.libs.apptoolkit.app.theme.domain.model.WallpaperSwatchColors
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.ThemePaletteProvider.paletteById
import com.d4rk.android.libs.apptoolkit.app.theme.ui.views.WallpaperColorOptionCard
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.rememberCommonDataStore
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsEvent
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsValue
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.theme.ThemeModeChoice
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.cards.ThemeChoicePreviewCard
import com.d4rk.android.libs.apptoolkit.core.ui.views.drawable.rememberPaletteImageVector
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.theme.ThemePalettePager
import com.d4rk.android.libs.apptoolkit.core.ui.views.theme.dedupeStaticPaletteIds
import com.d4rk.android.libs.apptoolkit.core.ui.views.theme.isAmoledAllowed
import com.d4rk.android.libs.apptoolkit.core.utils.constants.colorscheme.DynamicPaletteVariant
import com.d4rk.android.libs.apptoolkit.core.utils.constants.colorscheme.StaticPaletteIds
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.logging.THEME_SETTINGS_LOG_TAG
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.colorscheme.applyDynamicVariant
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openDisplaySettings
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.datastore.rememberThemePreferencesState
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.date.isChristmasSeason
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.date.isHalloweenSeason
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.time.LocalDate
import java.time.ZoneId

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
 * The current selections are read from [CommonDataStore] and updates are persisted asynchronously.
 */
@Composable
fun ThemeSettingsList(paddingValues: PaddingValues) {
    val firebaseController: FirebaseController = koinInject()
    val firebase = rememberUpdatedState(firebaseController)

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = THEME_SCREEN_NAME,
        screenClass = THEME_SCREEN_CLASS,
    )
    TrackScreenState(
        firebaseController = firebaseController,
        screenName = THEME_SCREEN_NAME,
        screenState = ScreenState.Success(),
    )

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val dataStore = rememberCommonDataStore()

    val themePreferences = rememberThemePreferencesState()
    val currentThemeModeKey = themePreferences.themeMode
    val isAmoledMode = rememberUpdatedState(themePreferences.amoledMode)
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
                                                "screen" to AnalyticsValue.Str(THEME_SCREEN_NAME),
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
                        pages = listOf(
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
                                                coroutineScope.launch {
                                                    dataStore.saveDynamicColors(true)
                                                    dataStore.saveDynamicPaletteVariant(index)
                                                }
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
                                                coroutineScope.launch {
                                                    dataStore.saveDynamicColors(false)
                                                    dataStore.saveStaticPaletteId(id)
                                                }
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
                                                "screen" to AnalyticsValue.Str(THEME_SCREEN_NAME),
                                                "palette_type" to AnalyticsValue.Str("static"),
                                                "palette_id" to AnalyticsValue.Str(id),
                                                "seasonal" to AnalyticsValue.Str(
                                                    ((isChristmasSeason && id == StaticPaletteIds.CHRISTMAS) ||
                                                            (isHalloweenSeason && id == StaticPaletteIds.HALLOWEEN)).toString()
                                                ),
                                            ),
                                        ),
                                    )
                                    coroutineScope.launch {
                                        dataStore.saveDynamicColors(false)
                                        dataStore.saveStaticPaletteId(id)
                                    }
                                }
                            )
                        }
                    }
                }
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
                                        name = "theme_mode_select",
                                        params = mapOf(
                                            "screen" to AnalyticsValue.Str(THEME_SCREEN_NAME),
                                            "mode" to AnalyticsValue.Str(choice.key),
                                        ),
                                    ),
                                )
                                coroutineScope.launch {
                                    dataStore.saveThemeMode(mode = choice.key)
                                    dataStore.themeModeState.value = choice.key
                                    if (choice.key == DataStoreNamesConstants.THEME_MODE_LIGHT &&
                                        isAmoledMode.value
                                    ) {
                                        dataStore.saveAmoledMode(isChecked = false)
                                    }
                                }
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
                    switchState = isAmoledMode,
                    onSwitchToggled = { isChecked ->
                        firebase.value.logEvent(
                            AnalyticsEvent(
                                name = "theme_toggle_amoled",
                                params = mapOf(
                                    "screen" to AnalyticsValue.Str(THEME_SCREEN_NAME),
                                    "enabled" to AnalyticsValue.Str(isChecked.toString()),
                                ),
                            ),
                        )
                        coroutineScope.launch { dataStore.saveAmoledMode(isChecked) }
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
                                    "screen" to AnalyticsValue.Str(THEME_SCREEN_NAME),
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
