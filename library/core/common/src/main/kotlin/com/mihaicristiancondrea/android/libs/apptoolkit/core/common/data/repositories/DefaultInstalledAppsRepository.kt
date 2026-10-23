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

import android.content.Context
import android.content.Intent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.packagemanager.isAppInstalled
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext

class DefaultInstalledAppsRepository(
    context: Context,
    private val dispatchers: DispatcherProvider,
) : InstalledAppsRepository {
    private val appContext = context.applicationContext

    override suspend fun isInstalled(packageName: String): Boolean = withContext(dispatchers.io) {
        packageName.isNotBlank() && appContext.isAppInstalled(packageName)
    }

    override suspend fun getLaunchIntent(packageName: String): Intent? = withContext(dispatchers.io) {
        if (packageName.isBlank()) return@withContext null
        try {
            appContext.packageManager.getLaunchIntentForPackage(packageName)
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (_: Exception) {
            null
        }
    }
}
