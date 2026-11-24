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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models.FaqCatalogDto
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models.FaqQuestionDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/** Reads the FAQ catalog and question arrays using the host's JSON-enabled [HttpClient]. */
class FaqRemoteDataSource(private val client: HttpClient) {
    /** Decodes the catalog document at [url]. Request and decoding failures propagate to the caller. */
    suspend fun fetchCatalog(url: String): FaqCatalogDto = client.get(urlString = url).body()
    /** Decodes the question array at [url]. Request and decoding failures propagate to the caller. */
    suspend fun fetchQuestions(url: String): List<FaqQuestionDto> = client.get(urlString = url).body()
}

