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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.usecases

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewOutcome
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.data.repositories.ReviewRepository
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
     *
     * An eligible user on an install Play cannot serve — sideloaded, no Play Store, a debug build
     * run from the IDE — gets [ReviewOutcome.Unavailable] rather than [ReviewOutcome.Failed]. The
     * two say different things: one is a device that was never going to show a dialog, the other is
     * a launch that should have worked and did not. The prompt flag stays unset either way, so the
     * same user still gets their one prompt once Play can serve it.
     *
     * The session count is recorded on every invocation, eligible or not, so call this once per app
     * session. Calling it on every resume counts resumes instead, and the threshold stops meaning
     * what it says.
     */
    suspend operator fun invoke(host: ReviewHost): ReviewOutcome {
        val sessionCount = reviewRepository.sessionCount().first()
        val hasPromptedBefore = reviewRepository.hasPromptedReview().first()
        val eligible = sessionCount >= MIN_SESSIONS_FOR_REVIEW && !hasPromptedBefore

        val outcome = when {
            !eligible -> ReviewOutcome.NotEligible

            !reviewRepository.isReviewAvailable(activity = host.activity) ->
                ReviewOutcome.Unavailable

            reviewRepository.launchReview(activity = host.activity) -> {
                reviewRepository.setHasPromptedReview(value = true)
                ReviewOutcome.Launched
            }

            else -> ReviewOutcome.Failed
        }

        reviewRepository.incrementSessionCount()
        return outcome
    }

    private companion object {
        const val MIN_SESSIONS_FOR_REVIEW: Int = 3
    }
}
