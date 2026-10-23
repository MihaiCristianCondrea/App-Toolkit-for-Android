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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig

/** Reads the host's fixed build values without depending on its generated BuildConfig class. */
class DefaultBuildInfoRepository(
    private val config: AppToolkitHostBuildConfig,
) : BuildInfoRepository {
    override val appVersion: String get() = config.versionName
    override val appVersionCode: Int get() = config.versionCode.toInt()
    override val packageName: String get() = config.applicationId
    override val isDebugBuild: Boolean get() = config.isDebugBuild
}
