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

package com.d4rk.android.apps.apptoolkit.app.apps.list

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInstallInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.InstalledAppsRepository

class FakeInstalledAppsRepository(
    private val installedPackages: Set<String> = emptySet(),
    private val installInfoMap: Map<String, AppInstallInfo> = emptyMap(),
) : InstalledAppsRepository {
    override fun getInstalledPackages(packageNames: Collection<String>): Set<String> {
        return packageNames.filter { it in installedPackages }.toSet()
    }

    override fun getInstallInfo(packageName: String): AppInstallInfo {
        return installInfoMap[packageName] ?: AppInstallInfo(
            isInstalled = false,
            versionInfo = null
        )
    }
}
