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

package com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalLayoutDirection
import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppListItem
import com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views.AppCard
import com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views.utils.buildAppListItems
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state.AppListUiState
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state.AppsListFilter
import com.d4rk.android.libs.apptoolkit.core.ui.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.views.ads.AppsListNativeAdCard
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.NavigationBarSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import com.d4rk.android.apps.apptoolkit.core.utils.constants.ads.AppAdsQualifiers
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

/**
 * A composable that displays a grid of applications.
 * This function is responsible for determining the grid layout based on the window size,
 * injecting ads into the list at a specified frequency, and passing the data to the
 * underlying `AppsGrid` composable for rendering.
 *
 * @param uiHomeScreen The state object containing the list of apps to display.
 * @param favorites A set of package names for the apps marked as favorite.
 * @param installedPackages A set of package names detected as installed on the current device.
 * @param paddingValues Padding to be applied from the outside, typically from a Scaffold.
 * @param adsEnabled A boolean flag to determine if ads should be displayed in the list.
 * @param onFilterSelected A callback invoked when the user chooses an app filter chip.
 * @param onFavoriteToggle A lambda function to be invoked when the favorite icon on an app card is toggled. It receives the package name.
 * @param onAppClick A lambda function to be invoked when an app card is clicked. It receives the [AppInfo] of the clicked app.
 * @param onShareClick A lambda function to be invoked when the share icon on an app card is clicked. It receives the [AppInfo] of the app to be shared.
 * @param adFrequency The frequency at which ads are inserted into the list (e.g., an ad every `adFrequency` items).
 * @param windowWidthSizeClass The current window width size class, used to determine the number of columns in the grid.
 */
@Composable
fun AppsList(
    uiHomeScreen: AppListUiState,
    favorites: ImmutableSet<String>,
    installedPackages: ImmutableSet<String>,
    paddingValues: PaddingValues,
    adsEnabled: Boolean,
    onFilterSelected: (AppsListFilter) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onShareClick: (AppInfo) -> Unit,
    onFirstVisibleAppChanged: (AppInfo) -> Unit = {},
    adFrequency: Int = BuildConfig.APPS_LIST_AD_FREQUENCY,
    windowWidthSizeClass: AppWindowWidthSizeClass,
) {
    val apps: ImmutableList<AppInfo> = remember(
        uiHomeScreen.apps,
        uiHomeScreen.selectedFilter,
        installedPackages,
        favorites,
    ) {
        uiHomeScreen.apps.filterFor(
            filter = uiHomeScreen.selectedFilter,
            installedPackages = installedPackages,
            favorites = favorites,
        ).toImmutableList()
    }

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

    val adsConfig: AdsConfig = koinInject(qualifier = named(AppAdsQualifiers.APPS_LIST_NATIVE_AD))

    AppsGrid(
        items = items,
        allAppsCount = uiHomeScreen.apps.size,
        favorites = favorites,
        installedPackages = installedPackages,
        selectedFilter = uiHomeScreen.selectedFilter,
        onFilterSelected = onFilterSelected,
        paddingValues = paddingValues,
        columnCount = columnCount,
        listState = listState,
        onFavoriteToggle = onFavoriteToggle,
        onAppClick = onAppClick,
        onShareClick = onShareClick,
        onFirstVisibleAppChanged = onFirstVisibleAppChanged,
        adUnitId = adsConfig.bannerAdUnitId,
    )
}

/**
 * A composable that displays a grid of applications and ads.
 * It uses a [LazyVerticalGrid] to efficiently display a potentially large list of items.
 *
 * This function is responsible for the layout and rendering of individual app cards and ad cards within the grid.
 *
 * @param items The list of [AppListItem]s to display, which can be either an app or an ad.
 * @param allAppsCount Total number of available apps.
 * @param favorites A set of package names for the apps that are marked as favorites.
 * @param installedPackages A set of package names for the apps that are installed.
 * @param selectedFilter The currently selected chip filter.
 * @param onFilterSelected A callback invoked when the user chooses an app filter chip.
 * @param paddingValues Padding to be applied from the parent composable, typically from a Scaffold.
 * @param columnCount The number of columns in the grid.
 * @param listState The state object to be used for the [LazyVerticalGrid], allowing for observation and control of the scroll position.
 * @param onFavoriteToggle A callback lambda that is invoked when the favorite icon on an app card is toggled. It receives the package name of the app.
 * @param onAppClick A callback lambda that is invoked when an app card is clicked. It receives the [AppInfo] of the clicked app.
 * @param onShareClick A callback lambda that is invoked when the share icon on an app card is clicked. It receives the [AppInfo] of the app to be shared.
 * @param adUnitId The ad unit ID for the native ads to be displayed in the grid.
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
    onFirstVisibleAppChanged: (AppInfo) -> Unit,
    adUnitId: String,
) {
    LaunchedEffect(items) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo
                .firstNotNullOfOrNull { visibleItem ->
                    (items.getOrNull(visibleItem.index) as? AppListItem.App)?.appInfo
                }
        }
            .filterNotNull()
            .distinctUntilChangedBy(AppInfo::packageName)
            .collect(onFirstVisibleAppChanged)
    }

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
                when (item) {
                    is AppListItem.App -> item.appInfo.packageName
                    AppListItem.Ad -> "ad_$index"
                }
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

@Composable
private fun AppsListFilters(
    allAppsCount: Int,
    installedPackages: ImmutableSet<String>,
    favorites: ImmutableSet<String>,
    selectedFilter: AppsListFilter,
    onFilterSelected: (AppsListFilter) -> Unit,
) {
    val filters = remember(allAppsCount, installedPackages, favorites) {
        val list = mutableListOf<AppsFilterItem>()
        list.add(AppsFilterItems[0]) // All

        if (installedPackages.isNotEmpty()) {
            list.add(AppsFilterItems[1]) // Installed
        }

        if (installedPackages.isNotEmpty() && installedPackages.size < allAppsCount) {
            list.add(AppsFilterItems[2]) // Not Installed
        }

        if (favorites.isNotEmpty()) {
            list.add(AppsFilterItems[3]) // Favorites
        }
        list.toImmutableList()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
    ) {
        filters.forEach { item ->
            FilterChip(
                selected = selectedFilter == item.filter,
                onClick = { onFilterSelected(item.filter) },
                label = { Text(text = stringResource(id = item.labelResId)) },
                leadingIcon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                    )
                },
            )
        }
    }
}

private data class AppsFilterItem(
    val filter: AppsListFilter,
    val labelResId: Int,
    val icon: ImageVector,
)

private val AppsFilterItems: ImmutableList<AppsFilterItem> = persistentListOf(
    AppsFilterItem(AppsListFilter.All, R.string.apps_filter_all, Icons.Outlined.Apps),
    AppsFilterItem(AppsListFilter.Installed, R.string.app_details_installed, Icons.Outlined.CheckCircle),
    AppsFilterItem(AppsListFilter.NotInstalled, R.string.app_details_not_installed, Icons.Outlined.Block),
    AppsFilterItem(AppsListFilter.Favorites, R.string.favorite_apps, Icons.Outlined.StarOutline),
)

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

/**
 * A composable that wraps the [AppCard] and provides it with the necessary data and callbacks.
 * This function acts as a bridge, extracting the [AppInfo] from the [AppListItem.App]
 * and passing it along with other parameters to the [AppCard].
 *
 * @param item The app item data, containing the [AppInfo].
 * @param isFavorite A boolean indicating whether the app is marked as a favorite.
 * @param modifier A [Modifier] for this composable.
 * @param onFavoriteToggle A lambda function to be invoked when the favorite icon is toggled. It receives the package name.
 * @param onAppClick A lambda function to be invoked when the app card is clicked. It receives the [AppInfo] of the clicked app.
 * @param onShareClick A lambda function to be invoked when the share icon is clicked. It receives the [AppInfo] of the app to be shared.
 */
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
