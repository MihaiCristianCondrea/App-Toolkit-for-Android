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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.mappers.toDomain
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.models.AppDetailsResponseDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.models.AppsListResponseDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppSummary
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repositories.DeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.core.domain.models.network.AppErrors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repository.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.api.ApiHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.result.runSuspendCatching
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

class DefaultDeveloperAppsRepository(
    private val client: HttpClient,
    private val baseUrl: String,
    private val firebaseController: FirebaseController,
) : DeveloperAppsRepository {

    override fun fetchDeveloperApps(): Flow<DataState<List<AppSummary>, AppErrors>> = flow {
        val requestUrl = ApiHost.appsUrl(baseUrl)
        firebaseController.logBreadcrumb(
            message = "Developer apps fetch",
            attributes = mapOf("url" to requestUrl),
        )
        val result: DataState<List<AppSummary>, AppErrors> = runSuspendCatching {
            val response = client.get(requestUrl)
            if (!response.status.isSuccess()) {
                return@runSuspendCatching DataState.Error<List<AppSummary>, AppErrors>(
                    error = mapHttpStatusToError(response.status),
                )
            }

            val dto = response.body<AppsListResponseDto>()
            val apps = dto.data.apps
                .map { it.toDomain() }
                .sortedBy { it.name.lowercase() }

            DataState.Success<List<AppSummary>, AppErrors>(data = apps)
        }.fold(
            onSuccess = { state -> state },
            onFailure = { throwable ->
                DataState.Error<List<AppSummary>, AppErrors>(
                    error = mapThrowableToError(
                        throwable = throwable,
                        default = AppErrors.UseCase.FAILED_TO_LOAD_APPS,
                    ),
                )
            },
        )
        emit(result)
    }

    override fun fetchAppDetails(
        packageName: String,
    ): Flow<DataState<AppDetails, AppErrors>> = flow {
        if (packageName.isBlank()) {
            emit(DataState.Error(error = AppErrors.UseCase.FAILED_TO_LOAD_APP_DETAILS))
            return@flow
        }
        val requestUrl = ApiHost.appDetailsUrl(packageName = packageName, baseUrl = baseUrl)
        firebaseController.logBreadcrumb(
            message = "Developer app details fetch",
            attributes = mapOf("packageName" to packageName),
        )
        val result: DataState<AppDetails, AppErrors> = runSuspendCatching {
            val response = client.get(requestUrl)
            if (!response.status.isSuccess()) {
                return@runSuspendCatching DataState.Error<AppDetails, AppErrors>(
                    error = mapHttpStatusToError(response.status),
                )
            }
            DataState.Success<AppDetails, AppErrors>(
                data = response.body<AppDetailsResponseDto>().data.app.toDomain(),
            )
        }.fold(
            onSuccess = { state -> state },
            onFailure = { throwable ->
                DataState.Error<AppDetails, AppErrors>(
                    error = mapThrowableToError(
                        throwable = throwable,
                        default = AppErrors.UseCase.FAILED_TO_LOAD_APP_DETAILS,
                    ),
                )
            },
        )
        emit(result)
    }

    private fun mapHttpStatusToError(status: HttpStatusCode): AppErrors {
        return when {
            status == HttpStatusCode.RequestTimeout -> AppErrors.Common(Errors.Network.REQUEST_TIMEOUT)
            status == HttpStatusCode.TooManyRequests -> AppErrors.Common(Errors.Network.RATE_LIMITED)
            status.value in 300..399 -> AppErrors.Common(Errors.Network.HTTP_REDIRECT)
            status.value in 400..499 -> AppErrors.Common(Errors.Network.HTTP_CLIENT_ERROR)
            status.value >= 500 -> AppErrors.Common(Errors.Network.HTTP_SERVER_ERROR)
            else -> AppErrors.Common(Errors.Network.UNKNOWN)
        }
    }

    private fun mapThrowableToError(
        throwable: Throwable,
        default: AppErrors.UseCase,
    ): AppErrors {
        return when (throwable) {
            is CancellationException -> throw throwable
            is HttpRequestTimeoutException, is SocketTimeoutException ->
                AppErrors.Common(Errors.Network.REQUEST_TIMEOUT)

            is UnknownHostException -> AppErrors.Common(Errors.Network.NO_INTERNET)
            is IOException -> AppErrors.Common(Errors.Network.CONNECTION_ERROR)
            is SerializationException -> AppErrors.Common(Errors.Network.SERIALIZATION)
            is RedirectResponseException -> mapHttpStatusToError(throwable.response.status)
            is ClientRequestException -> mapHttpStatusToError(throwable.response.status)
            is ServerResponseException -> mapHttpStatusToError(throwable.response.status)
            else -> default
        }
    }
}
