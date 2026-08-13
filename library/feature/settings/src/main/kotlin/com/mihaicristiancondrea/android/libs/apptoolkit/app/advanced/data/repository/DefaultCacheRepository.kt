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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repository.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.Errors
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
class DefaultCacheRepository(
    private val context: Context,
    private val firebaseController: FirebaseController,
    /**
     * Seam for the delete itself. Without it the failure branch is unreachable from a test: an
     * empty temp directory always deletes cleanly, so the error path shipped uncovered.
     */
    private val deleteRecursively: (File) -> Boolean = File::deleteRecursively,
) : CacheRepository {

    override fun clearCache(): Flow<DataState<Unit, Errors.Database>> = flow {
        firebaseController.logBreadcrumb(
            message = "Cache clear requested",
            attributes = mapOf("source" to "DefaultCacheRepository"),
        )
        // Resolving and deleting cache directories can both throw — SecurityException from a
        // restricted profile, IO failures mid-delete. Those have to surface as DataState.Error, or
        // the exception escapes the flow and the caller reports nothing at all.
        val state: DataState<Unit, Errors.Database> = runCatching {
            val cacheDirs: List<File> = buildList {
                add(context.cacheDir)
                add(context.codeCacheDir)
                context.externalCacheDir?.let(::add)
            }.distinct()

            cacheDirs.filterNot(deleteRecursively)
        }.fold(
            onSuccess = { failed ->
                if (failed.isEmpty()) {
                    DataState.Success(Unit)
                } else {
                    // Named here rather than by the ViewModel: which directory refused to go is
                    // something only this class can see, and the caller cannot re-derive it.
                    firebaseController.logBreadcrumb(
                        message = "Cache clear incomplete",
                        attributes = mapOf("failedDirectories" to failed.size.toString()),
                    )
                    DataState.Error(error = Errors.Database.DATABASE_OPERATION_FAILED)
                }
            },
            onFailure = { throwable ->
                if (throwable is CancellationException) throw throwable
                firebaseController.recordNonFatal(throwable = throwable)
                DataState.Error(
                    error = if (throwable is SecurityException) {
                        Errors.Database.DATABASE_CANT_OPEN
                    } else {
                        Errors.Database.DATABASE_OPERATION_FAILED
                    },
                )
            },
        )

        emit(state)
    }
}

