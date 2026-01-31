package com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.FavoritesRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController

class ToggleFavoriteUseCase(
    private val repository: FavoritesRepository,
    private val firebaseController: FirebaseController,
) {
    suspend operator fun invoke(packageName: String) {
        firebaseController.logBreadcrumb(
            message = "Toggle favorite",
            attributes = mapOf("packageName" to packageName),
        )
        repository.toggleFavorite(packageName)
    }
}
