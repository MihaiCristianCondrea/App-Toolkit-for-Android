package com.d4rk.android.libs.apptoolkit.app.ads.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases.ObserveAdsEnabledUseCase
import com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases.SetAdsEnabledUseCase
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsAction
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.ads.ui.state.AdsSettingsUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class AdsSettingsViewModel(
    private val observeAdsEnabled: ObserveAdsEnabledUseCase,
    private val setAdsEnabled: SetAdsEnabledUseCase,
    private val repository: AdsSettingsRepository,
    private val dispatchers: DispatcherProvider,
) : ScreenViewModel<AdsSettingsUiState, AdsSettingsEvent, AdsSettingsAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.IsLoading(),
        data = AdsSettingsUiState()
    )
) {

    private var observeJob: Job? = null
    private var setJob: Job? = null

    init {
        onEvent(AdsSettingsEvent.Initialize)
    }

    override fun onEvent(event: AdsSettingsEvent) {
        when (event) {
            AdsSettingsEvent.Initialize -> observe()
            is AdsSettingsEvent.SetAdsEnabled -> persist(event.enabled)
        }
    }

    private fun observe() {
        observeJob?.cancel()
        observeJob = observeAdsEnabled()
            .flowOn(dispatchers.io)
            .onStart { screenState.setLoading() }
            .map<Boolean, DataState<Boolean, Errors>> { enabled -> DataState.Success(enabled) }
            .catch {
                emit(
                    DataState.Error(
                        data = repository.defaultAdsEnabled,
                        error = Errors.Database.DATABASE_OPERATION_FAILED
                    )
                )
            }
            .onEach { result ->
                result
                    .onSuccess { enabled ->
                        screenState.updateData(newState = ScreenState.Success()) { current ->
                            current.copy(adsEnabled = enabled)
                        }
                    }
                    .onFailure { _ ->
                        screenState.updateData(newState = ScreenState.Error()) { current ->
                            current.copy(adsEnabled = repository.defaultAdsEnabled)
                        }
                    }
            }
            .launchIn(viewModelScope)
    }

    private fun persist(enabled: Boolean) {
        screenState.updateData(newState = ScreenState.Success()) { current ->
            current.copy(adsEnabled = enabled)
        }

        setJob?.cancel()
        setJob = flow<DataState<Unit, Errors>> {
            setAdsEnabled(enabled)
            emit(DataState.Success(Unit))
        }
            .flowOn(dispatchers.io)
            .catch {
                emit(DataState.Error(error = Errors.Database.DATABASE_OPERATION_FAILED))
            }
            .onEach { result ->
                result.onFailure {
                    screenState.updateData(newState = ScreenState.Error()) { current ->
                        current.copy(adsEnabled = !enabled)
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
