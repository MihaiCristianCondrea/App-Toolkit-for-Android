package com.d4rk.android.apps.apptoolkit.app.apps.common.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.apps.apptoolkit.app.apps.common.AppCard
import com.d4rk.android.apps.apptoolkit.app.apps.common.utils.buildAppListItems
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppListItem
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state.AppListUiState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.components.ads.AppsListNativeAdCard
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.animateVisibility
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
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
 * @param paddingValues Padding to be applied from the outside, typically from a Scaffold.
 * @param adsEnabled A boolean flag to determine if ads should be displayed in the list.
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
    paddingValues: PaddingValues,
    adsEnabled: Boolean,
    onFavoriteToggle: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onShareClick: (AppInfo) -> Unit,
    adFrequency: Int = BuildConfig.APPS_LIST_AD_FREQUENCY,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    val apps: ImmutableList<AppInfo> = uiHomeScreen.apps

    val columnCount by remember(windowWidthSizeClass) {
        derivedStateOf {
            when (windowWidthSizeClass) {
                WindowWidthSizeClass.Compact -> 2
                WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> 4
                else -> 2
            }
        }
    }

    val listState = rememberLazyGridState()

    val items: ImmutableList<AppListItem> by remember(apps, adsEnabled, adFrequency) {
        derivedStateOf { buildAppListItems(apps, adsEnabled, adFrequency) }
    }

    val adsConfig: AdsConfig = koinInject(qualifier = named("apps_list_native_ad"))

    AppsGrid(
        items = items,
        favorites = favorites,
        paddingValues = paddingValues,
        columnCount = columnCount,
        listState = listState,
        onFavoriteToggle = onFavoriteToggle,
        onAppClick = onAppClick,
        onShareClick = onShareClick,
        adUnitId = adsConfig.bannerAdUnitId
    )
}

/**
 * A composable that displays a grid of applications and ads.
 * It uses a [LazyVerticalGrid] to efficiently display a potentially large list of items.
 *
 * This function is responsible for the layout and rendering of individual app cards and ad cards within the grid.
 *
 * @param items The list of [AppListItem]s to display, which can be either an app or an ad.
 * @param favorites A set of package names for the apps that are marked as favorites.
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
    favorites: ImmutableSet<String>,
    paddingValues: PaddingValues,
    columnCount: Int,
    listState: LazyGridState,
    onFavoriteToggle: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onShareClick: (AppInfo) -> Unit,
    adUnitId: String,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = columnCount),
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(SizeConstants.LargeSize),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize)
    ) {
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
                    val isFavorite by remember(favorites, packageName) {
                        derivedStateOf { favorites.contains(packageName) }
                    }
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

                AppListItem.Ad -> {
                    AppsListNativeAdCard(
                        adUnitId = adUnitId,
                        modifier = Modifier
                            .animateItem()
                            .animateVisibility(index = index),
                    )
                }
            }
        }

        repeat(4) {
            item(span = { GridItemSpan(columnCount) }) {
                LargeVerticalSpacer()
            }
        }
    }
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
        modifier = modifier
    )
}

