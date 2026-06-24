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
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.usecase.GetBreathingDataUseCase
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.usecase.GetSensorDataUseCase
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.usecase.GetSystemDataUseCase
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.usecase.GetToolkitTilesUseCase
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.usecase.SyncToolkitTileStatusesUseCase
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.contract.ToolkitTilesAction
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.contract.ToolkitTilesEvent
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.state.ToolkitSensorData
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
import kotlinx.coroutines.launch

/** Coordinates the static Toolkit Tiles catalog, filtering, and add-tile requests. */
class ToolkitTilesViewModel(
    private val getToolkitTilesUseCase: GetToolkitTilesUseCase,
    private val getSensorDataUseCase: GetSensorDataUseCase,
    private val getBreathingDataUseCase: GetBreathingDataUseCase,
    private val getSystemDataUseCase: GetSystemDataUseCase,
    private val syncToolkitTileStatusesUseCase: SyncToolkitTileStatusesUseCase,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<ToolkitTilesUiState, ToolkitTilesEvent, ToolkitTilesAction>(
    initialState = UiStateScreen(data = ToolkitTilesUiState()),
    firebaseController = firebaseController,
    screenName = "ToolkitTiles",
) {
    private var loadJob: Job? = null
    private var sensorJob: Job? = null

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
            is ToolkitTilesEvent.TilePreviewOpened -> startSensorTracking(event.tileId)
            is ToolkitTilesEvent.TilePreviewClosed -> stopSensorTracking()
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

    private fun handleTileSetup(tileId: String) {
        startOperation(
            action = Actions.OPEN_TILE_SETUP,
            extra = mapOf(ExtraKeys.TILE_ID to tileId),
        )
        showSetupMessage()
    }

    private fun startSensorTracking(tileId: String) {
        sensorJob?.cancel()
        sensorJob = viewModelScope.launch(dispatchers.default) {
            when (tileId) {
                "compass" -> {
                    getSensorDataUseCase.getCompassAzimuth()
                        .onEach { azimuth ->
                            updateSensorData { it.copy(compassAzimuth = azimuth) }
                        }
                        .launchIn(this)
                }

                "bubble_level" -> {
                    getSensorDataUseCase.getLevelOrientation()
                        .onEach { (pitch, roll) ->
                            updateSensorData { it.copy(levelPitch = pitch, levelRoll = roll) }
                        }
                        .launchIn(this)
                }

                "lux_meter" -> {
                    getSensorDataUseCase.getLuxLevel()
                        .onEach { lux ->
                            updateSensorData { it.copy(luxLevel = lux) }
                        }
                        .launchIn(this)
                }

                "breathing" -> {
                    getBreathingDataUseCase.start()
                    getBreathingDataUseCase.breathingState
                        .onEach { state ->
                            screenState.update { current ->
                                val data = current.data ?: return@update current
                                current.copy(data = data.copy(breathingState = state))
                            }
                        }
                        .launchIn(this)
                }

                "memory" -> {
                    getSystemDataUseCase.getMemoryInfo()
                        .onEach { info ->
                            screenState.update { it.copy(data = it.data?.copy(memoryInfo = info)) }
                        }
                        .launchIn(this)
                }

                "network_traffic" -> {
                    getSystemDataUseCase.getNetworkTraffic()
                        .onEach { traffic ->
                            screenState.update { it.copy(data = it.data?.copy(networkTraffic = traffic)) }
                        }
                        .launchIn(this)
                }
            }
        }
    }

    private fun stopSensorTracking() {
        sensorJob?.cancel()
        sensorJob = null
        getBreathingDataUseCase.stop()
        updateSensorData { ToolkitSensorData() }
    }

    private fun updateSensorData(update: (ToolkitSensorData) -> ToolkitSensorData) {
        screenState.update { current ->
            val data = current.data ?: return@update current
            current.copy(data = data.copy(sensorData = update(data.sensorData)))
        }
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
