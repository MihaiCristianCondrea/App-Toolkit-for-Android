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

package com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.net.toUri
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openUrl
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.startActivitySafely

/**
 * Launches Android and external app surfaces for a selected catalog app.
 *
 * The launcher intentionally opens public Android Settings pages instead of trying to mutate
 * settings directly. That keeps the app detail sheet reliable across Android releases and OEM
 * skins while still giving users fast access to notifications, permissions, storage, battery,
 * sharing, and store pages.
 */
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
}

/** Android implementation of [AppActionLauncher] backed by public intents and safe fallbacks. */
class AndroidAppActionLauncher(
    private val context: Context,
) : AppActionLauncher {

    companion object {
        private const val PLAY_STORE_PACKAGE_NAME: String = "com.android.vending"
    }

    override fun openApp(packageName: String): Boolean {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
            ?.apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

        return if (launchIntent != null) {
            context.startActivitySafely(launchIntent) || openAppInfo(packageName)
        } else {
            openAppInfo(packageName)
        }
    }

    override fun openAppInfo(packageName: String): Boolean {
        val appDetails = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null),
        ).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
        }

        return context.startActivitySafely(appDetails) ||
            context.startActivitySafely(Intent(Settings.ACTION_APPLICATION_SETTINGS)) ||
            context.startActivitySafely(Intent(Settings.ACTION_SETTINGS))
    }

    override fun openNotifications(packageName: String): Boolean {
        val notificationIntent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            putExtra("app_package", packageName)
            putExtra("android.provider.extra.APP_PACKAGE", packageName)
        }
        return context.startActivitySafely(notificationIntent) || openAppInfo(packageName)
    }

    override fun openPermissions(packageName: String): Boolean = openAppInfo(packageName)

    override fun openStorage(packageName: String): Boolean = openAppInfo(packageName)

    override fun openBattery(packageName: String): Boolean = openAppInfo(packageName)

    override fun openPlayStore(packageName: String): Boolean {
        val marketIntent = Intent(
            Intent.ACTION_VIEW,
            "${AppLinks.MARKET_APP_PAGE}$packageName".toUri(),
        ).apply {
            setPackage(PLAY_STORE_PACKAGE_NAME)
        }
        val marketChooserIntent = Intent(
            Intent.ACTION_VIEW,
            "${AppLinks.MARKET_APP_PAGE}$packageName".toUri(),
        )

        return context.startActivitySafely(marketIntent) ||
            context.startActivitySafely(marketChooserIntent) ||
            context.openUrl("${AppLinks.PLAY_STORE_APP}$packageName")
    }

    override fun shareApp(packageName: String, appName: String): Boolean {
        val playStoreUrl = "${AppLinks.PLAY_STORE_APP}$packageName"
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, appName)
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.app_details_share_text, appName, playStoreUrl)
            )
        }
        val chooser = Intent.createChooser(send, context.getString(R.string.app_details_share_title))
        return context.startActivitySafely(chooser)
    }

    override fun copyPackageName(packageName: String): Boolean {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(
            ClipData.newPlainText(
                context.getString(R.string.app_details_package_name_clip_label),
                packageName
            )
        )
        Toast.makeText(
            context,
            context.getString(R.string.app_details_package_name_copied),
            Toast.LENGTH_SHORT
        ).show()
        return true
    }

    override fun openUrl(url: String): Boolean = context.openUrl(url)
}
