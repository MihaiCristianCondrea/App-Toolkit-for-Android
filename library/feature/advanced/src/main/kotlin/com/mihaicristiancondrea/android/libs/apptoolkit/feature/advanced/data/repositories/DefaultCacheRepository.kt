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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions.storageCall
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Clears application cache directories on the IO dispatcher, since the recursive delete blocks.
 * An incomplete deletion is logged here because callers cannot identify the failed directories.
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

    override suspend fun clearCache() {
        telemetryRepository.logBreadcrumb(
            message = "Cache clear requested",
            attributes = mapOf("source" to "DefaultCacheRepository"),
        )
        val failed: List<File> = withContext(dispatchers.io) {
            storageCall { deleteCacheDirectories() }
        }
        if (failed.isNotEmpty()) {
            telemetryRepository.logBreadcrumb(
                message = "Cache clear incomplete",
                attributes = mapOf("failedDirectories" to failed.size.toString()),
            )
            throw StorageException(reason = StorageException.Reason.FAILED)
        }
    }

    /**
     * Deletes each cache directory and returns the ones that were not fully deleted. A restricted
     * profile can refuse access to a directory, which is reported as [StorageException.Reason.UNAVAILABLE].
     */
    private fun deleteCacheDirectories(): List<File> = try {
        buildList {
            add(context.cacheDir)
            add(context.codeCacheDir)
            context.externalCacheDir?.let(::add)
        }.distinct().filterNot(deleteRecursively)
    } catch (security: SecurityException) {
        throw StorageException(reason = StorageException.Reason.UNAVAILABLE, cause = security)
    }
}
