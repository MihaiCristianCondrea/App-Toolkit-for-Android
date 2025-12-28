package com.d4rk.android.libs.apptoolkit.core.utils.extensions.context

import android.Manifest
import android.app.Activity
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
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.canResolveActivityCompat
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.hasPackage

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

    if (clipboard == null) {
        Log.w(CLIPBOARD_HELPER_LOG_TAG, "Clipboard service unavailable")
        return
    }

    val clip: ClipData = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
        onShowSnackbar()
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
): Boolean {
    val canHandle = when {
        intent.component != null -> true
        intent.action == Intent.ACTION_CHOOSER -> true
        else -> packageManager.canResolveActivityCompat(intent)
    }

    if (!canHandle) {
        onFailure(null)
        return false
    }

    val launchIntent =
        intent.takeUnless { addNewTaskFlag && this !is Activity }
            ?: Intent(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    return runCatching { startActivity(launchIntent) }
        .onFailure(onFailure)
        .isSuccess
}

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
