package com.d4rk.android.libs.apptoolkit.app.onboarding.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingAction
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingEvent
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.state.OnboardingUiState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.copyData
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * ViewModel for handling onboarding state and actions.
 *
 * Follows the common ScreenViewModel pattern used in the toolkit by exposing
 * [UiStateScreen] data and responding to [OnboardingEvent]s.
 */
class OnboardingViewModel(
    private val repository: OnboardingRepository
) : ScreenViewModel<OnboardingUiState, OnboardingEvent, OnboardingAction>(
    initialState = UiStateScreen(data = OnboardingUiState())
) {

    init {
        repository
            .observeOnboardingCompletion()
            .onEach { completed ->
                screenState.copyData { copy(isOnboardingCompleted = completed) }
            }
            .onCompletion { cause ->
                if (cause != null) {
                    screenState.copyData { copy(isOnboardingCompleted = false) }
                }
            }
            .catch { _ ->
                screenState.copyData { copy(isOnboardingCompleted = false) }
            }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.UpdateCurrentTab -> updateCurrentTab(event.index)
            OnboardingEvent.CompleteOnboarding -> completeOnboarding()
        }
    }

    private fun updateCurrentTab(index: Int) {
        screenState.copyData { copy(currentTabIndex = index) }
    }

    private fun completeOnboarding() {
        flow {
            repository.setOnboardingCompleted()
            emit(Unit)
        }
            .onStart { screenState.copyData { copy(isOnboardingCompleted = false) } }
            .onEach { screenState.copyData { copy(isOnboardingCompleted = true) } }
            .onCompletion { cause ->
                if (cause == null) {
                    sendAction(OnboardingAction.OnboardingCompleted)
                } else {
                    screenState.copyData { copy(isOnboardingCompleted = false) }
                }
            }
            .catch { _ ->
                screenState.copyData { copy(isOnboardingCompleted = false) }
            }
            .launchIn(viewModelScope)
    }

    companion object {
        fun provideFactory(repository: OnboardingRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return OnboardingViewModel(repository) as T
                }
            }
    }
}
