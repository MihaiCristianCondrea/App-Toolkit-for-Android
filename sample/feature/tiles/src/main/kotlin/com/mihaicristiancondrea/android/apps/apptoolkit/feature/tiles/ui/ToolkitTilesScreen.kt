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

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitQuickTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolkitTilesEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.PositionedToolkitTilesListItem
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTilesListItem
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.isVisible
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.stableKey
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ToolkitTilesUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.utils.filterFor
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.utils.requestQuickSettingsTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.utils.search
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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logViewItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.LocalNativeAdViewFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.NavigationBarSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellSearch
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.viewmodel.koinViewModel

private val TilesScreen = AppScreenTracking.Screens.TOOLKIT_TILES

/**
 * The Toolkit Tiles catalog. Owns [ToolkitTilesViewModel], tracking, the Quick Settings add-tile
 * request and the tab's search query, and re-reads tile membership on every resume.
 */
@Composable
fun ToolkitTilesScreen() {
    val viewModel: ToolkitTilesViewModel = koinViewModel()
    val state: ToolkitTilesUiState by viewModel.state.collectAsStateWithLifecycle()
    val messages: ImmutableList<UiMessage> by viewModel.messages.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    TrackScreenView(
        screenName = TilesScreen.name,
        screenClass = TilesScreen.className,
    )
    TrackScreenState(
        screenName = TilesScreen.name,
        state = state.categories,
    )

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

    LaunchedEffect(state.pendingTileRequest) {
        val requestKey: String = state.pendingTileRequest ?: return@LaunchedEffect
        requestQuickSettingsTile(
            context = context,
            requestKey = requestKey,
            onResult = { outcome ->
                viewModel.onEvent(ToolkitTilesEvent.TileRequestFinished(requestKey = requestKey, outcome = outcome))
            },
        )
        viewModel.onEvent(ToolkitTilesEvent.TileRequestLaunched)
    }

    MessageToasts(messages = messages, onShown = viewModel::messageShown)

    CompositionLocalProvider(
        LocalNativeAdViewFactory provides remember { ToolkitTilesNativeAdViewFactory() }
    ) {
        ToolkitTilesScreenContent(
            state = state,
            contentPadding = contentPadding(),
            onEvent = viewModel::onEvent,
            searchQuery = LocalShellSearch.current?.query.orEmpty(),
        )
    }
}

/**
 * Shows [messages] as toasts, oldest first, and calls [onShown] with each one's id once its toast
 * is posted. The messages come while a tool's bottom sheet is open, which would cover a snackbar.
 */
@Composable
private fun MessageToasts(
    messages: ImmutableList<UiMessage>,
    onShown: (id: Long) -> Unit,
) {
    val context: Context = LocalContext.current
    val next: UiMessage? = messages.firstOrNull()

    LaunchedEffect(next?.id) {
        val message: UiMessage = next ?: return@LaunchedEffect
        Toast.makeText(context.applicationContext, message.text.asString(context = context), Toast.LENGTH_SHORT)
            .show()
        onShown(message.id)
    }
}

/**
 * The stateless catalog: the loading or failure state, or the filters, the categories with ad
 * cards between them, and the tool a tapped tile opens.
 *
 * @param searchQuery What the tab's search field holds; matches show expanded.
 */
@Composable
internal fun ToolkitTilesScreenContent(
    state: ToolkitTilesUiState,
    contentPadding: PaddingValues,
    onEvent: (ToolkitTilesEvent) -> Unit,
    searchQuery: String = "",
) {
    ScreenStateHandler(
        state = state.categories,
        contentPadding = contentPadding,
        onRetry = { onEvent(ToolkitTilesEvent.Load) },
    ) { ready ->
        ToolkitTilesCatalog(
            categories = ready.value,
            state = state,
            contentPadding = contentPadding,
            onEvent = onEvent,
            searchQuery = searchQuery,
        )
    }
}

@Composable
private fun ToolkitTilesCatalog(
    categories: ImmutableList<ToolkitTileCategory>,
    state: ToolkitTilesUiState,
    contentPadding: PaddingValues,
    onEvent: (ToolkitTilesEvent) -> Unit,
    searchQuery: String,
) {
    val showAds = rememberAdsEnabled()
    val telemetryRepository = LocalTelemetry.current
    var selectedTile by remember { mutableStateOf<ToolkitTile?>(null) }
    var quickToolDialog by remember { mutableStateOf<ToolkitQuickTool?>(null) }
    val resources = LocalContext.current.resources
    val filteredCategories = remember(categories, state.selectedFilter, searchQuery, resources) {
        categories.filterFor(state.selectedFilter).search(searchQuery, resources::getString)
    }
    val searching = searchQuery.isNotBlank()
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
                top = contentPadding.calculateTopPadding() + SizeConstants.LargeSize,
                end = SizeConstants.LargeSize,
                bottom = contentPadding.calculateBottomPadding() + SizeConstants.LargeSize,
            ),
        ) {
            item {
                TilesFilters(
                    categories = categories,
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
                            val expanded = searching || category.id in state.expandedCategoryIds
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
                                    telemetryRepository.logViewItem(
                                        itemId = tile.id,
                                        itemName = tile.id,
                                        itemCategory = category.id,
                                    )
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

    selectedTile?.let { selected ->
        val tile = categories.asSequence().flatMap { it.tiles }
            .firstOrNull { it.id == selected.id } ?: selected
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

@Preview(showBackground = true)
@Composable
private fun ToolkitTilesScreenContentPreview() {
    MaterialTheme {
        Surface {
            ToolkitTilesScreenContent(
                state = ToolkitTilesUiState(
                    categories = Loadable.Ready(
                        persistentListOf(
                            ToolkitTileCategory(
                                id = "sensors",
                                titleResId = android.R.string.unknownName,
                                icon = ToolkitTileIcon.Compass,
                                tiles = persistentListOf(
                                    ToolkitTile(
                                        id = "level",
                                        titleResId = android.R.string.unknownName,
                                        summaryResId = android.R.string.unknownName,
                                        icon = ToolkitTileIcon.Level,
                                        status = ToolkitTileStatus.Available,
                                    ),
                                    ToolkitTile(
                                        id = "compass",
                                        titleResId = android.R.string.unknownName,
                                        summaryResId = android.R.string.unknownName,
                                        icon = ToolkitTileIcon.Compass,
                                        status = ToolkitTileStatus.NotAdded,
                                    ),
                                ),
                            ),
                        ),
                    ),
                    expandedCategoryIds = persistentSetOf("sensors"),
                ),
                contentPadding = PaddingValues(),
                onEvent = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ToolkitTilesScreenContentLoadingPreview() {
    MaterialTheme {
        ToolkitTilesScreenContent(
            state = ToolkitTilesUiState(categories = Loadable.Loading),
            contentPadding = PaddingValues(),
            onEvent = {},
        )
    }
}
