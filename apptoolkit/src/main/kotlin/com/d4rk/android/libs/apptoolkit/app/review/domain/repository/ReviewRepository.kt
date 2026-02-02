package com.d4rk.android.libs.apptoolkit.app.review.domain.repository

import android.app.Activity
import kotlinx.coroutines.flow.Flow

/**
 * Repository that encapsulates the in-app review flow and its persisted state.
 */
interface ReviewRepository {
    /** Returns a stream of recorded app session counts. */
    fun sessionCount(): Flow<Int>

    /** Returns a stream of whether the user has been prompted for review. */
    fun hasPromptedReview(): Flow<Boolean>

    /** Persists the latest session count. */
    suspend fun incrementSessionCount()

    /** Persists the "has prompted review" flag. */
    suspend fun setHasPromptedReview(value: Boolean)

    /** Checks whether the in-app review flow can be requested. */
    suspend fun isReviewAvailable(activity: Activity): Boolean

    /** Attempts to launch the in-app review flow. */
    suspend fun launchReview(activity: Activity): Boolean
}
