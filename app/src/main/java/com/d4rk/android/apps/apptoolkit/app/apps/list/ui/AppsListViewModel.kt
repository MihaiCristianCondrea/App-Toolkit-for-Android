package com.d4rk.android.apps.apptoolkit.app.apps.list.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ToggleFavoriteUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.contract.HomeAction
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.contract.HomeEvent
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.usecases.FetchDeveloperAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state.AppListUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateData
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateState
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AppsListViewModel(
    private val fetchDeveloperAppsUseCase: FetchDeveloperAppsUseCase,
    observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val dispatchers: DispatcherProvider,
) : ScreenViewModel<AppListUiState, HomeEvent, HomeAction>(
    initialState = UiStateScreen(screenState = ScreenState.IsLoading(), data = AppListUiState())
) {

    private val fetchAppsTrigger = MutableSharedFlow<Unit>(replay = 1)

    val favorites = flow { emitAll(observeFavoritesUseCase()) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptySet()
    )

    val canOpenRandomApp = screenState
        .map { state -> state.data?.apps?.isNotEmpty() == true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    init {
        viewModelScope.launch {
            fetchAppsTrigger
                .flatMapLatest {
                    fetchDeveloperAppsUseCase()
                        .flowOn(dispatchers.io)
                        .onCompletion { cause ->
                            if (cause != null && cause !is CancellationException) {
                                cause.printStackTrace()
                                showLoadAppsError()
                            }
                        }
                }
                .collect { result ->
                    when (result) {
                        is DataState.Success -> {
                            val apps = result.data.toImmutableList()

                            if (apps.isEmpty()) {
                                screenState.update { current ->
                                    current.copy(
                                        screenState = ScreenState.NoData(),
                                        data = AppListUiState(apps = persistentListOf<AppInfo>())
                                    )
                                }
                            } else {
                                screenState.updateData(newState = ScreenState.Success()) { currentData ->
                                    currentData.copy(apps = apps)
                                }
                            }
                        }

                        is DataState.Error -> showLoadAppsError()

                        is DataState.Loading -> screenState.updateState(ScreenState.IsLoading())
                    }
                }
        }

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

    private fun showLoadAppsError() {
        screenState.updateState(ScreenState.Error())
        screenState.showSnackbar(
            UiSnackbar(
                message = UiTextHelper.StringResource(R.string.error_failed_to_load_apps),
                isError = true,
                timeStamp = System.currentTimeMillis(),
                type = ScreenMessageType.SNACKBAR,
            )
        )
    }

    fun toggleFavorite(packageName: String) {
        viewModelScope.launch(dispatchers.io) {
            runCatching { toggleFavoriteUseCase(packageName) }
                .onFailure { error ->
                    error.printStackTrace()
                    screenState.update { currentState ->
                        currentState.copy(screenState = ScreenState.Error())
                    }
                }
        }
    }
}