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

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.domain.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.repositories.FaqRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.contracts.FaqAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.contracts.FaqEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.states.FaqUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewOutcome
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.usecases.ForceInAppReviewUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.links.AppLinks
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions.asUiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.onFailure
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.onSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.dismissSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setNoData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.R
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FaqViewModel(
    private val faqRepository: FaqRepository,
    private val forceInAppReviewUseCase: ForceInAppReviewUseCase,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<FaqUiState, FaqEvent, FaqAction>(
    initialState = UiStateScreen(data = FaqUiState()),
    firebaseController = firebaseController,
    screenName = "Help",
) {
    private var observeJob: Job? = null
    private var reviewJob: Job? = null

    init {
        onEvent(event = FaqEvent.LoadFaq)
    }

    override fun handleEvent(event: FaqEvent) {
        when (event) {
            is FaqEvent.LoadFaq -> loadFaq()
            is FaqEvent.DismissSnackbar -> dismissSnackbar()
            is FaqEvent.OpenFeatureRequestForm -> sendAction(FaqAction.OpenUrl(AppLinks.FEATURE_REQUESTS_FORM))
            is FaqEvent.RequestReview -> requestReview(host = event.host)
        }
    }

    private fun loadFaq() {
        startOperation(action = "loadFaq")
        observeJob = observeJob.restart {
            faqRepository.fetchFaq()
                .flowOn(context = dispatchers.io)
                .onStart {
                    firebaseController.logBreadcrumb(
                        message = "FAQ fetch started",
                        attributes = mapOf("source" to "FaqRepository")
                    )
                    updateStateThreadSafe {
                        screenState.setLoading()
                    }
                }
                .onEach { result: DataState<List<FaqItem>, Errors> ->
                    result
                        .onSuccess { faqs ->
                            updateStateThreadSafe {
                                val data = FaqUiState(questions = faqs.toImmutableList())
                                if (faqs.isEmpty()) {
                                    screenState.setNoData(data = data)
                                } else {
                                    screenState.setSuccess(data = data)
                                }
                            }
                        }
                        .onFailure { error ->
                            updateStateThreadSafe {
                                screenState.setError(message = error.asUiText())
                            }
                        }
                }
                .catchReport(action = "loadFaq") {
                    updateStateThreadSafe {
                        screenState.setError(
                            message = UiTextHelper.StringResource(R.string.error_failed_to_load_faq)
                        )
                    }
                }
                .launchIn(scope = viewModelScope)
        }
    }

    private fun dismissSnackbar() {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.dismissSnackbar()
            }
        }
    }

    private fun requestReview(host: ReviewHost) {
        startOperation(action = Actions.REQUEST_REVIEW)
        reviewJob = reviewJob.restart {
            launchReport(
                action = Actions.REQUEST_REVIEW,
                block = {
                    val outcome = withContext(dispatchers.io) {
                        forceInAppReviewUseCase(host = host)
                    }
                    sendAction(action = FaqAction.ReviewOutcomeReported(outcome = outcome))
                    if (outcome != ReviewOutcome.Launched) {
                        sendAction(action = FaqAction.OpenPlayStoreReview)
                    }
                },
                onError = {
                    sendAction(action = FaqAction.OpenPlayStoreReview)
                }
            )
        }
    }

    private object Actions {
        const val REQUEST_REVIEW = "requestReview"
    }
}
