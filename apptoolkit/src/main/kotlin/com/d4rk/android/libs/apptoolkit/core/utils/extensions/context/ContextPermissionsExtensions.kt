package com.d4rk.android.libs.apptoolkit.core.utils.extensions.context

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Returns true if POST_NOTIFICATIONS is granted, or if the platform doesn't require it (<33).
 */
fun Context.hasPostNotificationsPermissions(): Boolean =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED