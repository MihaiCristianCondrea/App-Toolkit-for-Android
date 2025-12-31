package com.d4rk.android.libs.apptoolkit.core.utils.extensions.context

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

/**
 * Traverses the context chain and returns the first [ComponentActivity] if present.
 */
fun Context.findActivity(): ComponentActivity? {
    var context: Context = this
    while (true) {
        when (context) {
            is ComponentActivity -> return context
            is ContextWrapper -> {
                val base = context.baseContext ?: return null
                if (base === context) return null
                context = base
            }

            else -> return null
        }
    }
}