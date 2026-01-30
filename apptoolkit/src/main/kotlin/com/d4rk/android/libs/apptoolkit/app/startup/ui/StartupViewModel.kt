package com.d4rk.android.libs.apptoolkit.app.startup.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.RequestConsentUseCase
import com.d4rk.android.libs.apptoolkit.app.startup.ui.contract.StartupAction
import com.d4rk.android.libs.apptoolkit.app.startup.ui.contract.StartupEvent
import com.d4rk.android.libs.apptoolkit.app.startup.ui.state.StartupUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class StartupViewModel(
    private val requestConsentUseCase: RequestConsentUseCase,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
) : ScreenViewModel<StartupUiState, StartupEvent, StartupAction>(
    initialState = UiStateScreen(data = StartupUiState())
) {

    private var consentJob: Job? = null

    override fun onEvent(event: StartupEvent) {
        when (event) {
            is StartupEvent.RequestConsent -> requestConsent(event.host)
            StartupEvent.ConsentFormLoaded -> screenState.updateData(
                newState = ScreenState.Success()
            ) { current -> current.copy(consentFormLoaded = true) }

            StartupEvent.Continue -> sendAction(StartupAction.NavigateNext)
        }
    }

    private fun requestConsent(host: ConsentHost) {
        consentJob?.cancel()
        consentJob = viewModelScope.launch {
            val result = requestConsentUseCase(host = host)
                .flowOn(dispatchers.main)
                .catch { throwable ->
                    if (throwable is CancellationException) throw throwable
                    firebaseController.reportViewModelError(
                        viewModelName = "StartupViewModel",
                        action = "requestConsent",
                        throwable = throwable,
                    )
                    emit(DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO))
                }
                .first { state -> state !is DataState.Loading }

            when (result) {
                is DataState.Success,
                is DataState.Error -> onEvent(StartupEvent.ConsentFormLoaded)

                is DataState.Loading -> Unit
            }
        }
    }
}
