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
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui.contracts.AdsSettingsAction
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui.contracts.AdsSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui.states.AdsSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.ScreenMessageType
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.remote.extensions.asUiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.onFailure
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.dismissSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.showSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.updateData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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
            is AdsSettingsEvent.SetReduceAds -> persistReduceAds(enabled = event.enabled)
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
            combine(
                repository.observeAdsEnabled(),
                repository.observeReduceAds(),
            ) { adsEnabled, reduceAds -> AdsSettingsUiState(adsEnabled, reduceAds) }
                .flowOn(dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        screenState.dismissSnackbar()
                        screenState.setLoading()
                    }
                }
                .onEach { settings ->
                    updateStateThreadSafe {
                        screenState.updateData(newState = ScreenState.Success()) { settings }
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

    private fun persistReduceAds(enabled: Boolean) {
        startOperation(
            action = Actions.PERSIST_REDUCE_ADS,
            extra = mapOf(ExtraKeys.ENABLED to enabled.toString()),
        )
        persistJob = persistJob.restart {
            var previousValue = false
            flow { emit(repository.setReduceAds(enabled)) }
                .flowOn(dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        previousValue = screenState.value.data?.reduceAds ?: false
                        screenState.dismissSnackbar()
                        screenState.updateData(newState = ScreenState.Success()) { current ->
                            current.copy(reduceAds = enabled)
                        }
                    }
                }
                .onEach { result ->
                    result.onFailure { error ->
                        updateStateThreadSafe {
                            screenState.updateData(newState = ScreenState.Error()) { current ->
                                current.copy(reduceAds = previousValue)
                            }
                            screenState.setError(message = error.asUiText())
                        }
                    }
                }
                .catchReport(action = Actions.PERSIST_REDUCE_ADS) {
                    updateStateThreadSafe {
                        screenState.updateData(newState = ScreenState.Error()) { current ->
                            current.copy(reduceAds = previousValue)
                        }
                        screenState.setError(message = Errors.Database.DATABASE_OPERATION_FAILED.asUiText())
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun persistAdsEnabled(enabled: Boolean): Flow<DataState<Unit, Errors>> =
        flow { emit(repository.setAdsEnabled(enabled)) }

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
        const val PERSIST_REDUCE_ADS: String = "persistReduceAds"
        const val REQUEST_CONSENT: String = "requestConsent"
    }

    private object ExtraKeys {
        const val ENABLED: String = "enabled"
        const val HOST: String = "host"
    }
}
