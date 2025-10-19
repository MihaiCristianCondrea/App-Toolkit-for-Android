package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.actions.UsageAndDiagnosticsAction
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.actions.UsageAndDiagnosticsEvent
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.model.UsageAndDiagnosticsSettings
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.model.ui.UiUsageAndDiagnosticsScreen
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.repository.UsageAndDiagnosticsRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.setErrors
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.setLoading
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.successData
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateState
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ConsentManagerHelper
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class UsageAndDiagnosticsViewModel(
    private val repository: UsageAndDiagnosticsRepository,
) : ScreenViewModel<UiUsageAndDiagnosticsScreen, UsageAndDiagnosticsEvent, UsageAndDiagnosticsAction>(
    initialState = UiStateScreen(data = UiUsageAndDiagnosticsScreen()),
) {

    init {
        observeConsents()
    }

    override fun onEvent(event: UsageAndDiagnosticsEvent) {
        when (event) {
            is UsageAndDiagnosticsEvent.SetUsageAndDiagnostics -> updateUsageAndDiagnostics(event.enabled)
            is UsageAndDiagnosticsEvent.SetAnalyticsConsent -> updateAnalyticsConsent(event.granted)
            is UsageAndDiagnosticsEvent.SetAdStorageConsent -> updateAdStorageConsent(event.granted)
            is UsageAndDiagnosticsEvent.SetAdUserDataConsent -> updateAdUserDataConsent(event.granted)
            is UsageAndDiagnosticsEvent.SetAdPersonalizationConsent -> updateAdPersonalizationConsent(event.granted)
        }
    }

    private fun observeConsents() {
        repository.observeSettings()
            .onStart { screenState.setLoading() }
            .onEach { settings ->
                screenState.successData {
                    UiUsageAndDiagnosticsScreen(
                        usageAndDiagnostics = settings.usageAndDiagnostics,
                        analyticsConsent = settings.analyticsConsent,
                        adStorageConsent = settings.adStorageConsent,
                        adUserDataConsent = settings.adUserDataConsent,
                        adPersonalizationConsent = settings.adPersonalizationConsent,
                    )
                }
                updateConsent(settings)
            }
            .onCompletion { cause ->
                if (cause != null && cause !is CancellationException) {
                    handleObservationError(cause)
                }
            }
            .catch { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }
            }
            .launchIn(viewModelScope)
    }

    private fun updateUsageAndDiagnostics(enabled: Boolean) {
        viewModelScope.launch { repository.setUsageAndDiagnostics(enabled) }
    }

    private fun updateAnalyticsConsent(granted: Boolean) {
        viewModelScope.launch { repository.setAnalyticsConsent(granted) }
    }

    private fun updateAdStorageConsent(granted: Boolean) {
        viewModelScope.launch { repository.setAdStorageConsent(granted) }
    }

    private fun updateAdUserDataConsent(granted: Boolean) {
        viewModelScope.launch { repository.setAdUserDataConsent(granted) }
    }

    private fun updateAdPersonalizationConsent(granted: Boolean) {
        viewModelScope.launch { repository.setAdPersonalizationConsent(granted) }
    }

    private fun updateConsent(settings: UsageAndDiagnosticsSettings) {
        ConsentManagerHelper.updateConsent(
            analyticsGranted = settings.analyticsConsent,
            adStorageGranted = settings.adStorageConsent,
            adUserDataGranted = settings.adUserDataConsent,
            adPersonalizationGranted = settings.adPersonalizationConsent,
        )
    }

    private fun handleObservationError(cause: Throwable) {
        screenState.setErrors(
            errors = listOf(
                UiSnackbar(
                    message = UiTextHelper.DynamicString(
                        cause.message ?: OBSERVATION_ERROR_MESSAGE,
                    ),
                ),
            ),
        )
        screenState.updateState(ScreenState.Error())
    }

    private companion object {
        const val OBSERVATION_ERROR_MESSAGE = "Unable to observe usage and diagnostics settings."
    }
}
