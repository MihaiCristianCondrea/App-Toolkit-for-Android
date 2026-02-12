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

package com.d4rk.android.libs.apptoolkit.app.help.data.remote

import com.d4rk.android.libs.apptoolkit.app.help.data.remote.model.FaqCatalogDto
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.model.FaqQuestionDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * Remote data source for fetching help and FAQ related data from a network API.
 *
 * This class uses an [HttpClient] to perform network requests and retrieve
 * data in the form of DTOs (Data Transfer Objects).
 *
 * @property client The Ktor [HttpClient] used to perform network operations.
 */
class HelpRemoteDataSource(private val client: HttpClient) {
    suspend fun fetchCatalog(url: String): FaqCatalogDto = client.get(url).body()
    suspend fun fetchQuestions(url: String): List<FaqQuestionDto> = client.get(url).body()
}
