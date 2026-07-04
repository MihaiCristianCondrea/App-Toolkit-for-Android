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

package com.d4rk.android.apps.apptoolkit.app.tiles.service

import android.accessibilityservice.AccessibilityService
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import java.lang.ref.WeakReference

/**
 * Accessibility bridge for Quick Tools that require Android system global actions.
 *
 * The service intentionally does not inspect window content or gestures; it only keeps a weak
 * reference to the currently connected service instance so feature code can request supported
 * global actions after the user explicitly enables the accessibility service.
 */
class TileAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        activeService = WeakReference(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit

    override fun onInterrupt() = Unit

    override fun onDestroy() {
        if (activeService?.get() == this) {
            activeService = null
        }
        super.onDestroy()
    }

    companion object {
        private var activeService: WeakReference<TileAccessibilityService>? = null

        /** Returns whether the accessibility bridge is currently connected and ready. */
        fun isActive(): Boolean = activeService?.get() != null

        /** Requests Android's screenshot global action when the platform supports it. */
        fun takeScreenshot(): Boolean = performGlobalActionIfAvailable(
            minSdk = Build.VERSION_CODES.P,
            action = AccessibilityService.GLOBAL_ACTION_TAKE_SCREENSHOT,
        )

        /** Requests Android's lock-screen global action when the platform supports it. */
        fun lockScreen(): Boolean = performGlobalActionIfAvailable(
            minSdk = Build.VERSION_CODES.P,
            action = AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN,
        )

        /** Opens Android's power dialog through the accessibility global action API. */
        fun openPowerMenu(): Boolean = performGlobalActionIfAvailable(
            minSdk = Build.VERSION_CODES.LOLLIPOP,
            action = AccessibilityService.GLOBAL_ACTION_POWER_DIALOG,
        )

        private fun performGlobalActionIfAvailable(minSdk: Int, action: Int): Boolean {
            if (Build.VERSION.SDK_INT < minSdk) return false
            return activeService?.get()?.performGlobalAction(action) == true
        }
    }
}
