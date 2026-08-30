/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
