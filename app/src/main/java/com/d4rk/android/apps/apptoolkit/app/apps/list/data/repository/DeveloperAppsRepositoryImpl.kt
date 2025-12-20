package com.d4rk.android.apps.apptoolkit.app.apps.list.data.repository

import com.d4rk.android.apps.apptoolkit.app.apps.list.data.model.api.ApiResponse
import com.d4rk.android.apps.apptoolkit.app.apps.list.data.model.api.toDomain
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.repository.DeveloperAppsRepository
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiPaths
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.SocketTimeoutException

class DeveloperAppsRepositoryImpl(
    private val client: HttpClient,
    private val baseUrl: String,
) : DeveloperAppsRepository {

    override fun fetchDeveloperApps(): Flow<DataState<List<AppInfo>, Errors>> = flow {
        val url = "$baseUrl${ApiPaths.DEVELOPER_APPS_API}"
        runCatching {
            client.get(url)
        }.onSuccess { httpResponse ->
            if (!httpResponse.status.isSuccess()) {
                val error = if (httpResponse.status == HttpStatusCode.RequestTimeout) {
                    Errors.Network.REQUEST_TIMEOUT
                } else {
                    Errors.UseCase.FAILED_TO_LOAD_APPS
                }
                emit(DataState.Error(error = error))
                return@flow
            }

            runCatching { httpResponse.body<ApiResponse>() }
                .onSuccess { apiResponse ->
                    emit(
                        DataState.Success(
                            apiResponse.data.apps
                                .map { it.toDomain() }
                                .sortedBy { it.name.lowercase() }
                        )
                    )
                }
                .onFailure { throwable ->
                    if (throwable is CancellationException) {
                        throw throwable
                    }
                    emit(DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_APPS))
                }
        }.onFailure { throwable ->
            if (throwable is CancellationException) {
                throw throwable
            }
            val error = when (throwable) {
                is SocketTimeoutException -> Errors.Network.REQUEST_TIMEOUT
                is IOException -> Errors.Network.NO_INTERNET
                is ClientRequestException ->
                    if (throwable.response.status == HttpStatusCode.RequestTimeout) {
                        Errors.Network.REQUEST_TIMEOUT
                    } else {
                        Errors.UseCase.FAILED_TO_LOAD_APPS
                    }

                else -> Errors.UseCase.FAILED_TO_LOAD_APPS
            }
            emit(DataState.Error(error = error))
        }
    }
}
