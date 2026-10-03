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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.repositories.DeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppSummary

/**
 * In-memory [DeveloperAppsRepository]. Each call throws its failure when one is set, so a test can
 * fail the download, the saved catalogue or the details, and clear the failure to retry.
 */
class FakeDeveloperAppsRepository(
    var apps: List<AppSummary> = emptyList(),
    var fetchFailure: Throwable? = null,
    var savedApps: List<AppSummary>? = null,
    var savedAppsFailure: Throwable? = null,
    var detailsFailure: Throwable? = null,
) : DeveloperAppsRepository {

    var fetchCount: Int = 0
        private set

    override suspend fun fetchDeveloperApps(): List<AppSummary> {
        fetchCount++
        fetchFailure?.let { failure -> throw failure }
        return apps
    }

    override suspend fun savedDeveloperApps(): List<AppSummary>? {
        savedAppsFailure?.let { failure -> throw failure }
        return savedApps
    }

    override suspend fun fetchAppDetails(packageName: String): AppDetails {
        detailsFailure?.let { failure -> throw failure }
        val app = requireNotNull(apps.firstOrNull { it.packageName == packageName }) {
            "No app with package $packageName"
        }
        return AppDetails(
            name = app.name,
            packageName = app.packageName,
            iconUrl = app.iconUrl,
            description = app.shortDescription,
            shortDescription = app.shortDescription,
            category = app.category,
        )
    }
}
