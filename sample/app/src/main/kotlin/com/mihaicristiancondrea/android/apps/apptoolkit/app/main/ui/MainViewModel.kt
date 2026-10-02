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

import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.contracts.MainEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.states.MainUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.models.AppScreenTracking
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.apps.apptoolkit.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.usecases.RequestInAppReviewUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.data.repositories.InAppUpdateRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.domain.models.InAppUpdateHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.domain.models.InAppUpdateResult
import kotlinx.coroutines.Job

/**
 * Runs consent, review, and update requests raised on activity resume. Guards live in this
 * ViewModel so configuration changes do not repeat completed requests. Review and consent run
 * once per ViewModel session; an interrupted immediate update remains eligible for a later
 * resume. The repositories are main-safe, and the update flow talks to Play on the main thread.
 */
class MainViewModel(
    private val consentRepository: ConsentRepository,
    private val requestInAppReviewUseCase: RequestInAppReviewUseCase,
    private val inAppUpdateRepository: InAppUpdateRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<MainUiState, MainEvent>(
    initialState = MainUiState,
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
            MainEvent.ApplyInitialConsent -> applyInitialConsent()
            is MainEvent.RequestConsent -> requestConsent(host = event.host)
            is MainEvent.RequestReview -> requestReview(host = event.host)
            is MainEvent.RequestInAppUpdate -> requestInAppUpdate(host = event.host)
        }
    }

    private fun applyInitialConsent() {
        initialConsentJob = initialConsentJob.restart {
            launchReport(
                action = Actions.APPLY_INITIAL_CONSENT,
                onError = { error ->
                    breadcrumb(
                        message = "consent_initialization_failed",
                        attributes = mapOf(ExtraKeys.ERROR to error::class.java.simpleName),
                    )
                },
            ) {
                consentRepository.applyInitialConsent()
            }
        }
    }

    /** Asks once per session; a resume while the round trip runs, or after it ended, is skipped. */
    private fun requestConsent(host: ConsentHost) {
        if (hasRequestedConsent) {
            breadcrumb(
                message = "consent_request_skipped",
                attributes = mapOf(
                    ExtraKeys.HOST to host.activity::class.java.name,
                    ExtraKeys.REASON to "already_requested_this_session",
                ),
            )
            return
        }
        hasRequestedConsent = true

        val hostAttributes = mapOf(ExtraKeys.HOST to host.activity::class.java.name)
        consentJob = consentJob.restart {
            launchReport(
                action = Actions.REQUEST_CONSENT,
                extra = hostAttributes,
                onError = { error ->
                    breadcrumb(
                        message = "consent_request_state",
                        attributes = hostAttributes + mapOf(
                            ExtraKeys.STAGE to "error",
                            ExtraKeys.ERROR to error::class.java.simpleName,
                        ),
                    )
                    showMessage(error.toErrorMessage(fallback = ConsentFailedText))
                },
            ) {
                breadcrumb(
                    message = "consent_request_state",
                    attributes = hostAttributes + (ExtraKeys.STAGE to "loading"),
                )
                consentRepository.requestConsent(host = host)
                breadcrumb(
                    message = "consent_request_state",
                    attributes = hostAttributes + (ExtraKeys.STAGE to "success"),
                )
            }
        }
    }

    /** The use case records a session per call, so a resume must not ask again. */
    private fun requestReview(host: ReviewHost) {
        if (hasRequestedReview) return
        hasRequestedReview = true

        val hostAttributes = mapOf(ExtraKeys.HOST to host.activity::class.java.name)
        reviewJob = reviewJob.restart {
            launchReport(action = Actions.REQUEST_REVIEW, extra = hostAttributes) {
                val outcome = requestInAppReviewUseCase(host = host)
                breadcrumb(
                    message = "review_outcome",
                    attributes = hostAttributes + (ExtraKeys.OUTCOME to outcome::class.java.simpleName),
                )
            }
        }
    }

    /**
     * Checks for an update on each resume until Play gives a settled answer. A started immediate
     * update stays unsettled, so the next resume can resume it as Play expects.
     */
    private fun requestInAppUpdate(host: InAppUpdateHost) {
        if (isUpdateSettledForSession) {
            breadcrumb(
                message = "update_request_skipped",
                attributes = mapOf(ExtraKeys.REASON to "already_settled_this_session"),
            )
            return
        }

        updateJob = updateJob.restart {
            inAppUpdateRepository.requestUpdate(host = host).collectReport(
                action = Actions.REQUEST_UPDATE,
                onError = { isUpdateSettledForSession = true },
            ) { result ->
                isUpdateSettledForSession = result !is InAppUpdateResult.Started
            }
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

    private companion object {
        val ConsentFailedText = UiTextHelper.StringResource(R.string.error_failed_to_load_consent_info)
    }
}
