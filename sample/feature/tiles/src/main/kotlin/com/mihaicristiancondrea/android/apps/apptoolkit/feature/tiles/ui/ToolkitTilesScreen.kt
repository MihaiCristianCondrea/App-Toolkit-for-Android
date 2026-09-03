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


package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitQuickTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolkitTilesAction
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolkitTilesEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.PositionedToolkitTilesListItem
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTilesListItem
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.isVisible
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.stableKey
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ToolkitTilesUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.utils.filterFor
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.utils.requestQuickSettingsTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.MaterialColorsToolDialog
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ToolkitToolBottomSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ads.QuickToolsNativeAdCard
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ads.ToolkitTilesNativeAdViewFactory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.EmptyFilterCard
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.HiddenAdPreloaders
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.HowToAddTilesCard
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.TileCategorySection
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.TilesFilters
import com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.constants.AdsConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.LocalNativeAdViewFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.NavigationBarSpacer
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.viewmodel.koinViewModel

/** Route-level composable for the Toolkit Tiles catalog. */
@Composable
fun ToolkitTilesScreen(
    paddingValues: PaddingValues,
) {
    val viewModel: ToolkitTilesViewModel = koinViewModel()
    val screenState: UiStateScreen<ToolkitTilesUiState> by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(viewModel, lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(ToolkitTilesEvent.Refresh)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(viewModel, context) {
        viewModel.actionEvent.collect { action ->
            when (action) {
                is ToolkitTilesAction.RequestAddTile -> requestQuickSettingsTile(
                    context = context,
                    requestKey = action.requestKey,
                )

                ToolkitTilesAction.ShowSetupRequiredMessage -> Toast.makeText(
                    context,
                    R.string.tiles_setup_required_message,
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    ScreenStateHandler(
        screenState = screenState,
        onLoading = { LoadingScreen(paddingValues = paddingValues) },
        onEmpty = { NoDataScreen() },
        onError = { NoDataScreen() },
        onSuccess = { state ->
            CompositionLocalProvider(
                LocalNativeAdViewFactory provides remember { ToolkitTilesNativeAdViewFactory() }
            ) {
                ToolkitTilesScreen(
                    state = state,
                    paddingValues = paddingValues,
                    onEvent = viewModel::onEvent,
                )
            }
        },
    )
}

/** Stateless Material 3 screen that renders Quick Settings tile categories and actions. */
@Composable
fun ToolkitTilesScreen(
    state: ToolkitTilesUiState,
    paddingValues: PaddingValues,
    onEvent: (ToolkitTilesEvent) -> Unit,
) {
    val showAds = rememberAdsEnabled()
    var selectedTile by remember { mutableStateOf<ToolkitTile?>(null) }
    var quickToolDialog by remember { mutableStateOf<ToolkitQuickTool?>(null) }
    val filteredCategories = remember(state.categories, state.selectedFilter) {
        state.categories.filterFor(state.selectedFilter)
    }
    val listItems = remember(filteredCategories) {
        buildList {
            filteredCategories.forEachIndexed { index, category ->
                add(ToolkitTilesListItem.Category(category))
                if ((index + 1) % 2 == 0) {
                    add(
                        ToolkitTilesListItem.Ad(
                            id = "ad_after_${category.id}",
                            adUnitId = AdsConstants.QUICK_TOOLS_NATIVE_AD_UNIT_ID,
                        )
                    )
                }
            }
            if (isNotEmpty() && last() !is ToolkitTilesListItem.Ad) {
                add(
                    ToolkitTilesListItem.Ad(
                        id = "ad_trailing",
                        adUnitId = AdsConstants.QUICK_TOOLS_NATIVE_AD_UNIT_ID,
                    )
                )
            }
        }.toImmutableList()
    }

    val visibleListItems = remember(listItems, state.loadedAdIds, showAds) {
        listItems
            .filter { item -> item.isVisible(loadedAdIds = state.loadedAdIds, showAds = showAds) }
            .let { visibleItems ->
                visibleItems.mapIndexed { index, item ->
                    PositionedToolkitTilesListItem(
                        item = item,
                        position = groupedItemPosition(index, visibleItems.size),
                    )
                }
            }
            .toImmutableList()
    }
    val preloadedAdItems = remember(listItems, state.loadedAdIds, showAds) {
        if (!showAds) return@remember persistentListOf()
        listItems
            .filterIsInstance<ToolkitTilesListItem.Ad>()
            .filterNot { adItem -> adItem.id in state.loadedAdIds }
            .toImmutableList()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
            contentPadding = PaddingValues(
                start = SizeConstants.LargeSize,
                top = paddingValues.calculateTopPadding() + SizeConstants.LargeSize,
                end = SizeConstants.LargeSize,
                bottom = paddingValues.calculateBottomPadding() + SizeConstants.LargeSize,
            ),
        ) {
            item {
                TilesFilters(
                    categories = state.categories,
                    selectedFilter = state.selectedFilter,
                    onFilterSelected = { filter -> onEvent(ToolkitTilesEvent.FilterSelected(filter)) },
                )
            }
            item { Spacer(modifier = Modifier.height(SizeConstants.SmallSize)) }
            if (listItems.isEmpty()) {
                item {
                    EmptyFilterCard()
                }
            } else {
                itemsIndexed(
                    items = visibleListItems,
                    key = { _, positionedItem -> "${state.selectedFilter}_${positionedItem.item.stableKey}" },
                ) { index, positionedItem ->
                    val item = positionedItem.item
                    val position = positionedItem.position

                    when (item) {
                        is ToolkitTilesListItem.Category -> {
                            val category = item.category
                            val expanded = category.id in state.expandedCategoryIds
                            TileCategorySection(
                                category = category,
                                position = position,
                                expanded = expanded,
                                modifier = Modifier
                                    .animateItem()
                                    .animateVisibility(index = index),
                                selectedFilter = state.selectedFilter,
                                onToggle = { onEvent(ToolkitTilesEvent.CategoryToggled(category.id)) },
                                onPreviewTile = { tile ->
                                    if (tile.quickTool == ToolkitQuickTool.MaterialColors) {
                                        quickToolDialog = ToolkitQuickTool.MaterialColors
                                    } else {
                                        selectedTile = tile
                                    }
                                },
                            )
                        }

                        is ToolkitTilesListItem.Ad -> {
                            QuickToolsNativeAdCard(
                                modifier = Modifier
                                    .animateItem()
                                    .animateVisibility(index = index),
                                adUnitId = item.adUnitId,
                                position = position,
                                initiallyLoaded = item.id in state.loadedAdIds,
                                onStatusChanged = { isLoaded ->
                                    onEvent(ToolkitTilesEvent.AdStatusChanged(item.id, isLoaded))
                                },
                            )
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(SizeConstants.SmallSize)) }
            item {
                HowToAddTilesCard()
            }
            item {
                NavigationBarSpacer()
            }
        }

        HiddenAdPreloaders(
            adItems = preloadedAdItems,
            onEvent = onEvent,
        )
    }

    selectedTile?.let { tile ->
        ToolkitToolBottomSheet(
            tile = tile,
            onClose = { selectedTile = null },
            onAddTile = { onEvent(ToolkitTilesEvent.AddTileClicked(tile.requestKey)) },
            onSetupTile = { onEvent(ToolkitTilesEvent.TileSetupClicked(tile.id)) },
        )
    }

    if (quickToolDialog == ToolkitQuickTool.MaterialColors) {
        MaterialColorsToolDialog(onClose = { quickToolDialog = null })
    }
}
