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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.DeveloperAppsRemoteError
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.DeveloperAppsRemoteException
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppSummary
import com.mihaicristiancondrea.android.apps.apptoolkit.core.common.domain.models.network.AppErrors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.result.runSuspendCatching
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.Errors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.cancellation.CancellationException

class DefaultDeveloperAppsRepository(
    private val remoteDataSource: DeveloperAppsRemoteDataSource,
    private val firebaseController: FirebaseController,
    private val localDataSource: DeveloperAppsLocalDataSource,
) : DeveloperAppsRepository {

    override fun fetchDeveloperApps(): Flow<DataState<List<AppSummary>, AppErrors>> = flow {
        firebaseController.logBreadcrumb(
            message = "Developer apps fetch",
        )
        val result: Result<DataState<List<AppSummary>, AppErrors>> = runSuspendCatching {
            val apps = remoteDataSource.fetchDeveloperApps()
                .sortedBy { it.name.lowercase() }
            localDataSource.write(apps)

            DataState.Success(data = apps)
        }
        val cachedApps = if (result.isFailure) localDataSource.read() else null
        val state: DataState<List<AppSummary>, AppErrors> = result.fold(
            onSuccess = { state -> state },
            onFailure = { throwable ->
                DataState.Error(
                    data = cachedApps,
                    error = mapThrowableToError(
                        throwable = throwable,
                        default = AppErrors.UseCase.FAILED_TO_LOAD_APPS,
                    ),
                )
            },
        )
        emit(state)
    }

    override fun fetchAppDetails(
        packageName: String,
    ): Flow<DataState<AppDetails, AppErrors>> = flow {
        if (packageName.isBlank()) {
            emit(DataState.Error(error = AppErrors.UseCase.FAILED_TO_LOAD_APP_DETAILS))
            return@flow
        }
        firebaseController.logBreadcrumb(
            message = "Developer app details fetch",
            attributes = mapOf("packageName" to packageName),
        )
        val result: DataState<AppDetails, AppErrors> = runSuspendCatching {
            DataState.Success<AppDetails, AppErrors>(
                data = remoteDataSource.fetchAppDetails(packageName),
            )
        }.fold(
            onSuccess = { state -> state },
            onFailure = { throwable ->
                DataState.Error(
                    error = mapThrowableToError(
                        throwable = throwable,
                        default = AppErrors.UseCase.FAILED_TO_LOAD_APP_DETAILS,
                    ),
                )
            },
        )
        emit(result)
    }

    private fun mapThrowableToError(
        throwable: Throwable,
        default: AppErrors.UseCase,
    ): AppErrors {
        return when (throwable) {
            is CancellationException -> throw throwable
            is DeveloperAppsRemoteException -> throwable.error.toAppError()
            else -> default
        }
    }

    private fun DeveloperAppsRemoteError.toAppError(): AppErrors.Common = AppErrors.Common(
        when (this) {
            DeveloperAppsRemoteError.RequestTimeout -> Errors.Network.REQUEST_TIMEOUT
            DeveloperAppsRemoteError.RateLimited -> Errors.Network.RATE_LIMITED
            DeveloperAppsRemoteError.Redirect -> Errors.Network.HTTP_REDIRECT
            DeveloperAppsRemoteError.Client -> Errors.Network.HTTP_CLIENT_ERROR
            DeveloperAppsRemoteError.Server -> Errors.Network.HTTP_SERVER_ERROR
            DeveloperAppsRemoteError.NoInternet -> Errors.Network.NO_INTERNET
            DeveloperAppsRemoteError.Connection -> Errors.Network.CONNECTION_ERROR
            DeveloperAppsRemoteError.Serialization -> Errors.Network.SERIALIZATION
            DeveloperAppsRemoteError.Unknown -> Errors.Network.UNKNOWN
        }
    )
}
