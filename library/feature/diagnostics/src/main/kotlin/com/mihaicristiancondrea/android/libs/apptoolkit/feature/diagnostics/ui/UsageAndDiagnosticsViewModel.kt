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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.data.models.UsageAndDiagnosticsSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.data.repositories.UsageAndDiagnosticsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.analytics.USAGE_DIAGNOSTICS_SCREEN_NAME
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.constants.UsageAndDiagnosticsOperationNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.contracts.UsageAndDiagnosticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.states.UsageAndDiagnosticsUiState
import kotlinx.coroutines.Job

/**
 * Shows and stores the reporting and consent choices for the usage and diagnostics screen and the
 * onboarding page. The repository applies each stored choice to the consent SDKs, so this only
 * follows and writes them. A failed read replaces the choices with a retryable failure; a failed
 * write keeps them on screen and shows an error message.
 */
class UsageAndDiagnosticsViewModel(
    private val repository: UsageAndDiagnosticsRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<UsageAndDiagnosticsUiState, UsageAndDiagnosticsEvent>(
    initialState = UsageAndDiagnosticsUiState(),
    telemetryRepository = telemetryRepository,
    screenName = USAGE_DIAGNOSTICS_SCREEN_NAME,
    viewModelName = "UsageAndDiagnosticsViewModel",
) {

    private var observeConsentsJob: Job? = null

    private var setUsageAndDiagnosticsJob: Job? = null
    private var setAnalyticsConsentJob: Job? = null
    private var setAdStorageConsentJob: Job? = null
    private var setAdUserDataConsentJob: Job? = null
    private var setAdPersonalizationConsentJob: Job? = null
    private var setConsentBundleJob: Job? = null

    init {
        onEvent(event = UsageAndDiagnosticsEvent.Load)
    }

    override fun handleEvent(event: UsageAndDiagnosticsEvent) {
        when (event) {
            is UsageAndDiagnosticsEvent.Load -> observeConsents()

            is UsageAndDiagnosticsEvent.SetUsageAndDiagnostics ->
                setUsageAndDiagnosticsJob = persistChoice(
                    job = setUsageAndDiagnosticsJob,
                    action = UsageAndDiagnosticsOperationNames.SET_USAGE_AND_DIAGNOSTICS,
                ) { repository.setUsageAndDiagnostics(enabled = event.enabled) }

            is UsageAndDiagnosticsEvent.SetAnalyticsConsent ->
                setAnalyticsConsentJob = persistChoice(
                    job = setAnalyticsConsentJob,
                    action = UsageAndDiagnosticsOperationNames.SET_ANALYTICS_CONSENT,
                ) { repository.setAnalyticsConsent(granted = event.granted) }

            is UsageAndDiagnosticsEvent.SetAdStorageConsent ->
                setAdStorageConsentJob = persistChoice(
                    job = setAdStorageConsentJob,
                    action = UsageAndDiagnosticsOperationNames.SET_AD_STORAGE_CONSENT,
                ) { repository.setAdStorageConsent(granted = event.granted) }

            is UsageAndDiagnosticsEvent.SetAdUserDataConsent ->
                setAdUserDataConsentJob = persistChoice(
                    job = setAdUserDataConsentJob,
                    action = UsageAndDiagnosticsOperationNames.SET_AD_USER_DATA_CONSENT,
                ) { repository.setAdUserDataConsent(granted = event.granted) }

            is UsageAndDiagnosticsEvent.SetAdPersonalizationConsent ->
                setAdPersonalizationConsentJob = persistChoice(
                    job = setAdPersonalizationConsentJob,
                    action = UsageAndDiagnosticsOperationNames.SET_AD_PERSONALIZATION_CONSENT,
                ) { repository.setAdPersonalizationConsent(granted = event.granted) }

            is UsageAndDiagnosticsEvent.AllowAllConsent -> applyConsentBundle(
                adUserData = true,
                adPersonalization = true,
            )

            is UsageAndDiagnosticsEvent.AllowEssentialConsent -> applyConsentBundle(
                adUserData = false,
                adPersonalization = false,
            )
        }
    }

    /** Follows the stored choices. A retry restarts the collection that failed. */
    private fun observeConsents() {
        observeConsentsJob = observeConsentsJob.restart {
            setState { copy(settings = Loadable.Loading) }
            repository.observeSettings().collectReport(
                action = UsageAndDiagnosticsOperationNames.OBSERVE_CONSENTS,
                onError = { error ->
                    setState {
                        copy(
                            settings = error.toFailed(
                                fallback = UiText.StringResource(resourceId = R.string.error_an_error_occurred),
                            ),
                        )
                    }
                },
            ) { settings ->
                setState { copy(settings = Loadable.Ready(value = settings)) }
            }
        }
    }

    /**
     * Stores one of the dialog's whole-bundle answers in one write, after cancelling single-choice
     * writes still in flight so they cannot overwrite it. Both answers grant reporting, analytics
     * and ad storage, and differ only in targeting consents ([adUserData], [adPersonalization]).
     */
    private fun applyConsentBundle(
        adUserData: Boolean,
        adPersonalization: Boolean,
    ) {
        val settings = UsageAndDiagnosticsSettings(
            usageAndDiagnostics = true,
            analyticsConsent = true,
            adStorageConsent = true,
            adUserDataConsent = adUserData,
            adPersonalizationConsent = adPersonalization,
        )
        listOf(
            setUsageAndDiagnosticsJob,
            setAnalyticsConsentJob,
            setAdStorageConsentJob,
            setAdUserDataConsentJob,
            setAdPersonalizationConsentJob,
        ).forEach { job -> job?.cancel() }
        setConsentBundleJob = setConsentBundleJob.restart {
            launchReport(
                action = UsageAndDiagnosticsOperationNames.SET_CONSENT_BUNDLE,
                onError = { error ->
                    showMessage(
                        message = error.toErrorMessage(
                            fallback = UiText.StringResource(resourceId = R.string.error_an_error_occurred),
                        ),
                    )
                },
            ) {
                repository.setAll(settings = settings)
            }
        }
    }

    /**
     * Restarts [job] with a reported [write] of one choice. Each choice keeps its own job, so
     * changing one never cancels another's write.
     */
    private fun persistChoice(
        job: Job?,
        action: String,
        write: suspend () -> Unit,
    ): Job = job.restart {
        launchReport(
            action = action,
            onError = { error ->
                showMessage(
                    message = error.toErrorMessage(
                        fallback = UiText.StringResource(resourceId = R.string.error_an_error_occurred),
                    ),
                )
            },
            block = write,
        )
    }
}
