package com.d4rk.android.libs.apptoolkit.app.theme.ui

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.theme.domain.model.ThemeSettingOption
import com.d4rk.android.libs.apptoolkit.app.theme.domain.model.WallpaperSwatchColors
import com.d4rk.android.libs.apptoolkit.app.theme.style.ThemePaletteProvider.paletteById
import com.d4rk.android.libs.apptoolkit.app.theme.ui.components.WallpaperColorOptionCard
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.RadioButtonPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.SwitchCardItem
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.DynamicPaletteVariant
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.StaticPaletteIds
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.applyDynamicVariant
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val dataStore: CommonDataStore = CommonDataStore.getInstance(context = context)

    val currentThemeModeKey: String by dataStore.themeMode.collectAsStateWithLifecycle(
        initialValue = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM
    )

    val isAmoledMode: State<Boolean> =
        dataStore.amoledMode.collectAsStateWithLifecycle(initialValue = false)

    val isDynamicColors: Boolean by dataStore.dynamicColors
        .collectAsStateWithLifecycle(initialValue = true)

    val dynamicVariantIndex: Int by dataStore.dynamicPaletteVariant
        .collectAsStateWithLifecycle(initialValue = 0)

    val staticPaletteId: String by dataStore.staticPaletteId
        .collectAsStateWithLifecycle(initialValue = StaticPaletteIds.DEFAULT)

    val supportsDynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val themeOptions: List<ThemeSettingOption> = listOf(
        ThemeSettingOption(
            key = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
            displayName = stringResource(id = R.string.follow_system)
        ),
        ThemeSettingOption(
            key = DataStoreNamesConstants.THEME_MODE_DARK,
            displayName = stringResource(id = R.string.dark_mode)
        ),
        ThemeSettingOption(
            key = DataStoreNamesConstants.THEME_MODE_LIGHT,
            displayName = stringResource(id = R.string.light_mode)
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
                tertiary = scheme.tertiary
            )
        }
    }

    val staticOptions: List<String> = remember {
        listOf(
            StaticPaletteIds.DEFAULT,
            StaticPaletteIds.MONOCHROME,
            StaticPaletteIds.BLUE,
            StaticPaletteIds.GREEN,
            StaticPaletteIds.RED,
            StaticPaletteIds.YELLOW,
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

    val pagerState = rememberPagerState(
        initialPage = if (supportsDynamic && isDynamicColors) 0 else 1,
        pageCount = { 2 }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                AsyncImage(
                    model = R.drawable.il_startup,
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
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        when (page) {
                            0 -> {
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
                                            selected = index == dynamicVariantIndex,
                                            onClick = {
                                                coroutineScope.launch {
                                                    dataStore.saveDynamicColors(true)
                                                    dataStore.saveDynamicPaletteVariant(index)
                                                }
                                            }
                                        )
                                    }
                                }
                            }

                            else -> {
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
                        }
                    }
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
            }

            item {
                SwitchCardItem(
                    title = stringResource(id = R.string.amoled_mode),
                    switchState = isAmoledMode
                ) { isChecked ->
                    coroutineScope.launch { dataStore.saveAmoledMode(isChecked) }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SizeConstants.LargeSize)
                        .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize)),
                    verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)
                ) {
                    themeOptions.forEach { option: ThemeSettingOption ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(size = SizeConstants.ExtraTinySize)),
                            shape = RoundedCornerShape(size = SizeConstants.ExtraTinySize),
                        ) {
                            RadioButtonPreferenceItem(
                                text = option.displayName,
                                isChecked = (option.key == currentThemeModeKey),
                                onCheckedChange = {
                                    coroutineScope.launch {
                                        dataStore.saveThemeMode(mode = option.key)
                                        dataStore.themeModeState.value = option.key
                                    }
                                }
                            )
                        }
                    }
                }
            }

            item {
                InfoMessageSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SizeConstants.LargeSize),
                    message = stringResource(id = R.string.summary_dark_theme),
                    newLine = false,
                    learnMoreText = stringResource(id = R.string.screen_and_display_settings),
                    learnMoreAction = { IntentsHelper.openDisplaySettings(context) }
                )
            }
        }
    }
}
