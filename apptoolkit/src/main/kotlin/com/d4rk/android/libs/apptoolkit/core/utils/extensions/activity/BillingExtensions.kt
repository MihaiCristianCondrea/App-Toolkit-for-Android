package com.d4rk.android.libs.apptoolkit.core.utils.extensions.activity

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * Checks if the current Activity is in a valid state to perform billing operations.
 *
 * This typically verifies that the Activity is not finishing or destroyed to ensure
 * that UI interactions related to the billing flow can be safely initiated.
 *
 * @return `true` if the Activity is valid for billing operations, `false` otherwise.
 */
fun Activity.isValidForBilling(): Boolean {
    if (isFinishing || isDestroyed) return false
    val lifecycleOwner = this as? LifecycleOwner ?: return true
    return lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
}