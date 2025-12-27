package com.d4rk.android.libs.apptoolkit.core.utils.extensions.context

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.tasks.await

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
