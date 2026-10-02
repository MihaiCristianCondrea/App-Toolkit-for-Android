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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppSummary

/**
 * Network source for the developer-app catalogue and package details.
 *
 * A response with an error status throws its `NetworkException`. Other failures are the HTTP
 * client's own, which the repository translates with `networkCall`.
 */
interface DeveloperAppsRemoteDataSource {
    suspend fun fetchDeveloperApps(): List<AppSummary>

    suspend fun fetchAppDetails(packageName: String): AppDetails
}
