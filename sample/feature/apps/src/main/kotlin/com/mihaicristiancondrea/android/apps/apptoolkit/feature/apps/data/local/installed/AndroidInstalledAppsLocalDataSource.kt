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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.local.installed

import android.content.Context
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.packagemanager.getVersionMetadata
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInstallInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppVersionInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.packagemanager.isAppInstalled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.withContext

class AndroidInstalledAppsLocalDataSource(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
) : InstalledAppsLocalDataSource {
    override suspend fun getInstalledPackages(packageNames: Collection<String>): Set<String> =
        withContext(dispatchers.io) {
            packageNames
                .asSequence()
                .filter { packageName -> packageName.isNotBlank() && context.isAppInstalled(packageName) }
                .toSet()
        }

    override suspend fun getInstallInfo(packageName: String): AppInstallInfo {
        if (packageName.isBlank()) return AppInstallInfo(isInstalled = false, versionInfo = null)
        return withContext(dispatchers.io) {
            AppInstallInfo(
                isInstalled = context.isAppInstalled(packageName),
                versionInfo = context.packageManager.getVersionMetadata(packageName)?.let { metadata ->
                    AppVersionInfo(metadata.versionName, metadata.versionCode)
                },
            )
        }
    }
}
