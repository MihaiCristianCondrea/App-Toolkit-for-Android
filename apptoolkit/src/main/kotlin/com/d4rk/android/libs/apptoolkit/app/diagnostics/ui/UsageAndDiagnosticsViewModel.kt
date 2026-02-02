package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.model.UsageAndDiagnosticsSettings
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.repository.UsageAndDiagnosticsRepository
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.contract.UsageAndDiagnosticsAction
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.contract.UsageAndDiagnosticsEvent
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.state.UsageAndDiagnosticsUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setErrors
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.setSuccess
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.platform.ConsentManagerHelper
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
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
) {

    private var observeConsentsJob: Job? = null

    private var setUsageAndDiagnosticsJob: Job? = null
    private var setAnalyticsConsentJob: Job? = null
    private var setAdStorageConsentJob: Job? = null
    private var setAdUserDataConsentJob: Job? = null
    private var setAdPersonalizationConsentJob: Job? = null

    init {
        onEvent(event = UsageAndDiagnosticsEvent.Initialize)
    }

    override fun handleEvent(event: UsageAndDiagnosticsEvent) {
        when (event) {
            is UsageAndDiagnosticsEvent.Initialize -> observeConsents()
            is UsageAndDiagnosticsEvent.SetUsageAndDiagnostics -> updateUsageAndDiagnostics(enabled = event.enabled)
            is UsageAndDiagnosticsEvent.SetAnalyticsConsent -> updateAnalyticsConsent(granted = event.granted)
            is UsageAndDiagnosticsEvent.SetAdStorageConsent -> updateAdStorageConsent(granted = event.granted)
            is UsageAndDiagnosticsEvent.SetAdUserDataConsent -> updateAdUserDataConsent(granted = event.granted)
            is UsageAndDiagnosticsEvent.SetAdPersonalizationConsent -> updateAdPersonalizationConsent(
                granted = event.granted
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
                        updateConsent(settings)
                    }
                }
                .catchReport(action = Actions.OBSERVE_CONSENTS) {
                    updateStateThreadSafe {
                        handleObservationError(
                            message = Errors.Database.DATABASE_OPERATION_FAILED.asUiText()
                        )
                    }
                }
                .launchIn(viewModelScope) // returns Job :contentReference[oaicite:2]{index=2}
        }
    }

    private fun updateUsageAndDiagnostics(enabled: Boolean) {
        setUsageAndDiagnosticsJob = setUsageAndDiagnosticsJob.restart {
            launchReport(
                action = Actions.SET_USAGE_AND_DIAGNOSTICS,
                extra = mapOf(ExtraKeys.ENABLED to enabled.toString()),
                block = { repository.setUsageAndDiagnostics(enabled) },
                onError = {
                    updateStateThreadSafe {
                        handleObservationError(
                            message = UiTextHelper.StringResource(R.string.error_an_error_occurred)
                        )
                    }
                },
            )
        }
    }

    private fun updateAnalyticsConsent(granted: Boolean) {
        setAnalyticsConsentJob = setAnalyticsConsentJob.restart {
            launchReport(
                action = Actions.SET_ANALYTICS_CONSENT,
                extra = mapOf(ExtraKeys.GRANTED to granted.toString()),
                block = { repository.setAnalyticsConsent(granted) },
                onError = {
                    updateStateThreadSafe {
                        handleObservationError(
                            message = UiTextHelper.StringResource(R.string.error_an_error_occurred)
                        )
                    }
                },
            )
        }
    }

    private fun updateAdStorageConsent(granted: Boolean) {
        setAdStorageConsentJob = setAdStorageConsentJob.restart {
            launchReport(
                action = Actions.SET_AD_STORAGE_CONSENT,
                extra = mapOf(ExtraKeys.GRANTED to granted.toString()),
                block = { repository.setAdStorageConsent(granted) },
                onError = {
                    updateStateThreadSafe {
                        handleObservationError(
                            message = UiTextHelper.StringResource(R.string.error_an_error_occurred)
                        )
                    }
                },
            )
        }
    }

    private fun updateAdUserDataConsent(granted: Boolean) {
        setAdUserDataConsentJob = setAdUserDataConsentJob.restart {
            launchReport(
                action = Actions.SET_AD_USER_DATA_CONSENT,
                extra = mapOf(ExtraKeys.GRANTED to granted.toString()),
                block = { repository.setAdUserDataConsent(granted) },
                onError = {
                    updateStateThreadSafe {
                        handleObservationError(
                            message = UiTextHelper.StringResource(R.string.error_an_error_occurred)
                        )
                    }
                },
            )
        }
    }

    private fun updateAdPersonalizationConsent(granted: Boolean) {
        setAdPersonalizationConsentJob = setAdPersonalizationConsentJob.restart {
            launchReport(
                action = Actions.SET_AD_PERSONALIZATION_CONSENT,
                extra = mapOf(ExtraKeys.GRANTED to granted.toString()),
                block = { repository.setAdPersonalizationConsent(granted) },
                onError = {
                    updateStateThreadSafe {
                        handleObservationError(message = UiTextHelper.StringResource(R.string.error_an_error_occurred))
                    }
                },
            )
        }
    }

    private fun updateConsent(settings: UsageAndDiagnosticsSettings) {
        ConsentManagerHelper.updateConsent(
            analyticsGranted = settings.analyticsConsent,
            adStorageGranted = settings.adStorageConsent,
            adUserDataGranted = settings.adUserDataConsent,
            adPersonalizationGranted = settings.adPersonalizationConsent,
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
    }

    private object ExtraKeys {
        const val ENABLED: String = "enabled"
        const val GRANTED: String = "granted"
    }
}
