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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.contracts.AdsSettingsAction
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.contracts.AdsSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.states.AdsSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.ScreenMessageType
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions.asUiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.onFailure
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
 */
class AdsSettingsViewModel(
    private val repository: AdsSettingsRepository,
    private val consentRepository: ConsentRepository,
    private val dispatchers: DispatcherProvider,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<AdsSettingsUiState, AdsSettingsEvent, AdsSettingsAction>(
    initialState = UiStateScreen(data = AdsSettingsUiState()),
    telemetryRepository = telemetryRepository,
    screenName = "AdsSettings",
    viewModelName = "AdsSettingsViewModel",
) {

    private var observeJob: Job? = null
    // One job per setting: restarting a shared job would cancel the other setting's write after its
    // switch had already moved.
    private var persistAdsEnabledJob: Job? = null
    private var persistReduceAdsJob: Job? = null
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
            is AdsSettingsEvent.DismissSnackbar -> screenState.dismissSnackbar()
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
        persistAdsEnabledJob = persistSwitch(
            job = persistAdsEnabledJob,
            action = Actions.PERSIST_ADS_ENABLED,
            enabled = enabled,
            fallback = repository.defaultAdsEnabled,
            read = { state -> state.adsEnabled },
            set = { state, value -> state.copy(adsEnabled = value) },
        ) { repository.setAdsEnabled(enabled) }
    }

    private fun persistReduceAds(enabled: Boolean) {
        persistReduceAdsJob = persistSwitch(
            job = persistReduceAdsJob,
            action = Actions.PERSIST_REDUCE_ADS,
            enabled = enabled,
            fallback = false,
            read = { state -> state.reduceAds },
            set = { state, value -> state.copy(reduceAds = value) },
        ) { repository.setReduceAds(enabled) }
    }

    /**
     * Writes one switch, showing [enabled] straight away and putting the previous value back,
     * with an error snackbar, if [write] fails.
     *
     * @param fallback The value to restore when the screen has no state yet.
     * @param read Reads this switch from the screen state.
     * @param set Returns the screen state with this switch set to a value.
     */
    private fun persistSwitch(
        job: Job?,
        action: String,
        enabled: Boolean,
        fallback: Boolean,
        read: (AdsSettingsUiState) -> Boolean,
        set: (AdsSettingsUiState, Boolean) -> AdsSettingsUiState,
        write: suspend () -> DataState<Unit, Errors>,
    ): Job {
        val extra = mapOf(ExtraKeys.ENABLED to enabled.toString())
        startOperation(action = action, extra = extra)
        return job.restart {
            var previousValue = fallback

            suspend fun revert(message: UiTextHelper) {
                updateStateThreadSafe {
                    screenState.updateData(newState = ScreenState.Error()) { current ->
                        set(current, previousValue)
                    }
                    screenState.setError(message = message)
                }
            }

            flow { emit(write()) }
                .flowOn(dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        previousValue = screenState.value.data?.let(read) ?: fallback
                        screenState.dismissSnackbar()
                        screenState.updateData(newState = ScreenState.Success()) { current ->
                            set(current, enabled)
                        }
                    }
                }
                .onEach { result -> result.onFailure { error -> revert(error.asUiText()) } }
                .catchReport(action = action, extra = extra) {
                    revert(Errors.Database.DATABASE_OPERATION_FAILED.asUiText())
                }
                .launchIn(viewModelScope)
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
        const val PERSIST_REDUCE_ADS: String = "persistReduceAds"
        const val REQUEST_CONSENT: String = "requestConsent"
    }

    private object ExtraKeys {
        const val ENABLED: String = "enabled"
        const val HOST: String = "host"
    }
}
