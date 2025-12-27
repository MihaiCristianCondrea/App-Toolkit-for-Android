package com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ObserveFavoriteAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ToggleFavoriteUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.contract.FavoriteAppsAction
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.contract.FavoriteAppsEvent
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state.AppListUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState.IsLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class FavoriteAppsViewModel(
    private val observeFavoriteAppsUseCase: ObserveFavoriteAppsUseCase,
    observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val dispatchers: DispatcherProvider,
) : ScreenViewModel<AppListUiState, FavoriteAppsEvent, FavoriteAppsAction>(
    initialState = UiStateScreen(screenState = IsLoading(), data = AppListUiState())
) {

    private var observeJob: Job? = null
    private var toggleJob: Job? = null

    val favorites = observeFavoritesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    val canOpenRandomApp = screenState
        .map { it.data?.apps?.isNotEmpty() == true }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

    init {
        onEvent(FavoriteAppsEvent.LoadFavorites)
    }

    override fun onEvent(event: FavoriteAppsEvent) {
        when (event) {
            is FavoriteAppsEvent.LoadFavorites -> observe()
            is FavoriteAppsEvent.OpenRandomApp -> {
                val randomApp = screenData?.apps?.randomOrNull() ?: return
                sendAction(FavoriteAppsAction.OpenRandomApp(randomApp))
            }
        }
    }

    private fun observe() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            observeFavoriteAppsUseCase()
                .flowOn(dispatchers.io)
                .catch { t ->
                    if (t is CancellationException) throw t
                    screenState.updateState(ScreenState.Error())
                    screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.StringResource(R.string.error_an_error_occurred),
                            isError = true,
                            timeStamp = System.nanoTime(),
                            type = ScreenMessageType.SNACKBAR,
                        )
                    )
                }
                .collect { result ->
                    when (result) {
                        is DataState.Loading -> screenState.updateState(IsLoading())

                        is DataState.Success -> {
                            val apps = result.data
                            if (apps.isEmpty()) {
                                screenState.updateData(ScreenState.NoData()) { it.copy(apps = apps.toImmutableList()) }
                            } else {
                                screenState.updateData(ScreenState.Success()) { it.copy(apps = apps.toImmutableList()) }
                            }
                        }

                        is DataState.Error -> {
                            screenState.updateState(ScreenState.Error())
                            screenState.showSnackbar(
                                UiSnackbar(
                                    message = UiTextHelper.StringResource(R.string.error_an_error_occurred),
                                    isError = true,
                                    timeStamp = System.nanoTime(),
                                    type = ScreenMessageType.SNACKBAR,
                                )
                            )
                        }
                    }
                }
        }
    }

    fun toggleFavorite(packageName: String) {
        toggleJob?.cancel()
        toggleJob = viewModelScope.launch {
            runCatching {
                withContext(dispatchers.io) { toggleFavoriteUseCase(packageName) }
            }.onFailure { t ->
                if (t is CancellationException) throw t
                screenState.updateState(ScreenState.Error())
                screenState.showSnackbar(
                    UiSnackbar(
                        message = UiTextHelper.StringResource(R.string.error_failed_to_update_favorite),
                        isError = true,
                        timeStamp = System.nanoTime(),
                        type = ScreenMessageType.SNACKBAR,
                    )
                )
            }
        }
    }
}