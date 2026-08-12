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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.advanced.data.repository

import android.content.Context
import com.mihaicristiancondrea.android.libs.apptoolkit.app.advanced.domain.repository.CacheRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.model.Result
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.repository.FirebaseController
import kotlinx.coroutines.CancellationException
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
    /**
     * Seam for the delete itself. Without it the failure branch is unreachable from a test: an
     * empty temp directory always deletes cleanly, so the error path shipped uncovered.
     */
    private val deleteRecursively: (File) -> Boolean = File::deleteRecursively,
) : CacheRepository {

    override fun clearCache(): Flow<Result<Unit>> = flow {
        firebaseController.logBreadcrumb(
            message = "Cache clear requested",
            attributes = mapOf("source" to "CacheRepositoryImpl"),
        )
        // Resolving and deleting cache directories can both throw — SecurityException from a
        // restricted profile, IO failures mid-delete. Those have to surface as Result.Error, or the
        // exception escapes the flow and the caller reports nothing at all.
        val result: Result<Unit> = runCatching {
            val cacheDirs: List<File> = buildList {
                add(context.cacheDir)
                add(context.codeCacheDir)
                context.externalCacheDir?.let(::add)
            }.distinct()

            cacheDirs.filterNot(deleteRecursively)
        }.fold(
            onSuccess = { failed ->
                if (failed.isEmpty()) {
                    Result.Success(Unit)
                } else {
                    Result.Error(Exception("Failed to clear cache"))
                }
            },
            onFailure = { throwable ->
                if (throwable is CancellationException) throw throwable
                Result.Error(throwable as? Exception ?: Exception(throwable))
            },
        )

        emit(result)
    }
}

