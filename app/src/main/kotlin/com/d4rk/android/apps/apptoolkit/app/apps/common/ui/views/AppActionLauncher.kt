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

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.net.toUri
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openUrl

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

    fun openNotificationChannel(packageName: String, channelId: String): Boolean
    fun openByDefault(packageName: String): Boolean
    fun openAppLocale(packageName: String): Boolean
    fun openAllFilesAccess(packageName: String): Boolean
    fun openIgnoreBatteryOptimizationsList(): Boolean
}

/** Android implementation of [AppActionLauncher] backed by public intents and safe fallbacks. */
class AndroidAppActionLauncher(
    private val context: Context,
) : AppActionLauncher {

    companion object {
        private const val PLAY_STORE_PACKAGE_NAME: String = "com.android.vending"

        private const val LEGACY_EXTRA_APP_PACKAGE: String = "app_package"
        private const val LEGACY_PROVIDER_EXTRA_APP_PACKAGE: String =
            "android.provider.extra.APP_PACKAGE"
    }

    override fun openApp(packageName: String): Boolean {
        if (!packageName.isProbablyValidPackageName()) return false

        return openAppWithIntentSender(packageName) ||
                openAppWithLaunchIntent(packageName) ||
                openAppInfo(packageName)
    }

    override fun openAppInfo(packageName: String): Boolean {
        if (!packageName.isProbablyValidPackageName()) return false

        return startFirstAvailable(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                packageName.toPackageUri(),
            ),
            Intent(Settings.ACTION_APPLICATION_SETTINGS),
            Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS),
            Intent(Settings.ACTION_SETTINGS),
        )
    }

    override fun openNotifications(packageName: String): Boolean {
        if (!packageName.isProbablyValidPackageName()) return false

        val notificationIntent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)

            // OEM/old Settings compatibility. Harmless if ignored.
            putExtra(LEGACY_EXTRA_APP_PACKAGE, packageName)
            putExtra(LEGACY_PROVIDER_EXTRA_APP_PACKAGE, packageName)
        }

        return startFirstAvailable(notificationIntent, appInfoIntent(packageName))
    }

    /*
     * There is no stable public cross-OEM intent for:
     * Settings > Apps > App > Permissions
     *
     * Some AOSP/private Settings fragments exist on some builds, but they are not API contracts.
     * For many OEMs they either do nothing, open the wrong page, or throw.
     */
    override fun openPermissions(packageName: String): Boolean = openAppInfo(packageName)

    /*
     * There is no stable public cross-OEM intent for:
     * Settings > Apps > App > Storage / Storage & cache
     *
     * ACTION_STORAGE_VOLUME_ACCESS_SETTINGS is not this screen and is deprecated.
     */
    override fun openStorage(packageName: String): Boolean = openAppInfo(packageName)

    /*
     * There is no stable public cross-OEM intent for:
     * Settings > Apps > App > Battery / App battery usage
     *
     * Battery saver and battery optimization actions are different, broader system pages.
     */
    override fun openBattery(packageName: String): Boolean = openAppInfo(packageName)

    override fun openPlayStore(packageName: String): Boolean {
        if (!packageName.isProbablyValidPackageName()) return false

        val marketUri = "${AppLinks.MARKET_APP_PAGE}$packageName".toUri()
        val webUrl = "${AppLinks.PLAY_STORE_APP}$packageName"
        val webUri = webUrl.toUri()

        return startFirstAvailable(
            Intent(Intent.ACTION_VIEW, marketUri).apply {
                setPackage(PLAY_STORE_PACKAGE_NAME)
            },
            Intent(Intent.ACTION_VIEW, marketUri),
            Intent(Intent.ACTION_VIEW, webUri),
        ) || context.openUrl(webUrl)
    }

    override fun shareApp(packageName: String, appName: String): Boolean {
        if (!packageName.isProbablyValidPackageName()) return false

        val playStoreUrl = "${AppLinks.PLAY_STORE_APP}$packageName"

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, appName)
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.app_details_share_text, appName, playStoreUrl),
            )
        }

        val chooser = Intent.createChooser(
            sendIntent,
            context.getString(R.string.app_details_share_title),
        )

        return startFirstAvailable(chooser)
    }

    override fun copyPackageName(packageName: String): Boolean {
        if (!packageName.isProbablyValidPackageName()) return false

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        clipboard.setPrimaryClip(
            ClipData.newPlainText(
                context.getString(R.string.app_details_package_name_clip_label),
                packageName,
            ),
        )

        Toast.makeText(
            context,
            context.getString(R.string.app_details_package_name_copied),
            Toast.LENGTH_SHORT,
        ).show()

        return true
    }

    override fun openUrl(url: String): Boolean = context.openUrl(url)

    override fun openNotificationChannel(packageName: String, channelId: String): Boolean {
        if (!packageName.isProbablyValidPackageName()) return false
        if (channelId.isBlank()) return openNotifications(packageName)

        val channelIntent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
        }

        val appNotificationIntent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }

        return startFirstAvailable(
            channelIntent,
            appNotificationIntent,
            appInfoIntent(packageName),
        )
    }

    override fun openByDefault(packageName: String): Boolean {
        if (!packageName.isProbablyValidPackageName()) return false

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return openAppInfo(packageName)
        }

        return startFirstAvailable(
            Intent(
                Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
                packageName.toPackageUri(),
            ),
            appInfoIntent(packageName),
        )
    }

    override fun openAppLocale(packageName: String): Boolean {
        if (!packageName.isProbablyValidPackageName()) return false

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return openAppInfo(packageName)
        }

        return startFirstAvailable(
            Intent(
                Settings.ACTION_APP_LOCALE_SETTINGS,
                packageName.toPackageUri(),
            ),
            appInfoIntent(packageName),
        )
    }

    override fun openAllFilesAccess(packageName: String): Boolean {
        if (!packageName.isProbablyValidPackageName()) return false

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return openAppInfo(packageName)
        }

        return startFirstAvailable(
            Intent(
                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                packageName.toPackageUri(),
            ),
            Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION),
            appInfoIntent(packageName),
        )
    }

    override fun openIgnoreBatteryOptimizationsList(): Boolean {
        return startFirstAvailable(
            Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS),
            Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS),
            Intent(Settings.ACTION_SETTINGS),
        )
    }

    private fun openAppWithIntentSender(packageName: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return false

        return try {
            val sender = context.packageManager.getLaunchIntentSenderForPackage(packageName)
            context.startIntentSender(sender, null, 0, 0, 0)
            true
        } catch (_: IntentSender.SendIntentException) {
            false
        } catch (_: ActivityNotFoundException) {
            false
        } catch (_: SecurityException) {
            false
        } catch (_: IllegalArgumentException) {
            false
        } catch (_: NullPointerException) {
            false
        }
    }

    private fun openAppWithLaunchIntent(packageName: String): Boolean {
        val launchIntent = context.packageManager
            .getLaunchIntentForPackage(packageName)
            ?.asExternalActivityIntent()

        return launchIntent != null && startFirstAvailable(launchIntent)
    }

    private fun appInfoIntent(packageName: String): Intent {
        return Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            packageName.toPackageUri(),
        )
    }

    private fun startFirstAvailable(vararg intents: Intent): Boolean {
        for (intent in intents) {
            val preparedIntent = intent.asExternalActivityIntent()

            try {
                context.startActivity(preparedIntent)
                return true
            } catch (_: ActivityNotFoundException) {
                // Try next fallback.
            } catch (_: SecurityException) {
                // Try next fallback.
            } catch (_: IllegalArgumentException) {
                // Try next fallback.
            } catch (_: NullPointerException) {
                // Some OEM Settings implementations are not very defensive.
            }
        }

        return false
    }

    private fun Intent.asExternalActivityIntent(): Intent {
        addCategory(Intent.CATEGORY_DEFAULT)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return this
    }

    private fun String.toPackageUri(): Uri = Uri.fromParts("package", this, null)

    private fun String.isProbablyValidPackageName(): Boolean {
        if (isBlank()) return false
        if (!contains('.')) return false

        return split('.').all { segment ->
            segment.isNotBlank() &&
                    segment.first().isLetter() &&
                    segment.all { char -> char.isLetterOrDigit() || char == '_' }
        }
    }
}
