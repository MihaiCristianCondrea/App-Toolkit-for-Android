package com.d4rk.android.libs.apptoolkit.core.utils.extensions.intent

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Ensures FLAG_ACTIVITY_NEW_TASK is set when launching from a non-Activity [Context].
 *
 * Mutates and returns the same [Intent] instance for convenience.
 */
fun Intent.requireNewTask(context: Context): Intent = apply {
    if (context !is Activity && (flags and Intent.FLAG_ACTIVITY_NEW_TASK) == 0) {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
