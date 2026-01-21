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
