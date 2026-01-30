package com.d4rk.android.libs.apptoolkit.app.onboarding.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.RequestConsentUseCase
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.usecases.CompleteOnboardingUseCase
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.usecases.ObserveOnboardingCompletionUseCase
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingAction
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingEvent
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.state.OnboardingUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.copyData
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class OnboardingViewModel(
    private val observeOnboardingCompletionUseCase: ObserveOnboardingCompletionUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val requestConsentUseCase: RequestConsentUseCase,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
) : ScreenViewModel<OnboardingUiState, OnboardingEvent, OnboardingAction>(
    initialState = UiStateScreen(data = OnboardingUiState())
) {

    private var observeCompletionJob: Job? = null
    private var completeJob: Job? = null
    private var consentJob: Job? = null

    init {
        observeCompletion()
    }

    override fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.UpdateCurrentTab -> updateCurrentTab(event.index)
            is OnboardingEvent.CompleteOnboarding -> completeOnboarding()
            is OnboardingEvent.RequestConsent -> requestConsent(event.host)
            is OnboardingEvent.ShowCrashlyticsDialog -> setCrashlyticsDialogVisibility(true)
            is OnboardingEvent.HideCrashlyticsDialog -> setCrashlyticsDialogVisibility(false)
        }
    }

    private fun observeCompletion() {
        observeCompletionJob?.cancel()
        observeCompletionJob = observeOnboardingCompletionUseCase()
            .flowOn(dispatchers.io)
            .onEach { completed ->
                screenState.copyData { copy(isOnboardingCompleted = completed) }
            }
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                firebaseController.reportViewModelError(
                    viewModelName = "OnboardingViewModel",
                    action = "observeCompletion",
                    throwable = throwable,
                )
                screenState.copyData { copy(isOnboardingCompleted = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun updateCurrentTab(index: Int) {
        screenState.copyData { copy(currentTabIndex = index) }
    }

    private fun completeOnboarding() {
        completeJob?.cancel()
        completeJob = flow<DataState<Unit, Errors.UseCase>> {
            emit(DataState.Loading())
            withContext(dispatchers.io) { completeOnboardingUseCase() }
            emit(DataState.Success(Unit))
        }
            .flowOn(dispatchers.io)
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                firebaseController.reportViewModelError(
                    viewModelName = "OnboardingViewModel",
                    action = "completeOnboarding",
                    throwable = throwable,
                )
                emit(DataState.Error(error = Errors.UseCase.INVALID_STATE))
            }
            .onEach { result ->
                result
                    .onSuccess { sendAction(OnboardingAction.OnboardingCompleted) }
                    .onFailure {
                        screenState.copyData { copy(isOnboardingCompleted = false) }
                    }

                if (result is DataState.Loading) {
                    screenState.copyData { copy(isOnboardingCompleted = true) }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun requestConsent(host: ConsentHost) {
        consentJob?.cancel()
        consentJob = requestConsentUseCase(host = host)
            .flowOn(dispatchers.main)
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                firebaseController.reportViewModelError(
                    viewModelName = "OnboardingViewModel",
                    action = "requestConsent",
                    throwable = throwable,
                )
                emit(DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO))
            }
            .launchIn(viewModelScope)
    }

    private fun setCrashlyticsDialogVisibility(isVisible: Boolean) {
        screenState.copyData { copy(isCrashlyticsDialogVisible = isVisible) }
    }
}
