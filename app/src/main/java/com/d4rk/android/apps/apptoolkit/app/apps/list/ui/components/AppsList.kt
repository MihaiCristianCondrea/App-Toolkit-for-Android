package com.d4rk.android.apps.apptoolkit.app.apps.list.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppListItem
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.ui.UiHomeScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.components.ads.AdBanner
import com.d4rk.android.libs.apptoolkit.core.ui.components.animations.rememberAnimatedVisibilityStateForGrids
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.animateVisibility
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ScreenHelper

@Composable
fun AppsList(
    uiHomeScreen: UiHomeScreen,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    adsConfig: AdsConfig,
    adsEnabled: Boolean,
    onFavoriteToggle: (String) -> Unit,
) {
    val apps: List<AppInfo> = uiHomeScreen.apps
    val context = LocalContext.current
    val isTabletOrLandscape = remember(context) {
        ScreenHelper.isLandscapeOrTablet(context = context)
    }
    val columnCount by remember(isTabletOrLandscape) {
        derivedStateOf { if (isTabletOrLandscape) 4 else 2 }
    }
    val listState = rememberLazyGridState()
    val adFrequency = 4
    val items by remember(apps, adsEnabled) {
        derivedStateOf { buildAppListItems(apps, adsEnabled, adFrequency) }
    }

    AppsGrid(
        items = items,
        favorites = favorites,
        paddingValues = paddingValues,
        columnCount = columnCount,
        listState = listState,
        adsConfig = adsConfig,
        onFavoriteToggle = onFavoriteToggle
    )
}

@Composable
private fun AppsGrid(
    items: List<AppListItem>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    columnCount: Int,
    listState: LazyGridState,
    adsConfig: AdsConfig,
    onFavoriteToggle: (String) -> Unit
) {
    val (visibilityStates: SnapshotStateList<Boolean>) = rememberAnimatedVisibilityStateForGrids(
        gridState = listState,
        itemCount = items.size
    )

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
                if (item is AppListItem.Ad) GridItemSpan(columnCount) else GridItemSpan(1)
            }
        ) { index: Int, item: AppListItem ->
            when (item) {
                is AppListItem.App -> AppCardItem(
                    item = item,
                    isFavorite = favorites.contains(item.appInfo.packageName),
                    visibilityStates = visibilityStates,
                    index = index,
                    onFavoriteToggle = onFavoriteToggle
                )

                AppListItem.Ad -> AdListItem(adsConfig = adsConfig)
            }
        }
    }
}

@Composable
private fun AppCardItem(
    item: AppListItem.App,
    isFavorite: Boolean,
    visibilityStates: SnapshotStateList<Boolean>,
    index: Int,
    onFavoriteToggle: (String) -> Unit
) {
    val appInfo = item.appInfo
    AppCard(
        appInfo = appInfo,
        isFavorite = isFavorite,
        onFavoriteToggle = { onFavoriteToggle(appInfo.packageName) },
        modifier = Modifier.animateVisibility(
            visible = visibilityStates.getOrElse(index = index) { false },
            index = index
        )
    )
}

@Composable
private fun AdListItem(adsConfig: AdsConfig) {
    AdBanner(
        modifier = Modifier.fillMaxWidth(),
        adsConfig = adsConfig
    )
}

private fun buildAppListItems(
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

