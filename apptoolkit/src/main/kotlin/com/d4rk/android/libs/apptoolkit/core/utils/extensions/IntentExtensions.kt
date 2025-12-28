package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.CheckResult

/**
 * Ensures [Intent.FLAG_ACTIVITY_NEW_TASK] is present when launching from a non-activity
 * [Context], returning the original intent when no changes are required.
 */
fun Intent.requireNewTask(context: Context): Intent =
    takeUnless { context is Activity || hasNewTaskFlag() }?.let { source ->
        Intent(source).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    } ?: this

/**
 * Safely launches an [Intent], optionally validating for available handlers and adding the
 * new-task flag when needed.
 */
@CheckResult
fun Context.launchIntentSafely(
    intent: Intent,
    addNewTaskFlag: Boolean = true,
    requireResolver: Boolean = true,
    onFailure: (Throwable?) -> Unit = {},
): Boolean {
    val launchIntent = if (addNewTaskFlag) intent.requireNewTask(this) else intent
    if (launchIntent.needsResolution(requireResolver) &&
        !packageManager.canResolveActivityCompat(launchIntent)
    ) {
        onFailure(null)
        return false
    }

    return runCatching { startActivity(launchIntent) }
        .onFailure(onFailure)
        .isSuccess
}

private fun Intent.needsResolution(requireResolver: Boolean): Boolean =
    requireResolver && component == null && action != Intent.ACTION_CHOOSER

private fun Intent.hasNewTaskFlag(): Boolean =
    flags and Intent.FLAG_ACTIVITY_NEW_TASK == Intent.FLAG_ACTIVITY_NEW_TASK
