package com.d4rk.android.libs.apptoolkit.core.utils.platform

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Helper for launching the Google Play in-app review flow.
 *
 * The helper exposes convenience methods that encapsulate the eligibility
 * checks and coroutine handling required to show the review dialog.
 */
object ReviewHelper {

    /**
     * Triggers the in-app review dialog if the user meets the eligibility criteria.
     *
     * The review is only requested when [sessionCount] is at least three and the
     * user has not been prompted before. When the review dialog is shown,
     * [onReviewLaunched] is invoked.
     */
    fun launchInAppReviewIfEligible(
        activity: Activity,
        sessionCount: Int,
        hasPromptedBefore: Boolean,
        scope: CoroutineScope,
        onReviewLaunched: () -> Unit
    ) {
        if (sessionCount < 3 || hasPromptedBefore) return
        scope.launch {
            val launched = launchReview(activity)
            if (launched) {
                onReviewLaunched()
            }
        }
    }

    /**
     * Forces the in-app review dialog to be displayed regardless of eligibility.
     * Useful for debugging or providing a manual trigger within the app.
     */
    fun forceLaunchInAppReview(activity: Activity, scope: CoroutineScope) {
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            launchReview(activity)
        }
    }

    /**
     * Requests and launches the review flow.
     *
     * @return `true` if the review dialog was shown, `false` otherwise.
     */
    suspend fun launchReview(activity: Activity): Boolean {
        val reviewManager = ReviewManagerFactory.create(activity)
        return runCatching {
            val reviewInfo = reviewManager.requestReviewFlow().await()
            reviewManager.launchReviewFlow(activity, reviewInfo).await()
            true
        }.getOrDefault(false)
    }
}
