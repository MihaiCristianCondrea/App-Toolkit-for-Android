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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.local.FaqLocalDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.mappers.toFaqItems
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.FaqRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models.FaqQuestionDto
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.result.runSuspendCatching
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions.networkCall

/**
 * Implementation of [FaqRepository] that manages the retrieval of FAQ items
 * from both remote and local data sources.
 *
 * Prioritizes remote data from a specified catalog and product, falling back to the bundled local
 * questions when the remote fetch fails or yields nothing usable.
 *
 * Both sources are normalized before they are considered: entries are trimmed, blanks are dropped
 * and repeated ids collapse to their first occurrence. Normalizing here rather than downstream is
 * what makes the fallback correct, a remote catalog of nothing but blank rows now counts as empty
 * and falls through to the local questions instead of rendering blank rows.
 *
 * It needs no dispatcher: the remote calls suspend inside Ktor, and the local questions are string
 * resources, which the system already holds in memory.
 *
 * @property localDataSource The local data source for accessing cached or bundled FAQ questions.
 * @property remoteDataSource The remote data source for fetching FAQ catalogs and questions via network.
 * @property catalogUrl The URL of the remote catalog containing product information.
 * @property productId The identifier used to find the specific product within the catalog.
 */
class DefaultFaqRepository(
    private val localDataSource: FaqLocalDataSource,
    private val remoteDataSource: FaqRemoteDataSource,
    private val catalogUrl: String,
    private val productId: String,
    private val firebaseController: FirebaseController,
) : FaqRepository {

    override suspend fun getFaq(): List<FaqItem> {
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

        val remoteItems = remoteResult.getOrNull().orEmpty().normalize()
        if (remoteItems.isNotEmpty()) return remoteItems

        val localItems = localDataSource.loadLocalQuestions().normalize()
        if (localItems.isNotEmpty()) return localItems

        // Nothing to show: the remote failure is the reason, so it reaches the screen. A remote
        // catalog that loaded but had nothing for this product is not a failure, only empty.
        remoteResult.exceptionOrNull()?.let { failure -> throw failure }
        return emptyList()
    }

    // The catalog must load for there to be anything remote; a question source that fails only
    // drops its own questions, so the rest still show.
    private suspend fun fetchRemoteFaqItems(): List<FaqItem> {
        val catalog = networkCall { remoteDataSource.fetchCatalog(catalogUrl) }
        val product = catalog.products.firstOrNull { it.productId == productId || it.key == productId }
            ?: return emptyList()

        val questions: List<FaqQuestionDto> = product.questionSources.flatMap { source ->
            runSuspendCatching {
                networkCall { remoteDataSource.fetchQuestions(source.url) }
            }.getOrDefault(emptyList())
        }

        return questions.toFaqItems()
    }

    /**
     * Trims each entry, drops the ones left without a question or an answer, and keeps the first
     * of any repeated id.
     */
    private fun List<FaqItem>.normalize(): List<FaqItem> = asSequence()
        .map { faqItem ->
            faqItem.copy(
                question = faqItem.question.trim(),
                answer = faqItem.answer.trim(),
            )
        }
        .filter { it.question.isNotBlank() && it.answer.isNotBlank() }
        .distinctBy { it.id.value }
        .toList()
}
