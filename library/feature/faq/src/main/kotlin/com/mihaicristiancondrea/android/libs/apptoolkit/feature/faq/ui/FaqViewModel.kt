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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics.FaqAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.constants.FaqOperationNames
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.repositories.FaqRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.contracts.FaqEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.states.FaqUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewOutcome
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.usecases.ForceInAppReviewUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job

/**
 * Owns the FAQ load state and pending store listing request.
 *
 * Starts the first load on creation and accepts [FaqEvent.Load] for retry.
 */
class FaqViewModel(
    private val faqRepository: FaqRepository,
    private val forceInAppReviewUseCase: ForceInAppReviewUseCase,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<FaqUiState, FaqEvent>(
    initialState = FaqUiState(),
    telemetryRepository = telemetryRepository,
    screenName = FaqAnalytics.SCREEN_NAME,
    viewModelName = "FaqViewModel",
) {
    private var loadJob: Job? = null
    private var reviewJob: Job? = null

    init {
        onEvent(event = FaqEvent.Load)
    }

    override fun handleEvent(event: FaqEvent) {
        when (event) {
            is FaqEvent.Load -> loadFaq()
            is FaqEvent.RequestReview -> requestReview(host = event.host)
            is FaqEvent.StoreListingOpened -> setState { copy(openStoreListing = false) }
        }
    }

    /** Loads questions and publishes a load result. Duplicate requests retain the active load. */
    private fun loadFaq() {
        loadJob = loadJob.ifIsActive {
            launchReport(
                action = FaqOperationNames.LOAD_FAQ,
                onError = { error ->
                    setState {
                        copy(
                            questions = error.toFailed(
                                fallback = UiText.StringResource(resourceId = R.string.error_failed_to_load_faq),
                            ),
                        )
                    }
                },
            ) {
                telemetryRepository.logBreadcrumb(
                    message = "FAQ fetch started",
                    attributes = mapOf("source" to "FaqRepository"),
                )
                setState { copy(questions = Loadable.Loading) }
                val questions = faqRepository.getFaq()
                setState {
                    copy(
                        questions = if (questions.isEmpty()) {
                            Loadable.Empty()
                        } else {
                            Loadable.Ready(value = questions.toImmutableList())
                        },
                    )
                }
            }
        }
    }

    /** Requests an in-app review with a Play Store listing fallback. */
    private fun requestReview(host: ReviewHost) {
        reviewJob = reviewJob.restart {
            launchReport(
                action = FaqOperationNames.REQUEST_REVIEW,
                onError = { setState { copy(openStoreListing = true) } },
            ) {
                val outcome: ReviewOutcome = forceInAppReviewUseCase(host = host)
                if (outcome != ReviewOutcome.Launched) {
                    setState { copy(openStoreListing = true) }
                }
            }
        }
    }
}
