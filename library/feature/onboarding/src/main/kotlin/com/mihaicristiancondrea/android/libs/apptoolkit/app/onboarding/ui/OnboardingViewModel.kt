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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.data.repositories.OnboardingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui.contracts.OnboardingAction
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui.contracts.OnboardingEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui.states.OnboardingUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.copyData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.dismissSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.showSnackbar
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
    private val onboardingRepository: OnboardingRepository,
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
            is OnboardingEvent.DismissSnackbar -> dismissSnackbar()
        }
    }

    private fun observeCompletion() {
        startOperation(action = Actions.OBSERVE_COMPLETION)
        observerJob = observerJob.restart {
            onboardingRepository.observeOnboardingCompletion()
                .flowOn(dispatchers.io)
                .onStart {
                    firebaseController.logBreadcrumb(
                        message = "Observe onboarding completion started",
                        attributes = mapOf("source" to "OnboardingRepository")
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
                        onboardingRepository.setOnboardingCompleted()
                    }

                    updateStateThreadSafe {
                        screenState.copyData { copy(isOnboardingCompleted = true) }
                    }

                    sendAction(OnboardingAction.OnboardingCompleted)
                },
                onError = {
                    // Finishing is the only way out of onboarding, so a failure that says nothing
                    // reads as a dead button: the user taps Finish and stays where they are.
                    updateStateThreadSafe {
                        screenState.copyData { copy(isOnboardingCompleted = false) }
                        screenState.showSnackbar(
                            snackbar = UiSnackbar(
                                message = UiTextHelper.StringResource(
                                    resourceId = R.string.onboarding_completion_failed,
                                ),
                                isError = true,
                                timeStamp = System.currentTimeMillis(),
                            ),
                        )
                    }
                },
            )
        }
    }

    private fun dismissSnackbar() {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.dismissSnackbar()
            }
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

