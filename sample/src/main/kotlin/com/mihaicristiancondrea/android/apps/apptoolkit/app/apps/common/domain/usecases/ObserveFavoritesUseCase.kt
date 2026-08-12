/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.usecases

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repository.FavoritesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repository.FirebaseController
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
