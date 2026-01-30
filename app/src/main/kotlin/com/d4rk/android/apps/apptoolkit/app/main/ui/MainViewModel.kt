package com.d4rk.android.apps.apptoolkit.app.main.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.app.main.domain.usecases.GetNavigationDrawerItemsUseCase
import com.d4rk.android.apps.apptoolkit.app.main.ui.contract.MainAction
import com.d4rk.android.apps.apptoolkit.app.main.ui.contract.MainEvent
import com.d4rk.android.apps.apptoolkit.app.main.ui.states.MainUiState
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.RequestConsentUseCase
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.successData
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.toError
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/**
 * ViewModel for the main screen that loads navigation drawer content.
 */
class MainViewModel(
    private val getNavigationDrawerItemsUseCase: GetNavigationDrawerItemsUseCase,
    private val requestConsentUseCase: RequestConsentUseCase,
    private val firebaseController: FirebaseController,
    private val dispatchers: DispatcherProvider,
) : ScreenViewModel<MainUiState, MainEvent, MainAction>(
    initialState = UiStateScreen(data = MainUiState())
) {

    private var consentJob: Job? = null

    init {
        onEvent(MainEvent.LoadNavigation)
    }

    override fun onEvent(event: MainEvent) {
        when (event) {
            MainEvent.LoadNavigation -> loadNavigationItems()
            is MainEvent.RequestConsent -> requestConsent(event.host)
        }
    }

    private fun loadNavigationItems() {
        viewModelScope.launch {
            getNavigationDrawerItemsUseCase()
                .flowOn(dispatchers.io)
                .map<List<NavigationDrawerItem>, DataState<List<NavigationDrawerItem>, Errors>> { items ->
                    if (items.isEmpty()) {
                        DataState.Error(error = Errors.UseCase.NO_DATA)
                    } else {
                        DataState.Success(items)
                    }
                }
                .catch { throwable ->
                    if (throwable is CancellationException) throw throwable
                    firebaseController.reportViewModelError(
                        viewModelName = "MainViewModel",
                        action = "loadNavigationItems",
                        throwable = throwable,
                    )
                    emit(
                        DataState.Error(
                            error = throwable.toError(default = Errors.UseCase.INVALID_STATE)
                        )
                    )
                }
                .collect { result ->
                    result
                        .onSuccess { items ->
                            screenState.successData {
                                copy(
                                    navigationDrawerItems = items.toImmutableList(),
                                    showSnackbar = false,
                                    snackbarMessage = UiTextHelper.DynamicString("")
                                )
                            }
                        }
                        .onFailure {
                            val message =
                                UiTextHelper.StringResource(R.string.error_failed_to_load_navigation)
                            screenState.update { current ->
                                current.copy(
                                    screenState = ScreenState.Error(),
                                    data = current.data?.copy(
                                        showSnackbar = true,
                                        snackbarMessage = message
                                    )
                                )
                            }
                        }
                }
        }
    }

    private fun requestConsent(host: ConsentHost) {
        consentJob?.cancel()
        consentJob = viewModelScope.launch {
            requestConsentUseCase(host = host)
                .flowOn(dispatchers.main)
                .catch { throwable ->
                    if (throwable is CancellationException) throw throwable
                    firebaseController.reportViewModelError(
                        viewModelName = "MainViewModel",
                        action = "requestConsent",
                        throwable = throwable,
                    )
                    emit(DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO))
                }
                .first { state -> state !is DataState.Loading }
        }
    }
}
