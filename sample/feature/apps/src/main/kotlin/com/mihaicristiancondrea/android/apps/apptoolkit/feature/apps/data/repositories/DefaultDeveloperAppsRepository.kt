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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.local.DeveloperAppsLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.DeveloperAppsRemoteDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppSummary
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.result.runSuspendCatching
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions.storageCall
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions.networkCall

/**
 * Downloads the catalogue and keeps the last successful copy for offline use. Package names are
 * unique identities, so both copies keep one entry per package before keyed lazy layouts read
 * them.
 *
 * It needs no dispatcher: the remote calls suspend inside Ktor, and the local data source moves
 * its file work off the main thread itself.
 */
class DefaultDeveloperAppsRepository(
    private val remoteDataSource: DeveloperAppsRemoteDataSource,
    private val telemetryRepository: TelemetryRepository,
    private val localDataSource: DeveloperAppsLocalDataSource,
) : DeveloperAppsRepository {

    override suspend fun fetchDeveloperApps(): List<AppSummary> {
        telemetryRepository.logBreadcrumb(
            message = "Developer apps fetch",
        )
        val apps = networkCall { remoteDataSource.fetchDeveloperApps() }
            .distinctBy { it.packageName }
            .sortedBy { it.name.lowercase() }
        writeCache(apps)
        return apps
    }

    override suspend fun savedDeveloperApps(): List<AppSummary>? =
        storageCall { localDataSource.read() }?.distinctBy { it.packageName }

    override suspend fun fetchAppDetails(packageName: String): AppDetails {
        require(packageName.isNotBlank()) { "An app's details need its package name" }
        telemetryRepository.logBreadcrumb(
            message = "Developer app details fetch",
            attributes = mapOf("packageName" to packageName),
        )
        return networkCall { remoteDataSource.fetchAppDetails(packageName) }
    }

    /**
     * Saves [apps] for offline use. A failed write is reported but does not fail the fetch: the
     * fresh catalogue is still shown, only the next offline start falls back to an older copy.
     */
    private suspend fun writeCache(apps: List<AppSummary>) {
        runSuspendCatching { localDataSource.write(apps) }
            .onFailure { throwable ->
                telemetryRepository.recordNonFatal(
                    throwable = throwable,
                    attributes = mapOf("operation" to "writeDeveloperAppsCache"),
                )
            }
    }
}
