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

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.ToolkitTilesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.logQuickSettingsTileRequest
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolkitTilesEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.toUiModels
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ToolkitTilesFilter
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ToolkitTilesUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logSelectContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logViewItemList
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

/**
 * Coordinates the static Toolkit Tiles catalog, filtering, and add-tile requests. Building the UI
 * models from the catalogue is its own CPU work, so that runs on the default dispatcher.
 */
class ToolkitTilesViewModel(
    private val toolkitTilesRepository: ToolkitTilesRepository,
    private val dispatchers: DispatcherProvider,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<ToolkitTilesUiState, ToolkitTilesEvent>(
    initialState = ToolkitTilesUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "ToolkitTilesViewModel",
) {
    private var loadJob: Job? = null
    private var saveExpandedJob: Job? = null
    private var hasLoggedCatalogueView: Boolean = false

    init {
        onEvent(ToolkitTilesEvent.Load)
    }

    override fun handleEvent(event: ToolkitTilesEvent) {
        when (event) {
            ToolkitTilesEvent.Load -> loadTiles()
            ToolkitTilesEvent.Refresh -> refreshStatuses()
            is ToolkitTilesEvent.FilterSelected -> selectFilter(event.filter)
            is ToolkitTilesEvent.CategoryToggled -> toggleCategory(event.categoryId)
            is ToolkitTilesEvent.AddTileClicked -> handleAddTile(event.requestKey)
            ToolkitTilesEvent.TileRequestLaunched -> setState { copy(pendingTileRequest = null) }
            is ToolkitTilesEvent.TileRequestFinished ->
                handleTileRequestFinished(event.requestKey, event.outcome)
            is ToolkitTilesEvent.TileSetupClicked -> handleTileSetup(event.tileId)
            is ToolkitTilesEvent.AdStatusChanged -> updateAdStatus(event.adId, event.isLoaded)
        }
    }

    private fun updateAdStatus(adId: String, isLoaded: Boolean) {
        setState {
            copy(loadedAdIds = loadedAdIds.mutate { if (isLoaded) it.add(adId) else it.remove(adId) })
        }
    }

    /**
     * Follows the catalogue and the expanded categories. The catalogue re-emits on every refresh
     * and on every expand or collapse, so the list view is reported once rather than per emission.
     */
    private fun loadTiles() {
        loadJob = loadJob.restart {
            setState { copy(categories = Loadable.Loading) }
            combine(
                toolkitTilesRepository.tileCategories(),
                toolkitTilesRepository.expandedCategoryIds,
            ) { categories, expandedCategoryIds ->
                categories.toUiModels() to expandedCategoryIds.toPersistentSet()
            }
                .flowOn(dispatchers.default)
                .collectReport(
                    action = Actions.LOAD_TILES,
                    onError = { error -> setState { copy(categories = error.toFailed(fallback = LoadFailedText)) } },
                ) { (categories, expandedCategoryIds) ->
                    if (!hasLoggedCatalogueView) {
                        hasLoggedCatalogueView = true
                        telemetryRepository.logViewItemList(
                            itemListId = "all",
                            itemListName = "quick_tools_catalog",
                        )
                    }
                    setState {
                        copy(categories = Loadable.Ready(categories), expandedCategoryIds = expandedCategoryIds)
                    }
                }
        }
    }

    /**
     * Refreshes membership through the catalog flow so a later catalog emission cannot
     * overwrite the refreshed status.
     */
    private fun refreshStatuses() {
        toolkitTilesRepository.refreshTileCategories()
    }

    /**
     * One event per filter tap: the list it shows already names the filter, so a separate
     * `select_content` for the chip would count the same tap twice.
     */
    private fun selectFilter(filter: ToolkitTilesFilter) {
        telemetryRepository.logViewItemList(
            itemListId = filter.name.lowercase(),
            itemListName = "quick_tools_${filter.name.lowercase()}",
        )
        setState { copy(selectedFilter = filter) }
    }

    /** Opening a category is the interest worth counting; closing it again is not a second one. */
    private fun toggleCategory(categoryId: String) {
        val expandedIds = state.value.expandedCategoryIds
        if (categoryId !in expandedIds) {
            telemetryRepository.logSelectContent(
                contentType = "tile_category",
                itemId = categoryId,
            )
        }
        val updated = expandedIds.mutate { if (categoryId in it) it.remove(categoryId) else it.add(categoryId) }
        setState { copy(expandedCategoryIds = updated) }
        saveExpandedJob = saveExpandedJob.restart {
            launchReport(action = Actions.SAVE_EXPANDED_CATEGORIES) {
                toolkitTilesRepository.saveExpandedCategoryIds(updated)
            }
        }
    }

    private fun handleAddTile(requestKey: String?) {
        startOperation(action = Actions.ADD_TILE)
        if (requestKey == null) {
            showNoTileMessage()
        } else {
            setState { copy(pendingTileRequest = requestKey) }
        }
    }

    /**
     * Records how the request to add a tile ended and re-reads Quick Settings membership.
     *
     * The tap itself is not reported: every tap ends here with an outcome, so the outcome event
     * already counts the requests and says what became of each.
     */
    private fun handleTileRequestFinished(requestKey: String, outcome: String) {
        telemetryRepository.logQuickSettingsTileRequest(tileId = requestKey, outcome = outcome)
        refreshStatuses()
    }

    private fun handleTileSetup(tileId: String) {
        telemetryRepository.logSelectContent(
            contentType = "tile_setup",
            itemId = tileId,
        )
        startOperation(
            action = Actions.OPEN_TILE_SETUP,
            extra = mapOf(ExtraKeys.TILE_ID to tileId),
        )
        showNoTileMessage()
    }

    private fun showNoTileMessage() {
        showMessage(UiMessage(NoTileText))
    }

    private object Actions {
        const val LOAD_TILES: String = "loadTiles"
        const val ADD_TILE: String = "addTile"
        const val OPEN_TILE_SETUP: String = "openTileSetup"
        const val SAVE_EXPANDED_CATEGORIES: String = "saveExpandedCategories"
    }

    private object ExtraKeys {
        const val TILE_ID: String = "tileId"
    }

    private companion object {
        val LoadFailedText = UiTextHelper.StringResource(R.string.tiles_error_failed_to_load)
        val NoTileText = UiTextHelper.StringResource(R.string.tiles_no_tile_message)
    }
}
