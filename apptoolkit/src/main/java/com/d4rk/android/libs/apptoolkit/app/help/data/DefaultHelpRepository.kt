package com.d4rk.android.libs.apptoolkit.app.help.data

import android.content.Context
import android.util.Log
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.FaqCatalog
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.FaqQuestion
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.UiHelpQuestion
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.HelpRepository
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.logging.FAQ_LOG_TAG
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DefaultHelpRepository(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
    private val client: HttpClient,
    private val catalogUrl: String,
    private val productId: String,
) : HelpRepository {

    override fun fetchFaq(): Flow<List<UiHelpQuestion>> = flow {
        val remoteQuestions = fetchRemoteQuestions()
        if (remoteQuestions.isNotEmpty()) {
            emit(remoteQuestions)
            return@flow
        }
        Log.i(FAQ_LOG_TAG, "Remote FAQs unavailable, falling back to bundled strings")
        emit(loadLocalQuestions())
    }.flowOn(dispatchers.io)

    private suspend fun fetchRemoteQuestions(): List<UiHelpQuestion> {
        return try {
            Log.d(FAQ_LOG_TAG, "Fetching FAQ catalog from $catalogUrl for productId=$productId")
            val catalog = client.get(catalogUrl).body<FaqCatalog>()

            Log.d(
                FAQ_LOG_TAG,
                "Loaded FAQ catalog schemaVersion=${catalog.schemaVersion} products=${catalog.products.size}",
            )

            val product = catalog.products.firstOrNull { product ->
                product.productId == productId || product.key == productId
            }

            if (product == null) {
                Log.w(FAQ_LOG_TAG, "No FAQ catalog entry for productId=$productId")
                return emptyList()
            }

            Log.d(
                FAQ_LOG_TAG,
                "Found ${product.questionSources.size} question sources for ${product.productId}",
            )

            val remoteQuestions = product.questionSources.flatMap { source ->
                runCatching {
                    val questions = client.get(source.url).body<List<FaqQuestion>>()
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

            val sanitizedQuestions = remoteQuestions.mapIndexed { index, question ->
                UiHelpQuestion(
                    id = index,
                    question = question.question,
                    answer = question.answer.trim(),
                )
            }.filter { it.question.isNotBlank() && it.answer.isNotBlank() }

            Log.d(
                FAQ_LOG_TAG,
                "Prepared ${sanitizedQuestions.size} sanitized FAQ entries from remote sources",
            )

            sanitizedQuestions
        } catch (error: Throwable) {
            if (error is CancellationException) throw error
            Log.e(FAQ_LOG_TAG, "Failed to load FAQs from remote catalog", error)
            emptyList()
        }
    }

    private fun loadLocalQuestions(): List<UiHelpQuestion> {
        val faq = listOf(
            R.string.question_1 to R.string.summary_preference_faq_1,
            R.string.question_2 to R.string.summary_preference_faq_2,
            R.string.question_3 to R.string.summary_preference_faq_3,
            R.string.question_4 to R.string.summary_preference_faq_4,
            R.string.question_5 to R.string.summary_preference_faq_5,
            R.string.question_6 to R.string.summary_preference_faq_6,
            R.string.question_7 to R.string.summary_preference_faq_7,
            R.string.question_8 to R.string.summary_preference_faq_8,
            R.string.question_9 to R.string.summary_preference_faq_9
        ).mapIndexed { index, (questionRes, answerRes) ->
            UiHelpQuestion(
                id = index,
                question = context.getString(questionRes),
                answer = context.getString(answerRes),
            )
        }.filter { it.question.isNotBlank() && it.answer.isNotBlank() }
        return faq
    }
}