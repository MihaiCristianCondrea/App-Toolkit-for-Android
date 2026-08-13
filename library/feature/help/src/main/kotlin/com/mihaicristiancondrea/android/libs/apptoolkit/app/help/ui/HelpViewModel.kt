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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.help.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.domain.usecases.GetFaqUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.ui.contract.HelpAction
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.ui.contract.HelpEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.ui.state.HelpUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.app.review.domain.model.ReviewHost
import com.mihaicristiancondrea.android.libs.apptoolkit.app.review.domain.model.ReviewOutcome
import com.mihaicristiancondrea.android.libs.apptoolkit.app.review.domain.usecases.ForceInAppReviewUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repository.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.links.AppLinks
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.remote.extensions.asUiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.setError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.setNoData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.setSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.R
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HelpViewModel(
    private val getFaqUseCase: GetFaqUseCase,
    private val forceInAppReviewUseCase: ForceInAppReviewUseCase,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<HelpUiState, HelpEvent, HelpAction>(
    initialState = UiStateScreen(data = HelpUiState()),
    firebaseController = firebaseController,
    screenName = "Help",
) {
    private var observeJob: Job? = null
    private var reviewJob: Job? = null

    init {
        onEvent(event = HelpEvent.LoadFaq)
    }

    override fun handleEvent(event: HelpEvent) {
        when (event) {
            is HelpEvent.LoadFaq -> loadFaq()
            is HelpEvent.DismissSnackbar -> dismissSnackbar()
            is HelpEvent.OpenFeatureRequestForm -> sendAction(HelpAction.OpenUrl(AppLinks.FEATURE_REQUESTS_FORM))
            is HelpEvent.OpenContactPage -> sendAction(HelpAction.OpenUrl(AppLinks.CONTACT_PAGE))
            is HelpEvent.RequestReview -> requestReview(host = event.host)
        }
    }

    private fun loadFaq() {
        startOperation(action = "loadFaq")
        observeJob = observeJob.restart {
            getFaqUseCase.invoke()
                .flowOn(context = dispatchers.io)
                .onStart {
                    firebaseController.logBreadcrumb(
                        message = "FAQ fetch started",
                        attributes = mapOf("source" to "GetFaqUseCase")
                    )
                    updateStateThreadSafe {
                        screenState.setLoading()
                    }
                }
                .onEach { result: DataState<List<FaqItem>, Errors> ->
                    result
                        .onSuccess { faqs ->
                            updateStateThreadSafe {
                                val data = HelpUiState(questions = faqs.toImmutableList())
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
                    sendAction(action = HelpAction.ReviewOutcomeReported(outcome = outcome))
                    if (outcome != ReviewOutcome.Launched) {
                        sendAction(action = HelpAction.OpenPlayStoreReview)
                    }
                },
                onError = {
                    sendAction(action = HelpAction.OpenPlayStoreReview)
                }
            )
        }
    }

    private object Actions {
        const val REQUEST_REVIEW = "requestReview"
    }
}
