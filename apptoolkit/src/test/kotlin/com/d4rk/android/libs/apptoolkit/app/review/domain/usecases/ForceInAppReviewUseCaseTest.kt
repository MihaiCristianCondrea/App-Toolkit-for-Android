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

import android.app.Activity
import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewHost
import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewOutcome
import com.d4rk.android.libs.apptoolkit.app.review.domain.repository.ReviewRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ForceInAppReviewUseCaseTest {

    private val reviewRepository = mockk<ReviewRepository>(relaxed = true)
    private val useCase = ForceInAppReviewUseCase(reviewRepository = reviewRepository)
    private val host = object : ReviewHost {
        override val activity: Activity = mockk()
    }

    @Test
    fun `launches review when available`() = runTest {
        coEvery { reviewRepository.isReviewAvailable(host.activity) } returns true
        coEvery { reviewRepository.launchReview(host.activity) } returns true

        val outcome = useCase(host = host)

        assertEquals(ReviewOutcome.Launched, outcome)
        coVerify(exactly = 1) { reviewRepository.launchReview(host.activity) }
    }

    @Test
    fun `returns unavailable when review cannot be requested`() = runTest {
        coEvery { reviewRepository.isReviewAvailable(host.activity) } returns false

        val outcome = useCase(host = host)

        assertEquals(ReviewOutcome.Unavailable, outcome)
        coVerify(exactly = 0) { reviewRepository.launchReview(any()) }
    }

    @Test
    fun `returns failure when launch attempt fails`() = runTest {
        coEvery { reviewRepository.isReviewAvailable(host.activity) } returns true
        coEvery { reviewRepository.launchReview(host.activity) } returns false

        val outcome = useCase(host = host)

        assertEquals(ReviewOutcome.Failed, outcome)
        coVerify(exactly = 1) { reviewRepository.launchReview(host.activity) }
    }
}
