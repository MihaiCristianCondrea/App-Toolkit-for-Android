/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.advanced.domain.repository

import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.Errors
import kotlinx.coroutines.flow.Flow

/**
 * Repository abstraction for cache-related operations.
 */
interface CacheRepository {
    /**
     * Clears the application's cache directories.
     *
     * @return A [Flow] emitting [DataState.Success] once every directory is gone, or
     * [DataState.Error] naming why the clear failed.
     */
    fun clearCache(): Flow<DataState<Unit, Errors.Database>>
}

