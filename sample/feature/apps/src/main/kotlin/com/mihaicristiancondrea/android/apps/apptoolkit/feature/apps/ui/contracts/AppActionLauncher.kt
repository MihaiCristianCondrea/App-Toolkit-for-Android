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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts

import android.provider.Settings
import androidx.compose.runtime.Stable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl

/**
 * Launches Android and external app surfaces for a selected catalog app.
 *
 * The launcher intentionally opens public Android Settings pages instead of trying to mutate
 * settings directly. That keeps the app detail sheet reliable across Android releases and OEM
 * skins while still giving users fast access to notifications, permissions, storage, battery,
 * sharing, and store pages.
 */
@Stable
interface AppActionLauncher {
    fun openApp(packageName: String): Boolean
    fun openAppInfo(packageName: String): Boolean
    fun openNotifications(packageName: String): Boolean
    fun openPermissions(packageName: String): Boolean
    fun openStorage(packageName: String): Boolean
    fun openBattery(packageName: String): Boolean
    fun openPlayStore(packageName: String): Boolean
    fun shareApp(packageName: String, appName: String): Boolean
    fun copyPackageName(packageName: String): Boolean
    fun openUrl(url: String): Boolean

    fun openNotificationChannel(packageName: String, channelId: String): Boolean
    fun openByDefault(packageName: String): Boolean
    fun openAppLocale(packageName: String): Boolean
    fun openAllFilesAccess(packageName: String): Boolean
    fun openIgnoreBatteryOptimizationsList(): Boolean
}
