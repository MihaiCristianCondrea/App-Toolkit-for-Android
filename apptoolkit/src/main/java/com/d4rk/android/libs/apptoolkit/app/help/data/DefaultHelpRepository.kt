package com.d4rk.android.libs.apptoolkit.app.help.data

import android.util.Log
import com.d4rk.android.libs.apptoolkit.app.help.data.local.HelpLocalDataSource
import com.d4rk.android.libs.apptoolkit.app.help.data.mapper.toFaqItems
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.HelpRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.HelpRepository
import com.d4rk.android.libs.apptoolkit.core.logging.FAQ_LOG_TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.cancellation.CancellationException

class DefaultHelpRepository(
    private val localDataSource: HelpLocalDataSource,
    private val remoteDataSource: HelpRemoteDataSource,
    private val catalogUrl: String,
    private val productId: String,
) : HelpRepository {

    override fun fetchFaq(): Flow<List<FaqItem>> = flow {
        val remoteItems: List<FaqItem> = try {
            remoteDataSource.fetchFaqQuestions(
                catalogUrl = catalogUrl,
                productId = productId,
            ).toFaqItems()
        } catch (t: Throwable) {
            if (t is CancellationException) throw t
            Log.w(FAQ_LOG_TAG, "Remote FAQs failed; falling back to local", t)
            emptyList()
        }

        if (remoteItems.isNotEmpty()) {
            Log.d(FAQ_LOG_TAG, "Loaded ${remoteItems.size} FAQ entries from remote")
            emit(remoteItems)
        } else {
            Log.i(FAQ_LOG_TAG, "Remote FAQs unavailable; using bundled strings")
            emit(localDataSource.loadLocalQuestions())
        }
    }
}