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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.DynamicPaletteVariant
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.colorscheme.applyDynamicVariant
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.date.isChristmasSeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.date.isHalloweenSeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.filterSeasonalStaticPalettes
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.WallpaperSwatchColors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.toSwatchColors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ThemePaletteProvider.paletteById
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.views.WallpaperColorOptionCard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import java.time.LocalDate
import java.time.ZoneId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/** The [ThemePalettePicker] page of the wallpaper color variants. */
const val WALLPAPER_PALETTE_PAGE: Int = 0

/** The [ThemePalettePicker] page of the static palettes. */
const val OTHER_PALETTE_PAGE: Int = 1

private const val DARK_SURFACE_LUMINANCE: Float = 0.5f

/** The selection index of a row that has nothing selected. */
private const val NO_SELECTION: Int = -1

private val PaletteTabTitles: ImmutableList<Int> = persistentListOf(R.string.wallpaper_colors, R.string.other_colors)

/**
 * The color palette choice that the theme settings page and the onboarding theme page both show.
 * With wallpaper colors ([supportsDynamicColors], Android 12 and later) it is two tabs over a pager,
 * the wallpaper color variants and the static palettes; without them, the static palettes alone.
 *
 * Swatches follow the theme the app is drawn in, which can differ from the system's. A holiday
 * palette shows in its season with a badge, all year once [seasonalThemesUnlocked], and always while
 * it is the one selected. Each row opens scrolled to its selection.
 *
 * @param onStaticPaletteSelected Called with the palette id and whether it is a holiday palette in
 * its season.
 * @param onPaletteTabSelected Called with the page a tab opens, [WALLPAPER_PALETTE_PAGE] or
 * [OTHER_PALETTE_PAGE], for a caller that logs it.
 * @param horizontalPadding Inset of the tabs and of the rows' first and last swatch.
 */
@Composable
fun ThemePalettePicker(
    preferences: ThemePreferencesState,
    seasonalThemesUnlocked: Boolean,
    onDynamicPaletteSelected: (variant: Int) -> Unit,
    onStaticPaletteSelected: (id: String, seasonal: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onPaletteTabSelected: (page: Int) -> Unit = {},
    supportsDynamicColors: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
    horizontalPadding: Dp = SizeConstants.LargeSize,
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
    val rowPadding = PaddingValues(horizontal = horizontalPadding)

    val initialPage: Int = if (supportsDynamicColors && isDynamicColors) WALLPAPER_PALETTE_PAGE else OTHER_PALETTE_PAGE
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
        horizontalPadding,
        onDynamicPaletteSelected,
        onStaticPaletteSelected,
    ) {
        persistentListOf<@Composable () -> Unit>(
            {
                DynamicPaletteRow(
                    swatches = variantSwatches,
                    selectedVariant = selectedVariant,
                    state = variantRowState,
                    contentPadding = rowPadding,
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
                    contentPadding = rowPadding,
                    onSelected = onStaticPaletteSelected,
                )
            },
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
    ) {
        if (supportsDynamicColors) {
            PaletteTabs(
                pagerState = pagerState,
                onTabSelected = onPaletteTabSelected,
                modifier = Modifier.padding(horizontal = horizontalPadding),
            )
            ThemePalettePager(
                pagerState = pagerState,
                pages = palettePages,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            StaticPaletteRow(
                options = staticOptions,
                swatches = staticSwatches,
                selectedId = selectedStaticId,
                isChristmasSeason = isChristmasSeason,
                isHalloweenSeason = isHalloweenSeason,
                state = staticRowState,
                contentPadding = rowPadding,
                onSelected = onStaticPaletteSelected,
            )
        }
    }
}

/** The wallpaper and other colors tabs above the palette pager, which animate it to their page. */
@Composable
private fun PaletteTabs(
    pagerState: PagerState,
    onTabSelected: (page: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
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
    contentPadding: PaddingValues,
    onSelected: (variant: Int) -> Unit,
) {
    LazyRow(
        state = state,
        contentPadding = contentPadding,
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
    contentPadding: PaddingValues,
    onSelected: (id: String, seasonal: Boolean) -> Unit,
) {
    LazyRow(
        state = state,
        contentPadding = contentPadding,
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

private val PaletteRowArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
    space = SizeConstants.MediumSize,
    alignment = Alignment.CenterHorizontally,
)

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

@Preview(showBackground = true)
@Composable
private fun ThemePalettePickerPreview() {
    MaterialTheme {
        ThemePalettePicker(
            preferences = ThemePreferencesState(
                themeMode = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
                dynamicColors = false,
                amoledMode = false,
                dynamicPaletteVariant = 0,
                staticPaletteId = StaticPaletteIds.DEFAULT,
            ),
            seasonalThemesUnlocked = false,
            onDynamicPaletteSelected = {},
            onStaticPaletteSelected = { _, _ -> },
            supportsDynamicColors = false,
        )
    }
}
