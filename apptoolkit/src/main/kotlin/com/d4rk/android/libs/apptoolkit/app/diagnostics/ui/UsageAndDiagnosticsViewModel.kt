package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.model.UsageAndDiagnosticsSettings
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.repository.UsageAndDiagnosticsRepository
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.contract.UsageAndDiagnosticsAction
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.contract.UsageAndDiagnosticsEvent
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.state.UsageAndDiagnosticsUiState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setErrors
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.successData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.platform.ConsentManagerHelper
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * ViewModel for the Usage and Diagnostics screen.
 *
 * This ViewModel manages the state and logic for user consent settings related to usage data,
 * diagnostics, and advertising. It interacts with the [UsageAndDiagnosticsRepository] to
 * observe and update these settings.
 *
 * It handles events from the UI to update specific consents, such as analytics, ad storage,
 * and personalization, and reflects these changes in the [UsageAndDiagnosticsUiState]. The
 * ViewModel also ensures that the underlying consent framework (via [ConsentManagerHelper])
 * is updated whenever settings change.
 *
 * @param repository The repository responsible for persisting and retrieving usage and diagnostics settings.
 */
class UsageAndDiagnosticsViewModel(
    private val repository: UsageAndDiagnosticsRepository,
) : ScreenViewModel<UsageAndDiagnosticsUiState, UsageAndDiagnosticsEvent, UsageAndDiagnosticsAction>(
    initialState = UiStateScreen(data = UsageAndDiagnosticsUiState()),
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
            is UsageAndDiagnosticsEvent.SetAdPersonalizationConsent -> updateAdPersonalizationConsent(
                event.granted
            )
        }
    }

    private fun observeConsents() {
        repository.observeSettings()
            .onStart { screenState.setLoading() }
            .map<UsageAndDiagnosticsSettings, DataState<UsageAndDiagnosticsSettings, Error>> { settings -> // FIXME: Type argument is not within its bounds: must be subtype of 'com.d4rk.android.libs.apptoolkit.core.domain.model.network.Error'.
                DataState.Success(settings)
            }
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                emit(
                    DataState.Error(
                        error = Error(throwable.message) // FIXME: Argument type mismatch: actual type is 'java.lang.Error', but 'com.d4rk.android.libs.apptoolkit.core.domain.model.network.Error' was expected.
                    )
                )
            }
            .onEach { result ->
                result
                    .onSuccess { settings -> // FIXME: <html>Unresolved reference. None of the following candidates is applicable because of a receiver type mismatch:<br/>fun &lt;D, E : Error&gt; DataState&lt;D, E&gt;.onSuccess(action: (D) -&gt; Unit): DataState&lt;D, E&gt;
                        screenState.successData {
                            UsageAndDiagnosticsUiState(
                                usageAndDiagnostics = settings.usageAndDiagnostics,
                                analyticsConsent = settings.analyticsConsent,
                                adStorageConsent = settings.adStorageConsent,
                                adUserDataConsent = settings.adUserDataConsent,
                                adPersonalizationConsent = settings.adPersonalizationConsent,
                            )
                        }
                        updateConsent(settings)
                    }
                    .onFailure { handleObservationError() } // FIXME: <html>Unresolved reference. None of the following candidates is applicable because of a receiver type mismatch:<br/>fun &lt;D, E : Error&gt; DataState&lt;D, E&gt;.onFailure(action: (E) -&gt; Unit): DataState&lt;D, E&gt;
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

    private fun handleObservationError() {
        screenState.setErrors(
            errors = listOf(
                UiSnackbar(
                    message = UiTextHelper.StringResource(R.string.error_an_error_occurred),
                ),
            ),
        )
        screenState.updateState(ScreenState.Error())
    }
}
