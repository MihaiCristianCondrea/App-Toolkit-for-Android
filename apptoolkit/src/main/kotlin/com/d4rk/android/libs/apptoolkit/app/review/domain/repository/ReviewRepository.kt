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
