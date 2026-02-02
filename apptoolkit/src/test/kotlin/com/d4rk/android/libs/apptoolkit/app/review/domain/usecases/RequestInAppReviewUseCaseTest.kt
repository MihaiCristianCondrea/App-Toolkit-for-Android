package com.d4rk.android.libs.apptoolkit.app.review.domain.usecases

import android.app.Activity
import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewHost
import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewOutcome
import com.d4rk.android.libs.apptoolkit.app.review.domain.repository.ReviewRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RequestInAppReviewUseCaseTest {

    private val reviewRepository = mockk<ReviewRepository>(relaxed = true)
    private val useCase = RequestInAppReviewUseCase(reviewRepository = reviewRepository)
    private val host = object : ReviewHost {
        override val activity: Activity = mockk()
    }

    @Test
    fun `launches review and marks prompted when eligible`() = runTest {
        every { reviewRepository.sessionCount() } returns flowOf(3)
        every { reviewRepository.hasPromptedReview() } returns flowOf(false)
        coEvery { reviewRepository.launchReview(host.activity) } returns true

        val outcome = useCase(host = host)

        assertEquals(ReviewOutcome.Launched, outcome)
        coVerify(exactly = 1) { reviewRepository.launchReview(host.activity) }
        coVerify(exactly = 1) { reviewRepository.setHasPromptedReview(value = true) }
        coVerify(exactly = 1) { reviewRepository.incrementSessionCount() }
    }

    @Test
    fun `skips review when session count below threshold`() = runTest {
        every { reviewRepository.sessionCount() } returns flowOf(2)
        every { reviewRepository.hasPromptedReview() } returns flowOf(false)

        val outcome = useCase(host = host)

        assertEquals(ReviewOutcome.NotEligible, outcome)
        coVerify(exactly = 0) { reviewRepository.launchReview(any()) }
        coVerify(exactly = 1) { reviewRepository.incrementSessionCount() }
    }

    @Test
    fun `skips review when user was already prompted`() = runTest {
        every { reviewRepository.sessionCount() } returns flowOf(3)
        every { reviewRepository.hasPromptedReview() } returns flowOf(true)

        val outcome = useCase(host = host)

        assertEquals(ReviewOutcome.NotEligible, outcome)
        coVerify(exactly = 0) { reviewRepository.launchReview(any()) }
        coVerify(exactly = 1) { reviewRepository.incrementSessionCount() }
    }

    @Test
    fun `reports failure when review launch fails`() = runTest {
        every { reviewRepository.sessionCount() } returns flowOf(3)
        every { reviewRepository.hasPromptedReview() } returns flowOf(false)
        coEvery { reviewRepository.launchReview(host.activity) } returns false

        val outcome = useCase(host = host)

        assertEquals(ReviewOutcome.Failed, outcome)
        coVerify(exactly = 1) { reviewRepository.launchReview(host.activity) }
        coVerify(exactly = 0) { reviewRepository.setHasPromptedReview(any()) }
        coVerify(exactly = 1) { reviewRepository.incrementSessionCount() }
    }
}
