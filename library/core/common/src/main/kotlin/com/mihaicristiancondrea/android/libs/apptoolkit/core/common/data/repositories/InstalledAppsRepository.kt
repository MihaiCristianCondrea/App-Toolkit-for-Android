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

import android.content.Intent
import android.content.IntentSender
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.AppInstallInfo

/** Installed-package queries, safe to call from the main thread. Android package visibility applies. */
interface InstalledAppsRepository {
    suspend fun isInstalled(packageName: String): Boolean
    suspend fun getInstalledPackages(packageNames: Collection<String>): Set<String>
    suspend fun getInstallInfo(packageName: String): AppInstallInfo

    /** A fresh launcher intent, or null when lookup fails or no launcher is accessible. */
    suspend fun getLaunchIntent(packageName: String): Intent?

    /** API 33+ launch token, or null when unsupported or unavailable. */
    suspend fun getLaunchIntentSender(packageName: String): IntentSender?
}
