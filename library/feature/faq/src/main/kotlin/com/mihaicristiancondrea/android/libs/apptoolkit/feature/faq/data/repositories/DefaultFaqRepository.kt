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

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.transformers.normalizeFaqItems
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.result.runSuspendCatching
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.networkCall
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.local.FaqLocalDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.FaqRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.mappers.toFaqItems
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models.FaqQuestionDto

/** Uses bundled questions when the remote catalog fails or has no usable entries. */
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

    private suspend fun fetchRemoteFaqItems(): List<FaqItem> {
        val catalog = networkCall { remoteDataSource.fetchCatalog(url = catalogUrl) }
        val product = catalog.products.firstOrNull { it.productId == productId || it.key == productId }
            ?: return emptyList()

        val questions: List<FaqQuestionDto> = product.questionSources.flatMap { source ->
            runSuspendCatching {
                networkCall { remoteDataSource.fetchQuestions(url = source.url) }
            }.getOrDefault(defaultValue = emptyList())
        }

        return questions.toFaqItems()
    }
}
