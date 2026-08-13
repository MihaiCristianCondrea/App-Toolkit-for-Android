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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.data.local

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.models.DeviceInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.providers.DeviceInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.utils.extensions.packagemanager.getVersionInfo
import kotlinx.coroutines.withContext

/**
 * Reads the platform for the details attached to an issue report.
 *
 * All the `android.os.Build` access and the package-manager lookup live here rather than on the
 * [DeviceInfo] model, which is why that model no longer needs a `Context` to exist.
 */
class DeviceInfoLocalDataSource(
    private val app: Application,
    private val dispatchers: DispatcherProvider,
) : DeviceInfoProvider {

    @SuppressLint("NewApi")
    override suspend fun capture(): DeviceInfo = withContext(dispatchers.io) {
        val versionInfo = app.packageManager.getVersionInfo(app.packageName)

        DeviceInfo(
            appVersionName = versionInfo?.versionName,
            appVersionCode = versionInfo?.versionCode ?: UNKNOWN_VERSION_CODE,
            buildVersion = Build.VERSION.INCREMENTAL ?: Build.UNKNOWN,
            releaseVersion = Build.VERSION.RELEASE ?: Build.UNKNOWN,
            sdkVersion = Build.VERSION.SDK_INT,
            buildId = Build.DISPLAY ?: Build.UNKNOWN,
            brand = Build.BRAND ?: Build.UNKNOWN,
            manufacturer = Build.MANUFACTURER ?: Build.UNKNOWN,
            device = Build.DEVICE ?: Build.UNKNOWN,
            model = Build.MODEL ?: Build.UNKNOWN,
            product = Build.PRODUCT ?: Build.UNKNOWN,
            hardware = Build.HARDWARE ?: Build.UNKNOWN,
            abis = Build.SUPPORTED_ABIS.orEmpty().toList(),
            abis32Bit = Build.SUPPORTED_32_BIT_ABIS.orEmpty().toList(),
            abis64Bit = Build.SUPPORTED_64_BIT_ABIS.orEmpty().toList(),
        )
    }

    private companion object {
        const val UNKNOWN_VERSION_CODE: Long = -1
    }
}
