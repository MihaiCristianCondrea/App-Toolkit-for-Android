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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.mappers.toDomain
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppDetailsResponseDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppsListResponseDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppSummary
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.api.ApiHost
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.serialization.SerializationException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

class DefaultDeveloperAppsRemoteDataSource(
    private val client: HttpClient,
    private val baseUrl: String,
) : DeveloperAppsRemoteDataSource {

    override suspend fun fetchDeveloperApps(): List<AppSummary> = remoteCall {
        val response = client.get(ApiHost.appsUrl(baseUrl))
        if (!response.status.isSuccess()) throw response.status.toRemoteException()
        response.body<AppsListResponseDto>().data.apps.map { it.toDomain() }
    }

    override suspend fun fetchAppDetails(packageName: String): AppDetails = remoteCall {
        val response =
            client.get(ApiHost.appDetailsUrl(packageName = packageName, baseUrl = baseUrl))
        if (!response.status.isSuccess()) throw response.status.toRemoteException()
        response.body<AppDetailsResponseDto>().data.app.toDomain()
    }

    private suspend fun <T> remoteCall(block: suspend () -> T): T = try {
        block()
    } catch (throwable: Throwable) {
        throw throwable.toRemoteException()
    }
}

private fun Throwable.toRemoteException(): DeveloperAppsRemoteException = when (this) {
    is DeveloperAppsRemoteException -> this
    is CancellationException -> throw this
    is HttpRequestTimeoutException, is SocketTimeoutException ->
        DeveloperAppsRemoteException(DeveloperAppsRemoteError.RequestTimeout, this)

    is UnknownHostException -> DeveloperAppsRemoteException(
        DeveloperAppsRemoteError.NoInternet,
        this
    )

    is SerializationException -> DeveloperAppsRemoteException(
        DeveloperAppsRemoteError.Serialization,
        this
    )

    is RedirectResponseException -> response.status.toRemoteException(this)
    is ClientRequestException -> response.status.toRemoteException(this)
    is ServerResponseException -> response.status.toRemoteException(this)
    is IOException -> DeveloperAppsRemoteException(DeveloperAppsRemoteError.Connection, this)
    else -> DeveloperAppsRemoteException(DeveloperAppsRemoteError.Unknown, this)
}

private fun HttpStatusCode.toRemoteException(cause: Throwable? = null): DeveloperAppsRemoteException =
    DeveloperAppsRemoteException(
        error = when {
            this == HttpStatusCode.RequestTimeout -> DeveloperAppsRemoteError.RequestTimeout
            this == HttpStatusCode.TooManyRequests -> DeveloperAppsRemoteError.RateLimited
            value in 300..399 -> DeveloperAppsRemoteError.Redirect
            value in 400..499 -> DeveloperAppsRemoteError.Client
            value >= 500 -> DeveloperAppsRemoteError.Server
            else -> DeveloperAppsRemoteError.Unknown
        },
        cause = cause,
    )
