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

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.data.repositories.UsageAndDiagnosticsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.domain.models.UsageAndDiagnosticsSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.contracts.UsageAndDiagnosticsAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.contracts.UsageAndDiagnosticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.states.UsageAndDiagnosticsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions.asUiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.dismissSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setErrors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.updateState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class UsageAndDiagnosticsViewModel(
    private val repository: UsageAndDiagnosticsRepository,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<UsageAndDiagnosticsUiState, UsageAndDiagnosticsEvent, UsageAndDiagnosticsAction>(
    initialState = UiStateScreen(data = UsageAndDiagnosticsUiState()),
    firebaseController = firebaseController,
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
        onEvent(event = UsageAndDiagnosticsEvent.Initialize)
    }

    override fun handleEvent(event: UsageAndDiagnosticsEvent) {
        when (event) {
            is UsageAndDiagnosticsEvent.Initialize -> observeConsents()
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

            is UsageAndDiagnosticsEvent.AllowAllConsent -> applyConsentBundle(
                analytics = true,
                adStorage = true,
                adUserData = true,
                adPersonalization = true,
            )

            is UsageAndDiagnosticsEvent.AllowEssentialConsent -> applyConsentBundle(
                analytics = true,
                adStorage = true,
                adUserData = false,
                adPersonalization = false,
            )
        }
    }

    private fun observeConsents() {
        startOperation(action = Actions.OBSERVE_CONSENTS)

        observeConsentsJob = observeConsentsJob.restart {
            repository.observeSettings()
                .flowOn(dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        screenState.dismissSnackbar()
                        screenState.setLoading()
                    }
                }
                // Only shows the stored choices; the repository applies them to the consent SDKs
                // when they are written.
                .onEach { settings: UsageAndDiagnosticsSettings ->
                    updateStateThreadSafe {
                        val updated = UsageAndDiagnosticsUiState(
                            usageAndDiagnostics = settings.usageAndDiagnostics,
                            analyticsConsent = settings.analyticsConsent,
                            adStorageConsent = settings.adStorageConsent,
                            adUserDataConsent = settings.adUserDataConsent,
                            adPersonalizationConsent = settings.adPersonalizationConsent,
                        )

                        screenState.setSuccess(data = updated)
                    }
                }
                .catchReport(action = Actions.OBSERVE_CONSENTS) {
                    updateStateThreadSafe {
                        handleObservationError(
                            message = Errors.Database.DATABASE_OPERATION_FAILED.asUiText()
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    /**
     * Applies one of the dialog's whole-bundle answers.
     *
     * Reporting is turned on with any of them: a person choosing what to share has said they are
     * sharing something, and leaving the master switch off would silently drop every choice they
     * just made.
     *
     * The whole bundle is stored in one write, and the repository applies it to the consent SDKs
     * after that write, so they are never handed a mix of the old and new answers, such as ad
     * storage granted while analytics is still denied.
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
        // A bundle replaces every single choice, so single writes still in flight are dropped.
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
                block = { repository.setAll(settings) },
                onError = { updateStateThreadSafe { handleObservationError() } },
            )
        }
    }

    /**
     * Restarts [job] with a reported [write] of one choice, showing the error state if it fails.
     * Each choice keeps its own job, so changing one never cancels another's write.
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
            block = write,
            onError = { updateStateThreadSafe { handleObservationError() } },
        )
    }

    private fun handleObservationError(message: UiTextHelper = UiTextHelper.StringResource(R.string.error_an_error_occurred)) {
        screenState.setErrors(errors = listOf(UiSnackbar(message = message, isError = true)))
        screenState.updateState(ScreenState.Error())
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
}
