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

package com.wstxda.toolkit.services.foreground

import android.app.Notification
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.service.quicksettings.TileService
import androidx.core.app.ServiceCompat

fun TileService.startForegroundCompat(notificationId: Int, notification: Notification) {
    if (VERSION.SDK_INT >= VERSION_CODES.Q) {
        ServiceCompat.startForeground(
            this, notificationId, notification, FOREGROUND_SERVICE_TYPE_MANIFEST
        )
    } else {
        startForeground(notificationId, notification)
    }
}