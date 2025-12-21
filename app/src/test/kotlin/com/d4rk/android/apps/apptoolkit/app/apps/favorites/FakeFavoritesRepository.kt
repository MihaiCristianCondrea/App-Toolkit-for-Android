package com.d4rk.android.apps.apptoolkit.app.apps.favorites

import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Simple in-memory implementation of [FavoritesRepository] for tests.
 *
 * The repository holds favorites in a mutable flow so that tests can observe
 * changes and toggle values without relying on mocking frameworks.
 */
class FakeFavoritesRepository(
    initialFavorites: Set<String> = emptySet(),
    favoritesFlow: Flow<Set<String>>? = null,
    private val toggleError: Throwable? = null,
) : FavoritesRepository {

    private val stateFlow: MutableStateFlow<Set<String>>?
    private val sharedFlow: MutableSharedFlow<Set<String>>?
    private val flow: Flow<Set<String>>

    init {
        when (favoritesFlow) {
            is MutableStateFlow -> {
                stateFlow = favoritesFlow
                sharedFlow = null
                flow = favoritesFlow
                if (favoritesFlow.value.isEmpty() && initialFavorites.isNotEmpty()) {
                    favoritesFlow.value = initialFavorites
                }
            }

            is MutableSharedFlow -> {
                stateFlow = null
                sharedFlow = favoritesFlow
                flow = favoritesFlow
                if (initialFavorites.isNotEmpty()) {
                    favoritesFlow.tryEmit(initialFavorites)
                }
            }

            null -> {
                val state = MutableStateFlow(initialFavorites)
                stateFlow = state
                sharedFlow = null
                flow = state
            }

            else -> {
                // Non-mutable flow provided; toggling will be no-op
                stateFlow = null
                sharedFlow = null
                flow = favoritesFlow
            }
        }
    }

    override fun observeFavorites(): Flow<Set<String>> = flow

    override suspend fun toggleFavorite(packageName: String) {
        toggleError?.let { throw it }
        val current = when {
            stateFlow != null -> stateFlow.value.toMutableSet()
            sharedFlow != null -> sharedFlow.replayCache.lastOrNull()?.toMutableSet()
                ?: mutableSetOf()

            else -> return
        }
        if (!current.add(packageName)) {
            current.remove(packageName)
        }
        when {
            stateFlow != null -> stateFlow.value = current
            sharedFlow != null -> sharedFlow.emit(current)
        }
    }
}

