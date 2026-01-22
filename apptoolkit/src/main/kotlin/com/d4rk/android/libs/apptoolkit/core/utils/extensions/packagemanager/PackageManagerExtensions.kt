package com.d4rk.android.libs.apptoolkit.core.utils.extensions.packagemanager

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo

/**
 * Returns `true` when [packageName] is installed AND visible to the caller.
 *
 * On Android 11+ this is affected by package visibility (<queries> / QUERY_ALL_PACKAGES).
 */
fun PackageManager.hasPackageVisible(packageName: String): Boolean =
    runCatching { getPackageInfoCompat(packageName) }.isSuccess

/**
 * Returns `true` when [packageName] is installed.
 *
 * The visibility rules documented in [hasPackageVisible] apply here as well on Android 11+
 * devices.
 */
fun PackageManager.isAppInstalled(packageName: String): Boolean =
    hasPackageVisible(packageName)

/**
 * Returns `true` when [packageName] is installed on this [Context]'s device.
 */
fun Context.isAppInstalled(packageName: String): Boolean =
    packageManager.isAppInstalled(packageName)

/**
 * Returns version metadata for [packageName], or `null` when unavailable (not installed, not visible, or error).
 */
fun PackageManager.getVersionInfo(packageName: String): AppVersionInfo? =
    runCatching {
        val packageInfo = getPackageInfoCompat(packageName)

        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }

        AppVersionInfo(
            versionName = packageInfo.versionName,
            versionCode = versionCode,
        )
    }.getOrNull()

/**
 * Best-effort handler check for an implicit [intent].
 *
 * WARNING: On Android 11+ this can return false even when startActivity(intent) would succeed,
 * due to package visibility filtering. Prefer try/catch for actual launches.
 */
fun PackageManager.canResolveActivityCompat(intent: Intent): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        resolveActivity(
            intent,
            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
        ) != null
    } else {
        @Suppress("DEPRECATION")
        resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null
    }

private fun PackageManager.getPackageInfoCompat(packageName: String) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0L))
    } else {
        @Suppress("DEPRECATION")
        getPackageInfo(packageName, 0)
    }
