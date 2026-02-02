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
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.successData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class StartupViewModel(
    private val requestConsentUseCase: RequestConsentUseCase,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<StartupUiState, StartupEvent, StartupAction>(
    initialState = UiStateScreen(data = StartupUiState()),
    firebaseController = firebaseController,
    screenName = "Startup",
) {

    private var observeJob: Job? = null

    override fun handleEvent(event: StartupEvent) {
        when (event) {
            is StartupEvent.RequestConsent -> requestConsent(host = event.host)
            is StartupEvent.ConsentFormLoaded -> markConsentFormLoaded()
            is StartupEvent.Continue -> sendAction(action = StartupAction.NavigateNext)
        }
    }

    private fun requestConsent(host: ConsentHost) {
        startOperation(
            action = Actions.REQUEST_CONSENT,
            extra = mapOf(ExtraKeys.HOST to host.activity::class.java.name)
        )
        observeJob = observeJob.restart {
            requestConsentUseCase.invoke(host = host)
                .flowOn(dispatchers.main)
                .onStart {
                    updateStateThreadSafe {
                        screenState.setLoading()
                    }
                }
                .catchReport(
                    action = Actions.REQUEST_CONSENT,
                    extra = mapOf(ExtraKeys.HOST to host.activity::class.java.name)
                ) {
                    emit(DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO))
                }
                .onEach { result ->
                    when (result) {
                        is DataState.Success, is DataState.Error -> onEvent(event = StartupEvent.ConsentFormLoaded)
                        is DataState.Loading -> Unit
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun markConsentFormLoaded() {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.successData { copy(consentFormLoaded = true) }
            }
        }
    }

    private object Actions {
        const val REQUEST_CONSENT: String = "requestConsent"
    }

    private object ExtraKeys {
        const val HOST: String = "host"
    }
}
