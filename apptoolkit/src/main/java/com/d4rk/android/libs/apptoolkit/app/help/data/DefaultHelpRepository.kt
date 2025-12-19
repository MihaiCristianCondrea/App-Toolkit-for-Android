package com.d4rk.android.libs.apptoolkit.app.help.data

import android.util.Log
import com.d4rk.android.libs.apptoolkit.app.help.data.local.HelpLocalDataSource
import com.d4rk.android.libs.apptoolkit.app.help.data.mapper.toFaqItems
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.HelpRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.HelpRepository
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.logging.FAQ_LOG_TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DefaultHelpRepository(
    private val localDataSource: HelpLocalDataSource,
    private val remoteDataSource: HelpRemoteDataSource,
    private val dispatchers: DispatcherProvider,
    private val catalogUrl: String,
    private val productId: String,
) : HelpRepository {

    override fun fetchFaq(): Flow<List<FaqItem>> = flow {
        val remoteQuestions = remoteDataSource.fetchFaqQuestions(
            catalogUrl = catalogUrl,
            productId = productId,
        ).toFaqItems()

        if (remoteQuestions.isNotEmpty()) {
            emit(remoteQuestions)
            Log.d(FAQ_LOG_TAG, "Prepared ${remoteQuestions.size} sanitized FAQ entries from remote sources")
            return@flow
        }

        Log.i(FAQ_LOG_TAG, "Remote FAQs unavailable, falling back to bundled strings")
        emit(localDataSource.loadLocalQuestions())
    }.flowOn(dispatchers.io)
}
