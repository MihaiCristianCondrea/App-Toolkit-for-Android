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

package com.d4rk.android.libs.apptoolkit.app.review.data.repository

import android.app.Activity
import com.d4rk.android.libs.apptoolkit.app.review.domain.repository.ReviewRepository
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.hasPlayStore
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.isInstalledFromPlayStore
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

/**
 * Data-layer implementation that coordinates persisted review metadata and Play review requests.
 */
class ReviewRepositoryImpl(
    private val dataStore: CommonDataStore,
) : ReviewRepository {
    override fun sessionCount(): Flow<Int> = dataStore.sessionCount

    override fun hasPromptedReview(): Flow<Boolean> = dataStore.hasPromptedReview

    override suspend fun incrementSessionCount() {
        dataStore.incrementSessionCount()
    }

    override suspend fun setHasPromptedReview(value: Boolean) {
        dataStore.setHasPromptedReview(value = value)
    }

    override suspend fun isReviewAvailable(activity: Activity): Boolean {
        val context = activity.applicationContext
        if (!context.hasPlayStore()) return false
        if (!context.isInstalledFromPlayStore()) return false

        val manager = ReviewManagerFactory.create(context)
        return runCatching {
            manager.requestReviewFlow().await()
            true
        }.getOrDefault(false)
    }

    override suspend fun launchReview(activity: Activity): Boolean {
        val reviewManager = ReviewManagerFactory.create(activity)
        return runCatching {
            val reviewInfo = reviewManager.requestReviewFlow().await()
            reviewManager.launchReviewFlow(activity, reviewInfo).await()
            true
        }.getOrDefault(false)
    }
}
