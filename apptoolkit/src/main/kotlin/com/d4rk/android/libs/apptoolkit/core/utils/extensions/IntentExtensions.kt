package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Ensures [Intent.FLAG_ACTIVITY_NEW_TASK] is present when launching from a non-activity
 * [Context], returning the original intent when no changes are required.
 */
fun Intent.requireNewTask(context: Context): Intent {
    if (context is Activity || hasNewTaskFlag()) return this
    return Intent(this).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

private fun Intent.hasNewTaskFlag(): Boolean =
    flags and Intent.FLAG_ACTIVITY_NEW_TASK == Intent.FLAG_ACTIVITY_NEW_TASK
