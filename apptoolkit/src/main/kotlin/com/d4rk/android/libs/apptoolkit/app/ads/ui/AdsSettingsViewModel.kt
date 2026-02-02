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
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setError
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * ViewModel for ads settings and consent interaction.
 */
class AdsSettingsViewModel(
    private val observeAdsEnabled: ObserveAdsEnabledUseCase,
    private val setAdsEnabled: SetAdsEnabledUseCase,
    private val requestConsentUseCase: RequestConsentUseCase,
    private val repository: AdsSettingsRepository,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<AdsSettingsUiState, AdsSettingsEvent, AdsSettingsAction>(
    initialState = UiStateScreen(data = AdsSettingsUiState()),
    firebaseController = firebaseController,
    screenName = "AdsSettings",
) {

    private var observeJob: Job? = null
    private var persistJob: Job? = null
    private var consentJob: Job? = null

    init {
        onEvent(event = AdsSettingsEvent.Initialize)
    }

    override fun handleEvent(event: AdsSettingsEvent) {
        when (event) {
            is AdsSettingsEvent.Initialize -> observe()
            is AdsSettingsEvent.SetAdsEnabled -> persist(enabled = event.enabled)
            is AdsSettingsEvent.RequestConsent -> requestConsent(host = event.host)
        }
    }

    private fun errorSnackbar(message: UiTextHelper): UiSnackbar =
        UiSnackbar(
            type = ScreenMessageType.SNACKBAR,
            message = message,
            isError = true,
            timeStamp = System.nanoTime(),
        )

    private fun observe() {
        startOperation(action = Actions.OBSERVE_ADS_ENABLED)
        observeJob = observeJob.restart {
            observeAdsEnabled.invoke()
                .flowOn(dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        screenState.dismissSnackbar()
                        screenState.setLoading()
                    }
                }
                .onEach { enabled ->
                    updateStateThreadSafe {
                        screenState.updateData(newState = ScreenState.Success()) { current ->
                            current.copy(adsEnabled = enabled)
                        }
                    }
                }
                .catchReport(action = Actions.OBSERVE_ADS_ENABLED) {
                    updateStateThreadSafe {
                        val fallback =
                            screenState.value.data?.adsEnabled ?: repository.defaultAdsEnabled
                        screenState.updateData(newState = ScreenState.Error()) { current ->
                            current.copy(adsEnabled = fallback)
                        }
                        screenState.setError(message = Errors.Database.DATABASE_OPERATION_FAILED.asUiText())
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun persist(enabled: Boolean) {
        startOperation(
            action = Actions.PERSIST_ADS_ENABLED,
            extra = mapOf(ExtraKeys.ENABLED to enabled.toString())
        )
        persistJob = persistJob.restart {
            var previousValue = repository.defaultAdsEnabled

            persistAdsEnabled(enabled)
                .flowOn(dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        previousValue =
                            screenState.value.data?.adsEnabled ?: repository.defaultAdsEnabled
                        screenState.dismissSnackbar()
                        screenState.updateData(newState = ScreenState.Success()) { current ->
                            current.copy(adsEnabled = enabled)
                        }
                    }
                }
                .onEach { result ->
                    result
                        .onFailure { error ->
                            updateStateThreadSafe {
                                screenState.updateData(newState = ScreenState.Error()) { current ->
                                    current.copy(adsEnabled = previousValue)
                                }
                                screenState.setError(message = error.asUiText())
                            }
                        }

                }
                .catchReport(
                    action = Actions.PERSIST_ADS_ENABLED,
                    extra = mapOf(ExtraKeys.ENABLED to enabled.toString())
                ) {
                    updateStateThreadSafe {
                        screenState.updateData(newState = ScreenState.Error()) { current ->
                            current.copy(adsEnabled = previousValue)
                        }
                        screenState.setError(message = Errors.Database.DATABASE_OPERATION_FAILED.asUiText())
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun persistAdsEnabled(enabled: Boolean): Flow<DataState<Unit, Errors>> =
        flow {
            when (setAdsEnabled(enabled)) {
                is Result.Success -> emit(DataState.Success(Unit))
                is Result.Error -> emit(DataState.Error(error = Errors.Database.DATABASE_OPERATION_FAILED))
            }
        }

    private fun requestConsent(host: ConsentHost) {
        startOperation(
            action = Actions.REQUEST_CONSENT,
            extra = mapOf(ExtraKeys.HOST to host.activity::class.java.name)
        )
        consentJob = consentJob.restart {
            requestConsentUseCase.invoke(host = host, showIfRequired = false)
                .flowOn(dispatchers.main)
                .onEach { result ->
                    result.onFailure { error ->
                        updateStateThreadSafe {
                            screenState.showSnackbar(errorSnackbar(error.asUiText()))
                        }
                    }
                }
                .catchReport(
                    action = Actions.REQUEST_CONSENT,
                    extra = mapOf(ExtraKeys.HOST to host.activity::class.java.name)
                ) {
                    updateStateThreadSafe {
                        screenState.showSnackbar(
                            errorSnackbar(Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO.asUiText())
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private object Actions {
        const val OBSERVE_ADS_ENABLED: String = "observeAdsEnabled"
        const val PERSIST_ADS_ENABLED: String = "persistAdsEnabled"
        const val REQUEST_CONSENT: String = "requestConsent"
    }

    private object ExtraKeys {
        const val ENABLED: String = "enabled"
        const val HOST: String = "host"
    }
}
