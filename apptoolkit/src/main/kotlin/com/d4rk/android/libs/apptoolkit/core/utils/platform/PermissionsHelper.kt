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

package com.d4rk.android.libs.apptoolkit.core.utils.platform

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat
import com.d4rk.android.libs.apptoolkit.core.utils.constants.permissions.PermissionsConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.hasPostNotificationsPermissions

/**
 * Utility class for handling runtime permissions.
 */
object PermissionsHelper {

    /**
     * Checks if the app has permission to post notifications.
     *
     * @param context The application context.
     * @return True if the permission is granted, false otherwise.
     */
    fun hasNotificationPermission(context: Context): Boolean {
        return context.hasPostNotificationsPermissions()
    }

    /**
     * Requests the notification permission.
     *
     * @param activity The Activity instance required to request the permission.
     */
    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !hasNotificationPermission(activity)
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                PermissionsConstants.REQUEST_CODE_NOTIFICATION_PERMISSION
            )
        }
    }
}
