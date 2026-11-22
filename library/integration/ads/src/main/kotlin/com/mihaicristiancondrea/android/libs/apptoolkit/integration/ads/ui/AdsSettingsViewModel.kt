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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.R
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.analytics.ADS_SETTINGS_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.analytics.AdsPreferenceKeys
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.analytics.adsToggleEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.constants.AdsSettingsOperationNames
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.contracts.AdsSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.models.AdsPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.states.AdsSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine

/**
 * Shows and stores the ad preferences, and opens the UMP privacy form. The switches follow the
 * store, so a failed write leaves them where they were, shows an error message, and reports no
 * toggle event. A failed read replaces them with a retryable failure.
 */
class AdsSettingsViewModel(
    private val repository: AdsSettingsRepository,
    private val consentRepository: ConsentRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<AdsSettingsUiState, AdsSettingsEvent>(
    initialState = AdsSettingsUiState(),
    telemetryRepository = telemetryRepository,
    screenName = ADS_SETTINGS_SCREEN.name,
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
            is AdsSettingsEvent.Load -> observe()

            is AdsSettingsEvent.SetAdsEnabled ->
                persistAdsEnabledJob = persistPreference(
                    job = persistAdsEnabledJob,
                    action = AdsSettingsOperationNames.PERSIST_ADS_ENABLED,
                    preferenceKey = AdsPreferenceKeys.DISPLAY_ADS,
                    enabled = event.enabled,
                ) { repository.setAdsEnabled(enabled = event.enabled) }

            is AdsSettingsEvent.SetReduceAds ->
                persistReduceAdsJob = persistPreference(
                    job = persistReduceAdsJob,
                    action = AdsSettingsOperationNames.PERSIST_REDUCE_ADS,
                    preferenceKey = AdsPreferenceKeys.REDUCE_ADS,
                    enabled = event.enabled,
                ) { repository.setReduceAds(enabled = event.enabled) }

            is AdsSettingsEvent.RequestConsent -> requestConsent(host = event.host)
        }
    }

    /** Follows both stored preferences. A retry restarts the collection that failed. */
    private fun observe() {
        setState {
            if (preferences is Loadable.Ready) {
                this
            } else {
                copy(preferences = Loadable.Loading)
            }
        }
        observeJob = observeJob.restart {
            combine(
                flow = repository.observeAdsEnabled(),
                flow2 = repository.observeReduceAds(),
            ) { adsEnabled, reduceAds ->
                AdsPreferences(adsEnabled = adsEnabled, reduceAds = reduceAds)
            }.collectReport(
                action = AdsSettingsOperationNames.OBSERVE_ADS_ENABLED,
                onError = { error ->
                    setState {
                        copy(
                            preferences = error.toFailed(
                                fallback = UiText.StringResource(resourceId = R.string.error_ads_settings_storage),
                            ),
                        )
                    }
                },
            ) { preferences ->
                setState { copy(preferences = Loadable.Ready(value = preferences)) }
            }
        }
    }

    /**
     * Restarts [job] with a reported [write] of one preference and logs the toggle event only
     * after persistence succeeds. Each preference keeps its own job, so changing one switch never
     * cancels the other's write.
     */
    private fun persistPreference(
        job: Job?,
        action: String,
        preferenceKey: String,
        enabled: Boolean,
        write: suspend () -> Unit,
    ): Job = job.restart {
        launchReport(
            action = action,
            extra = mapOf(AdsSettingsOperationNames.ExtraKeys.ENABLED to enabled.toString()),
            onError = { error ->
                showMessage(
                    message = error.toErrorMessage(
                        fallback = UiText.StringResource(resourceId = R.string.error_ads_settings_storage),
                    ),
                )
            },
        ) {
            write()
            telemetryRepository.logEvent(
                event = adsToggleEvent(enabled = enabled, preferenceKey = preferenceKey),
            )
        }
    }

    /**
     * Shows the privacy form from [host]. A new request replaces the wait for the previous one;
     * the round trip itself belongs to [ConsentRepository] and goes on.
     */
    private fun requestConsent(host: ConsentHost) {
        consentJob = consentJob.restart {
            launchReport(
                action = AdsSettingsOperationNames.REQUEST_CONSENT,
                onError = { error ->
                    showMessage(
                        message = error.toErrorMessage(
                            fallback = UiText.StringResource(resourceId = R.string.error_ads_consent_failed),
                        ),
                    )
                },
            ) {
                consentRepository.requestConsent(host = host, showIfRequired = false)
            }
        }
    }
}
