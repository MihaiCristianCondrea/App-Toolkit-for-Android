package com.d4rk.android.libs.apptoolkit.app.help.data.remote

import android.util.Log
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.model.FaqCatalogDto
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.model.FaqProductDto
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.model.FaqQuestionDto
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.model.FaqQuestionSourceDto
import com.d4rk.android.libs.apptoolkit.core.logging.FAQ_LOG_TAG
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CancellationException

class HelpRemoteDataSource(
    private val client: HttpClient,
) {
    suspend fun fetchCatalog(url: String): HttpResponse = client.get(url)
    suspend fun fetchQuestions(url: String): HttpResponse = client.get(url)
}
