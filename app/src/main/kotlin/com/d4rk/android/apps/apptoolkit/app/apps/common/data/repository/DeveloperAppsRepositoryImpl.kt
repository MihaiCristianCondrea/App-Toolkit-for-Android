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

package com.d4rk.android.apps.apptoolkit.app.apps.common.data.repository

import com.d4rk.android.apps.apptoolkit.app.apps.common.data.mapper.toDomain
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.ApiResponseDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.DeveloperAppsRepository
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.result.runSuspendCatching
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

class DeveloperAppsRepositoryImpl(
    private val client: HttpClient,
    private val baseUrl: String,
    private val firebaseController: FirebaseController,
) : DeveloperAppsRepository {

    override fun fetchDeveloperApps(): Flow<DataState<List<AppInfo>, AppErrors>> = flow {
        firebaseController.logBreadcrumb(
            message = "Developer apps fetch",
            attributes = mapOf("baseUrl" to baseUrl),
        )
        runSuspendCatching {
            client.get(baseUrl)
        }.onSuccess { response ->
            if (!response.status.isSuccess()) {
                emit(DataState.Error(error = mapHttpStatusToError(response.status)))
                return@flow
            }

            val dto = response.body<ApiResponseDto>()
            val apps = dto.data.apps
                .map { it.toDomain() }
                .sortedBy { it.name.lowercase() }

            emit(DataState.Success(apps))
        }.onFailure { throwable ->
            if (throwable is CancellationException) throw throwable
            emit(DataState.Error(error = mapThrowableToError(throwable)))
        }
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

    private fun mapThrowableToError(t: Throwable): AppErrors {
        return when (t) {
            is CancellationException -> throw t
            is HttpRequestTimeoutException, is SocketTimeoutException ->
                AppErrors.Common(Errors.Network.REQUEST_TIMEOUT)

            is UnknownHostException -> AppErrors.Common(Errors.Network.NO_INTERNET)
            is IOException -> AppErrors.Common(Errors.Network.CONNECTION_ERROR)
            is SerializationException -> AppErrors.Common(Errors.Network.SERIALIZATION)
            is RedirectResponseException -> mapHttpStatusToError(t.response.status)
            is ClientRequestException -> mapHttpStatusToError(t.response.status)
            is ServerResponseException -> mapHttpStatusToError(t.response.status)
            else -> AppErrors.UseCase.FAILED_TO_LOAD_APPS
        }
    }
}
