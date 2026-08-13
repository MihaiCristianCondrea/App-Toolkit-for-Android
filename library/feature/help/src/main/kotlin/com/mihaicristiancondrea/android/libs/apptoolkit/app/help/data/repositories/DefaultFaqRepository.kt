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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.help.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.data.local.HelpLocalDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.data.mappers.toFaqItems
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.data.remote.HelpRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.data.remote.models.FaqQuestionDto
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.domain.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.result.runSuspendCatching
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.remote.extensions.toError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.Errors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementation of [FaqRepository] that manages the retrieval of FAQ items
 * from both remote and local data sources.
 *
 * This repositories prioritizes remote data from a specified catalog and product,
 * falling back to local data if the remote fetch fails or returns no results.
 *
 * @property localDataSource The local data source for accessing cached or bundled FAQ questions.
 * @property remoteDataSource The remote data source for fetching FAQ catalogs and questions via network.
 * @property catalogUrl The URL of the remote catalog containing product information.
 * @property productId The identifier used to find the specific product within the catalog.
 */
class DefaultFaqRepository(
    private val localDataSource: HelpLocalDataSource,
    private val remoteDataSource: HelpRemoteDataSource,
    private val catalogUrl: String,
    private val productId: String,
    private val firebaseController: FirebaseController,
) : FaqRepository {

    override fun fetchFaq(): Flow<DataState<List<FaqItem>, Errors>> = flow {
        firebaseController.logBreadcrumb(
            message = "FAQ repositories fetch",
            attributes = mapOf(
                "catalogUrl" to catalogUrl,
                "productId" to productId,
            ),
        )
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
