package com.d4rk.android.apps.apptoolkit.app.main.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.app.main.domain.usecases.GetNavigationDrawerItemsUseCase
import com.d4rk.android.apps.apptoolkit.app.main.ui.contract.MainAction
import com.d4rk.android.apps.apptoolkit.app.main.ui.contract.MainEvent
import com.d4rk.android.apps.apptoolkit.app.main.ui.state.MainUiState
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.RequestConsentUseCase
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setError
import com.d4rk.android.libs.apptoolkit.core.ui.state.successData
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.toError
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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

    init {
        firebaseController.logBreadcrumb(
            message = "MainViewModel initialized",
            attributes = mapOf("screen" to "Main"),
        )
        onEvent(MainEvent.LoadNavigation)
    }

    override fun onEvent(event: MainEvent) {
        firebaseController.logBreadcrumb(
            message = "MainViewModel event",
            attributes = mapOf("event" to event::class.java.simpleName),
        )
        when (event) {
            MainEvent.LoadNavigation -> loadNavigationItems()
            is MainEvent.RequestConsent -> requestConsent(event.host)
        }
    }

    private fun loadNavigationItems() {
        firebaseController.logBreadcrumb(
            message = "Main navigation load started",
            attributes = mapOf("source" to "MainViewModel"),
        )
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
                                    snackbarMessage = UiTextHelper.DynamicString(""),
                                )
                            }
                        }
                        .onFailure {
                            screenState.setError(message = UiTextHelper.StringResource(R.string.error_failed_to_load_navigation))
                        }
                }
        }
    }

    private fun requestConsent(host: ConsentHost) {
        firebaseController.logBreadcrumb(
            message = "Main consent request",
            attributes = mapOf("host" to host.activity::class.java.name),
        )
        generalJob?.cancel()
        generalJob = requestConsentUseCase(host = host)
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
            .launchIn(viewModelScope)
    }
}
