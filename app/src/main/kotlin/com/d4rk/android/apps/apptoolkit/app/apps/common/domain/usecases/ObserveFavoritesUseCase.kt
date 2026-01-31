package com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.FavoritesRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Use case responsible for observing the collection of favorite item identifiers.
 *
 * This use case retrieves a reactive stream of favorite IDs from the [FavoritesRepository]
 * and logs a breadcrumb to [FirebaseController] whenever the observation starts.
 *
 * @property repository The repository providing the stream of favorite items.
 * @property firebaseController The controller used for logging analytics and breadcrumbs.
 */
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
