package com.d4rk.android.apps.apptoolkit.app.apps.favorites

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ObserveFavoriteAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ToggleFavoriteUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.FavoriteAppsViewModel
import com.d4rk.android.apps.apptoolkit.app.apps.list.FakeDeveloperAppsRepository
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.usecases.FetchDeveloperAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import org.junit.jupiter.api.AfterEach

@OptIn(ExperimentalCoroutinesApi::class)
open class TestFavoriteAppsViewModelBase {

    protected lateinit var viewModel: FavoriteAppsViewModel
    protected fun setup(
        fetchApps: List<AppInfo>,
        initialFavorites: Set<String> = emptySet(),
        favoritesFlow: Flow<Set<String>>? = null,
        toggleError: Throwable? = null,
        dispatchers: DispatcherProvider = TestDispatchers(),
    ) {
        println("\uD83E\uDDEA [SETUP] Initial favorites: $initialFavorites")
        val developerAppsRepository = FakeDeveloperAppsRepository(fetchApps)
        val fetchUseCase = FetchDeveloperAppsUseCase(developerAppsRepository)
        val favoritesRepository =
            FakeFavoritesRepository(initialFavorites, favoritesFlow, toggleError)
        val observeFavoritesUseCase = ObserveFavoritesUseCase(favoritesRepository)
        val toggleFavoriteUseCase = ToggleFavoriteUseCase(favoritesRepository)
        val observeFavoriteAppsUseCase = ObserveFavoriteAppsUseCase(
            fetchUseCase,
            observeFavoritesUseCase,
            dispatchers,
        )
        viewModel = FavoriteAppsViewModel(
            observeFavoriteAppsUseCase = observeFavoriteAppsUseCase,
            observeFavoritesUseCase = observeFavoritesUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase,
            dispatchers = dispatchers,
        )
        println("\u2705 [SETUP] ViewModel initialized")
    }

    @AfterEach
    fun tearDown() {
        if (this::viewModel.isInitialized) {
            viewModel.viewModelScope.cancel()
        }
    }
}
