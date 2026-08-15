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
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.contracts.MainAction
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.contracts.MainEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.states.MainUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.app.review.domain.models.ReviewHost
import com.mihaicristiancondrea.android.libs.apptoolkit.app.review.domain.models.ReviewOutcome
import com.mihaicristiancondrea.android.libs.apptoolkit.app.review.domain.usecases.RequestInAppReviewUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.ScreenMessageType
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.remote.extensions.asUiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.onFailure
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.dismissSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setNoData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.showSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.data.repositories.NavigationRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDrawerItem
import com.mihaicristiancondrea.android.libs.apptoolkit.playservices.update.data.repositories.InAppUpdateRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.playservices.update.domain.models.InAppUpdateHost
import com.mihaicristiancondrea.android.libs.apptoolkit.playservices.update.domain.models.InAppUpdateResult
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

class MainViewModel(
    private val navigationRepository: NavigationRepository,
    private val consentRepository: ConsentRepository,
    private val requestInAppReviewUseCase: RequestInAppReviewUseCase,
    private val inAppUpdateRepository: InAppUpdateRepository,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<MainUiState, MainEvent, MainAction>(
    initialState = UiStateScreen(data = MainUiState()),
    firebaseController = firebaseController,
    screenName = "Main",
) {

    private var navigationJob: Job? = null
    private var initialConsentJob: Job? = null
    private var consentJob: Job? = null
    private var reviewJob: Job? = null
    private var updateJob: Job? = null

    init {
        onEvent(MainEvent.ApplyInitialConsent)
        onEvent(MainEvent.LoadNavigation)
    }

    override fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.ApplyInitialConsent -> applyInitialConsent()
            is MainEvent.LoadNavigation -> loadNavigationItems()
            is MainEvent.RequestConsent -> requestConsent(host = event.host)
            is MainEvent.RequestReview -> requestReview(host = event.host)
            is MainEvent.RequestInAppUpdate -> requestInAppUpdate(host = event.host)
        }
    }

    private fun loadNavigationItems() {
        startOperation(action = Actions.LOAD_NAVIGATION)
        navigationJob = navigationJob.restart {
            navigationRepository.getNavigationDrawerItems()
                .flowOn(dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        screenState.dismissSnackbar()
                        screenState.setLoading()
                    }
                }
                .onEach { items: List<NavigationDrawerItem> ->
                    updateStateThreadSafe {
                        val immutable = items.toImmutableList()
                        val base = screenData ?: MainUiState()
                        val updated = base.copy(navigationDrawerItems = immutable)

                        if (items.isEmpty()) {
                            screenState.setNoData(data = updated)
                        } else {
                            screenState.setSuccess(data = updated)
                        }
                    }
                }
                .catchReport(action = Actions.LOAD_NAVIGATION) {
                    updateStateThreadSafe {
                        val base = screenData ?: MainUiState()

                        if (base.navigationDrawerItems.isEmpty()) {
                            screenState.setNoData(data = base)
                        } else {
                            screenState.setSuccess(data = base)
                        }

                        screenState.showSnackbar(
                            UiSnackbar(
                                type = ScreenMessageType.SNACKBAR,
                                message = UiTextHelper.StringResource(R.string.error_failed_to_load_navigation),
                                isError = true,
                                timeStamp = System.nanoTime(),
                            )
                        )
                    }
                }

                .launchIn(viewModelScope)
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
        if (consentJob?.isActive == true) {
            breadcrumb(
                message = "consent_request_skipped",
                attributes = mapOf(
                    ExtraKeys.HOST to host.activity::class.java.name,
                    ExtraKeys.REASON to "already_in_progress"
                )
            )
            return
        }

        startOperation(
            action = Actions.REQUEST_CONSENT,
            extra = mapOf(ExtraKeys.HOST to host.activity::class.java.name)
        )
        consentJob = consentJob.restart {
            consentRepository.requestConsent(host = host)
                // Keep consent flow collection on ViewModel scope (main-safe for UI updates)
                // and avoid forcing the whole upstream chain onto Main via flowOn(main).
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
        startOperation(
            action = Actions.REQUEST_REVIEW,
            extra = mapOf(ExtraKeys.HOST to host.activity::class.java.name)
        )
        reviewJob = reviewJob.restart {
            launchReport(
                action = Actions.REQUEST_REVIEW,
                extra = mapOf(ExtraKeys.HOST to host.activity::class.java.name),
                block = {
                    val outcome = withContext(dispatchers.io) {
                        requestInAppReviewUseCase(host = host)
                    }
                    sendAction(action = MainAction.ReviewOutcomeReported(outcome = outcome))
                },
                onError = {
                    sendAction(action = MainAction.ReviewOutcomeReported(outcome = ReviewOutcome.Failed))
                }
            )
        }
    }

    private fun requestInAppUpdate(host: InAppUpdateHost) {
        startOperation(action = Actions.REQUEST_UPDATE)
        updateJob = updateJob.restart {
            inAppUpdateRepository.requestUpdate(host = host)
                .flowOn(dispatchers.io)
                .onEach { result ->
                    sendAction(action = MainAction.InAppUpdateResultReported(result = result))
                }
                .catchReport(action = Actions.REQUEST_UPDATE) {
                    sendAction(action = MainAction.InAppUpdateResultReported(result = InAppUpdateResult.Failed))
                }
                .launchIn(viewModelScope)
        }
    }

    private object Actions {
        const val APPLY_INITIAL_CONSENT: String = "applyInitialConsent"
        const val LOAD_NAVIGATION: String = "loadNavigationItems"
        const val REQUEST_CONSENT: String = "requestConsent"
        const val REQUEST_REVIEW: String = "requestReview"
        const val REQUEST_UPDATE: String = "requestInAppUpdate"
    }

    private object ExtraKeys {
        const val HOST: String = "host"
        const val STAGE: String = "stage"
        const val ERROR: String = "error"
        const val REASON: String = "reason"
    }
}

