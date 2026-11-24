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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.result.runSuspendCatching
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.networkCall
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.local.FaqLocalDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.FaqRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.mappers.toFaqItems
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models.FaqQuestionDto
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.transformers.normalizeFaqItems

/**
 * Resolves the host product from the remote catalog and normalizes both sources before selection.
 *
 * @param catalogUrl Full URL for the selected catalog environment.
 * @param productId Matches the product ID or alternate key in the catalog.
 */
class DefaultFaqRepository(
    private val localDataSource: FaqLocalDataSource,
    private val remoteDataSource: FaqRemoteDataSource,
    private val catalogUrl: String,
    private val productId: String,
    private val diagnostics: DiagnosticsReporter,
) : FaqRepository {

    override suspend fun getFaq(): List<FaqItem> {
        diagnostics.logBreadcrumb(
            message = "FAQ repositories fetch",
            attributes = emptyMap(),
        )
        val remoteResult: Result<List<FaqItem>> = runSuspendCatching {
            fetchRemoteFaqItems()
        }

        val remoteItems = remoteResult.getOrNull().orEmpty().normalizeFaqItems()
        if (remoteItems.isNotEmpty()) return remoteItems

        val localItems = localDataSource.loadLocalQuestions().normalizeFaqItems()
        if (localItems.isNotEmpty()) return localItems

        remoteResult.exceptionOrNull()?.let { failure -> throw failure }
        return emptyList()
    }

    /**
     * Reads the host product's question sources and keeps results from sources that succeed.
     * Propagates the first source failure when every configured source fails.
     */
    private suspend fun fetchRemoteFaqItems(): List<FaqItem> {
        val catalog = networkCall { remoteDataSource.fetchCatalog(url = catalogUrl) }
        val product = catalog.products.firstOrNull { it.productId == productId || it.key == productId }
            ?: return emptyList()

        val questions = mutableListOf<FaqQuestionDto>()
        var succeededSourceCount = 0
        var firstFailure: Throwable? = null

        for (source in product.questionSources) {
            val result = runSuspendCatching {
                networkCall { remoteDataSource.fetchQuestions(url = source.url) }
            }
            result.fold(
                onSuccess = { fetched ->
                    succeededSourceCount += 1
                    questions.addAll(elements = fetched)
                },
                onFailure = { failure ->
                    if (firstFailure == null) {
                        firstFailure = failure
                    }
                },
            )
        }

        if (succeededSourceCount == 0 && firstFailure != null) {
            throw firstFailure
        }

        return questions.toFaqItems()
    }
}
