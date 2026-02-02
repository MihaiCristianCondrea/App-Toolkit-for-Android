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
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.copyData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

/**
 * ViewModel for the onboarding flow, including completion and consent requests.
 */
class OnboardingViewModel(
    private val observeOnboardingCompletionUseCase: ObserveOnboardingCompletionUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val requestConsentUseCase: RequestConsentUseCase,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<OnboardingUiState, OnboardingEvent, OnboardingAction>(
    initialState = UiStateScreen(data = OnboardingUiState()),
    firebaseController = firebaseController,
    screenName = "Onboarding",
) {

    private var observerJob: Job? = null
    private var completeJob: Job? = null
    private var consentJob: Job? = null

    init {
        handleEvent(OnboardingEvent.ObserveCompletion)
    }

    override fun handleEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.ObserveCompletion -> observeCompletion()
            is OnboardingEvent.UpdateCurrentTab -> updateCurrentTab(event.index)
            is OnboardingEvent.CompleteOnboarding -> completeOnboarding()
            is OnboardingEvent.RequestConsent -> requestConsent(event.host)
            is OnboardingEvent.ShowCrashlyticsDialog -> setCrashlyticsDialogVisibility(isVisible = true)
            is OnboardingEvent.HideCrashlyticsDialog -> setCrashlyticsDialogVisibility(isVisible = false)
        }
    }

    private fun observeCompletion() {
        startOperation(action = Actions.OBSERVE_COMPLETION)
        observerJob = observerJob.restart {
            observeOnboardingCompletionUseCase.invoke()
                .flowOn(dispatchers.io)
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

    private fun requestConsent(host: ConsentHost) {
        val hostName = host.activity::class.java.name
        startOperation(action = Actions.REQUEST_CONSENT, extra = mapOf(ExtraKeys.HOST to hostName))
        consentJob = consentJob.restart {
            requestConsentUseCase.invoke(host = host)
                .flowOn(dispatchers.main)
                .catchReport(
                    action = Actions.REQUEST_CONSENT,
                    extra = mapOf(ExtraKeys.HOST to hostName)
                ) {
                    // No UI change requested for consent failures in onboarding.
                    // If needed later: updateStateThreadSafe { screenState.setError(...) } or showSnackbar(...)
                }
                .launchIn(viewModelScope)
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

    private object ExtraKeys {
        const val HOST: String = "host"
    }
}
