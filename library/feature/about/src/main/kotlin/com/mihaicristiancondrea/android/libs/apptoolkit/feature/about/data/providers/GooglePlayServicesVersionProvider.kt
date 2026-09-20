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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.providers

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * Inspects the device for Google Play services and provides its installed version string.
 *
 * Returns `null` when Google Play services is not installed on the device (e.g. AOSP builds,
 * Huawei devices without GMS, or custom ROMs).
 */
open class GooglePlayServicesVersionProvider(
    private val context: Context,
) {

    /**
     * Resolves the installed version of Google Play services, prioritizing the human-readable
     * version name with a fallback to the version code.
     */
    open fun getVersion(): String? {
        return runCatching {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    GOOGLE_PLAY_SERVICES_PACKAGE,
                    PackageManager.PackageInfoFlags.of(0),
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(
                    GOOGLE_PLAY_SERVICES_PACKAGE,
                    0,
                )
            }

            @Suppress("DEPRECATION")
            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                packageInfo.versionCode.toLong()
            }

            packageInfo.versionName ?: versionCode.toString()
        }.getOrNull()
    }

    private companion object {
        const val GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms"
    }
}
