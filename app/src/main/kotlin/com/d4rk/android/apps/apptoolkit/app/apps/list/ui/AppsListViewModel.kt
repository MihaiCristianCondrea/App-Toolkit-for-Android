package com.d4rk.android.apps.apptoolkit.app.apps.list.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ToggleFavoriteUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.FetchDeveloperAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.contract.HomeAction
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.contract.HomeEvent
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state.AppListUiState
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.d4rk.android.apps.apptoolkit.core.utils.extensions.toErrorMessage
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for the Apps List screen.
 *
 * This ViewModel is responsible for fetching and managing the list of developer applications,
 * handling user interactions such as fetching apps, opening a random app, and toggling favorites.
 * It observes changes in favorite apps and updates the UI state accordingly.
 *
 * @param fetchDeveloperAppsUseCase Use case to fetch the list of applications.
 * @param observeFavoritesUseCase Use case to observe the set of favorite app package names.
 * @param toggleFavoriteUseCase Use case to add or remove an app from favorites.
 * @param dispatchers Provides coroutine dispatchers for different contexts (IO, Main, etc.).
 * @param firebaseController Reports ViewModel flow failures to Firebase.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AppsListViewModel(
    private val fetchDeveloperAppsUseCase: FetchDeveloperAppsUseCase,
    observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
) : ScreenViewModel<AppListUiState, HomeEvent, HomeAction>(
    initialState = UiStateScreen(screenState = ScreenState.IsLoading(), data = AppListUiState())
) {

    private val fetchAppsTrigger = MutableSharedFlow<Unit>(replay = 1)
    private var fetchJob: Job? = null
    private var toggleJob: Job? = null

    val favorites = observeFavoritesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    val canOpenRandomApp = screenState
        .map { it.data?.apps?.isNotEmpty() == true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    init {
        observeFetch()
        fetchAppsTrigger.tryEmit(Unit)
    }

    override fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.FetchApps -> fetchAppsTrigger.tryEmit(Unit)
            HomeEvent.OpenRandomApp -> {
                val randomApp = screenData?.apps?.randomOrNull() ?: return
                sendAction(HomeAction.OpenRandomApp(randomApp))
            }
        }
    }

    private fun observeFetch() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            fetchAppsTrigger
                .flatMapLatest {
                    fetchDeveloperAppsUseCase()
                        .flowOn(dispatchers.io)
                        .onStart { screenState.setLoading() }
                }
                .catch { throwable ->
                    if (throwable is CancellationException) throw throwable
                    firebaseController.reportViewModelError(
                        viewModelName = "AppsListViewModel",
                        action = "observeFetch",
                        throwable = throwable,
                    )
                    updateStateThreadSafe { showLoadAppsError() }
                }
                .collect { result ->
                    updateStateThreadSafe {
                        result
                            .onSuccess { apps ->
                                val data = apps.toImmutableList()
                                if (data.isEmpty()) {
                                    screenState.update { current ->
                                        current.copy(
                                            screenState = ScreenState.NoData(),
                                            data = AppListUiState(apps = persistentListOf())
                                        )
                                    }
                                } else {
                                    screenState.updateData(newState = ScreenState.Success()) { current ->
                                        current.copy(apps = data)
                                    }
                                }
                            }
                            .onFailure { showLoadAppsError(it) }
                    }
                }
        }
    }

    private fun showLoadAppsError(error: AppErrors? = null) {
        screenState.updateState(ScreenState.Error())
        screenState.showSnackbar(
            UiSnackbar(
                message = error?.toErrorMessage()
                    ?: UiTextHelper.StringResource(R.string.error_failed_to_load_apps),
                isError = true,
                timeStamp = System.nanoTime(),
                type = ScreenMessageType.SNACKBAR,
            )
        )
    }

    fun toggleFavorite(packageName: String) {
        toggleJob?.cancel()
        toggleJob = viewModelScope.launch {
            try {
                withContext(dispatchers.io) { toggleFavoriteUseCase(packageName) }
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                screenState.updateState(ScreenState.Error())
            }
        }
    }
}
