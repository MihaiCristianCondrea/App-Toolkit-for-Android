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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.data.repositories

import android.app.Activity
import android.util.Log
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.result.runSuspendCatching
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.utils.extensions.hasPlayStore
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.utils.extensions.isInstalledFromPlayStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Data-layer implementation that coordinates persisted review metadata and Play review requests.
 *
 * The repository dispatches its own work, so callers can invoke it from anywhere.
 * `launchReviewFlow` puts a dialog in front of the activity it is given, so it runs on the main
 * thread whatever dispatcher the caller is on; awaiting the Play task suspends rather than blocks,
 * so nothing is held up by that. The availability check reads the package manager, which is binder
 * IPC, so that one goes to IO.
 */
class DefaultReviewRepository(
    private val dataStore: CommonDataStore,
    private val dispatchers: DispatcherProvider,
) : ReviewRepository {
    override fun sessionCount(): Flow<Int> = dataStore.sessionCount

    override fun hasPromptedReview(): Flow<Boolean> = dataStore.hasPromptedReview

    override suspend fun incrementSessionCount() {
        dataStore.incrementSessionCount()
    }

    override suspend fun setHasPromptedReview(value: Boolean) {
        dataStore.setHasPromptedReview(value = value)
    }

    /**
     * The review flow the last availability check requested, handed to the next [launchReview] so
     * a review costs one Play round trip instead of two. A [ReviewInfo] can be used once, so it is
     * cleared when taken.
     */
    @Volatile
    private var preparedReviewInfo: ReviewInfo? = null

    override suspend fun isReviewAvailable(activity: Activity): Boolean =
        withContext(dispatchers.io) {
            val context = activity.applicationContext
            if (!context.hasPlayStore()) return@withContext false
            if (!context.isInstalledFromPlayStore()) return@withContext false

            val manager = ReviewManagerFactory.create(context)
            runSuspendCatching { manager.requestReviewFlow().await() }
                .onSuccess { reviewInfo -> preparedReviewInfo = reviewInfo }
                .onFailure { throwable -> Log.w(LOG_TAG, "Could not request the review flow.", throwable) }
                .isSuccess
        }

    override suspend fun launchReview(activity: Activity): Boolean =
        withContext(dispatchers.main) {
            val reviewManager = ReviewManagerFactory.create(activity)
            runSuspendCatching {
                val reviewInfo = preparedReviewInfo?.also { preparedReviewInfo = null }
                    ?: reviewManager.requestReviewFlow().await()
                reviewManager.launchReviewFlow(activity, reviewInfo).await()
            }
                .onFailure { throwable -> Log.w(LOG_TAG, "Could not launch the review flow.", throwable) }
                .isSuccess
        }
}

private const val LOG_TAG: String = "ReviewRepository"
