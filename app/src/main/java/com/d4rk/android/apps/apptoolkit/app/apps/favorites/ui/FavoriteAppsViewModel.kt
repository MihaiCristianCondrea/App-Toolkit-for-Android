package com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.actions.FavoriteAppsAction
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.actions.FavoriteAppsEvent
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ObserveFavoriteAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ToggleFavoriteUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.ui.UiHomeScreen
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState.Error
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState.IsLoading
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState.NoData
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState.Success
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateData
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateState
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteAppsViewModel(
    private val observeFavoriteAppsUseCase: ObserveFavoriteAppsUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val dispatchers: DispatcherProvider,
) : ScreenViewModel<UiHomeScreen, FavoriteAppsEvent, FavoriteAppsAction>(
    initialState = UiStateScreen(screenState = IsLoading(), data = UiHomeScreen())
) {

    private val loadFavoritesTrigger = MutableSharedFlow<Unit>(replay = 1)

    val favorites = loadFavoritesTrigger
        .flatMapLatest { observeFavoritesUseCase() }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    val canOpenRandomApp = screenState
        .map { state -> state.data?.apps.orEmpty().isNotEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )


    init {
        viewModelScope.launch {
            loadFavoritesTrigger
                .flatMapLatest {
                    observeFavoriteAppsUseCase()
                        .onCompletion { cause ->
                            if (cause == null && screenState.value.screenState is IsLoading) {
                                screenState.update { current ->
                                    current.copy(
                                        screenState = NoData(),
                                        data = current.data ?: UiHomeScreen(apps = emptyList())
                                    )
                                }
                            }
                        }
                }
                .collect { result ->
                    when (result) {
                        is DataState.Success -> {
                            val apps = result.data
                            if (apps.isEmpty()) {
                                screenState.update { current ->
                                    current.copy(
                                        screenState = NoData(),
                                        data = UiHomeScreen(apps = emptyList())
                                    )
                                }
                            } else {
                                screenState.updateData(Success()) { current ->
                                    current.copy(apps = apps)
                                }
                            }
                        }

                        is DataState.Loading -> screenState.updateState(IsLoading())

                        is DataState.Error -> {
                            screenState.update { current ->
                                current.copy(screenState = Error(), data = null)
                            }
                            screenState.showSnackbar(
                                UiSnackbar(
                                    message = UiTextHelper.StringResource(R.string.error_an_error_occurred),
                                    isError = true,
                                    timeStamp = System.currentTimeMillis(),
                                    type = ScreenMessageType.SNACKBAR,
                                )
                            )
                        }
                    }
                }
        }
        loadFavoritesTrigger.tryEmit(Unit)
    }

    override fun onEvent(event: FavoriteAppsEvent) {
        when (event) {
            FavoriteAppsEvent.LoadFavorites -> loadFavoritesTrigger.tryEmit(Unit)
            FavoriteAppsEvent.OpenRandomApp -> {
                val randomApp = screenData?.apps.orEmpty().randomOrNull() ?: return
                sendAction(FavoriteAppsAction.OpenRandomApp(randomApp))
            }
        }
    }

    fun toggleFavorite(packageName: String) {
        viewModelScope.launch(dispatchers.io) {
            runCatching { toggleFavoriteUseCase(packageName) }
                .onFailure { error ->
                    error.printStackTrace()
                    screenState.update { current ->
                        current.copy(screenState = Error())
                    }
                    screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.StringResource(R.string.error_failed_to_update_favorite),
                            isError = true,
                            timeStamp = System.currentTimeMillis(),
                            type = ScreenMessageType.SNACKBAR,
                        )
                    )
                }
        }
    }
}

