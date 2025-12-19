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
import kotlinx.coroutines.CancellationException

class HelpRemoteDataSource(
    private val client: HttpClient,
) {

    suspend fun fetchFaqQuestions(
        catalogUrl: String,
        productId: String,
    ): List<FaqQuestionDto> {
        return try {
            Log.d(FAQ_LOG_TAG, "Fetching FAQ catalog from $catalogUrl for productId=$productId")
            val catalog = client.get(catalogUrl).body<FaqCatalogDto>()

            Log.d(
                FAQ_LOG_TAG,
                "Loaded FAQ catalog schemaVersion=${catalog.schemaVersion} products=${catalog.products.size}",
            )

            val product = catalog.findProduct(productId)
            if (product == null) {
                Log.w(FAQ_LOG_TAG, "No FAQ catalog entry for productId=$productId")
                return emptyList()
            }

            Log.d(
                FAQ_LOG_TAG,
                "Found ${product.questionSources.size} question sources for ${product.productId}",
            )

            product.questionSources.flatMap { source ->
                loadQuestionsFromSource(source)
            }
        } catch (error: Throwable) {
            if (error is CancellationException) throw error
            Log.e(FAQ_LOG_TAG, "Failed to load FAQs from remote catalog", error)
            emptyList()
        }
    }

    private suspend fun loadQuestionsFromSource(source: FaqQuestionSourceDto): List<FaqQuestionDto> {
        return runCatching {
            val questions = client.get(source.url).body<List<FaqQuestionDto>>()
            Log.d(
                FAQ_LOG_TAG,
                "Fetched ${questions.size} questions from ${source.url} [${source.category}]",
            )
            questions
        }.onFailure { throwable ->
            if (throwable is CancellationException) throw throwable
            Log.w(FAQ_LOG_TAG, "Failed to fetch questions from ${source.url}", throwable)
        }.getOrDefault(emptyList())
    }

    private fun FaqCatalogDto.findProduct(productId: String): FaqProductDto? =
        products.firstOrNull { product -> product.productId == productId || product.key == productId }
}
