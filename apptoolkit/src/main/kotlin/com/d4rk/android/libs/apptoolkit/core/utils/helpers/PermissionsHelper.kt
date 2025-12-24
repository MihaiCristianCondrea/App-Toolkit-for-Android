package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat
import com.d4rk.android.libs.apptoolkit.core.utils.constants.permissions.PermissionsConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.hasNotificationPermission

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
        return context.hasNotificationPermission()
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
