package com.d4rk.android.libs.apptoolkit.core.utils.extensions.context

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.CheckResult
import androidx.annotation.StringRes
import androidx.core.net.toUri
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.intent.requireNewTask

/**
 * Safely starts an activity.
 *
 * This is the “truthy” launcher: it tries to launch and catches failures.
 * This avoids false negatives from resolveActivity() under Android 11+ package visibility. :contentReference[oaicite:6]{index=6}
 */
@CheckResult
fun Context.startActivitySafely(
    intent: Intent,
    addNewTaskFlag: Boolean = true,
    onFailure: (Throwable) -> Unit = {},
): Boolean {
    val launchIntent = if (addNewTaskFlag) intent.requireNewTask(this) else intent

    return try { // TODO: Make try catch to be runCatching
        startActivity(launchIntent)
        true
    } catch (t: ActivityNotFoundException) {
        onFailure(t)
        false
    } catch (t: SecurityException) {
        onFailure(t)
        false
    } catch (t: RuntimeException) {
        // Covers OEM/background launch restrictions, bad flags, etc.
        onFailure(t)
        false
    }
}

@CheckResult
fun Context.openActivity(activityClass: Class<*>): Boolean =
    startActivitySafely(Intent(this, activityClass))

@CheckResult
fun Context.openUrl(url: String): Boolean {
    val trimmed = url.trim()
    if (trimmed.isBlank()) return false

    val intent = Intent(Intent.ACTION_VIEW, trimmed.toUri()).apply {
        addCategory(Intent.CATEGORY_BROWSABLE)
    }
    return startActivitySafely(intent)
}

/**
 * Opens system display settings, with a fallback to general settings.
 */
@CheckResult
fun Context.openDisplaySettings(): Boolean {
    val display = Intent(Settings.ACTION_DISPLAY_SETTINGS)
    val general = Intent(Settings.ACTION_SETTINGS)
    return startActivitySafely(display) || startActivitySafely(general)
}

/**
 * Opens the Play Store page for [packageName], falling back to HTTPS.
 */
@CheckResult
fun Context.openPlayStoreForApp(packageName: String): Boolean {
    val market = Intent(Intent.ACTION_VIEW, "${AppLinks.MARKET_APP_PAGE}$packageName".toUri())
    if (startActivitySafely(market)) return true
    return openUrl("${AppLinks.PLAY_STORE_APP}$packageName")
}

/**
 * Opens this app's notification settings.
 *
 * minSdk=26 => we can use ACTION_APP_NOTIFICATION_SETTINGS on all devices.
 */
@CheckResult
fun Context.openAppNotificationSettings(): Boolean {
    val pkg = this.packageName

    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, pkg)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Optional, but harmless: some OEMs read the channel extra.
            putExtra(Settings.EXTRA_CHANNEL_ID, 0)
        }
    }
    return startActivitySafely(intent)
}

/**
 * Composes an email to the developer using ACTION_SENDTO (mailto:).
 */
@CheckResult
fun Context.sendEmailToDeveloper(
    @StringRes applicationNameRes: Int,
): Boolean {
    val developerEmail = AppLinks.CONTACT_EMAIL
    val appName = getString(applicationNameRes)

    val subject = getString(R.string.feedback_for, appName)
    val body = getString(R.string.dear_developer) + "\n\n"

    val mailtoUri: Uri = Uri.parse("mailto:$developerEmail").buildUpon()
        .appendQueryParameter("subject", subject)
        .appendQueryParameter("body", body)
        .build()

    val sendTo = Intent(Intent.ACTION_SENDTO, mailtoUri)
    val chooser = Intent.createChooser(sendTo, getString(R.string.send_email_using))
    return startActivitySafely(chooser)
}

/**
 * Shares a Play Store link for [packageName] using ACTION_SEND.
 */
@CheckResult
fun Context.shareApp(
    @StringRes shareMessageFormat: Int,
    packageName: String = this.packageName,
): Boolean {
    val message = getString(shareMessageFormat, "${AppLinks.PLAY_STORE_APP}$packageName")

    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }

    val chooser = Intent.createChooser(send, getString(R.string.send_email_using))
    return startActivitySafely(chooser)
}
