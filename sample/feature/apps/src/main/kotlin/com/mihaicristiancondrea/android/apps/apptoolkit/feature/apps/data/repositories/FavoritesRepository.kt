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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import kotlinx.coroutines.flow.Flow

/** The packages the user marked as favorites. Both calls are safe from the main thread. */
interface FavoritesRepository {
    /** The favorite package names, again after each change. Fails with [StorageException]. */
    fun observeFavorites(): Flow<Set<String>>

    /**
     * Adds [packageName] to the favorites, or removes it when it is one already.
     *
     * @throws StorageException when the change could not be saved.
     */
    suspend fun toggleFavorite(packageName: String)
}