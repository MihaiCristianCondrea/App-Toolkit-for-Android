package com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.favorites.FakeFavoritesRepository
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
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
}
