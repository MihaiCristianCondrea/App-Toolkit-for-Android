package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.model.OnboardingThemeChoice
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.cards.AmoledModeToggleCard
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews.DarkModePreview
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews.LightModePreview
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews.SystemModePreview
import com.d4rk.android.libs.apptoolkit.app.theme.domain.model.WallpaperSwatchColors
import com.d4rk.android.libs.apptoolkit.app.theme.ui.dedupeStaticPaletteIds
import com.d4rk.android.libs.apptoolkit.app.theme.ui.filterSeasonalStaticPalettes
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.ThemePaletteProvider.paletteById
import com.d4rk.android.libs.apptoolkit.app.theme.ui.views.ThemePalettePreviewDots
import com.d4rk.android.libs.apptoolkit.app.theme.ui.views.WallpaperColorOptionCard
import com.d4rk.android.libs.apptoolkit.core.ui.views.cards.ThemeChoicePreviewCard
import com.d4rk.android.libs.apptoolkit.core.ui.views.carousel.ThemePalettePager
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.colorscheme.DynamicPaletteVariant
import com.d4rk.android.libs.apptoolkit.core.utils.constants.colorscheme.StaticPaletteIds
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.colorscheme.applyDynamicVariant
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.datastore.rememberThemePreferencesState
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.date.isChristmasSeason
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.date.isHalloweenSeason
import com.d4rk.android.libs.apptoolkit.data.local.datastore.rememberCommonDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

private data class ThemePaletteChoice(
    val index: Int,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val previewColors: WallpaperSwatchColors?,
)

@Composable
fun ThemeOnboardingPageTab() {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val dataStore = rememberCommonDataStore()
    val themePreferences = rememberThemePreferencesState()
    val context = LocalContext.current

    val defaultThemeModeKey: String = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM
    val currentThemeMode: String = themePreferences.themeMode.ifBlank { defaultThemeModeKey }
    val isAmoledMode: Boolean = themePreferences.amoledMode
    val isDynamicColors: Boolean = themePreferences.dynamicColors
    val dynamicVariantIndex: Int = themePreferences.dynamicPaletteVariant
    val staticPaletteId: String = themePreferences.staticPaletteId

    val supportsDynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val isLightSelected = currentThemeMode == DataStoreNamesConstants.THEME_MODE_LIGHT
    val amoledAllowed = !isLightSelected

    val themeChoices: List<OnboardingThemeChoice> = listOf(
        OnboardingThemeChoice(
            key = DataStoreNamesConstants.THEME_MODE_LIGHT,
            displayName = stringResource(id = R.string.light_mode),
            icon = Icons.Filled.LightMode,
            description = stringResource(R.string.onboarding_theme_light_desc)
        ),
        OnboardingThemeChoice(
            key = DataStoreNamesConstants.THEME_MODE_DARK,
            displayName = stringResource(id = R.string.dark_mode),
            icon = Icons.Filled.DarkMode,
            description = stringResource(R.string.onboarding_theme_dark_desc)
        ),
        OnboardingThemeChoice(
            key = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
            displayName = stringResource(id = R.string.follow_system),
            icon = Icons.Filled.BrightnessAuto,
            description = stringResource(R.string.onboarding_theme_system_desc)
        )
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

    val paletteChoices = listOf(
        ThemePaletteChoice(
            index = 0,
            title = stringResource(id = R.string.wallpaper_colors),
            icon = Icons.Filled.Wallpaper,
            previewColors = variantSwatches.firstOrNull(),
        ),
        ThemePaletteChoice(
            index = 1,
            title = stringResource(id = R.string.other_colors),
            icon = Icons.Filled.Palette,
            previewColors = staticSwatches.firstOrNull(),
        ),
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SizeConstants.LargeSize),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
    ) {
        LargeVerticalSpacer()

        Text(
            text = stringResource(R.string.onboarding_theme_title),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.SemiBold, fontSize = 30.sp, textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(R.string.onboarding_theme_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = SizeConstants.LargeSize)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        ) {
            themeChoices.forEach { choice ->
                ThemeChoicePreviewCard(
                    title = choice.displayName,
                    description = choice.description,
                    icon = choice.icon,
                    isSelected = currentThemeMode == choice.key,
                    onClick = {
                        coroutineScope.launch {
                            dataStore.saveThemeMode(mode = choice.key)
                            if (choice.key == DataStoreNamesConstants.THEME_MODE_LIGHT && isAmoledMode) {
                                dataStore.saveAmoledMode(isChecked = false)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    preview = {
                        when (choice.key) {
                            DataStoreNamesConstants.THEME_MODE_LIGHT -> LightModePreview(Modifier.fillMaxWidth())
                            DataStoreNamesConstants.THEME_MODE_DARK -> DarkModePreview(Modifier.fillMaxWidth())
                            else -> SystemModePreview(Modifier.fillMaxWidth())
                        }
                    }
                )
            }
        }

        AmoledModeToggleCard(
            isAmoledMode = isAmoledMode,
            enabled = amoledAllowed,
            onCheckedChange = { isChecked ->
                if (!amoledAllowed) return@AmoledModeToggleCard
                coroutineScope.launch {
                    dataStore.saveAmoledMode(isChecked = isChecked)
                }
            }
        )

        if (supportsDynamic) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            ) {
                paletteChoices.forEach { choice ->
                    ThemeChoicePreviewCard(
                        title = choice.title,
                        description = null,
                        icon = choice.icon,
                        isSelected = pagerState.currentPage == choice.index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(choice.index)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        showPreview = choice.previewColors != null,
                        preview = {
                            choice.previewColors?.let { colors ->
                                ThemePalettePreviewDots(colors = colors)
                            }
                        }
                    )
                }
            }

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
        } else {
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
                            coroutineScope.launch {
                                dataStore.saveDynamicColors(false)
                                dataStore.saveStaticPaletteId(id)
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(SizeConstants.ExtraTinySize))
    }
}
