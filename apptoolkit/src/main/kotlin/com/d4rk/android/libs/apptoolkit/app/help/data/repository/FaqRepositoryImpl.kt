package com.d4rk.android.libs.apptoolkit.app.help.data.repository

import com.d4rk.android.libs.apptoolkit.app.help.data.local.HelpLocalDataSource
import com.d4rk.android.libs.apptoolkit.app.help.data.mapper.toFaqItems
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.HelpRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.model.FaqQuestionDto
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.FaqRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.toError
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.result.runSuspendCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FaqRepositoryImpl(
    private val localDataSource: HelpLocalDataSource,
    private val remoteDataSource: HelpRemoteDataSource,
    private val catalogUrl: String,
    private val productId: String,
) : FaqRepository {

    override fun fetchFaq(): Flow<DataState<List<FaqItem>, Errors>> = flow {
        val remoteResult: Result<List<FaqItem>> = runSuspendCatching {
            fetchRemoteFaqItems()
        }

        val remoteItems = remoteResult.getOrNull().orEmpty()
        if (remoteItems.isNotEmpty()) {
            emit(DataState.Success(remoteItems))
            return@flow
        }

        val localItems = localDataSource.loadLocalQuestions()
        if (localItems.isNotEmpty()) {
            emit(DataState.Success(localItems))
            return@flow
        }

        val error =
            remoteResult.exceptionOrNull()?.toError(default = Errors.UseCase.FAILED_TO_LOAD_FAQ)
                ?: Errors.UseCase.FAILED_TO_LOAD_FAQ
        emit(DataState.Error(error = error))
    }

    private suspend fun fetchRemoteFaqItems(): List<FaqItem> {
        val product =
            remoteDataSource.fetchCatalog(catalogUrl).products.firstOrNull { it.productId == productId || it.key == productId }
                ?: return emptyList()

        val questions: List<FaqQuestionDto> = product.questionSources.flatMap { source ->
            runSuspendCatching {
                remoteDataSource.fetchQuestions(
                    source.url
                )
            }.getOrDefault(emptyList())
        }

        return questions.toFaqItems()
    }
}
