package com.d4rk.android.libs.apptoolkit.app.ads.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases.ObserveAdsEnabledUseCase
import com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases.SetAdsEnabledUseCase
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsAction
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.ads.ui.state.AdsSettingsUiState
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.RequestConsentUseCase
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
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
 * This ViewModel manages the UI state for ads settings, allowing the user to
 * enable or disable ads. It observes changes from the repository and persists
 * user actions.
 *
 * Rules:
 * - Use Flow + flowOn + launchIn(viewModelScope)
 * - Report failures with firebaseController.reportViewModelError in catch blocks
 * - Update UI using onSuccess/onFailure
 * - Use setLoading in onStart when needed
 * - Prefer thread-safe state updates when concurrent flows can update the same state
 */
class AdsSettingsViewModel(
    private val observeAdsEnabled: ObserveAdsEnabledUseCase,
    private val setAdsEnabled: SetAdsEnabledUseCase,
    private val requestConsentUseCase: RequestConsentUseCase,
    private val repository: AdsSettingsRepository,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
) : ScreenViewModel<AdsSettingsUiState, AdsSettingsEvent, AdsSettingsAction>(
    initialState = UiStateScreen(
        data = AdsSettingsUiState()
    )
) {

    private var observeJob: Job? = null
    private var persistJob: Job? = null
    private var consentJob: Job? = null

    init {
        firebaseController.logBreadcrumb(
            message = "AdsSettingsViewModel initialized",
            attributes = mapOf("screen" to "AdsSettings"),
        )
        onEvent(event = AdsSettingsEvent.Initialize)
    }

    override fun onEvent(event: AdsSettingsEvent) {
        firebaseController.logBreadcrumb(
            message = "AdsSettingsViewModel event",
            attributes = mapOf("event" to event::class.java.simpleName),
        )
        when (event) {
            is AdsSettingsEvent.Initialize -> observe()
            is AdsSettingsEvent.SetAdsEnabled -> persist(enabled = event.enabled)
            is AdsSettingsEvent.RequestConsent -> requestConsent(host = event.host)
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
        firebaseController.logBreadcrumb(
            message = "Ads settings observe started",
            attributes = mapOf("source" to "AdsSettingsViewModel"),
        )
        observeJob?.cancel()
        observeJob = observeAdsEnabled()
            .flowOn(dispatchers.io)
            .onStart { updateStateThreadSafe { screenState.setLoading() } }
            .map<Boolean, DataState<Boolean, Errors>> { enabled -> DataState.Success(enabled) }
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                firebaseController.reportViewModelError(
                    viewModelName = "AdsSettingsViewModel",
                    action = "observe",
                    throwable = throwable,
                )
                emit(
                    DataState.Error(
                        data = repository.defaultAdsEnabled,
                        error = Errors.Database.DATABASE_OPERATION_FAILED,
                    )
                )
            }
            .onEach { result ->
                updateStateThreadSafe {
                    result
                        .onSuccess { enabled ->
                            screenState.updateData(newState = ScreenState.Success()) { current ->
                                current.copy(adsEnabled = enabled)
                            }
                        }
                        .onFailure { error ->
                            val fallback =
                                (result as? DataState.Error)?.data ?: repository.defaultAdsEnabled
                            screenState.updateData(newState = ScreenState.Error()) { current ->
                                current.copy(adsEnabled = fallback)
                            }
                            screenState.showSnackbar(
                                UiSnackbar(
                                    message = error.asUiText(),
                                    isError = true,
                                    timeStamp = System.nanoTime(),
                                    type = ScreenMessageType.SNACKBAR,
                                )
                            )
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
        firebaseController.logBreadcrumb(
            message = "Ads settings persist",
            attributes = mapOf("enabled" to enabled.toString()),
        )
        persistJob?.cancel()

        var previousValue = repository.defaultAdsEnabled

        persistJob = persistAdsEnabled(enabled)
            .flowOn(dispatchers.io)
            .onStart {
                updateStateThreadSafe {
                    previousValue =
                        screenState.value.data?.adsEnabled ?: repository.defaultAdsEnabled
                    screenState.updateData(newState = ScreenState.Success()) { current ->
                        current.copy(adsEnabled = enabled)
                    }
                }
            }
            .onEach { result ->
                updateStateThreadSafe {
                    result
                        .onSuccess {
                            screenState.updateState(ScreenState.Success())
                        }
                        .onFailure { error ->
                            screenState.updateData(newState = ScreenState.Error()) { current ->
                                current.copy(adsEnabled = previousValue)
                            }
                            screenState.showSnackbar(
                                UiSnackbar(
                                    message = error.asUiText(),
                                    isError = true,
                                    timeStamp = System.nanoTime(),
                                    type = ScreenMessageType.SNACKBAR,
                                )
                            )
                        }
                }
            }
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                firebaseController.reportViewModelError(
                    viewModelName = "AdsSettingsViewModel",
                    action = "persist",
                    throwable = throwable,
                )
                updateStateThreadSafe {
                    screenState.updateData(newState = ScreenState.Error()) { current ->
                        current.copy(adsEnabled = previousValue)
                    }
                    screenState.showSnackbar(
                        UiSnackbar(
                            message = Errors.Database.DATABASE_OPERATION_FAILED.asUiText(),
                            isError = true,
                            timeStamp = System.nanoTime(),
                            type = ScreenMessageType.SNACKBAR,
                        )
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun persistAdsEnabled(enabled: Boolean): Flow<DataState<Unit, Errors>> =
        flow<DataState<Unit, Errors>> {
            when (setAdsEnabled(enabled)) {
                is Result.Success -> emit(DataState.Success(Unit))
                is Result.Error -> emit(DataState.Error(error = Errors.Database.DATABASE_OPERATION_FAILED))
            }
        }
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                firebaseController.reportViewModelError(
                    viewModelName = "AdsSettingsViewModel",
                    action = "persistAdsEnabled",
                    throwable = throwable,
                )
                emit(DataState.Error(error = Errors.Database.DATABASE_OPERATION_FAILED))
            }

    private fun requestConsent(host: ConsentHost) {
        firebaseController.logBreadcrumb(
            message = "Ads consent request",
            attributes = mapOf("host" to host.activity::class.java.name),
        )
        consentJob?.cancel()
        consentJob = requestConsentUseCase(host = host, showIfRequired = false)
            .flowOn(dispatchers.main)
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                firebaseController.reportViewModelError(
                    viewModelName = "AdsSettingsViewModel",
                    action = "requestConsent",
                    throwable = throwable,
                )
                emit(DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO))
            }
            .onEach { result ->
                updateStateThreadSafe {
                    result.onFailure { error ->
                        screenState.showSnackbar(
                            UiSnackbar(
                                message = error.asUiText(),
                                isError = true,
                                timeStamp = System.nanoTime(),
                                type = ScreenMessageType.SNACKBAR,
                            )
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
