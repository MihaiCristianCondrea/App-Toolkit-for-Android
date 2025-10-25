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
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppListItem
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.ui.UiHomeScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.components.ads.AppsListNativeAdCard
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.animateVisibility
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun AppsList(
    uiHomeScreen: UiHomeScreen,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    adsEnabled: Boolean,
    onFavoriteToggle: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onShareClick: (AppInfo) -> Unit,
    adFrequency: Int = BuildConfig.APPS_LIST_AD_FREQUENCY,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    val apps: List<AppInfo> = uiHomeScreen.apps
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
    val items by remember(apps, adsEnabled, adFrequency) {
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
        adsConfig = adsConfig
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun AppsGrid(
    items: List<AppListItem>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    columnCount: Int,
    listState: LazyGridState,
    onFavoriteToggle: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onShareClick: (AppInfo) -> Unit,
    adsConfig: AdsConfig,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = columnCount),
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(SizeConstants.LargeSize),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.LargeSize),
        horizontalArrangement = Arrangement.spacedBy(space = SizeConstants.LargeSize)
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
                GridItemSpan(1)
            },
            contentType = { _, item ->
                when (item) {
                    is AppListItem.App -> "app"
                    AppListItem.Ad -> "ad"
                }
            }
        ) { index, item: AppListItem ->
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

                AppListItem.Ad -> AdListItem(
                    modifier = Modifier
                        .animateItem()
                        .animateVisibility(index = index),
                    adsConfig = adsConfig
                )
            }
        }
    }
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
        modifier = modifier
    )
}

@Composable
private fun AdListItem(
    modifier: Modifier = Modifier,
    adsConfig: AdsConfig,
) {
    AppsListNativeAdCard(
        adsConfig = adsConfig,
        modifier = modifier
    )
}

internal fun buildAppListItems(
    apps: List<AppInfo>,
    adsEnabled: Boolean,
    adFrequency: Int
): List<AppListItem> {
    return buildList {
        apps.forEachIndexed { index, appInfo ->
            add(AppListItem.App(appInfo))
            if (adsEnabled && (index + 1) % adFrequency == 0) {
                add(AppListItem.Ad)
            }
        }
        if (adsEnabled && apps.isNotEmpty() && apps.size % adFrequency != 0) {
            add(AppListItem.Ad)
        }
    }
}

