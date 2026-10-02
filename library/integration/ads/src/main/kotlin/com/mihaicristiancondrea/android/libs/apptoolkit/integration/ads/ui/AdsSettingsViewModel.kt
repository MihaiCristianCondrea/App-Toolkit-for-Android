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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.R
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.contracts.AdsSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.models.AdsPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.states.AdsSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine

/**
 * Shows and stores the ad preferences, and opens the UMP privacy form. The switches follow the
 * store, so a failed write leaves them where they were and shows an error message. A failed read
 * replaces them with a retryable failure.
 */
class AdsSettingsViewModel(
    private val repository: AdsSettingsRepository,
    private val consentRepository: ConsentRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<AdsSettingsUiState, AdsSettingsEvent>(
    initialState = AdsSettingsUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "AdsSettings",
    viewModelName = "AdsSettingsViewModel",
) {

    private var observeJob: Job? = null
    private var persistAdsEnabledJob: Job? = null
    private var persistReduceAdsJob: Job? = null
    private var consentJob: Job? = null

    init {
        onEvent(event = AdsSettingsEvent.Load)
    }

    override fun handleEvent(event: AdsSettingsEvent) {
        when (event) {
            AdsSettingsEvent.Load -> observe()

            is AdsSettingsEvent.SetAdsEnabled ->
                persistAdsEnabledJob = persistPreference(
                    job = persistAdsEnabledJob,
                    action = Actions.PERSIST_ADS_ENABLED,
                    enabled = event.enabled,
                ) { repository.setAdsEnabled(event.enabled) }

            is AdsSettingsEvent.SetReduceAds ->
                persistReduceAdsJob = persistPreference(
                    job = persistReduceAdsJob,
                    action = Actions.PERSIST_REDUCE_ADS,
                    enabled = event.enabled,
                ) { repository.setReduceAds(event.enabled) }

            is AdsSettingsEvent.RequestConsent -> requestConsent(host = event.host)
        }
    }

    /** Follows both stored preferences. A retry restarts the collection that failed. */
    private fun observe() {
        observeJob = observeJob.restart {
            setState { copy(preferences = Loadable.Loading) }
            combine(
                repository.observeAdsEnabled(),
                repository.observeReduceAds(),
            ) { adsEnabled, reduceAds -> AdsPreferences(adsEnabled = adsEnabled, reduceAds = reduceAds) }
                .collectReport(
                    action = Actions.OBSERVE_ADS_ENABLED,
                    onError = { error -> setState { copy(preferences = error.toFailed(fallback = StorageErrorText)) } },
                ) { preferences ->
                    setState { copy(preferences = Loadable.Ready(preferences)) }
                }
        }
    }

    /**
     * Restarts [job] with a reported [write] of one preference. Each preference keeps its own job,
     * so changing one switch never cancels the other's write.
     */
    private fun persistPreference(
        job: Job?,
        action: String,
        enabled: Boolean,
        write: suspend () -> Unit,
    ): Job = job.restart {
        launchReport(
            action = action,
            extra = mapOf(ExtraKeys.ENABLED to enabled.toString()),
            onError = { error -> showMessage(error.toErrorMessage(fallback = StorageErrorText)) },
            block = write,
        )
    }

    /**
     * Shows the privacy form from [host]. A new request replaces the wait for the previous one;
     * the round trip itself belongs to [ConsentRepository] and goes on.
     */
    private fun requestConsent(host: ConsentHost) {
        consentJob = consentJob.restart {
            launchReport(
                action = Actions.REQUEST_CONSENT,
                extra = mapOf(ExtraKeys.HOST to host.activity::class.java.name),
                onError = { error -> showMessage(error.toErrorMessage(fallback = ConsentErrorText)) },
            ) {
                consentRepository.requestConsent(host = host, showIfRequired = false)
            }
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

    private companion object {
        val StorageErrorText = UiTextHelper.StringResource(R.string.error_ads_settings_storage)
        val ConsentErrorText = UiTextHelper.StringResource(R.string.error_ads_consent_failed)
    }
}
