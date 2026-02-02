package com.d4rk.android.apps.apptoolkit.app.apps.list.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.FetchDeveloperAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ToggleFavoriteUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.contract.HomeAction
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.contract.HomeEvent
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state.AppListUiState
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.d4rk.android.apps.apptoolkit.core.utils.extensions.toErrorMessage
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setError
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.setNoData
import com.d4rk.android.libs.apptoolkit.core.ui.state.setSuccess
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<AppListUiState, HomeEvent, HomeAction>(
    initialState = UiStateScreen(data = AppListUiState()),
    firebaseController = firebaseController,
    screenName = "AppsList",
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

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.FetchApps -> fetchAppsTrigger.tryEmit(Unit)

            HomeEvent.OpenRandomApp -> {
                val randomApp = screenData?.apps?.randomOrNull() ?: return
                startOperation(
                    action = Actions.OPEN_RANDOM_APP,
                    extra = mapOf(ExtraKeys.PACKAGE_NAME to randomApp.packageName),
                )
                sendAction(HomeAction.OpenRandomApp(randomApp))
            }
        }
    }

    private fun observeFetch() {
        startOperation(action = Actions.OBSERVE_FETCH)
        fetchJob = fetchJob.restart {
            fetchAppsTrigger
                .flatMapLatest {
                    fetchDeveloperAppsUseCase()
                        .flowOn(dispatchers.io)
                        .onStart {
                            updateStateThreadSafe {
                                screenState.dismissSnackbar()
                                screenState.setLoading()
                            }
                        }
                }
                .catchReport(action = Actions.OBSERVE_FETCH) {
                    updateStateThreadSafe {
                        showLoadAppsError()
                    }
                }
                .onEach { result ->
                    result
                        .onSuccess { apps ->
                            updateStateThreadSafe {
                                val list = apps.toImmutableList()
                                val base = screenData ?: AppListUiState()
                                val updated = base.copy(apps = list)

                                if (list.isEmpty()) {
                                    screenState.setNoData(data = updated)
                                } else {
                                    screenState.setSuccess(data = updated)
                                }
                            }
                        }
                        .onFailure { error ->
                            updateStateThreadSafe {
                                showLoadAppsError(error)
                            }
                        }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun showLoadAppsError(error: AppErrors? = null) {
        screenState.setError(
            message = error?.toErrorMessage()
                ?: UiTextHelper.StringResource(R.string.error_failed_to_load_apps)
        )
    }

    fun toggleFavorite(packageName: String) {
        toggleJob = toggleJob.restart {
            launchReport(
                action = Actions.TOGGLE_FAVORITE,
                extra = mapOf(ExtraKeys.PACKAGE_NAME to packageName),
                block = {
                    withContext(dispatchers.io) { toggleFavoriteUseCase(packageName) }
                },
                onError = {
                    updateStateThreadSafe {
                        screenState.setError(
                            message = UiTextHelper.StringResource(R.string.error_failed_to_update_favorite),
                        )
                    }
                },
            )
        }
    }

    private object Actions {
        const val OBSERVE_FETCH: String = "observeFetch"
        const val TOGGLE_FAVORITE: String = "toggleFavorite"
        const val OPEN_RANDOM_APP: String = "openRandomApp"
    }

    private object ExtraKeys {
        const val PACKAGE_NAME: String = "packageName"
    }
}
