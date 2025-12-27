package com.d4rk.android.libs.apptoolkit.app.onboarding.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.usecases.CompleteOnboardingUseCase
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.usecases.ObserveOnboardingCompletionUseCase
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingAction
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingEvent
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.state.OnboardingUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.copyData
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OnboardingViewModel(
    private val observeOnboardingCompletionUseCase: ObserveOnboardingCompletionUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val dispatchers: DispatcherProvider,
) : ScreenViewModel<OnboardingUiState, OnboardingEvent, OnboardingAction>(
    initialState = UiStateScreen(data = OnboardingUiState())
) {

    private var observeCompletionJob: Job? = null
    private var completeJob: Job? = null

    init {
        observeCompletion()
    }

    override fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.UpdateCurrentTab -> updateCurrentTab(event.index)
            is OnboardingEvent.CompleteOnboarding -> completeOnboarding()
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
            .catch { t ->
                if (t is CancellationException) throw t
                screenState.copyData { copy(isOnboardingCompleted = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun updateCurrentTab(index: Int) {
        screenState.copyData { copy(currentTabIndex = index) }
    }

    private fun completeOnboarding() {
        completeJob?.cancel()
        completeJob = viewModelScope.launch {
            screenState.copyData { copy(isOnboardingCompleted = true) }

            runCatching {
                withContext(dispatchers.io) { completeOnboardingUseCase() }
            }.onSuccess {
                sendAction(OnboardingAction.OnboardingCompleted)
            }.onFailure { t ->
                if (t is CancellationException) throw t
                screenState.copyData { copy(isOnboardingCompleted = false) }
            }
        }
    }

    private fun setCrashlyticsDialogVisibility(isVisible: Boolean) {
        screenState.copyData { copy(isCrashlyticsDialogVisible = isVisible) }
    }
}
