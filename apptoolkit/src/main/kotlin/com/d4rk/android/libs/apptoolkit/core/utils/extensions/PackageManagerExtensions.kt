package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo

/**
 * Returns `true` when the provided [packageName] exists on the device.
 */
fun PackageManager.hasPackage(packageName: String): Boolean =
    runCatching { getPackageInfoCompat(packageName) }.isSuccess

/**
 * Returns version metadata for the provided [packageName], or `null` when unavailable.
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
 * Checks whether there is a handler for an implicit [intent], mirroring
 * `Context#startActivity` resolution with compatibility flags.
 */
fun PackageManager.canResolveActivityCompat(intent: Intent): Boolean {
    // MATCH_DEFAULT_ONLY mirrors how Context#startActivity resolves implicit intents. :contentReference[oaicite:2]{index=2}
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        resolveActivity(
            intent,
            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
        ) != null
    } else {
        @Suppress("DEPRECATION")
        resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null
    }
}

private fun PackageManager.getPackageInfoCompat(packageName: String) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0L))
    } else {
        @Suppress("DEPRECATION")
        getPackageInfo(packageName, 0)
    }
