package com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.FavoritesRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class ObserveFavoritesUseCase(
    private val repository: FavoritesRepository,
    private val firebaseController: FirebaseController,
) {
    operator fun invoke(): Flow<Set<String>> = repository.observeFavorites()
        .onStart {
            firebaseController.logBreadcrumb(
                message = "Observe favorites started",
                attributes = mapOf("source" to "ObserveFavoritesUseCase"),
            )
        }
}
