package com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ObserveFavoriteAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ToggleFavoriteUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.contract.FavoriteAppsAction
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.contract.FavoriteAppsEvent
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state.AppListUiState
import com.d4rk.android.apps.apptoolkit.core.utils.extensions.toErrorMessage
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState.IsLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * ViewModel for the Favorite Apps screen.
 *
 * This ViewModel is responsible for managing the UI state for the list of favorite applications.
 * It observes changes in the list of favorite apps, handles user actions like opening a random app,
 * and manages the process of toggling an app's favorite status.
 *
 * @param observeFavoriteAppsUseCase Use case to observe the list of favorite applications.
 * @param observeFavoritesUseCase Use case to observe the set of favorite package names.
 * @param toggleFavoriteUseCase Use case to add or remove an app from favorites.
 * @param dispatchers Provides CoroutineDispatchers for managing thread execution.
 * @param firebaseController Reports ViewModel flow failures to Firebase.
 */
class FavoriteAppsViewModel(
    private val observeFavoriteAppsUseCase: ObserveFavoriteAppsUseCase,
    observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
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
                .onStart { screenState.setLoading() }
                .catch { error ->
                    if (error is CancellationException) throw error
                    firebaseController.reportViewModelError(
                        viewModelName = "FavoriteAppsViewModel",
                        action = "observe",
                        throwable = error,
                    )
                    screenState.updateState(ScreenState.Error())
                    showErrorSnackbar(
                        UiTextHelper.StringResource(R.string.error_failed_to_load_apps)
                    )
                }
                .collect { result ->
                    result
                        .onSuccess { apps ->
                            val immutableApps = apps.toImmutableList()
                            if (immutableApps.isEmpty()) {
                                screenState.updateData(ScreenState.NoData()) {
                                    it.copy(apps = immutableApps)
                                }
                            } else {
                                screenState.updateData(ScreenState.Success()) {
                                    it.copy(apps = immutableApps)
                                }
                            }
                        }
                        .onFailure { error ->
                            screenState.updateState(ScreenState.Error())
                            showErrorSnackbar(error.toErrorMessage())
                        }
                }
        }
    }

    fun toggleFavorite(packageName: String) {
        toggleJob?.cancel()
        toggleJob = viewModelScope.launch {
            try {
                withContext(dispatchers.io) { toggleFavoriteUseCase(packageName) }
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                screenState.updateState(ScreenState.Error())
                showErrorSnackbar(UiTextHelper.StringResource(R.string.error_failed_to_update_favorite))
            }
        }
    }

    private fun showErrorSnackbar(message: UiTextHelper) {
        screenState.showSnackbar(
            UiSnackbar(
                message = message,
                isError = true,
                timeStamp = System.nanoTime(),
                type = ScreenMessageType.SNACKBAR,
            )
        )
    }
}
