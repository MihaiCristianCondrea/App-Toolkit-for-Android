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

package com.d4rk.android.libs.apptoolkit.app.onboarding.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.usecases.CompleteOnboardingUseCase
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.usecases.ObserveOnboardingCompletionUseCase
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingAction
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingEvent
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.state.OnboardingUiState
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.copyData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for the onboarding flow, including completion and consent requests.
 */
class OnboardingViewModel(
    private val observeOnboardingCompletionUseCase: ObserveOnboardingCompletionUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<OnboardingUiState, OnboardingEvent, OnboardingAction>(
    initialState = UiStateScreen(data = OnboardingUiState()),
    firebaseController = firebaseController,
    screenName = "Onboarding",
) {

    private var observerJob: Job? = null
    private var completeJob: Job? = null

    init {
        handleEvent(OnboardingEvent.ObserveCompletion)
    }

    override fun handleEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.ObserveCompletion -> observeCompletion()
            is OnboardingEvent.UpdateCurrentTab -> updateCurrentTab(event.index)
            is OnboardingEvent.CompleteOnboarding -> completeOnboarding()
            is OnboardingEvent.RequestConsent -> requestConsent()
            is OnboardingEvent.ShowCrashlyticsDialog -> setCrashlyticsDialogVisibility(isVisible = true)
            is OnboardingEvent.HideCrashlyticsDialog -> setCrashlyticsDialogVisibility(isVisible = false)
        }
    }

    private fun observeCompletion() {
        startOperation(action = Actions.OBSERVE_COMPLETION)
        observerJob = observerJob.restart {
            observeOnboardingCompletionUseCase.invoke()
                .flowOn(dispatchers.io)
                .onStart {
                    firebaseController.logBreadcrumb(
                        message = "Observe onboarding completion started",
                        attributes = mapOf("source" to "ObserveOnboardingCompletionUseCase")
                    )
                }
                .onEach { completed ->
                    updateStateThreadSafe {
                        screenState.copyData { copy(isOnboardingCompleted = completed) }
                    }
                }
                .catchReport(action = Actions.OBSERVE_COMPLETION) {
                    updateStateThreadSafe {
                        screenState.copyData { copy(isOnboardingCompleted = false) }
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun updateCurrentTab(index: Int) {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.copyData { copy(currentTabIndex = index) }
            }
        }
    }

    private fun completeOnboarding() {
        completeJob = completeJob.restart {
            launchReport(
                action = Actions.COMPLETE_ONBOARDING,
                block = {
                    withContext(dispatchers.io) {
                        completeOnboardingUseCase()
                    }

                    updateStateThreadSafe {
                        screenState.copyData { copy(isOnboardingCompleted = true) }
                    }

                    sendAction(OnboardingAction.OnboardingCompleted)
                },
                onError = {
                    updateStateThreadSafe {
                        screenState.copyData { copy(isOnboardingCompleted = false) }
                    }
                },
            )
        }
    }

    private fun requestConsent() {
        startOperation(action = Actions.REQUEST_CONSENT)
        viewModelScope.launch {
            sendAction(OnboardingAction.RequestConsentUi)
        }
    }

    private fun setCrashlyticsDialogVisibility(isVisible: Boolean) {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.copyData { copy(isCrashlyticsDialogVisible = isVisible) }
            }
        }
    }

    private object Actions {
        const val OBSERVE_COMPLETION: String = "observeCompletion"
        const val COMPLETE_ONBOARDING: String = "completeOnboarding"
        const val REQUEST_CONSENT: String = "requestConsent"
    }

}
