package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import android.content.pm.PackageManager
import android.os.Build
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo

/**
 * Returns `true` when the provided [packageName] exists on the device.
 */
fun PackageManager.hasPackage(packageName: String): Boolean =
    runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            getPackageInfo(packageName, 0)
        }
    }.isSuccess

fun PackageManager.getVersionInfo(packageName: String): AppVersionInfo? =
    runCatching {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            getPackageInfo(packageName, 0)
        }

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
