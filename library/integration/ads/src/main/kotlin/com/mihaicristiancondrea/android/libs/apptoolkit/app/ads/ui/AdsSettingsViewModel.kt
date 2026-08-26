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
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui.states.AdsToggleMode
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.ScreenMessageType
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.remote.extensions.asUiText
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * ViewModel for ads settings and consent interaction.
 *
 * One preference is observed. The build type only picks the toggle's wording, through
 * [AdsToggleMode]; what the preference actually does is `AdsDisplayPolicy`'s decision, and nothing
 * here branches on it.
 */
class AdsSettingsViewModel(
    private val repository: AdsSettingsRepository,
    private val consentRepository: ConsentRepository,
    private val dispatchers: DispatcherProvider,
    buildInfoProvider: BuildInfoProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<AdsSettingsUiState, AdsSettingsEvent, AdsSettingsAction>(
    initialState = UiStateScreen(
        data = AdsSettingsUiState(
            mode = if (buildInfoProvider.isDebugBuild) {
                AdsToggleMode.DISABLE
            } else {
                AdsToggleMode.REDUCE
            },
        ),
    ),
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
            is AdsSettingsEvent.SetLimitAds -> persist(enabled = event.enabled)
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
        startOperation(action = Actions.OBSERVE_LIMIT_ADS)
        observeJob = observeJob.restart {
            repository.observeLimitAds()
                .flowOn(dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        screenState.dismissSnackbar()
                        screenState.setLoading()
                    }
                }
                .onEach { limitAds ->
                    updateStateThreadSafe {
                        screenState.updateData(newState = ScreenState.Success()) { current ->
                            current.copy(limitAds = limitAds)
                        }
                    }
                }
                .catchReport(action = Actions.OBSERVE_LIMIT_ADS) {
                    updateStateThreadSafe {
                        screenState.updateData(newState = ScreenState.Error()) { current -> current }
                        screenState.setError(message = Errors.Database.DATABASE_OPERATION_FAILED.asUiText())
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    /** Optimistically applies [enabled], reverting to the rendered value if the write fails. */
    private fun persist(enabled: Boolean) {
        val extra = mapOf(ExtraKeys.ENABLED to enabled.toString())
        startOperation(action = Actions.PERSIST_LIMIT_ADS, extra = extra)

        var previousValue = false

        persistJob = persistJob.restart {
            flow { emit(repository.setLimitAds(enabled)) }
                .flowOn(dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        previousValue = screenState.value.data?.limitAds == true
                        screenState.dismissSnackbar()
                        screenState.updateData(newState = ScreenState.Success()) { current ->
                            current.copy(limitAds = enabled)
                        }
                    }
                }
                .onEach { result ->
                    result.onFailure { error ->
                        updateStateThreadSafe {
                            screenState.updateData(newState = ScreenState.Error()) { current ->
                                current.copy(limitAds = previousValue)
                            }
                            screenState.setError(message = error.asUiText())
                        }
                    }
                }
                .catchReport(action = Actions.PERSIST_LIMIT_ADS, extra = extra) {
                    updateStateThreadSafe {
                        screenState.updateData(newState = ScreenState.Error()) { current ->
                            current.copy(limitAds = previousValue)
                        }
                        screenState.setError(message = Errors.Database.DATABASE_OPERATION_FAILED.asUiText())
                    }
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
        const val OBSERVE_LIMIT_ADS: String = "observeLimitAds"
        const val PERSIST_LIMIT_ADS: String = "persistLimitAds"
        const val REQUEST_CONSENT: String = "requestConsent"
    }

    private object ExtraKeys {
        const val ENABLED: String = "enabled"
        const val HOST: String = "host"
    }
}
