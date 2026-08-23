package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.installed

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppInstallInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppVersionInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.packagemanager.isAppInstalled

class AndroidInstalledAppsLocalDataSource(
    private val context: Context,
) : InstalledAppsLocalDataSource {
    override fun getInstalledPackages(packageNames: Collection<String>): Set<String> = packageNames
        .asSequence()
        .filter { packageName -> packageName.isNotBlank() && context.isAppInstalled(packageName) }
        .toSet()

    override fun getInstallInfo(packageName: String): AppInstallInfo {
        if (packageName.isBlank()) return AppInstallInfo(isInstalled = false, versionInfo = null)
        return AppInstallInfo(
            isInstalled = context.isAppInstalled(packageName),
            versionInfo = context.packageManager.getVersionInfo(packageName),
        )
    }
}

private fun PackageManager.getVersionInfo(packageName: String): AppVersionInfo? = runCatching {
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0L))
    } else {
        @Suppress("DEPRECATION")
        getPackageInfo(packageName, 0)
    }
    AppVersionInfo(
        versionName = packageInfo.versionName,
        versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        },
    )
}.getOrNull()
