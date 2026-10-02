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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logTutorialBegin
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logTutorialComplete
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.data.repositories.OnboardingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts.OnboardingEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states.OnboardingCompletion
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states.OnboardingUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onStart

/**
 * ViewModel for the onboarding screen: the selected page, the consent request, and saving
 * completion.
 *
 * Both repositories are main-safe, so it needs no dispatcher. Finishing is the only way out of
 * onboarding, so a failed save shows an error message instead of leaving a button that seems to
 * do nothing.
 */
class OnboardingViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val consentRepository: ConsentRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<OnboardingUiState, OnboardingEvent>(
    initialState = OnboardingUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "Onboarding",
    viewModelName = "OnboardingViewModel",
) {
    private var consentJob: Job? = null
    private var completeJob: Job? = null

    init {
        telemetryRepository.logTutorialBegin()
        observeCompletion()
    }

    override fun handleEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.PageSelected -> setState { copy(currentTabIndex = event.index) }
            is OnboardingEvent.RequestConsent -> requestConsent(host = event.host)
            OnboardingEvent.CompleteOnboarding -> completeOnboarding()
        }
    }

    /** A failed read is reported and the flag reads as not completed. */
    private fun observeCompletion() {
        onboardingRepository.observeOnboardingCompletion()
            .onStart {
                telemetryRepository.logBreadcrumb(
                    message = "Observe onboarding completion started",
                    attributes = mapOf("source" to "OnboardingRepository"),
                )
            }
            .collectReport(
                action = Actions.OBSERVE_COMPLETION,
                onError = { setState { copy(isOnboardingCompleted = false) } },
            ) { completed ->
                setState { copy(isOnboardingCompleted = completed) }
            }
    }

    /**
     * Each resume restarts a request still waiting from the last one. The outcome does not change
     * the screen; a failure is only reported.
     */
    private fun requestConsent(host: ConsentHost) {
        consentJob = consentJob.restart {
            consentRepository.requestConsent(host = host).collectReport(action = Actions.REQUEST_CONSENT) { }
        }
    }

    /** A second tap restarts the save rather than running two, so only one result shows. */
    private fun completeOnboarding() {
        completeJob = completeJob.restart {
            launchReport(
                action = Actions.COMPLETE_ONBOARDING,
                onError = { error ->
                    setState { copy(completion = OnboardingCompletion.Failed) }
                    showMessage(error.toErrorMessage(fallback = CompletionFailedText))
                },
            ) {
                setState { copy(completion = OnboardingCompletion.Saving) }
                onboardingRepository.setOnboardingCompleted()
                telemetryRepository.logTutorialComplete()
                setState { copy(completion = OnboardingCompletion.Saved) }
            }
        }
    }

    private object Actions {
        const val OBSERVE_COMPLETION: String = "observeCompletion"
        const val COMPLETE_ONBOARDING: String = "completeOnboarding"
        const val REQUEST_CONSENT: String = "requestConsent"
    }

    private companion object {
        /** Shown for a failure with no text of its own; see `toUiText` for the ones that have one. */
        val CompletionFailedText = UiTextHelper.StringResource(R.string.onboarding_completion_failed)
    }
}
