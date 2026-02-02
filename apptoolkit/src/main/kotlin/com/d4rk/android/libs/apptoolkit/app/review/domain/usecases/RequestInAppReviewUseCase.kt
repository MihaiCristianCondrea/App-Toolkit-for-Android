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
