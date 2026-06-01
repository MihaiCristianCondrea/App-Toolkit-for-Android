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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.usecase.GetToolkitTilesUseCase
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.usecase.SyncToolkitTileStatusesUseCase
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.contract.ToolkitTilesAction
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.contract.ToolkitTilesEvent
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.state.ToolkitTilesFilter
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.state.ToolkitTilesUiState
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setError
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.setSuccess
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

/** Coordinates the static Toolkit Tiles catalog, filtering, and add-tile requests. */
class ToolkitTilesViewModel(
    private val getToolkitTilesUseCase: GetToolkitTilesUseCase,
    private val syncToolkitTileStatusesUseCase: SyncToolkitTileStatusesUseCase,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<ToolkitTilesUiState, ToolkitTilesEvent, ToolkitTilesAction>(
    initialState = UiStateScreen(data = ToolkitTilesUiState()),
    firebaseController = firebaseController,
    screenName = "ToolkitTiles",
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
        }
    }

    private fun loadTiles() {
        startOperation(action = Actions.LOAD_TILES)
        loadJob = loadJob.restart {
            getToolkitTilesUseCase()
                .flowOn(dispatchers.default)
                .onStart { screenState.setLoading() }
                .catchReport(action = Actions.LOAD_TILES) {
                    screenState.setError(
                        message = UiTextHelper.StringResource(R.string.tiles_error_failed_to_load),
                    )
                }
                .onEach { categories ->
                    val expandedIds = categories
                        .filter { category -> category.initiallyExpanded }
                        .map { category -> category.id }
                        .toPersistentSet()

                    val syncedCategories = syncToolkitTileStatusesUseCase(categories)
                    
                    screenState.setSuccess(
                        data = (screenData ?: ToolkitTilesUiState()).copy(
                            categories = syncedCategories.toImmutableList(),
                            expandedCategoryIds = expandedIds,
                        )
                    )
                }
                .launchIn(viewModelScope)
        }
    }

    private fun refreshStatuses() {
        screenState.update { current ->
            val data = current.data ?: return@update current
            current.copy(data = data.copy(categories = syncToolkitTileStatusesUseCase(data.categories).toImmutableList()))
        }
    }

    private fun selectFilter(filter: ToolkitTilesFilter) {
        screenState.update { current ->
            current.copy(data = current.data?.copy(selectedFilter = filter))
        }
    }

    private fun toggleCategory(categoryId: String) {
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
            current.copy(data = data.copy(expandedCategoryIds = updated))
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

    private fun handleTileSetup(tileId: String) { // FIXME: Parameter "tileId" is never used
        // Here we could navigate to a specific setup screen based on tileId
        // For now, we still show the message, but it's handled as a deliberate action
        showSetupMessage()
    }

    private fun showSetupMessage() {
        sendAction(ToolkitTilesAction.ShowSetupRequiredMessage)
    }

    private object Actions {
        const val LOAD_TILES: String = "loadTiles"
        const val ADD_TILE: String = "addTile"
    }
}
