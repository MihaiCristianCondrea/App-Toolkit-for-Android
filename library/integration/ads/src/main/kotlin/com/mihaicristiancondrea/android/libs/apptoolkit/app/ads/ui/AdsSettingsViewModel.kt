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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * ViewModel for ads settings and consent interaction.
 *
 * Both persisted ads preferences are observed together: `adsEnabled` is the hard gate that no
 * longer has a switch, and `reduceAds` is the opt-in the screen toggles. The gate is still needed
 * because the personalized-ads row is meaningless while ads are off entirely.
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
    private var persistAdsEnabledJob: Job? = null
    private var persistReduceAdsJob: Job? = null
    private var consentJob: Job? = null

    /** Last successfully observed pair, used to restore the screen after an observation failure. */
    private var lastObserved: AdsSettingsUiState? = null

    init {
        onEvent(event = AdsSettingsEvent.Initialize)
    }

    override fun handleEvent(event: AdsSettingsEvent) {
        when (event) {
            is AdsSettingsEvent.Initialize -> observe()
            is AdsSettingsEvent.SetAdsEnabled -> persistAdsEnabled(enabled = event.enabled)
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
        startOperation(action = Actions.OBSERVE_ADS_SETTINGS)
        observeJob = observeJob.restart {
            combine(
                repository.observeAdsEnabled(),
                repository.observeReduceAds(),
            ) { adsEnabled, reduceAds ->
                AdsSettingsUiState(adsEnabled = adsEnabled, reduceAds = reduceAds)
            }
                .flowOn(dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        screenState.dismissSnackbar()
                        screenState.setLoading()
                    }
                }
                .onEach { observed ->
                    lastObserved = observed
                    updateStateThreadSafe {
                        screenState.updateData(newState = ScreenState.Success()) { observed }
                    }
                }
                .catchReport(action = Actions.OBSERVE_ADS_SETTINGS) {
                    updateStateThreadSafe {
                        // Nothing was observed yet, so the build default is the only value the
                        // screen can honestly show; a later emission replaces it.
                        val fallback = lastObserved
                            ?: AdsSettingsUiState(adsEnabled = repository.defaultAdsEnabled)
                        screenState.updateData(newState = ScreenState.Error()) { fallback }
                        screenState.setError(message = Errors.Database.DATABASE_OPERATION_FAILED.asUiText())
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun persistAdsEnabled(enabled: Boolean) {
        persistAdsEnabledJob = persistAdsEnabledJob.restart {
            persist(
                action = Actions.PERSIST_ADS_ENABLED,
                enabled = enabled,
                currentValue = AdsSettingsUiState::adsEnabled,
                withValue = { state, value -> state.copy(adsEnabled = value) },
                write = { repository.setAdsEnabled(enabled) },
            )
        }
    }

    private fun persistReduceAds(enabled: Boolean) {
        persistReduceAdsJob = persistReduceAdsJob.restart {
            persist(
                action = Actions.PERSIST_REDUCE_ADS,
                enabled = enabled,
                currentValue = AdsSettingsUiState::reduceAds,
                withValue = { state, value -> state.copy(reduceAds = value) },
                write = { repository.setReduceAds(enabled) },
            )
        }
    }

    /**
     * Optimistically applies [enabled], then reverts to the previously rendered value if the write
     * fails. [currentValue] and [withValue] are the only parts that differ per preference.
     */
    private fun persist(
        action: String,
        enabled: Boolean,
        currentValue: (AdsSettingsUiState) -> Boolean,
        withValue: (AdsSettingsUiState, Boolean) -> AdsSettingsUiState,
        write: suspend () -> DataState<Unit, Errors.Database>,
    ): Job {
        val extra = mapOf(ExtraKeys.ENABLED to enabled.toString())
        startOperation(action = action, extra = extra)

        var previousValue = false

        return flow { emit(write()) }
            .flowOn(dispatchers.io)
            .onStart {
                updateStateThreadSafe {
                    previousValue = screenState.value.data?.let(currentValue) == true
                    screenState.dismissSnackbar()
                    screenState.updateData(newState = ScreenState.Success()) { current ->
                        withValue(current, enabled)
                    }
                }
            }
            .onEach { result ->
                result.onFailure { error ->
                    updateStateThreadSafe {
                        screenState.updateData(newState = ScreenState.Error()) { current ->
                            withValue(current, previousValue)
                        }
                        screenState.setError(message = error.asUiText())
                    }
                }
            }
            .catchReport(action = action, extra = extra) {
                updateStateThreadSafe {
                    screenState.updateData(newState = ScreenState.Error()) { current ->
                        withValue(current, previousValue)
                    }
                    screenState.setError(message = Errors.Database.DATABASE_OPERATION_FAILED.asUiText())
                }
            }
            .launchIn(viewModelScope)
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
        const val OBSERVE_ADS_SETTINGS: String = "observeAdsSettings"
        const val PERSIST_ADS_ENABLED: String = "persistAdsEnabled"
        const val PERSIST_REDUCE_ADS: String = "persistReduceAds"
        const val REQUEST_CONSENT: String = "requestConsent"
    }

    private object ExtraKeys {
        const val ENABLED: String = "enabled"
        const val HOST: String = "host"
    }
}
