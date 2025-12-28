package com.d4rk.android.libs.apptoolkit.core.utils.extensions.context

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.CheckResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.d4rk.android.libs.apptoolkit.core.logging.CLIPBOARD_HELPER_LOG_TAG
import com.d4rk.android.libs.apptoolkit.core.utils.constants.store.StoreConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.hasPackage
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.launchIntentSafely

/**
 * Traverses the context chain and returns the first [ComponentActivity] if present.
 */
fun Context.findActivity(): ComponentActivity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is AppCompatActivity) return ctx
        if (ctx is ComponentActivity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

fun Context.copyTextToClipboard(
    label: String,
    text: String,
    onShowSnackbar: () -> Unit = {},
) {
    val clipboard: ClipboardManager? = runCatching {
        getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    }.getOrNull()

    clipboard?.let { manager ->
        val clip: ClipData = ClipData.newPlainText(label, text)
        manager.setPrimaryClip(clip)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            onShowSnackbar()
        }
    } ?: run {
        Log.w(CLIPBOARD_HELPER_LOG_TAG, "Clipboard service unavailable")
    }
}

/**
 * Starts an activity defensively.
 *
 * - For implicit intents, checks if there is a handler to avoid ActivityNotFoundException.
 * - When called from a non-Activity Context, optionally adds FLAG_ACTIVITY_NEW_TASK.
 *
 * @return true if startActivity() succeeded, false otherwise.
 */
@CheckResult
fun Context.safeStartActivity(
    intent: Intent,
    addNewTaskFlag: Boolean = true,
    onFailure: (Throwable?) -> Unit = {},
): Boolean = launchIntentSafely(
    intent = intent,
    addNewTaskFlag = addNewTaskFlag,
    requireResolver = true,
    onFailure = onFailure,
)

fun Context.hasNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}

fun Context.hasPlayStore(): Boolean =
    packageManager.hasPackage(StoreConstants.PLAY_STORE_PACKAGE)

fun Context.isInstalledFromPlayStore(): Boolean =
    installingPackageNameOrNull() == StoreConstants.PLAY_STORE_PACKAGE

fun Context.installingPackageNameOrNull(): String? =
    runCatching {
        val pm = packageManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            pm.getInstallSourceInfo(packageName).installingPackageName
        } else {
            @Suppress("DEPRECATION")
            pm.getInstallerPackageName(packageName)
        }
    }.getOrNull()
