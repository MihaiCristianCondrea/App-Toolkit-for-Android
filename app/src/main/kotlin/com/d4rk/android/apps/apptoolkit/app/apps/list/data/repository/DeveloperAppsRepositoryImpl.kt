package com.d4rk.android.apps.apptoolkit.app.apps.list.data.repository

import com.d4rk.android.apps.apptoolkit.app.apps.list.data.mapper.toDomain
import com.d4rk.android.apps.apptoolkit.app.apps.list.data.remote.model.ApiResponseDto
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.repository.DeveloperAppsRepository
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class DeveloperAppsRepositoryImpl(
    private val client: HttpClient,
    private val baseUrl: String,
) : DeveloperAppsRepository {

    override fun fetchDeveloperApps(): Flow<DataState<List<AppInfo>, AppErrors>> = flow {
        runCatching {
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
        return if (status == HttpStatusCode.RequestTimeout) {
            AppErrors.Network.REQUEST_TIMEOUT
        } else {
            AppErrors.UseCase.FAILED_TO_LOAD_APPS
        }
    }

    private fun mapThrowableToError(t: Throwable): AppErrors {
        return when (t) {
            is SocketTimeoutException -> AppErrors.Network.REQUEST_TIMEOUT
            is IOException -> AppErrors.Network.NO_INTERNET
            is ClientRequestException -> mapHttpStatusToError(t.response.status)
            is ServerResponseException -> AppErrors.UseCase.FAILED_TO_LOAD_APPS
            else -> AppErrors.UseCase.FAILED_TO_LOAD_APPS
        }
    }
}
