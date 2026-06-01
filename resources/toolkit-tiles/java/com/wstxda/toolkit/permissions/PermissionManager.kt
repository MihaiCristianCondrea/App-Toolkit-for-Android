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

package com.wstxda.toolkit.permissions

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.text.TextUtils
import androidx.core.content.getSystemService
import com.wstxda.toolkit.services.accessibility.TileAccessibilityService

class PermissionManager(context: Context) {

    private val appContext = context.applicationContext

    fun isAccessibilityServiceEnabled(): Boolean {
        val expectedComponentName = ComponentName(appContext, TileAccessibilityService::class.java)

        val enabledServicesSetting = Settings.Secure.getString(
            appContext.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServicesSetting)

        while (colonSplitter.hasNext()) {
            val componentNameString = colonSplitter.next()
            val enabledComponent = ComponentName.unflattenFromString(componentNameString)
            if (enabledComponent != null && enabledComponent == expectedComponentName) {
                return true
            }
        }
        return false
    }

    fun hasWriteSettingsPermission(): Boolean = Settings.System.canWrite(appContext)

    fun hasWriteSecureSettingsPermission(): Boolean =
        appContext.checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS") == PackageManager.PERMISSION_GRANTED

    fun hasDoNotDisturbPermission(): Boolean {
        val notificationManager = appContext.getSystemService<NotificationManager>()
        return notificationManager?.isNotificationPolicyAccessGranted == true
    }
}