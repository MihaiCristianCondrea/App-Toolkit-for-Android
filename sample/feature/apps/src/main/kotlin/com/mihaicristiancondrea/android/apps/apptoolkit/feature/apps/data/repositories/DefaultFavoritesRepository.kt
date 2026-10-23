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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.local.FavoritesPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.mappers.toStorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.storageCall
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Favorites backed by the feature's [FavoritesPreferencesDataSource]. DataStore failures reach
 * callers as `StorageException`.
 */
class DefaultFavoritesRepository(
    private val preferences: FavoritesPreferencesDataSource,
    private val diagnostics: DiagnosticsReporter,
) : FavoritesRepository {

    override fun observeFavorites(): Flow<Set<String>> = preferences.favoriteApps
        .onStart {
            diagnostics.logBreadcrumb(
                message = "Favorites observe",
                attributes = mapOf("source" to "DefaultFavoritesRepository"),
            )
        }
        .catch { failure -> throw failure.toStorageException() ?: failure }

    override suspend fun toggleFavorite(packageName: String) {
        diagnostics.logBreadcrumb(
            message = "Favorite toggled",
            attributes = mapOf("packageName" to packageName),
        )
        storageCall { preferences.toggleFavoriteApp(packageName) }
    }
}
