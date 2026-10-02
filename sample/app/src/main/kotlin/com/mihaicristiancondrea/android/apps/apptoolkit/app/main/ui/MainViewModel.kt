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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.contracts.MainAction
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.contracts.MainEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.states.MainUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.ScreenMessageType
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions.asUiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.onFailure
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.showSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewOutcome
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.usecases.RequestInAppReviewUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.data.repositories.InAppUpdateRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.domain.models.InAppUpdateHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.domain.models.InAppUpdateResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

/**
 * Runs consent, review, and update requests raised on activity resume. Guards live in this
 * ViewModel so configuration changes do not repeat completed requests. Review and consent run
 * once per ViewModel session; an interrupted immediate update remains eligible for a later
 * resume.
 */
class MainViewModel(
    private val consentRepository: ConsentRepository,
    private val requestInAppReviewUseCase: RequestInAppReviewUseCase,
    private val inAppUpdateRepository: InAppUpdateRepository,
    private val dispatchers: DispatcherProvider,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<MainUiState, MainEvent, MainAction>(
    initialState = UiStateScreen(data = MainUiState),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.MAIN.name,
    viewModelName = "MainViewModel",
) {

    private var initialConsentJob: Job? = null
    private var consentJob: Job? = null
    private var reviewJob: Job? = null
    private var updateJob: Job? = null

    private var hasRequestedReview: Boolean = false

    private var hasRequestedConsent: Boolean = false

    private var isUpdateSettledForSession: Boolean = false

    init {
        onEvent(MainEvent.ApplyInitialConsent)
    }

    override fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.ApplyInitialConsent -> applyInitialConsent()
            is MainEvent.RequestConsent -> requestConsent(host = event.host)
            is MainEvent.RequestReview -> requestReview(host = event.host)
            is MainEvent.RequestInAppUpdate -> requestInAppUpdate(host = event.host)
        }
    }

    private fun applyInitialConsent() {
        initialConsentJob = initialConsentJob.restart {
            launchReport(
                action = Actions.APPLY_INITIAL_CONSENT,
                block = {
                    withContext(dispatchers.io) {
                        consentRepository.applyInitialConsent()
                    }
                },
                onError = {
                    breadcrumb(
                        message = "consent_initialization_failed",
                        attributes = mapOf(
                            ExtraKeys.ERROR to (it::class.java.simpleName ?: "Throwable")
                        )
                    )
                }
            )
        }
    }

    private fun requestConsent(host: ConsentHost) {
        if (hasRequestedConsent) {
            breadcrumb(
                message = "consent_request_skipped",
                attributes = mapOf(
                    ExtraKeys.HOST to host.activity::class.java.name,
                    ExtraKeys.REASON to "already_requested_this_session"
                )
            )
            return
        }
        hasRequestedConsent = true

        startOperation(
            action = Actions.REQUEST_CONSENT,
            extra = mapOf(ExtraKeys.HOST to host.activity::class.java.name)
        )
        consentJob = consentJob.restart {
            consentRepository.requestConsent(host = host)
                // Collect UI results in viewModelScope without forcing upstream consent work onto Main.
                .onEach { result: DataState<Unit, Errors> ->
                    when (result) {
                        is DataState.Loading -> {
                            breadcrumb(
                                message = "consent_request_state",
                                attributes = mapOf(
                                    ExtraKeys.HOST to host.activity::class.java.name,
                                    ExtraKeys.STAGE to "loading"
                                )
                            )
                        }

                        is DataState.Success -> {
                            breadcrumb(
                                message = "consent_request_state",
                                attributes = mapOf(
                                    ExtraKeys.HOST to host.activity::class.java.name,
                                    ExtraKeys.STAGE to "success"
                                )
                            )
                        }

                        is DataState.Error -> {
                            breadcrumb(
                                message = "consent_request_state",
                                attributes = mapOf(
                                    ExtraKeys.HOST to host.activity::class.java.name,
                                    ExtraKeys.STAGE to "error",
                                    ExtraKeys.ERROR to result.error.toString()
                                )
                            )
                            result.onFailure { error ->
                                updateStateThreadSafe {
                                    screenState.showSnackbar(
                                        UiSnackbar(
                                            type = ScreenMessageType.SNACKBAR,
                                            message = error.asUiText(),
                                            isError = true,
                                            timeStamp = System.nanoTime(),
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                .catchReport(
                    action = Actions.REQUEST_CONSENT,
                    extra = mapOf(ExtraKeys.HOST to host.activity::class.java.name)
                ) {
                    updateStateThreadSafe {
                        screenState.showSnackbar(
                            UiSnackbar(
                                type = ScreenMessageType.SNACKBAR,
                                message = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO.asUiText(),
                                isError = true,
                                timeStamp = System.nanoTime(),
                            )
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun requestReview(host: ReviewHost) {
        if (hasRequestedReview) return
        hasRequestedReview = true

        startOperation(
            action = Actions.REQUEST_REVIEW,
            extra = mapOf(ExtraKeys.HOST to host.activity::class.java.name)
        )
        reviewJob = reviewJob.restart {
            launchReport(
                action = Actions.REQUEST_REVIEW,
                extra = mapOf(ExtraKeys.HOST to host.activity::class.java.name),
                block = {
                    val outcome = requestInAppReviewUseCase(host = host)
                    breadcrumb(
                        message = "review_outcome",
                        attributes = mapOf(
                            ExtraKeys.HOST to host.activity::class.java.name,
                            ExtraKeys.OUTCOME to outcome::class.java.simpleName,
                        )
                    )
                    sendAction(action = MainAction.ReviewOutcomeReported(outcome = outcome))
                },
                onError = {
                    sendAction(action = MainAction.ReviewOutcomeReported(outcome = ReviewOutcome.Failed))
                }
            )
        }
    }

    private fun requestInAppUpdate(host: InAppUpdateHost) {
        if (isUpdateSettledForSession) {
            breadcrumb(
                message = "update_request_skipped",
                attributes = mapOf(ExtraKeys.REASON to "already_settled_this_session")
            )
            return
        }

        startOperation(action = Actions.REQUEST_UPDATE)
        updateJob = updateJob.restart {
            inAppUpdateRepository.requestUpdate(host = host)
                .flowOn(dispatchers.io)
                .onEach { result ->
                    // flowOn affects upstream work; this result handler still runs on the ViewModel main thread.
                    isUpdateSettledForSession = result !is InAppUpdateResult.Started
                    sendAction(action = MainAction.InAppUpdateResultReported(result = result))
                }
                .catchReport(action = Actions.REQUEST_UPDATE) {
                    isUpdateSettledForSession = true
                    sendAction(action = MainAction.InAppUpdateResultReported(result = InAppUpdateResult.Failed))
                }
                .launchIn(viewModelScope)
        }
    }

    private object Actions {
        const val APPLY_INITIAL_CONSENT: String = "applyInitialConsent"
        const val REQUEST_CONSENT: String = "requestConsent"
        const val REQUEST_REVIEW: String = "requestReview"
        const val REQUEST_UPDATE: String = "requestInAppUpdate"
    }

    private object ExtraKeys {
        const val HOST: String = "host"
        const val OUTCOME: String = "outcome"
        const val STAGE: String = "stage"
        const val ERROR: String = "error"
        const val REASON: String = "reason"
    }
}

