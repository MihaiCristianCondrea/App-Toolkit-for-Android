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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Sensors
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material.icons.rounded.Speaker
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleStartEffect
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.LiveToolsUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.QuickToolsWidgets
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolSection
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolWorkspace
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.imageVector
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.CompassToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.LevelToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.FlashDimmerToolEvent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitQuickTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolkitTilesEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.PositionedToolkitTilesListItem
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTilesListItem
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ToolkitTilesFilter
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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageToastHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.LocalNativeAdViewFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
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
    val compass: CompassToolViewModel = koinViewModel(key = "overview-compass")
    val level: LevelToolViewModel = koinViewModel(key = "overview-level")
    val flash: FlashDimmerToolViewModel = koinViewModel()
    LifecycleStartEffect(flash) { onStopOrDispose { flash.onEvent(FlashDimmerToolEvent.Dismiss) } }
    val heading by compass.state.collectAsStateWithLifecycle()
    val tilt by level.state.collectAsStateWithLifecycle()
    val torch by flash.state.collectAsStateWithLifecycle()
    LifecycleStartEffect(compass, level) {
        compass.onEvent(CompassToolEvent.StartSensor)
        level.onEvent(LevelToolEvent.StartSensor)
        onStopOrDispose {
            compass.onEvent(CompassToolEvent.StopSensor)
            level.onEvent(LevelToolEvent.StopSensor)
        }
    }
    val viewModel: ToolkitTilesViewModel = koinViewModel()
    val state: ToolkitTilesUiState by viewModel.state.collectAsStateWithLifecycle()
    val messages: ImmutableList<UiMessage> by viewModel.messages.collectAsStateWithLifecycle()
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

    LaunchedEffect(state.pendingTileRequest) {
        val requestKey: String = state.pendingTileRequest ?: return@LaunchedEffect
        requestQuickSettingsTile(
            context = context,
            requestKey = requestKey,
            onTileAdded = { componentName -> viewModel.onEvent(ToolkitTilesEvent.TileAdded(componentName)) },
            onResult = { outcome ->
                viewModel.onEvent(ToolkitTilesEvent.TileRequestFinished(requestKey = requestKey, outcome = outcome))
            },
        )
        viewModel.onEvent(ToolkitTilesEvent.TileRequestLaunched)
    }

    MessageToastHost(messages = messages, onShown = viewModel::messageShown, errorDuration = Toast.LENGTH_SHORT)

    ScreenHost(screen = TilesScreen, status = state.categories) {
        CompositionLocalProvider(
            LocalNativeAdViewFactory provides remember { ToolkitTilesNativeAdViewFactory() }
        ) {
            ToolkitTilesScreenContent(
                state = state,
                live = LiveToolsUiState(heading.azimuth, tilt.pitch, tilt.roll, torch.torch),
                onFlashLevel = { flash.onEvent(FlashDimmerToolEvent.LevelChanged(it)) },
                contentPadding = contentPadding(),
                onEvent = viewModel::onEvent,
                searchQuery = LocalShellSearch.current?.query.orEmpty(),
            )
        }
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
    live: LiveToolsUiState = LiveToolsUiState(),
    onFlashLevel: (Int) -> Unit = {},
) {
    ScreenStateHandler(
        state = state.categories,
        contentPadding = contentPadding,
        onRetry = { onEvent(ToolkitTilesEvent.Load) },
        showAd = false,
    ) { ready ->
        ToolkitTilesCatalog(
            categories = ready.value,
            state = state,
            contentPadding = contentPadding,
            onEvent = onEvent,
            searchQuery = searchQuery,
            live = live, onFlashLevel = onFlashLevel,
        )
    }
}

@Composable
private fun ToolkitTilesCatalog(
    categories: ImmutableList<ToolkitTileCategory>, state: ToolkitTilesUiState,
    contentPadding: PaddingValues, onEvent: (ToolkitTilesEvent) -> Unit, searchQuery: String,
    live: LiveToolsUiState, onFlashLevel: (Int) -> Unit,
) {
    val showAds = rememberAdsEnabled()
    val telemetry = LocalTelemetry.current
    var selectedId by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf<String?>(null) }
    val resources = LocalResources.current
    // Chance and Measure have tabs of their own; Quick Tools shows the rest, Colors last.
    val filtered = categories.filter { it.id !in setOf("chance", "measure") }
        .toImmutableList().search(searchQuery, resources::getString)
    val ad = ToolkitTilesListItem.Ad("ad_trailing", AdsConstants.QUICK_TOOLS_NATIVE_AD_UNIT_ID)
    fun open(tile: ToolkitTile) {
        telemetry.logViewItem(itemId = tile.id, itemName = tile.id, itemCategory = "quick_tools")
        selectedId = tile.id
    }
    Box(Modifier.fillMaxSize()) {
        ToolWorkspace(contentPadding) {
            if (filtered.isEmpty()) EmptyFilterCard()
            filtered.forEachIndexed { index, category ->
                Box(Modifier.animateVisibility(index)) {
                    ToolSection(stringResource(category.titleResId), when (category.id) {
                        "sensors" -> Icons.Rounded.Sensors
                        "system" -> Icons.Rounded.FlashlightOn
                        "sound" -> Icons.Rounded.Speaker
                        "wellbeing" -> Icons.Rounded.SelfImprovement
                        "colors" -> Icons.Rounded.Palette
                        else -> category.icon.imageVector()
                    }) {
                        QuickToolsWidgets(category.tiles, live, ::open, onFlashLevel)
                    }
                }
                if (index == 0 && showAds && searchQuery.isBlank() && ad.id in state.loadedAdIds) {
                    QuickToolsNativeAdCard(adUnitId = ad.adUnitId,
                        position = com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition.SINGLE,
                        initiallyLoaded = true,
                        onStatusChanged = { onEvent(ToolkitTilesEvent.AdStatusChanged(ad.id, it)) })
                }
            }
        }
        if (showAds && searchQuery.isBlank() && ad.id !in state.loadedAdIds) {
            HiddenAdPreloaders(adItems = persistentListOf(ad), onEvent = onEvent)
        }
    }
    val tile = categories.flatMap { it.tiles }.firstOrNull { it.id == selectedId }
    if (tile?.quickTool == ToolkitQuickTool.MaterialColors) {
        MaterialColorsToolDialog(onClose = { selectedId = null })
    } else if (tile != null) {
        ToolkitToolBottomSheet(tile, onClose = { selectedId = null },
            onAddTile = { onEvent(ToolkitTilesEvent.AddTileClicked(tile.requestKey)) },
            onSetupTile = { onEvent(ToolkitTilesEvent.TileSetupClicked(tile.id)) })
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

private val ToolkitTilesListItem.stableKey: String
    get() = when (this) {
        is ToolkitTilesListItem.Category -> category.id
        is ToolkitTilesListItem.Ad -> id
    }

private fun ToolkitTilesListItem.isVisible(
    loadedAdIds: Set<String>,
    showAds: Boolean,
): Boolean = when (this) {
    is ToolkitTilesListItem.Category -> true
    is ToolkitTilesListItem.Ad -> showAds && id in loadedAdIds
}
