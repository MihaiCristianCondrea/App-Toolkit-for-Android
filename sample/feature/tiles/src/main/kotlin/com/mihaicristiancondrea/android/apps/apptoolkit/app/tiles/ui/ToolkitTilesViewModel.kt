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

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.BreathingRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.CaffeineRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.SensorRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.SosRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.SystemRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.ToolkitTilesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.RingerMode
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.contracts.ToolkitTilesAction
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.contracts.ToolkitTilesEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.states.ToolkitSensorData
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.states.ToolkitTilesFilter
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.states.ToolkitTilesUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repository.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.setError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.setSuccess
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
    private val toolkitTilesRepository: ToolkitTilesRepository,
    private val sensorRepository: SensorRepository,
    private val breathingRepository: BreathingRepository,
    private val caffeineRepository: CaffeineRepository,
    private val systemRepository: SystemRepository,
    private val sosRepository: SosRepository,
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
            is ToolkitTilesEvent.CaffeineCycleClicked -> caffeineRepository.cycleState()
            is ToolkitTilesEvent.SoundModeClicked -> handleSoundModeCycle(event.current)
            is ToolkitTilesEvent.MusicSearchClicked -> systemRepository.launchMusicSearch()
            is ToolkitTilesEvent.SosClicked -> sosRepository.toggle()
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
            toolkitTilesRepository.tileCategories()
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

                    screenState.setSuccess(
                        data = (screenData ?: ToolkitTilesUiState()).copy(
                            categories = categories,
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
            val refreshed = toolkitTilesRepository.withCurrentStatuses(data.categories)
            current.copy(data = data.copy(categories = refreshed.toImmutableList()))
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
                    sensorRepository.getCompassAzimuth()
                        .onEach { azimuth ->
                            updateSensorData { it.copy(compassAzimuth = azimuth) }
                        }
                        .launchIn(this)
                }

                "bubble_level" -> {
                    sensorRepository.getLevelOrientation()
                        .onEach { (pitch, roll) ->
                            updateSensorData { it.copy(levelPitch = pitch, levelRoll = roll) }
                        }
                        .launchIn(this)
                }

                "lux_meter" -> {
                    sensorRepository.getLuxLevel()
                        .onEach { lux ->
                            updateSensorData { it.copy(luxLevel = lux) }
                        }
                        .launchIn(this)
                }

                "temperature" -> {
                    sensorRepository.getBatteryTemperature()
                        .onEach { temperature ->
                            updateSensorData { it.copy(batteryTemperature = temperature) }
                        }
                        .launchIn(this)
                }

                "caffeine" -> {
                    caffeineRepository.currentState
                        .onEach { state ->
                            screenState.update { current ->
                                val data = current.data ?: return@update current
                                current.copy(data = data.copy(caffeineState = state))
                            }
                        }
                        .launchIn(this)
                }

                "sound_mode" -> {
                    systemRepository.getRingerMode()
                        .onEach { mode ->
                            screenState.update { current ->
                                val data = current.data ?: return@update current
                                current.copy(data = data.copy(ringerMode = mode))
                            }
                        }
                        .launchIn(this)
                }

                "sos" -> {
                    sosRepository.isActive
                        .onEach { active ->
                            screenState.update { current ->
                                val data = current.data ?: return@update current
                                current.copy(data = data.copy(isSosActive = active))
                            }
                        }
                        .launchIn(this)
                }

                "breathing" -> {
                    breathingRepository.start()
                    breathingRepository.breathingState
                        .onEach { state ->
                            screenState.update { current ->
                                val data = current.data ?: return@update current
                                current.copy(data = data.copy(breathingState = state))
                            }
                        }
                        .launchIn(this)
                }
            }
        }
    }

    private fun stopSensorTracking() {
        sensorJob?.cancel()
        sensorJob = null
        breathingRepository.stop()
        sosRepository.cleanup()
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

    private fun handleSoundModeCycle(current: RingerMode) {
        val next = when (current) {
            RingerMode.Normal -> RingerMode.Vibrate
            RingerMode.Vibrate,
            RingerMode.Silent -> RingerMode.Normal
        }
        try {
            systemRepository.setRingerMode(next)
        } catch (_: Exception) {
            sendAction(ToolkitTilesAction.ShowMessage("Unable to change sound mode"))
        }
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
