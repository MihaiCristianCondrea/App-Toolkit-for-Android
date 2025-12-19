package com.d4rk.android.libs.apptoolkit.app.help.data

import android.util.Log
import com.d4rk.android.libs.apptoolkit.app.help.data.local.HelpLocalDataSource
import com.d4rk.android.libs.apptoolkit.app.help.data.mapper.toFaqItems
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.HelpRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.model.FaqCatalogDto
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.model.FaqQuestionDto
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.HelpRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.logging.FAQ_LOG_TAG
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DefaultHelpRepository(
    private val localDataSource: HelpLocalDataSource,
    private val remoteDataSource: HelpRemoteDataSource,
    private val catalogUrl: String,
    private val productId: String,
) : HelpRepository {

    override fun fetchFaq(): Flow<DataState<List<FaqItem>, Errors>> = flow {
        runCatching {
            val catalogResponse = remoteDataSource.fetchCatalog(catalogUrl)
            if (!catalogResponse.status.isSuccess()) {
                return@runCatching RemoteResult.Failed(mapHttpStatusToError(catalogResponse.status))
            }

            val catalog = catalogResponse.body<FaqCatalogDto>()
            val product = catalog.products.firstOrNull { it.productId == productId || it.key == productId }
                ?: return@runCatching RemoteResult.Success(emptyList())

            val questions: List<FaqQuestionDto> = product.questionSources.flatMap { source ->
                val resp = remoteDataSource.fetchQuestions(source.url)
                if (!resp.status.isSuccess()) {
                    Log.w(FAQ_LOG_TAG, "FAQ source failed: ${source.url} status=${resp.status}")
                    emptyList()
                } else {
                    runCatching { resp.body<List<FaqQuestionDto>>() }
                            .onFailure { t ->
                                if (t is CancellationException) throw t
                                Log.w(FAQ_LOG_TAG, "FAQ source parse failed: ${source.url}", t)
                            }
                            .getOrDefault(emptyList())
                }
            }

            RemoteResult.Success(questions.toFaqItems())
        }.onSuccess { remoteResult ->
            val remoteItems = (remoteResult as? RemoteResult.Success)?.items.orEmpty()

            if (remoteItems.isNotEmpty()) {
                emit(DataState.Success(remoteItems))
                return@flow
            }

            // Fallback local
            val localItems = localDataSource.loadLocalQuestions()
            if (localItems.isNotEmpty()) {
                Log.i(FAQ_LOG_TAG, "Remote empty/unavailable, using local FAQs")
                emit(DataState.Success(localItems))
            } else {
                val error = (remoteResult as? RemoteResult.Failed)?.error ?: Errors.UseCase.FAILED_TO_LOAD_FAQ
                emit(DataState.Error(error = error))
            }
        }.onFailure { throwable ->
            if (throwable is CancellationException) throw throwable

            Log.w(FAQ_LOG_TAG, "Remote FAQs failed, trying local fallback", throwable)

            val localItems = localDataSource.loadLocalQuestions()
            if (localItems.isNotEmpty()) {
                emit(DataState.Success(localItems))
            } else {
                emit(DataState.Error(error = mapThrowableToError(throwable)))
            }
        }
    }

    private fun mapHttpStatusToError(status: HttpStatusCode): Errors {
        return if (status == HttpStatusCode.RequestTimeout) {
            Errors.Network.REQUEST_TIMEOUT
        } else {
            Errors.UseCase.FAILED_TO_LOAD_FAQ
        }
    }

    private fun mapThrowableToError(t: Throwable): Errors {
        return when (t) {
            is SocketTimeoutException -> Errors.Network.REQUEST_TIMEOUT
            is IOException -> Errors.Network.NO_INTERNET
            is ClientRequestException ->
                if (t.response.status == HttpStatusCode.RequestTimeout) Errors.Network.REQUEST_TIMEOUT
                else Errors.UseCase.FAILED_TO_LOAD_FAQ

            is ServerResponseException -> Errors.UseCase.FAILED_TO_LOAD_FAQ
            else -> Errors.UseCase.FAILED_TO_LOAD_FAQ
        }
    }

    private sealed interface RemoteResult {
        data class Success(val items: List<FaqItem>) : RemoteResult
        data class Failed(val error: Errors) : RemoteResult
    }
}
