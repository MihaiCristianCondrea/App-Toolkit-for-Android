package com.d4rk.android.libs.apptoolkit.core.utils.extensions.context

import android.content.Context
import android.os.Build
import androidx.annotation.CheckResult
import com.d4rk.android.libs.apptoolkit.core.utils.constants.store.StoreConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.pm.hasPackageVisible

/**
 * Returns the installer package name when available, null otherwise.
 */
@CheckResult
fun Context.installingPackageNameOrNull(): String? =
    runCatching {
        val pm = packageManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            pm.getInstallSourceInfo(packageName).installingPackageName
        } else {
            @Suppress("DEPRECATION")
            pm.getInstallerPackageName(packageName)
        }
    }.getOrNull()

/**
 * True if the device has the Play Store installed AND visible to the caller.
 *
 * Requires <queries><package android:name="com.android.vending"/></queries> when targeting 30+.
 */
@CheckResult
fun Context.hasPlayStore(): Boolean =
    packageManager.hasPackageVisible(StoreConstants.PLAY_STORE_PACKAGE)

/**
 * True if the current app reports it was installed from the Play Store.
 */
@CheckResult
fun Context.isInstalledFromPlayStore(): Boolean =
    installingPackageNameOrNull() == StoreConstants.PLAY_STORE_PACKAGE
