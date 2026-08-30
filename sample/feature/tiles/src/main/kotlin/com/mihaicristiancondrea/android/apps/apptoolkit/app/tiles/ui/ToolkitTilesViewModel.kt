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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.AppScreenTracking
import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.ToolkitTilesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.contracts.ToolkitTilesAction
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.contracts.ToolkitTilesEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.mappers.toUiModels
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.states.ToolkitTilesFilter
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.states.ToolkitTilesUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Coordinates the static Toolkit Tiles catalog, filtering, and add-tile requests. */
class ToolkitTilesViewModel(
    private val toolkitTilesRepository: ToolkitTilesRepository,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<ToolkitTilesUiState, ToolkitTilesEvent, ToolkitTilesAction>(
    initialState = UiStateScreen(data = ToolkitTilesUiState()),
    firebaseController = firebaseController,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
) {
    private var loadJob: Job? = null

    init {
        onEvent(ToolkitTilesEvent.Initialize)
    }

    override fun handleEvent(event: ToolkitTilesEvent) {
        when (event) {
            is ToolkitTilesEvent.Initialize -> loadTiles()
            is ToolkitTilesEvent.Refresh -> refreshStatuses()
            is ToolkitTilesEvent.FilterSelected -> selectFilter(event.filter)
            is ToolkitTilesEvent.CategoryToggled -> toggleCategory(event.categoryId)
            is ToolkitTilesEvent.AddTileClicked -> handleAddTile(event.requestKey)
            is ToolkitTilesEvent.TileSetupClicked -> handleTileSetup(event.tileId)
            is ToolkitTilesEvent.AdStatusChanged -> updateAdStatus(event.adId, event.isLoaded)
        }
    }

    private fun updateAdStatus(adId: String, isLoaded: Boolean) {
        screenState.update { current ->
            val data = current.data ?: return@update current
            val updated = data.loadedAdIds.mutate {
                if (isLoaded) it.add(adId) else it.remove(adId)
            }
            current.copy(data = data.copy(loadedAdIds = updated))
        }
    }

    private fun loadTiles() {
        startOperation(action = Actions.LOAD_TILES)
        loadJob = loadJob.restart {
            combine(
                toolkitTilesRepository.tileCategories(),
                toolkitTilesRepository.expandedCategoryIds,
            ) { categories, expandedCategoryIds ->
                categories to expandedCategoryIds
            }
                .flowOn(dispatchers.default)
                .onStart { screenState.setLoading() }
                .catchReport(action = Actions.LOAD_TILES) {
                    screenState.setError(
                        message = UiTextHelper.StringResource(R.string.tiles_error_failed_to_load),
                    )
                }
                .onEach { (categories, expandedCategoryIds) ->
                    screenState.setSuccess(
                        data = (screenData ?: ToolkitTilesUiState()).copy(
                            categories = categories.toUiModels(),
                            expandedCategoryIds = expandedCategoryIds.toPersistentSet(),
                        )
                    )
                }
                .launchIn(viewModelScope)
        }
    }

    private fun refreshStatuses() {
        screenState.update { current ->
            val data = current.data ?: return@update current
            val refreshed = toolkitTilesRepository.currentTileCategories().toUiModels()
            current.copy(data = data.copy(categories = refreshed))
        }
    }

    private fun selectFilter(filter: ToolkitTilesFilter) {
        screenState.update { current ->
            current.copy(data = current.data?.copy(selectedFilter = filter))
        }
    }

    private fun toggleCategory(categoryId: String) {
        var updatedIds: Set<String>? = null
        screenState.update { current ->
            val data = current.data ?: return@update current
            val expandedIds = data.expandedCategoryIds
            val updated = expandedIds.mutate {
                if (categoryId in it) {
                    it.remove(categoryId)
                } else {
                    it.add(categoryId)
                }
            }
            updatedIds = updated
            current.copy(data = data.copy(expandedCategoryIds = updated))
        }
        updatedIds?.let { categoryIds ->
            viewModelScope.launch {
                toolkitTilesRepository.saveExpandedCategoryIds(categoryIds)
            }
        }
    }

    private fun handleAddTile(requestKey: String?) {
        startOperation(action = Actions.ADD_TILE)
        if (requestKey == null) {
            showSetupMessage()
        } else {
            sendAction(ToolkitTilesAction.RequestAddTile(requestKey))
        }
    }

    private fun handleTileSetup(tileId: String) {
        startOperation(
            action = Actions.OPEN_TILE_SETUP,
            extra = mapOf(ExtraKeys.TILE_ID to tileId),
        )
        showSetupMessage()
    }

    private fun showSetupMessage() {
        sendAction(ToolkitTilesAction.ShowSetupRequiredMessage)
    }

    private object Actions {
        const val LOAD_TILES: String = "loadTiles"
        const val ADD_TILE: String = "addTile"
        const val OPEN_TILE_SETUP: String = "openTileSetup"
    }

    private object ExtraKeys {
        const val TILE_ID: String = "tileId"
    }
}
