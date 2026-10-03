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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.models.AppListItem
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states.AppsListFilter
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states.isAvailable
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.AppCard
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.utils.buildAppListItems
import com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.constants.AdsConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdCache
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberNativeAdCache
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.ads.AppsListNativeAdCard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.FilterChipItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.TopListFilters
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.NavigationBarSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * Adaptive app grid with host-controlled favorites, installed state, filtering, and actions.
 * Ads are interleaved after filtering and search.
 *
 * @param contentPadding Padding from the shell, added inside the grid's own spacing.
 * @param adUnitId The native ad slots' unit, or null when no ad should show.
 * @param searchQuery Matches app names, packages, and short descriptions; blank shows every
 * filtered app.
 * @param adFrequency Number of apps between native ad slots.
 */
@Composable
fun AppsList(
    allApps: ImmutableList<AppInfo>,
    selectedFilter: AppsListFilter,
    favorites: ImmutableSet<String>,
    installedPackages: ImmutableSet<String>,
    contentPadding: PaddingValues,
    adUnitId: String?,
    onFilterSelected: (AppsListFilter) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onShareClick: (AppInfo) -> Unit,
    windowWidthSizeClass: AppWindowWidthSizeClass,
    adFrequency: Int = AdsConstants.APPS_LIST_AD_FREQUENCY,
    searchQuery: String = "",
) {
    val apps: ImmutableList<AppInfo> = remember(
        allApps,
        selectedFilter,
        installedPackages,
        favorites,
        searchQuery,
    ) {
        allApps.filterFor(
            filter = selectedFilter,
            installedPackages = installedPackages,
            favorites = favorites,
        ).search(searchQuery).toImmutableList()
    }
    val adsEnabled = adUnitId != null

    val columnCount = remember(windowWidthSizeClass) {
        when (windowWidthSizeClass) {
            AppWindowWidthSizeClass.Compact -> 2
            AppWindowWidthSizeClass.Medium -> 3
            AppWindowWidthSizeClass.Expanded -> 4
            AppWindowWidthSizeClass.Large -> 5
            AppWindowWidthSizeClass.ExtraLarge -> 6
        }
    }

    val listState = rememberLazyGridState()

    val items: ImmutableList<AppListItem> = remember(apps, adsEnabled, adFrequency) {
        buildAppListItems(apps, adsEnabled, adFrequency)
    }

    AppsGrid(
        items = items,
        allAppsCount = allApps.size,
        favorites = favorites,
        installedPackages = installedPackages,
        selectedFilter = selectedFilter,
        onFilterSelected = onFilterSelected,
        paddingValues = contentPadding,
        columnCount = columnCount,
        listState = listState,
        onFavoriteToggle = onFavoriteToggle,
        onAppClick = onAppClick,
        onShareClick = onShareClick,
        adUnitId = adUnitId.orEmpty(),
    )
}

/**
 * Owns the native-ad cache outside lazy cells so scrolling away and back reuses loaded ads.
 * Cell identity includes the selected filter to isolate exiting cells from their replacements.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AppsGrid(
    items: ImmutableList<AppListItem>,
    allAppsCount: Int,
    favorites: ImmutableSet<String>,
    installedPackages: ImmutableSet<String>,
    selectedFilter: AppsListFilter,
    onFilterSelected: (AppsListFilter) -> Unit,
    paddingValues: PaddingValues,
    columnCount: Int,
    listState: LazyGridState,
    onFavoriteToggle: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onShareClick: (AppInfo) -> Unit,
    adUnitId: String,
) {
    val adCache: NativeAdCache = rememberNativeAdCache()

    val layoutDirection = LocalLayoutDirection.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = columnCount),
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding() + SizeConstants.LargeSize,
            bottom = paddingValues.calculateBottomPadding() + SizeConstants.LargeSize,
            start = paddingValues.calculateStartPadding(layoutDirection) + SizeConstants.LargeSize,
            end = paddingValues.calculateEndPadding(layoutDirection) + SizeConstants.LargeSize,
        ),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
    ) {
        item(span = { GridItemSpan(columnCount) }, contentType = "filters") {
            AppsListFilters(
                allAppsCount = allAppsCount,
                installedPackages = installedPackages,
                favorites = favorites,
                selectedFilter = selectedFilter,
                onFilterSelected = onFilterSelected,
            )
        }

        itemsIndexed(
            items = items,
            key = { index, item ->
                appListItemKey(selectedFilter = selectedFilter, index = index, item = item)
            },
            span = { _, item ->
                when (item) {
                    is AppListItem.Ad -> GridItemSpan(1)
                    is AppListItem.App -> GridItemSpan(1)
                }
            },
            contentType = { _, item ->
                when (item) {
                    is AppListItem.App -> "app"
                    is AppListItem.Ad -> "ad"
                }
            }
        ) { index, item ->
            when (item) {
                is AppListItem.App -> {
                    val packageName = item.appInfo.packageName
                    val isFavorite = favorites.contains(packageName)
                    AppCardItem(
                        item = item,
                        isFavorite = isFavorite,
                        modifier = Modifier
                            .animateItem()
                            .animateVisibility(index = index),
                        onFavoriteToggle = onFavoriteToggle,
                        onAppClick = onAppClick,
                        onShareClick = onShareClick
                    )
                }

                is AppListItem.Ad -> {
                    AppsListNativeAdCard(
                        adUnitId = adUnitId,
                        cache = adCache,
                        cacheKey = appListItemKey(
                            selectedFilter = selectedFilter,
                            index = index,
                            item = item,
                        ),
                        modifier = Modifier
                            .animateItem()
                            .animateVisibility(index = index),
                    )
                }
            }
        }

        item(span = { GridItemSpan(columnCount) }) {
            NavigationBarSpacer()
        }
    }
}

/** The grid key of [item], unique per filter so a filter change never reuses a cell's state. */
private fun appListItemKey(selectedFilter: AppsListFilter, index: Int, item: AppListItem): String {
    val baseKey: String = when (item) {
        is AppListItem.App -> item.appInfo.packageName
        AppListItem.Ad -> "ad_$index"
    }
    return "${selectedFilter}_$baseKey"
}

@Composable
private fun AppsListFilters(
    allAppsCount: Int,
    installedPackages: ImmutableSet<String>,
    favorites: ImmutableSet<String>,
    selectedFilter: AppsListFilter,
    onFilterSelected: (AppsListFilter) -> Unit,
) {
    val filters = remember(allAppsCount, installedPackages, favorites) {
        AppsFilterItems.filter { item ->
            item.filter.isAvailable(
                appCount = allAppsCount,
                installedCount = installedPackages.size,
                favoritesCount = favorites.size,
            )
        }.toImmutableList()
    }

    if (filters.size > 1) {
        val chips = filters.map { item ->
            FilterChipItem(
                value = item.filter,
                label = stringResource(id = item.labelResId),
                icon = ToolkitIcon.Vector(item.icon),
            )
        }.toImmutableList()

        TopListFilters(
            filters = chips,
            selectedFilter = selectedFilter,
            onFilterSelected = onFilterSelected,
            // The surrounding list already insets this row.
            contentPadding = PaddingValues(),
        )
    }
}

private data class AppsFilterItem(
    val filter: AppsListFilter,
    val labelResId: Int,
    val icon: ImageVector,
)

private val AppsFilterItems: ImmutableList<AppsFilterItem> = persistentListOf(
    AppsFilterItem(AppsListFilter.All, R.string.apps_filter_all, Icons.Outlined.Apps),
    AppsFilterItem(
        AppsListFilter.Installed,
        R.string.app_details_installed,
        Icons.Outlined.CheckCircle
    ),
    AppsFilterItem(
        AppsListFilter.NotInstalled,
        R.string.app_details_not_installed,
        Icons.Outlined.Block
    ),
    AppsFilterItem(AppsListFilter.Favorites, R.string.favorite_apps, Icons.Outlined.StarOutline),
)

/** The apps whose name, package or short description contain [query]; all of them for a blank one. */
internal fun List<AppInfo>.search(query: String): List<AppInfo> {
    val needle = query.trim()
    if (needle.isEmpty()) return this
    return filter { app ->
        app.name.contains(needle, ignoreCase = true) ||
            app.packageName.contains(needle, ignoreCase = true) ||
            app.shortDescription.contains(needle, ignoreCase = true)
    }
}

private fun ImmutableList<AppInfo>.filterFor(
    filter: AppsListFilter,
    installedPackages: ImmutableSet<String>,
    favorites: ImmutableSet<String>,
): List<AppInfo> = when (filter) {
    AppsListFilter.All -> this
    AppsListFilter.Installed -> filter { app -> app.packageName in installedPackages }
    AppsListFilter.NotInstalled -> filter { app -> app.packageName !in installedPackages }
    AppsListFilter.Favorites -> filter { app -> app.packageName in favorites }
}

@Composable
private fun AppCardItem(
    item: AppListItem.App,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onFavoriteToggle: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onShareClick: (AppInfo) -> Unit
) {
    val appInfo = item.appInfo
    AppCard(
        appInfo = appInfo,
        isFavorite = isFavorite,
        onFavoriteToggle = { onFavoriteToggle(appInfo.packageName) },
        onAppClick = onAppClick,
        onShareClick = onShareClick,
        modifier = modifier,
    )
}
