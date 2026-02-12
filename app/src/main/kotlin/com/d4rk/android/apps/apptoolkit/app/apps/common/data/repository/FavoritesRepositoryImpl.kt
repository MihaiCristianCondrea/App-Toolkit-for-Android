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
