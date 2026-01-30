package com.d4rk.android.apps.apptoolkit.app.apps.common.data.repository

import com.d4rk.android.apps.apptoolkit.app.apps.common.data.local.FavoritesLocalDataSource
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class FavoritesRepositoryImpl(
    private val local: FavoritesLocalDataSource ,
) : FavoritesRepository {

    override fun observeFavorites(): Flow<Set<String>> = local.observeFavorites()

    override suspend fun toggleFavorite(packageName: String) {
        local.toggleFavorite(packageName)
    }
}