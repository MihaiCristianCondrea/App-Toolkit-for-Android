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

package com.d4rk.android.libs.apptoolkit.app.advanced.data.repository

import android.content.Context
import com.d4rk.android.libs.apptoolkit.app.advanced.domain.repository.CacheRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

/**
 * Implementation of the [CacheRepository] interface.
 * This class handles the logic for clearing the application's cache directories.
 *
 * @property context The application context used to access cache directories.
 */
class CacheRepositoryImpl(
    private val context: Context,
    private val firebaseController: FirebaseController,
) : CacheRepository {

    override fun clearCache(): Flow<Result<Unit>> = flow {
        firebaseController.logBreadcrumb(
            message = "Cache clear requested",
            attributes = mapOf("source" to "CacheRepositoryImpl"),
        )
        val cacheDirs: List<File> = buildList {
            add(context.cacheDir)
            add(context.codeCacheDir)
            context.externalCacheDir?.let(::add)
        }.distinct()

        val failed = cacheDirs.filterNot { it.deleteRecursively() }

        emit(if (failed.isEmpty()) Result.Success(Unit) else Result.Error(Exception("Failed to clear cache")))
    }
}
