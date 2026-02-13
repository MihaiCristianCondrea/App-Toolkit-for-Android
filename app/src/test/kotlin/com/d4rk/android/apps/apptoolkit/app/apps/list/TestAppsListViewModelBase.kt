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

package com.d4rk.android.apps.apptoolkit.app.apps.list

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.FetchDeveloperAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ToggleFavoriteUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.FakeFavoritesRepository
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.AppsListViewModel
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state.AppListUiState
import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.TestDispatchers
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.d4rk.android.apps.apptoolkit.core.utils.FakeFirebaseController
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
open class TestAppsListViewModelBase {

    protected lateinit var viewModel: AppsListViewModel
    private val firebaseController = FakeFirebaseController()
    protected fun setup(
        fetchApps: List<AppInfo>,
        initialFavorites: Set<String> = emptySet(),
        favoritesFlow: Flow<Set<String>>? = null,
        toggleError: Throwable? = null,
        fetchError: AppErrors? = null,
        dispatchers: DispatcherProvider = TestDispatchers(),
    ) {
        println("\uD83E\uDDEA [SETUP] Initial favorites: $initialFavorites")
        val developerAppsRepository = FakeDeveloperAppsRepository(fetchApps, fetchError)
        val fetchUseCase = FetchDeveloperAppsUseCase(developerAppsRepository, firebaseController)
        val favoritesRepository =
            FakeFavoritesRepository(initialFavorites, favoritesFlow, toggleError)
        val observeFavoritesUseCase = ObserveFavoritesUseCase(favoritesRepository, firebaseController)
        val toggleFavoriteUseCase = ToggleFavoriteUseCase(favoritesRepository, firebaseController)
        viewModel = AppsListViewModel(
            fetchUseCase,
            observeFavoritesUseCase,
            toggleFavoriteUseCase,
            dispatchers,
            firebaseController
        )
        println("\u2705 [SETUP] ViewModel initialized")
    }

    protected suspend fun Flow<UiStateScreen<AppListUiState>>.testSuccess(
        expectedSize: Int
    ) {
        println("\uD83D\uDE80 [TEST START] testSuccess expecting $expectedSize items")
        this@testSuccess.test {
            val first = awaitItem()
            println("\u23F3 [EMISSION 1] $first")
            if (first.screenState is ScreenState.IsLoading) {
                val second = awaitItem()
                println("\u2705 [EMISSION] $second")
                assertTrue(second.screenState is ScreenState.Success) { "Second emission should be Success but was ${second.screenState}" }
                assertThat(second.data?.apps?.size).isEqualTo(expectedSize)
            } else {
                assertTrue(first.screenState is ScreenState.Success) { "Expected Success state" }
                assertThat(first.data?.apps?.size).isEqualTo(expectedSize)
            }
            cancelAndIgnoreRemainingEvents()
        }
        println("\uD83C\uDFC1 [TEST END] testSuccess")
    }

    protected suspend fun toggleAndAssert(packageName: String, expected: Boolean) {
        println("\uD83D\uDE80 [TEST START] toggleAndAssert for $packageName expecting $expected")
        viewModel.favorites.test {
            val before = awaitItem()
            println("Favorites before: $before")

            viewModel.toggleFavorite(packageName)
            println("\uD83D\uDD04 [ACTION] toggled $packageName")

            val after = awaitItem()
            println("Favorites after: $after")
            if (after.contains(packageName) == expected) {
                println("\uD83D\uDC4D [ASSERTION PASSED] favorite state matches $expected")
            } else {
                println(
                    "\u274C [ASSERTION FAILED] expected $expected but was ${
                        after.contains(
                            packageName
                        )
                    }"
                )
            }
            assertThat(after.contains(packageName)).isEqualTo(expected)
            cancelAndIgnoreRemainingEvents()
        }
        println("\uD83C\uDFC1 [TEST END] toggleAndAssert")
    }

    @AfterEach
    fun tearDown() {
        if (this::viewModel.isInitialized) {
            viewModel.viewModelScope.cancel()
        }
    }
}
