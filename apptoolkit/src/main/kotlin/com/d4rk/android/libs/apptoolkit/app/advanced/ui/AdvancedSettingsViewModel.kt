package com.d4rk.android.libs.apptoolkit.app.advanced.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.advanced.domain.repository.CacheRepository
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.contract.AdvancedSettingsAction
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.contract.AdvancedSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.state.AdvancedSettingsUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.copyData
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

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
                .map<Result<Unit>, DataState<Unit, Errors.Database>> { result ->
                    when (result) {
                        is Result.Success -> DataState.Success(Unit)
                        is Result.Error -> DataState.Error(error = Errors.Database.DATABASE_OPERATION_FAILED)
                    }
                }
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
