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

package com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases

import app.cash.turbine.test
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.FavoritesRepository
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ToggleFavoriteUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.FakeFavoritesRepository
import com.d4rk.android.apps.apptoolkit.core.utils.FakeFirebaseController
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class FavoritesUseCasesTest {

    @Test
    fun `observe favorites emits initial set`() = runBlocking {
        val repository = FakeFavoritesRepository(initialFavorites = setOf("pkg1", "pkg2"))
        val useCase = ObserveFavoritesUseCase(repository, FakeFirebaseController())

        useCase().test {
            assertThat(awaitItem()).containsExactly("pkg1", "pkg2")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggle favorite updates repository`() = runBlocking {
        val repository = FakeFavoritesRepository()
        val firebaseController = FakeFirebaseController()
        val toggleUseCase = ToggleFavoriteUseCase(repository, firebaseController)
        val observeUseCase = ObserveFavoritesUseCase(repository, firebaseController)

        observeUseCase().test {
            assertThat(awaitItem()).isEmpty()
            toggleUseCase("pkg")
            assertThat(awaitItem()).containsExactly("pkg")
            toggleUseCase("pkg")
            assertThat(awaitItem()).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggle favorite use case invokes repository exactly once`() = runTest {
        val repository = RecordingFavoritesRepository()
        val useCase = ToggleFavoriteUseCase(repository, FakeFirebaseController())

        val result = useCase("pkg")

        assertThat(result).isEqualTo(Unit)
        assertThat(repository.toggleCalls).isEqualTo(1)
        assertThat(repository.lastToggleParam).isEqualTo("pkg")
        assertThat(repository.observeCalls).isEqualTo(0)
    }

    @Test
    fun `observe favorites use case emits repository values and invokes repository once`() =
        runTest {
            val expectedSet = setOf("pkg")
            val repositoryFlow = flowOf(expectedSet)
            val repository = RecordingFavoritesRepository(observeResult = repositoryFlow)
            val useCase = ObserveFavoritesUseCase(
                repository = repository,
                firebaseController = FakeFirebaseController()
            )

            val resultFlow = useCase()

            resultFlow.test {
                assertThat(awaitItem()).containsExactly("pkg")
                cancelAndIgnoreRemainingEvents()
            }

            assertThat(repository.observeCalls).isEqualTo(1)
            assertThat(repository.toggleCalls).isEqualTo(0)
        }

}

private class RecordingFavoritesRepository(
    private val observeResult: Flow<Set<String>> = flowOf(emptySet()),
) : FavoritesRepository {
    var observeCalls = 0
        private set
    var toggleCalls = 0
        private set
    var lastToggleParam: String? = null
        private set

    override fun observeFavorites(): Flow<Set<String>> {
        observeCalls += 1
        return observeResult
    }

    override suspend fun toggleFavorite(packageName: String) {
        toggleCalls += 1
        lastToggleParam = packageName
    }
}
