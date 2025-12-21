package com.d4rk.android.libs.apptoolkit.app.help.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class HelpRemoteDataSource(
    private val client: HttpClient,
) {
    suspend fun fetchCatalog(url: String): HttpResponse = client.get(url)
    suspend fun fetchQuestions(url: String): HttpResponse = client.get(url)
}
