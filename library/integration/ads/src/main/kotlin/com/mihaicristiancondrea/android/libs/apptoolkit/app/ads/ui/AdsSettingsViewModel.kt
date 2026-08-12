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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsAction
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui.state.AdsSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.model.Result
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.repository.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.setError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.updateData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.ScreenMessageType
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.remote.extensions.asUiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
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
    private val repository: AdsSettingsRepository,
    private val consentRepository: ConsentRepository,
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
            repository.observeAdsEnabled()
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
            when (repository.setAdsEnabled(enabled)) {
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
            consentRepository.requestConsent(host = host, showIfRequired = false)
                // Keep upstream consent work off Main by not applying flowOn(main) here.
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
