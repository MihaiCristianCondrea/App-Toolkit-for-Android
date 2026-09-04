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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.data.repositories.CacheRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.contracts.AdvancedSettingsAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.contracts.AdvancedSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.states.AdvancedSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.onFailure
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.onSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.copyData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.dismissSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.updateData
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.resources.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * ViewModel for advanced settings actions such as cache clearing.
 */
class AdvancedSettingsViewModel(
    private val repository: CacheRepository,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<AdvancedSettingsUiState, AdvancedSettingsEvent, AdvancedSettingsAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.Success(),
        data = AdvancedSettingsUiState(),
    ),
    firebaseController = firebaseController,
    screenName = "AdvancedSettings",
) {
    private var observeJob: Job? = null

    override fun handleEvent(event: AdvancedSettingsEvent) {
        when (event) {
            is AdvancedSettingsEvent.ClearCache -> clearCache()
            is AdvancedSettingsEvent.MessageShown -> onMessageShown()
        }
    }

    private fun clearCache() {
        startOperation(action = Actions.CLEAR_CACHE)
        observeJob = observeJob.restart {
            repository.clearCache()
                .flowOn(dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        screenState.dismissSnackbar()
                        screenState.setLoading()
                    }
                }
                .onEach { result ->
                    result
                        .onSuccess {
                            updateStateThreadSafe {
                                screenState.updateData(newState = ScreenState.Success()) { current ->
                                    current.copy(cacheClearMessage = R.string.cache_cleared_success)
                                }
                            }
                        }
                        .onFailure {
                            updateStateThreadSafe {
                                screenState.updateData(newState = ScreenState.Error()) { current ->
                                    current.copy(cacheClearMessage = R.string.cache_cleared_error)
                                }
                            }
                        }
                }
                .catchReport(action = Actions.CLEAR_CACHE) {
                    updateStateThreadSafe {
                        screenState.updateData(newState = ScreenState.Error()) { current ->
                            current.copy(cacheClearMessage = R.string.cache_cleared_error)
                        }
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun onMessageShown() {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.copyData { copy(cacheClearMessage = null) }
            }
        }
    }

    private object Actions {
        const val CLEAR_CACHE: String = "clearCache"
    }
}

