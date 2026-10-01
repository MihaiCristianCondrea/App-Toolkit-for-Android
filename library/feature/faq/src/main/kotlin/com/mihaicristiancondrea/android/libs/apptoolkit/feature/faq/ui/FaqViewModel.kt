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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
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
 * ViewModel for the help page: the questions, and the review request from the feedback sheet.
 *
 * Both the repository and the review use case are main-safe, so it needs no dispatcher.
 */
class FaqViewModel(
    private val faqRepository: FaqRepository,
    private val forceInAppReviewUseCase: ForceInAppReviewUseCase,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<FaqUiState, FaqEvent>(
    initialState = FaqUiState(),
    firebaseController = firebaseController,
    screenName = "Help",
    viewModelName = "FaqViewModel",
) {
    private var loadJob: Job? = null
    private var reviewJob: Job? = null

    init {
        onEvent(FaqEvent.Load)
    }

    override fun handleEvent(event: FaqEvent) {
        when (event) {
            FaqEvent.Load -> loadFaq()
            is FaqEvent.RequestReview -> requestReview(host = event.host)
            FaqEvent.StoreListingOpened -> setState { copy(openStoreListing = false) }
        }
    }

    private fun loadFaq() {
        loadJob = loadJob.restart {
            launchReport(
                action = Actions.LOAD_FAQ,
                onError = { error -> setState { copy(questions = error.toFailed(fallback = LoadFailedText)) } },
            ) {
                firebaseController.logBreadcrumb(
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
                            Loadable.Ready(questions.toImmutableList())
                        },
                    )
                }
            }
        }
    }

    // Any outcome but a shown review, a failure included, sends the user to the store listing, so
    // the request they made always leads somewhere.
    private fun requestReview(host: ReviewHost) {
        reviewJob = reviewJob.restart {
            launchReport(
                action = Actions.REQUEST_REVIEW,
                onError = { setState { copy(openStoreListing = true) } },
            ) {
                val outcome: ReviewOutcome = forceInAppReviewUseCase(host = host)
                if (outcome != ReviewOutcome.Launched) {
                    setState { copy(openStoreListing = true) }
                }
            }
        }
    }

    private object Actions {
        const val LOAD_FAQ: String = "loadFaq"
        const val REQUEST_REVIEW: String = "requestReview"
    }

    private companion object {
        val LoadFailedText = UiTextHelper.StringResource(R.string.error_failed_to_load_faq)
    }
}
