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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.OnboardingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.analytics.ONBOARDING_SCREEN_NAME
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.analytics.OnboardingActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.analytics.OnboardingParams
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.analytics.onboardingActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.analytics.onboardingStepViewEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.analytics.onboardingTutorialBeginEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.analytics.onboardingTutorialCompleteEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.constants.OnboardingOperationNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts.OnboardingCompletionMethod
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts.OnboardingEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts.OnboardingStepDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states.OnboardingCompletion
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states.OnboardingUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import kotlinx.coroutines.Job

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
    screenName = ONBOARDING_SCREEN_NAME,
    viewModelName = "OnboardingViewModel",
) {
    private var consentJob: Job? = null
    private var completeJob: Job? = null
    private var currentPageKey: String? = null

    init {
        telemetryRepository.logEvent(event = onboardingTutorialBeginEvent())
        observeCompletion()
    }

    override fun handleEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.PageSelected -> onPageSelected(event = event)
            is OnboardingEvent.RequestConsent -> requestConsent(host = event.host)
            is OnboardingEvent.CompleteOnboarding -> completeOnboarding(method = event.method)
        }
    }

    private fun onPageSelected(event: OnboardingEvent.PageSelected) {
        currentPageKey = event.pageKey
        setState { copy(currentTabIndex = event.index) }
        telemetryRepository.logEvent(
            event = onboardingStepViewEvent(
                stepIndex = event.index,
                pageKey = event.pageKey,
                direction = event.direction.trackingLabel,
            ),
        )
        if (event.direction == OnboardingStepDirection.BACKWARD) {
            telemetryRepository.logEvent(
                event = onboardingActionEvent(
                    actionName = OnboardingActionNames.STEP_BACK,
                    params = buildMap {
                        put(
                            OnboardingParams.STEP_INDEX,
                            AnalyticsValue.LongVal(value = event.index.toLong()),
                        )
                        event.pageKey?.let { key ->
                            put(OnboardingParams.PAGE_KEY, AnalyticsValue.Str(value = key))
                        }
                    },
                ),
            )
        }
    }

    /** A failed read is reported and the flag reads as not completed. */
    private fun observeCompletion() {
        onboardingRepository.observeOnboardingCompletion()
            .collectReport(
                action = OnboardingOperationNames.OBSERVE_COMPLETION,
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
            launchReport(action = OnboardingOperationNames.REQUEST_CONSENT) {
                consentRepository.requestConsent(host = host)
            }
        }
    }

    /**
     * Saves onboarding completion once. Repeated taps while a save is active or after completion
     * is saved are ignored; a failed save remains retryable.
     */
    private fun completeOnboarding(method: OnboardingCompletionMethod = OnboardingCompletionMethod.DONE) {
        if (state.value.completion == OnboardingCompletion.Saved) return
        val pageIndex = state.value.currentTabIndex
        val pageKey = currentPageKey
        completeJob = completeJob.ifIsActive {
            launchReport(
                action = OnboardingOperationNames.COMPLETE_ONBOARDING,
                onError = { error ->
                    setState { copy(completion = OnboardingCompletion.Failed) }
                    showMessage(
                        message = error.toErrorMessage(
                            fallback = UiText.StringResource(
                                resourceId = R.string.onboarding_completion_failed,
                            ),
                        ),
                    )
                },
            ) {
                setState { copy(completion = OnboardingCompletion.Saving) }
                onboardingRepository.setOnboardingCompleted()
                telemetryRepository.logEvent(
                    event = onboardingTutorialCompleteEvent(
                        method = method.trackingLabel,
                        pageIndex = pageIndex,
                        pageKey = pageKey,
                    ),
                )
                if (method == OnboardingCompletionMethod.SKIP) {
                    telemetryRepository.logEvent(
                        event = onboardingActionEvent(
                            actionName = OnboardingActionNames.SKIP,
                            params = buildMap {
                                put(
                                    OnboardingParams.STEP_INDEX,
                                    AnalyticsValue.LongVal(value = pageIndex.toLong()),
                                )
                                pageKey?.let { key ->
                                    put(OnboardingParams.PAGE_KEY, AnalyticsValue.Str(value = key))
                                }
                            },
                        ),
                    )
                }
                setState { copy(completion = OnboardingCompletion.Saved) }
            }
        }
    }
}
