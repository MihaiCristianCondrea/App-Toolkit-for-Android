package com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ObserveFavoriteAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ToggleFavoriteUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.contract.FavoriteAppsAction
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.contract.FavoriteAppsEvent
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state.AppListUiState
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class FavoriteAppsViewModel(
    private val observeFavoriteAppsUseCase: ObserveFavoriteAppsUseCase,
    observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<AppListUiState, FavoriteAppsEvent, FavoriteAppsAction>(
    initialState = UiStateScreen(
        data = AppListUiState(),
    ),
    firebaseController = firebaseController,
    screenName = "FavoriteApps",
) {

    private var toggleJob: Job? = null

    val favorites = observeFavoritesUseCase().stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5_000),
        initialValue = emptySet()
    )

    val canOpenRandomApp = screenState
        .map { state -> state.data?.apps?.isNotEmpty() == true }
        .stateIn(scope = viewModelScope, started = WhileSubscribed(5_000), initialValue = false)

    init {
        onEvent(FavoriteAppsEvent.LoadFavorites)
    }

    override fun handleEvent(event: FavoriteAppsEvent) {
        when (event) {
            is FavoriteAppsEvent.LoadFavorites -> observe()
            is FavoriteAppsEvent.OpenRandomApp -> openRandomApp()
        }
    }

    private fun openRandomApp() {
        val randomApp: AppInfo = screenData?.apps?.randomOrNull() ?: return
        startOperation(
            action = Actions.OPEN_RANDOM_APP,
            extra = mapOf(pair = ExtraKeys.PACKAGE_NAME to randomApp.packageName)
        )
        sendAction(action = FavoriteAppsAction.OpenRandomApp(app = randomApp))
    }

    private fun observe() {
        generalJob = generalJob.restart {
            startOperation(action = Actions.OBSERVE_FAVORITES)
            observeFavoriteAppsUseCase.invoke()
                .flowOn(context = dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        screenState.dismissSnackbar()
                        screenState.setLoading()
                    }
                }
                .onEach { result ->
                    result
                        .onSuccess { apps ->
                            updateStateThreadSafe {
                                val immutableApps = apps.toImmutableList()
                                val base = screenData ?: AppListUiState()
                                val updated = base.copy(apps = immutableApps)
                                if (immutableApps.isEmpty()) screenState.setNoData(data = updated) else screenState.setSuccess(
                                    data = updated
                                )
                            }

                        }
                        .onFailure { error ->
                            updateStateThreadSafe {
                                screenState.setError(message = error.toErrorMessage())
                            }
                        }

                }
                .catchReport(action = Actions.OBSERVE_FAVORITES) {
                    screenState.setError(message = UiTextHelper.StringResource(R.string.error_failed_to_load_apps))
                }
                .launchIn(viewModelScope)
        }
    }

    fun toggleFavorite(packageName: String) {
        toggleJob = toggleJob.restart {
            launchReport(
                action = Actions.TOGGLE_FAVORITE,
                extra = mapOf(ExtraKeys.PACKAGE_NAME to packageName),
                block = {
                    withContext(dispatchers.io) { toggleFavoriteUseCase.invoke(packageName = packageName) }
                },
                onError = {
                    updateStateThreadSafe {
                        screenState.setError(message = UiTextHelper.StringResource(R.string.error_failed_to_update_favorite))
                    }
                },
            )
        }
    }

    private object Actions {
        const val OBSERVE_FAVORITES: String = "observeFavorites"
        const val TOGGLE_FAVORITE: String = "toggleFavorite"
        const val OPEN_RANDOM_APP: String = "openRandomApp"
    }

    private object ExtraKeys {
        const val PACKAGE_NAME: String = "packageName"
    }
}
