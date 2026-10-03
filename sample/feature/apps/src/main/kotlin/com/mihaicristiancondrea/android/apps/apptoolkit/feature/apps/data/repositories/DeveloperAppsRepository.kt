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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppSummary
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException

/**
 * The developer's app catalogue and each app's details. Every call is safe from the main thread.
 */
interface DeveloperAppsRepository {

    /**
     * Downloads the catalogue, saves it for [savedDeveloperApps], and returns it sorted by name with
     * one entry per package. An empty list means the catalogue has no apps.
     *
     * @throws NetworkException when the catalogue could not be downloaded.
     */
    suspend fun fetchDeveloperApps(): List<AppSummary>

    /**
     * The catalogue saved by the last successful [fetchDeveloperApps], without touching the
     * network, or null when nothing has been saved yet.
     *
     * @throws StorageException when the saved catalogue could not be read.
     */
    suspend fun savedDeveloperApps(): List<AppSummary>?

    /**
     * Downloads the full metadata document for [packageName].
     *
     * @throws IllegalArgumentException when [packageName] is blank.
     * @throws NetworkException when the document could not be downloaded.
     */
    suspend fun fetchAppDetails(packageName: String): AppDetails
}
