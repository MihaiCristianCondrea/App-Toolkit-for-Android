package com.d4rk.android.apps.apptoolkit.app.apps.common.data.local

import kotlinx.coroutines.flow.Flow

interface FavoritesLocalDataSource {
    fun observeFavorites(): Flow<Set<String>>
    suspend fun toggleFavorite(packageName: String)
}
