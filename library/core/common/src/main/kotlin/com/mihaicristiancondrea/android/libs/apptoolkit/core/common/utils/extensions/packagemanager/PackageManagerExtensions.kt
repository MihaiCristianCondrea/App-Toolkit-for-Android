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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.packagemanager

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.platform.AppVersionMetadata

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

internal fun PackageManager.getPackageInfoCompat(packageName: String) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0L))
    } else {
        @Suppress("DEPRECATION")
        getPackageInfo(packageName, 0)
    }

/**
 * Returns package version metadata, or null when the package is unavailable, hidden, or unreadable.
 * Package visibility rules are the same as for [hasPackageVisible].
 */
fun PackageManager.getVersionMetadata(packageName: String): AppVersionMetadata? = runCatching {
    val info = getPackageInfoCompat(packageName)
    AppVersionMetadata(
        versionName = info.versionName,
        versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            info.versionCode.toLong()
        },
    )
}.getOrNull()
