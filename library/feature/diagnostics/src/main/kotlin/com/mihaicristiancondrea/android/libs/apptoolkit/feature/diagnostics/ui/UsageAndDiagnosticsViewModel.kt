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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.data.repositories.UsageAndDiagnosticsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.domain.models.UsageAndDiagnosticsSettings
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
    screenName = "UsageAndDiagnostics",
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
            UsageAndDiagnosticsEvent.Load -> observeConsents()

            is UsageAndDiagnosticsEvent.SetUsageAndDiagnostics ->
                setUsageAndDiagnosticsJob = persistChoice(
                    job = setUsageAndDiagnosticsJob,
                    action = Actions.SET_USAGE_AND_DIAGNOSTICS,
                    extra = mapOf(ExtraKeys.ENABLED to event.enabled.toString()),
                ) { repository.setUsageAndDiagnostics(event.enabled) }

            is UsageAndDiagnosticsEvent.SetAnalyticsConsent ->
                setAnalyticsConsentJob = persistChoice(
                    job = setAnalyticsConsentJob,
                    action = Actions.SET_ANALYTICS_CONSENT,
                    extra = mapOf(ExtraKeys.GRANTED to event.granted.toString()),
                ) { repository.setAnalyticsConsent(event.granted) }

            is UsageAndDiagnosticsEvent.SetAdStorageConsent ->
                setAdStorageConsentJob = persistChoice(
                    job = setAdStorageConsentJob,
                    action = Actions.SET_AD_STORAGE_CONSENT,
                    extra = mapOf(ExtraKeys.GRANTED to event.granted.toString()),
                ) { repository.setAdStorageConsent(event.granted) }

            is UsageAndDiagnosticsEvent.SetAdUserDataConsent ->
                setAdUserDataConsentJob = persistChoice(
                    job = setAdUserDataConsentJob,
                    action = Actions.SET_AD_USER_DATA_CONSENT,
                    extra = mapOf(ExtraKeys.GRANTED to event.granted.toString()),
                ) { repository.setAdUserDataConsent(event.granted) }

            is UsageAndDiagnosticsEvent.SetAdPersonalizationConsent ->
                setAdPersonalizationConsentJob = persistChoice(
                    job = setAdPersonalizationConsentJob,
                    action = Actions.SET_AD_PERSONALIZATION_CONSENT,
                    extra = mapOf(ExtraKeys.GRANTED to event.granted.toString()),
                ) { repository.setAdPersonalizationConsent(event.granted) }

            UsageAndDiagnosticsEvent.AllowAllConsent -> applyConsentBundle(
                analytics = true,
                adStorage = true,
                adUserData = true,
                adPersonalization = true,
            )

            UsageAndDiagnosticsEvent.AllowEssentialConsent -> applyConsentBundle(
                analytics = true,
                adStorage = true,
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
                action = Actions.OBSERVE_CONSENTS,
                onError = { error -> setState { copy(settings = error.toFailed(fallback = ErrorText)) } },
            ) { settings ->
                setState { copy(settings = Loadable.Ready(settings)) }
            }
        }
    }

    /**
     * Stores one of the dialog's whole-bundle answers in one write, after cancelling single-choice
     * writes still in flight so they cannot overwrite it. Reporting is turned on with either
     * answer, since leaving it off would drop every choice just made.
     */
    private fun applyConsentBundle(
        analytics: Boolean,
        adStorage: Boolean,
        adUserData: Boolean,
        adPersonalization: Boolean,
    ) {
        val settings = UsageAndDiagnosticsSettings(
            usageAndDiagnostics = true,
            analyticsConsent = analytics,
            adStorageConsent = adStorage,
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
                action = Actions.SET_CONSENT_BUNDLE,
                extra = mapOf(
                    ExtraKeys.ANALYTICS to analytics.toString(),
                    ExtraKeys.AD_PERSONALIZATION to adPersonalization.toString(),
                ),
                onError = { error -> showMessage(error.toErrorMessage(fallback = ErrorText)) },
            ) {
                repository.setAll(settings)
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
        extra: Map<String, String>,
        write: suspend () -> Unit,
    ): Job = job.restart {
        launchReport(
            action = action,
            extra = extra,
            onError = { error -> showMessage(error.toErrorMessage(fallback = ErrorText)) },
            block = write,
        )
    }

    private object Actions {
        const val OBSERVE_CONSENTS: String = "observeConsents"
        const val SET_USAGE_AND_DIAGNOSTICS: String = "setUsageAndDiagnostics"
        const val SET_ANALYTICS_CONSENT: String = "setAnalyticsConsent"
        const val SET_AD_STORAGE_CONSENT: String = "setAdStorageConsent"
        const val SET_AD_USER_DATA_CONSENT: String = "setAdUserDataConsent"
        const val SET_AD_PERSONALIZATION_CONSENT: String = "setAdPersonalizationConsent"
        const val SET_CONSENT_BUNDLE: String = "setConsentBundle"
    }

    private object ExtraKeys {
        const val ENABLED: String = "enabled"
        const val GRANTED: String = "granted"
        const val ANALYTICS: String = "analytics"
        const val AD_PERSONALIZATION: String = "adPersonalization"
    }

    private companion object {
        val ErrorText = UiTextHelper.StringResource(R.string.error_an_error_occurred)
    }
}
