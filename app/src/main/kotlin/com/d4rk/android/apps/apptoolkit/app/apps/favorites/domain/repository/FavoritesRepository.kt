package com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<Set<String>>
    suspend fun toggleFavorite(packageName: String)
}

