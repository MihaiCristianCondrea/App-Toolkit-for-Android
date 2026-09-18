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
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.IssueReporterBottomSheetFragment

/**
 * The single way to open the issue reporter.
 *
 * Every entry point calls this: the advanced settings row, the shake gesture, and anything a host
 * adds later. Keeping one launcher is what keeps one presentation, so a change to how the reporter
 * is shown is made once instead of once per caller.
 */
object IssueReporterLauncher {

    /** Fragment tag the sheet is shown under, and the handle used to detect it is already open. */
    const val FRAGMENT_TAG: String =
        "com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.BottomSheet"

    /**
     * Shows the reporter over [activity], unless it is already showing there.
     *
     * Returns false without doing anything when the reporter cannot be shown: [activity] is not a
     * [FragmentActivity], it is going away, its state is already saved, or the sheet is open. The
     * shake detector calls this from a sensor callback, so the activity can be in any of those
     * states by the time a gesture lands, and none of them is an error worth crashing an app over.
     */
    fun show(activity: Activity): Boolean {
        val fragmentActivity = activity as? FragmentActivity ?: return false
        if (fragmentActivity.isFinishing || fragmentActivity.isDestroyed) return false

        val fragmentManager: FragmentManager = fragmentActivity.supportFragmentManager
        if (fragmentManager.isStateSaved || fragmentManager.isDestroyed) return false
        if (fragmentManager.findFragmentByTag(FRAGMENT_TAG) != null) return false

        IssueReporterBottomSheetFragment().show(fragmentManager, FRAGMENT_TAG)
        return true
    }
}
