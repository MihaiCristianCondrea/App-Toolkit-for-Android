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
