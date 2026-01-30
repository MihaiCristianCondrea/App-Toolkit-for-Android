package com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases

import app.cash.turbine.test
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.FakeFavoritesRepository
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.FavoritesRepository
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ToggleFavoriteUseCase
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
        val useCase = ObserveFavoritesUseCase(repository)

        useCase().test {
            assertThat(awaitItem()).containsExactly("pkg1", "pkg2")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggle favorite updates repository`() = runBlocking {
        val repository = FakeFavoritesRepository()
        val toggleUseCase = ToggleFavoriteUseCase(repository)
        val observeUseCase = ObserveFavoritesUseCase(repository)

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
        val useCase = ToggleFavoriteUseCase(repository)

        val result = useCase("pkg")

        assertThat(result).isEqualTo(Unit)
        assertThat(repository.toggleCalls).isEqualTo(1)
        assertThat(repository.lastToggleParam).isEqualTo("pkg")
        assertThat(repository.observeCalls).isEqualTo(0)
    }

    @Test
    fun `observe favorites use case returns repository flow and invokes repository exactly once`() =
        runTest {
            val expectedFlow = flowOf(setOf("pkg"))
            val repository = RecordingFavoritesRepository(observeResult = expectedFlow)
            val useCase = ObserveFavoritesUseCase(repository)

            val result = useCase()

            assertThat(result).isSameInstanceAs(expectedFlow)
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
