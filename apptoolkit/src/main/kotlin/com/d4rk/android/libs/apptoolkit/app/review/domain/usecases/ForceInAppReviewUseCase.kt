package com.d4rk.android.libs.apptoolkit.app.review.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewHost
import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewOutcome
import com.d4rk.android.libs.apptoolkit.app.review.domain.repository.ReviewRepository

/**
 * Forces the in-app review flow to launch if it is available on the device.
 */
class ForceInAppReviewUseCase(
    private val reviewRepository: ReviewRepository,
) {
    /**
     * Launches the in-app review flow when possible.
     *
     * @return [ReviewOutcome.Launched] if the flow was shown, or a failure outcome otherwise.
     */
    suspend operator fun invoke(host: ReviewHost): ReviewOutcome {
        val isAvailable = reviewRepository.isReviewAvailable(activity = host.activity)
        if (!isAvailable) return ReviewOutcome.Unavailable

        return if (reviewRepository.launchReview(activity = host.activity)) {
            ReviewOutcome.Launched
        } else {
            ReviewOutcome.Failed
        }
    }
}
