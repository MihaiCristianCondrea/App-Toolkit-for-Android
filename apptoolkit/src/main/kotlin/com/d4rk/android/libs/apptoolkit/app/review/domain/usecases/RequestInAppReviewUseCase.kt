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

package com.d4rk.android.libs.apptoolkit.app.review.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewHost
import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewOutcome
import com.d4rk.android.libs.apptoolkit.app.review.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.first

/**
 * Requests the in-app review flow based on recorded app session data.
 */
class RequestInAppReviewUseCase(
    private val reviewRepository: ReviewRepository,
) {
    /**
     * Launches the review flow when the user is eligible.
     *
     * The caller does not need to manage persisted session counts or prompt flags; both are
     * updated inside this use case.
     */
    suspend operator fun invoke(host: ReviewHost): ReviewOutcome {
        val sessionCount = reviewRepository.sessionCount().first()
        val hasPromptedBefore = reviewRepository.hasPromptedReview().first()
        val eligible = sessionCount >= MIN_SESSIONS_FOR_REVIEW && !hasPromptedBefore

        val outcome = if (eligible) {
            if (reviewRepository.launchReview(activity = host.activity)) {
                reviewRepository.setHasPromptedReview(value = true)
                ReviewOutcome.Launched
            } else {
                ReviewOutcome.Failed
            }
        } else {
            ReviewOutcome.NotEligible
        }

        reviewRepository.incrementSessionCount()
        return outcome
    }

    private companion object {
        const val MIN_SESSIONS_FOR_REVIEW: Int = 3
    }
}
