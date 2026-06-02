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

package com.d4rk.android.apps.apptoolkit.app.apps.common.data.repository

import android.content.Context
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInstallInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.InstalledAppsRepository
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.packagemanager.getVersionInfo
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.packagemanager.isAppInstalled

/** Android package-manager-backed implementation of [InstalledAppsRepository]. */
class InstalledAppsRepositoryImpl(
    private val context: Context,
) : InstalledAppsRepository {
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
