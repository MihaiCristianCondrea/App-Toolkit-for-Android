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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.data.repositories

import android.content.Context
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.StandardDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

/**
 * Clears application cache directories on the IO dispatcher. Directory lookup and deletion
 * failures become [DataState.Error]; cancellation propagates. Incomplete deletion is reported
 * here because callers cannot identify the failed directories.
 *
 * @param deleteRecursively Returns `true` when the entire directory was deleted, or `false` for
 * an incomplete deletion.
 */
class DefaultCacheRepository(
    private val context: Context,
    private val telemetryRepository: TelemetryRepository,
    private val deleteRecursively: (File) -> Boolean = File::deleteRecursively,
    private val dispatchers: DispatcherProvider = StandardDispatchers(),
) : CacheRepository {

    override fun clearCache(): Flow<DataState<Unit, Errors.Database>> = flow {
        telemetryRepository.logBreadcrumb(
            message = "Cache clear requested",
            attributes = mapOf("source" to "DefaultCacheRepository"),
        )
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
                    telemetryRepository.logBreadcrumb(
                        message = "Cache clear incomplete",
                        attributes = mapOf("failedDirectories" to failed.size.toString()),
                    )
                    DataState.Error(error = Errors.Database.DATABASE_OPERATION_FAILED)
                }
            },
            onFailure = { throwable ->
                if (throwable is CancellationException) throw throwable
                telemetryRepository.recordNonFatal(throwable = throwable)
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
    }.flowOn(dispatchers.io)
}

