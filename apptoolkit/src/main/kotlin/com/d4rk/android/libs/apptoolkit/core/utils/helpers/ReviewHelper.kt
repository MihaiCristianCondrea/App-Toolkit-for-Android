package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.app.Activity
import android.content.Context
import android.os.Build
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.hasPackage
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

    private const val PLAY_STORE_PACKAGE = "com.android.vending"

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
     * Returns `true` when Google Play Store is available to handle the in-app review flow.
     *
     * The Play In-App Review API requires the Play Store app to be installed on the device.
     */
    // TODO: Move it to extensions bool
    suspend fun isInAppReviewAvailable(activity: Activity): Boolean {
        val context = activity.applicationContext

        if (!hasPlayStore(context)) return false
        if (!isInstalledFromPlayStore(context)) return false

        val manager = ReviewManagerFactory.create(context)
        return runCatching {
            manager.requestReviewFlow().await()
            true
        }.getOrDefault(false)
    }

    fun hasPlayStore(context: Context): Boolean =
        context.packageManager.hasPackage(PLAY_STORE_PACKAGE)

    private fun Context.installingPackageNameOrNull(): String? =
        runCatching {
            val pm = packageManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                pm.getInstallSourceInfo(packageName).installingPackageName
            } else {
                @Suppress("DEPRECATION")
                pm.getInstallerPackageName(packageName)
            }
        }.getOrNull()

    fun isInstalledFromPlayStore(context: Context): Boolean =
        context.installingPackageNameOrNull() == PLAY_STORE_PACKAGE

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
