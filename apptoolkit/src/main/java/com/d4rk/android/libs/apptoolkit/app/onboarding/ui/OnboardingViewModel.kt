package com.d4rk.android.libs.apptoolkit.app.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.state.OnboardingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

/**
 * ViewModel for handling onboarding state and actions.
 *
 * Exposes [uiState] as a [StateFlow] following unidirectional data flow
 * and delegates data operations to an [OnboardingRepository].
 */
class OnboardingViewModel(
    private val repository: OnboardingRepository
) : ViewModel() { // FIXME: Use the view model state like the other view models

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        repository
            .observeOnboardingCompletion()
            .onEach { completed ->
                _uiState.update { it.copy(isOnboardingCompleted = completed) }
            }
            .onCompletion { cause ->
                if (cause != null) {
                    _uiState.update { it.copy(isOnboardingCompleted = false) }
                }
            }
            .catch { _ ->
                _uiState.update { it.copy(isOnboardingCompleted = false) }
            }
            .launchIn(viewModelScope)
    }

    fun updateCurrentTab(index: Int) {
        _uiState.update { it.copy(currentTabIndex = index) }
    }

    fun completeOnboarding(onFinished: () -> Unit) {
        flow<Unit> {
            repository.setOnboardingCompleted()
        }
            .onCompletion { cause ->
                if (cause == null) {
                    onFinished()
                } else {
                    _uiState.update { it.copy(isOnboardingCompleted = false) }
                }
            }
            .catch { _ ->
                // Failure is already handled in onCompletion above.
            }
            .launchIn(viewModelScope)
    }

    companion object {
        fun provideFactory(repository: OnboardingRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return OnboardingViewModel(repository) as T
                }
            }
    }
}
