package com.d4rk.android.libs.apptoolkit.app.ads.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases.ObserveAdsEnabledUseCase
import com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases.SetAdsEnabledUseCase
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsAction
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.ads.ui.state.AdsSettingsUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * ViewModel for the Ads Settings screen.
 *
 * This ViewModel manages the UI state for the ads settings, allowing the user to
 * enable or disable advertisements within the application. It communicates with the
 * domain layer to observe and persist the ad-enabled status.
 *
 * @param observeAdsEnabled Use case to observe the current ad-enabled status from the repository.
 * @param setAdsEnabled Use case to update the ad-enabled status in the repository.
 * @param repository Repository for ads settings, used here to get the default value on error.
 * @param dispatchers Provides coroutine dispatchers for different threads (IO, Main, etc.).
 */
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

    /**
     * Observes the state of the "ads enabled" setting from the data source.
     *
     * This function initiates a flow that listens for changes to the ads enabled preference.
     * - It cancels any previous observation to avoid multiple listeners.
     * - On start, it sets the UI screen state to loading.
     * - It maps the boolean result from the use case to a [DataState.Success].
     * - If an error occurs during the observation (e.g., a database issue), it catches the exception
     *   and emits a [DataState.Error], falling back to the default ads enabled value from the repository.
     * - For each emitted result:
     *   - On success, it updates the UI state with the fetched `adsEnabled` value and sets the screen state to [ScreenState.Success].
     *   - On failure, it updates the UI state with the default `adsEnabled` value and sets the screen state to [ScreenState.Error].
     * The entire flow is launched within the `viewModelScope`.
     */
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

    /**
     * Persists the user's choice for enabling or disabling ads.
     *
     * This function first optimistically updates the UI state to reflect the new `enabled` status.
     * It then launches a coroutine to save this setting to the underlying data source via the
     * `setAdsEnabled` use case. If the persistence operation fails, it reverts the UI state
     * to the previous value and updates the screen state to show an error.
     *
     * @param enabled A boolean indicating whether ads should be enabled (`true`) or disabled (`false`).
     */
    private fun persist(enabled: Boolean) {
        val previousValue = uiState.value.data?.adsEnabled ?: repository.defaultAdsEnabled

        screenState.updateData(newState = ScreenState.Success()) { current ->
            current.copy(adsEnabled = enabled)
        }

        setJob?.cancel()
        setJob = persistAdsEnabled(enabled)
            .flowOn(dispatchers.io)
            .onEach { result ->
                result
                    .onSuccess {
                        screenState.updateState(ScreenState.Success())
                    }
                    .onFailure {
                        screenState.updateData(newState = ScreenState.Error()) { current ->
                            current.copy(adsEnabled = previousValue)
                        }
                    }
            }
            .launchIn(viewModelScope)
    }

    private fun persistAdsEnabled(enabled: Boolean): Flow<DataState<Unit, Errors>> =
        flow {
            emit(setAdsEnabled(enabled))

            // TODO: use onSuccess and onFailure like above
        }.map { result -> // FIXME: Cannot infer type for type parameter 'R'. Specify it explicitly.
            when (result) {
                is Result.Success -> DataState.Success(Unit) // FIXME: Cannot infer type for type parameter 'E'. Specify it explicitly.
                is Result.Error -> DataState.Error( // FIXME: Cannot infer type for type parameter 'D'. Specify it explicitly.
                    error = Errors.Database.DATABASE_OPERATION_FAILED
                )
            }
        }.catch { throwable ->
            if (throwable is CancellationException) throw throwable
            emit(DataState.Error(error = Errors.Database.DATABASE_OPERATION_FAILED))
        }
}
