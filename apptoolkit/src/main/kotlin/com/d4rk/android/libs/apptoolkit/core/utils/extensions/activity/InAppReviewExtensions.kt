package com.d4rk.android.libs.apptoolkit.core.utils.extensions.activity

import android.app.Activity
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.hasPlayStore
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.isInstalledFromPlayStore
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.tasks.await

/**
 * Checks whether the Play Store in-app review flow can be safely requested.
 *
 * The helper ensures the app originates from the Play Store and that a lightweight review
 * request succeeds before reporting availability.
 */
suspend fun Activity.isInAppReviewAvailable(): Boolean {
    val context = applicationContext

    if (!context.hasPlayStore()) return false
    if (!context.isInstalledFromPlayStore()) return false

    val manager = ReviewManagerFactory.create(context)
    return runCatching {
        manager.requestReviewFlow().await()
        true
    }.getOrDefault(false)
}
