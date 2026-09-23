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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.R

/**
 * Puts [SeasonalThemeOverlay] over every activity of the app: snow with the Christmas palette, and
 * the holiday greeting on the first screen opened during a holiday.
 *
 * Like shake-to-report, this is application-scoped so no activity has to opt in or copy anything.
 * When an activity resumes, a full-size `ComposeView` is added on top of its content view, once.
 *
 * The overlay is only ever added above content that already exists. `ComponentActivity.setContent`
 * reuses the content view's first child when that child is a `ComposeView`, so an overlay added to
 * an activity that sets its content late (after an asynchronous startup check, for example) was
 * taken over as the activity's own content, without the view tree owners `setContent` normally
 * installs, and navigation crashed. Activities that have no content yet get the overlay after the
 * layout that follows their `setContent`. A later `setContentView` clears the content view, and the
 * overlay is put back on top the same way.
 *
 * The overlay only draws: a Compose view with no pointer input returns false from
 * `dispatchTouchEvent`, so every touch reaches the screen underneath. It is also hidden from
 * accessibility services and cannot take focus, so it never interrupts a screen reader or keyboard.
 *
 * Activities from Google Play services, Play Billing and other SDKs are left alone. They are not
 * the app's screens, and styling them is not ours to do.
 *
 * Nothing happens until [install] is called, normally from `Application.onCreate`.
 */
class SeasonalThemeManager(
    private val application: Application,
) : Application.ActivityLifecycleCallbacks {

    private var installed: Boolean = false

    /** Starts following the app's activities. Calling it again does nothing. */
    fun install() {
        if (installed) return
        application.registerActivityLifecycleCallbacks(this)
        installed = true
    }

    /** Stops adding the overlay to activities resumed from now on. */
    fun uninstall() {
        if (!installed) return
        application.unregisterActivityLifecycleCallbacks(this)
        installed = false
    }

    override fun onActivityResumed(activity: Activity) {
        if (!isEligible(activity)) return
        val content: ViewGroup = activity.findViewById(android.R.id.content) ?: return
        watchContent(activity = activity, content = content)
        attachOverlay(activity = activity, content = content)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit

    private fun isEligible(activity: Activity): Boolean =
        activity is ComponentActivity &&
            !activity.isFinishing &&
            !activity.isDestroyed &&
            !isThirdPartyActivity(activity)

    /**
     * Re-checks the content view whenever it lays out, which is what adding the activity's content
     * or replacing it causes. The listener is registered once per content view and dies with it.
     */
    private fun watchContent(activity: Activity, content: ViewGroup) {
        if (content.getTag(R.id.seasonal_theme_overlay_watcher) != null) return
        val listener = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            // Adding a view from inside a layout pass would request another layout mid-pass.
            content.post {
                if (isEligible(activity)) attachOverlay(activity = activity, content = content)
            }
        }
        content.setTag(R.id.seasonal_theme_overlay_watcher, listener)
        content.addOnLayoutChangeListener(listener)
    }

    private fun attachOverlay(activity: Activity, content: ViewGroup) {
        if (activity !is ComponentActivity) return
        // Never be the first child: setContent would adopt the overlay as the activity's content.
        if (content.childCount == 0) return
        if (hasOverlay(content)) return

        val overlay = ComposeView(activity).apply {
            tag = OVERLAY_TAG
            id = R.id.seasonal_theme_overlay
            setViewTreeLifecycleOwner(activity)
            setViewTreeViewModelStoreOwner(activity)
            setViewTreeSavedStateRegistryOwner(activity)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
            descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
            isFocusable = false
            isClickable = false
            setContent {
                AppTheme {
                    SeasonalThemeOverlay()
                }
            }
        }

        content.addView(
            overlay,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT),
        )
    }

    /** Looks at direct children only, so the check stays cheap on every layout. */
    private fun hasOverlay(content: ViewGroup): Boolean =
        (0 until content.childCount).any { index -> content.getChildAt(index).tag == OVERLAY_TAG }

    private fun isThirdPartyActivity(activity: Activity): Boolean {
        val name: String = activity.javaClass.name
        return THIRD_PARTY_PREFIXES.any { prefix -> name.startsWith(prefix) }
    }

    private companion object {
        /** Marks the view this manager added, so it is added once per activity. */
        const val OVERLAY_TAG: String =
            "com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.SeasonalOverlay"

        val THIRD_PARTY_PREFIXES: List<String> = listOf(
            "com.google.android.",
            "com.android.billingclient.",
            "com.google.firebase.",
        )
    }
}
