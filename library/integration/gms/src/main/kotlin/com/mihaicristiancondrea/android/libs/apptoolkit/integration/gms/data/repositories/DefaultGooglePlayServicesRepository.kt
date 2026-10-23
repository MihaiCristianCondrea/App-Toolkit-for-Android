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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.gms.data.repositories

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.withContext

class DefaultGooglePlayServicesRepository(
    context: Context,
    private val dispatchers: DispatcherProvider,
) : GooglePlayServicesRepository {
    private val packageManager = context.applicationContext.packageManager

    override suspend fun getVersion(): String? = withContext(dispatchers.io) {
        try {
            val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(PACKAGE_NAME, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(PACKAGE_NAME, 0)
            }
            @Suppress("DEPRECATION")
            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info.longVersionCode
            } else {
                info.versionCode.toLong()
            }
            info.versionName ?: versionCode.toString()
        } catch (_: PackageManager.NameNotFoundException) {
            null
        }
    }

    private companion object {
        const val PACKAGE_NAME = "com.google.android.gms"
    }
}
