package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.app.Activity
import android.content.Context
import com.google.android.gms.tasks.Tasks
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewHelperTest {

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `launchInAppReviewIfEligible launches review when eligible`() = runTest {
        val activity = mockk<Activity>()
        mockkObject(ReviewHelper)
        coEvery { ReviewHelper.launchReview(activity) } returns true
        every {
            ReviewHelper.launchInAppReviewIfEligible(
                any(), any(), any(), any(), any()
            )
        } answers { callOriginal() }
        var reviewLaunched = false

        ReviewHelper.launchInAppReviewIfEligible(
            activity = activity,
            sessionCount = 3,
            hasPromptedBefore = false,
            scope = this,
            onReviewLaunched = { reviewLaunched = true },
        )
        runCurrent()

        assertTrue(reviewLaunched)
        coVerify(exactly = 1) { ReviewHelper.launchReview(activity) }
    }

    @Test
    fun `launchInAppReviewIfEligible does nothing when session count below threshold`() = runTest {
        val activity = mockk<Activity>()
        mockkObject(ReviewHelper)
        coEvery { ReviewHelper.launchReview(any()) } returns true
        every {
            ReviewHelper.launchInAppReviewIfEligible(
                any(), any(), any(), any(), any()
            )
        } answers { callOriginal() }

        ReviewHelper.launchInAppReviewIfEligible(
            activity = activity,
            sessionCount = 2,
            hasPromptedBefore = false,
            scope = this,
            onReviewLaunched = {},
        )

        coVerify(exactly = 0) { ReviewHelper.launchReview(any()) }
    }

    @Test
    fun `launchInAppReviewIfEligible does nothing when user has been prompted`() = runTest {
        val activity = mockk<Activity>()
        mockkObject(ReviewHelper)
        coEvery { ReviewHelper.launchReview(any()) } returns true
        every {
            ReviewHelper.launchInAppReviewIfEligible(
                any(), any(), any(), any(), any()
            )
        } answers { callOriginal() }

        ReviewHelper.launchInAppReviewIfEligible(
            activity = activity,
            sessionCount = 3,
            hasPromptedBefore = true,
            scope = this,
            onReviewLaunched = {},
        )

        coVerify(exactly = 0) { ReviewHelper.launchReview(any()) }
    }

    @Test
    fun `launchInAppReviewIfEligible does not invoke callback when launchReview fails`() = runTest {
        val activity = mockk<Activity>()
        mockkObject(ReviewHelper)
        coEvery { ReviewHelper.launchReview(activity) } returns false
        every {
            ReviewHelper.launchInAppReviewIfEligible(
                any(), any(), any(), any(), any()
            )
        } answers { callOriginal() }
        var reviewLaunched = false

        ReviewHelper.launchInAppReviewIfEligible(
            activity = activity,
            sessionCount = 3,
            hasPromptedBefore = false,
            scope = this,
            onReviewLaunched = { reviewLaunched = true },
        )
        runCurrent()

        assertFalse(reviewLaunched)
        coVerify(exactly = 1) { ReviewHelper.launchReview(activity) }
    }

    @Test
    fun `forceLaunchInAppReview always attempts to show the review`() = runTest {
        val activity = mockk<Activity>()
        mockkObject(ReviewHelper)
        coEvery { ReviewHelper.launchReview(activity) } returns false
        every { ReviewHelper.forceLaunchInAppReview(any(), any()) } answers { callOriginal() }

        ReviewHelper.forceLaunchInAppReview(
            activity = activity,
            scope = this,
        )

        coVerify(exactly = 1) { ReviewHelper.launchReview(activity) }
    }

    @Test
    fun `launchReview returns true when request and flow succeed`() = runTest {
        val activity = mockk<Activity>()
        val context = mockk<Context>()
        every { activity.applicationContext } returns context
        val reviewManager = mockk<ReviewManager>()
        val reviewInfo = mockk<ReviewInfo>()
        mockkStatic(ReviewManagerFactory::class)
        every { ReviewManagerFactory.create(activity) } returns reviewManager
        every { reviewManager.requestReviewFlow() } returns Tasks.forResult(reviewInfo)
        every { reviewManager.launchReviewFlow(activity, reviewInfo) } returns Tasks.forResult(null)

        val result = ReviewHelper.launchReview(activity)

        assertTrue(result)
        verify(exactly = 1) { reviewManager.requestReviewFlow() }
        verify(exactly = 1) { reviewManager.launchReviewFlow(activity, reviewInfo) }
    }

    @Test
    fun `launchReview returns false when requestReviewFlow fails`() = runTest {
        val activity = mockk<Activity>()
        val context = mockk<Context>()
        every { activity.applicationContext } returns context
        val reviewManager = mockk<ReviewManager>()
        mockkStatic(ReviewManagerFactory::class)
        every { ReviewManagerFactory.create(activity) } returns reviewManager
        every { reviewManager.requestReviewFlow() } returns Tasks.forException(Exception())

        val result = ReviewHelper.launchReview(activity)

        assertFalse(result)
        verify(exactly = 1) { reviewManager.requestReviewFlow() }
        verify(exactly = 0) { reviewManager.launchReviewFlow(any(), any()) }
    }

    @Test
    fun `launchReview returns false when launchReviewFlow fails`() = runTest {
        val activity = mockk<Activity>()
        val context = mockk<Context>()
        every { activity.applicationContext } returns context
        val reviewManager = mockk<ReviewManager>()
        val reviewInfo = mockk<ReviewInfo>()
        mockkStatic(ReviewManagerFactory::class)
        every { ReviewManagerFactory.create(activity) } returns reviewManager
        every { reviewManager.requestReviewFlow() } returns Tasks.forResult(reviewInfo)
        every { reviewManager.launchReviewFlow(activity, reviewInfo) } returns Tasks.forException(Exception())

        val result = ReviewHelper.launchReview(activity)

        assertFalse(result)
        verify(exactly = 1) { reviewManager.requestReviewFlow() }
        verify(exactly = 1) { reviewManager.launchReviewFlow(activity, reviewInfo) }
    }
}
