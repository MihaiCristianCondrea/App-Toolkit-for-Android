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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.utils

import android.content.Context
import android.widget.Toast
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.startActivitySafely
import kotlinx.coroutines.withContext
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.InstalledAppsRepository

class AppLauncher(
    private val installedApps: InstalledAppsRepository,
    private val dispatchers: DispatcherProvider,
) {

    /**
     * Opens a specific app if installed.
     *
     * Returns `true` if the app was successfully launched and `false` otherwise.
     * For callers that need to react programmatically to failures, use
     * [openAppResult].
     */
    suspend fun openApp(context: Context, packageName: String): Boolean =
        openAppResult(context = context, packageName = packageName).getOrElse { false }

    /**
     * Opens a specific app if installed and exposes the operation result.
     *
     * @return A [Result] containing `true` when the app was launched or a failure when
     *         the launch intent could not be obtained or starting the activity failed.
     */
    suspend fun openAppResult(context: Context, packageName: String): Result<Boolean> {
        val launchIntent = installedApps.getLaunchIntent(packageName)
        return withContext(dispatchers.main) {
            val failure = Result.failure<Boolean>(IllegalStateException("App not installed"))
            val showFailure: () -> Unit = {
                Toast.makeText(
                    context,
                    context.getString(R.string.app_not_installed),
                    Toast.LENGTH_SHORT,
                ).show()
            }
            if (launchIntent == null) {
                showFailure()
                failure
            } else if (context.startActivitySafely(intent = launchIntent, onFailure = { showFailure() })) {
                Result.success(true)
            } else {
                failure
            }
        }
    }
}
