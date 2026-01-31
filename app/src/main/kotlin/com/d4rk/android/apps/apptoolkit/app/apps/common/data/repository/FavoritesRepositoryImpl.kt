package com.d4rk.android.apps.apptoolkit.app.apps.common.data.repository

import com.d4rk.android.apps.apptoolkit.app.apps.common.data.local.FavoritesLocalDataSource
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.FavoritesRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class FavoritesRepositoryImpl(
    private val local: FavoritesLocalDataSource,
    private val firebaseController: FirebaseController,
) : FavoritesRepository {

    override fun observeFavorites(): Flow<Set<String>> = local.observeFavorites()
        .onStart {
            firebaseController.logBreadcrumb(
                message = "Favorites observe",
                attributes = mapOf("source" to "FavoritesRepositoryImpl"),
            )
        }

    override suspend fun toggleFavorite(packageName: String) {
        firebaseController.logBreadcrumb(
            message = "Favorite toggled",
            attributes = mapOf("packageName" to packageName),
        )
        local.toggleFavorite(packageName)
    }
}
