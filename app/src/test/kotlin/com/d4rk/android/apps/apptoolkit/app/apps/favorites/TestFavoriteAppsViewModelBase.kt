/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.d4rk.android.apps.apptoolkit.app.apps.favorites

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.FetchDeveloperAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ObserveFavoriteAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ToggleFavoriteUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.FavoriteAppsViewModel
import com.d4rk.android.apps.apptoolkit.app.apps.list.FakeDeveloperAppsRepository
import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.TestDispatchers
import com.d4rk.android.apps.apptoolkit.core.utils.FakeFirebaseController
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import org.junit.jupiter.api.AfterEach

@OptIn(ExperimentalCoroutinesApi::class)
open class TestFavoriteAppsViewModelBase {

    protected lateinit var viewModel: FavoriteAppsViewModel
    private val firebaseController = FakeFirebaseController()
    protected fun setup(
        fetchApps: List<AppInfo>,
        initialFavorites: Set<String> = emptySet(),
        favoritesFlow: Flow<Set<String>>? = null,
        toggleError: Throwable? = null,
        dispatchers: DispatcherProvider = TestDispatchers(),
    ) {
        println("\uD83E\uDDEA [SETUP] Initial favorites: $initialFavorites")
        val developerAppsRepository = FakeDeveloperAppsRepository(fetchApps)
        val fetchUseCase = FetchDeveloperAppsUseCase(developerAppsRepository, firebaseController)
        val favoritesRepository =
            FakeFavoritesRepository(initialFavorites, favoritesFlow, toggleError)
        val observeFavoritesUseCase = ObserveFavoritesUseCase(favoritesRepository, firebaseController)
        val toggleFavoriteUseCase = ToggleFavoriteUseCase(favoritesRepository, firebaseController)
        val observeFavoriteAppsUseCase = ObserveFavoriteAppsUseCase(
            fetchUseCase,
            observeFavoritesUseCase,
            firebaseController,
        )
        viewModel = FavoriteAppsViewModel(
            observeFavoriteAppsUseCase = observeFavoriteAppsUseCase,
            observeFavoritesUseCase = observeFavoritesUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase,
            dispatchers = dispatchers,
            firebaseController = firebaseController,
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
