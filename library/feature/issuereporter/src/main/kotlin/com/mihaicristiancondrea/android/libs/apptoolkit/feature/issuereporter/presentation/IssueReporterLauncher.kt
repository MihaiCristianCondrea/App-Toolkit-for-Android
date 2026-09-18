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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.presentation

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.IssueReporterBottomSheet

/**
 * Opens the report sheet for a caller that has an `Activity` but no composition.
 *
 * The shake gesture is detected by an application-scoped sensor listener, which cannot compose
 * anything. This mounts a `ComposeView` on the activity's content view and puts the same
 * [IssueReporterBottomSheet] in it, so the gesture and a host that composes the sheet itself show
 * the identical sheet rather than the feature carrying two presentations.
 *
 * A host that is already composing should call [IssueReporterBottomSheet] directly. This exists for
 * callers that cannot.
 */
object IssueReporterLauncher {

    /** Marks the view this launcher added, so it can be found again and removed. */
    private const val OVERLAY_TAG: String =
        "com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.Overlay"

    /**
     * Shows the reporter over [activity].
     *
     * Returns false without doing anything when it cannot be shown: [activity] is not a
     * [ComponentActivity], it is going away, or a sheet is already up, including one a host
     * composed itself. The shake detector calls this from a sensor callback, so the activity can be
     * in any of those states by the time a gesture lands, and none of them is worth taking an app
     * down for.
     *
     * Must be called on the main thread, which is where sensor callbacks are delivered.
     */
    fun show(activity: Activity): Boolean {
        if (activity !is ComponentActivity) return false
        if (activity.isFinishing || activity.isDestroyed) return false
        if (IssueReporterPresence.isShowing) return false

        val content: ViewGroup = activity.findViewById(android.R.id.content) ?: return false
        if (content.findViewWithTag<View?>(OVERLAY_TAG) != null) return false

        val host = ComposeView(activity).apply {
            tag = OVERLAY_TAG
            // The activity's decor view normally carries these already, but only once it has set
            // content. Naming them here means the sheet's ViewModel resolves the same way no matter
            // what the host activity has done with its own window.
            setViewTreeLifecycleOwner(activity)
            setViewTreeViewModelStoreOwner(activity)
            setViewTreeSavedStateRegistryOwner(activity)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        }

        host.setContent {
            AppTheme {
                // The sheet lives in its own window, so this view contributes no layout and is left
                // to wrap to nothing. A view that filled the activity would sit over the screen
                // behind the sheet with nothing to draw and touches to swallow.
                IssueReporterBottomSheet(onDismissRequest = { content.removeView(host) })
            }
        }

        content.addView(
            host,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            ),
        )
        return true
    }
}
