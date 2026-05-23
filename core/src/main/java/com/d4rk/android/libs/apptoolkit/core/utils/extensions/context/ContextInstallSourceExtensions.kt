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

package com.d4rk.android.libs.apptoolkit.core.utils.extensions.context

import android.content.Context
import android.os.Build
import com.d4rk.android.libs.apptoolkit.core.utils.constants.store.StoreConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.packagemanager.hasPackageVisible

/**
 * Returns the installer package name when available, null otherwise.
 */
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
fun Context.hasPlayStore(): Boolean =
    packageManager.hasPackageVisible(StoreConstants.PLAY_STORE_PACKAGE)

/**
 * True if the current app reports it was installed from the Play Store.
 */
fun Context.isInstalledFromPlayStore(): Boolean =
    installingPackageNameOrNull() == StoreConstants.PLAY_STORE_PACKAGE
