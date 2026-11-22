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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.extensions

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * Returns `true` when this [Activity] is in a safe state to start a Play Billing UI flow.
 *
 * Rejects activities that are finishing, destroyed, or undergoing a configuration change, and
 * requires [Lifecycle.State.RESUMED] when the activity implements [LifecycleOwner].
 */
fun Activity.isValidForBilling(): Boolean {
    if (isFinishing || isDestroyed || isChangingConfigurations) return false
    val lifecycleOwner: LifecycleOwner = this as? LifecycleOwner ?: return true
    return lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
}
