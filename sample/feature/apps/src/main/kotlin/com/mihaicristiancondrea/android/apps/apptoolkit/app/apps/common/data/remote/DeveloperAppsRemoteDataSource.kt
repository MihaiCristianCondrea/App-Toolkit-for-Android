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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppSummary

/** Network source for the developer-app catalogue and package details. */
interface DeveloperAppsRemoteDataSource {
    suspend fun fetchDeveloperApps(): List<AppSummary>

    suspend fun fetchAppDetails(packageName: String): AppDetails
}

enum class DeveloperAppsRemoteError {
    RequestTimeout,
    RateLimited,
    Redirect,
    Client,
    Server,
    NoInternet,
    Connection,
    Serialization,
    Unknown,
}

class DeveloperAppsRemoteException(
    val error: DeveloperAppsRemoteError,
    cause: Throwable? = null,
) : Exception(error.name, cause)
