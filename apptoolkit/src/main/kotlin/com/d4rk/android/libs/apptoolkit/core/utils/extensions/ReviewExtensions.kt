package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import android.app.Activity
import android.content.Context
import android.os.Build
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.tasks.await

private const val PLAY_STORE_PACKAGE = "com.android.vending"

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

fun Context.hasPlayStore(): Boolean =
    packageManager.hasPackage(PLAY_STORE_PACKAGE)

fun Context.isInstalledFromPlayStore(): Boolean =
    installingPackageNameOrNull() == PLAY_STORE_PACKAGE

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
