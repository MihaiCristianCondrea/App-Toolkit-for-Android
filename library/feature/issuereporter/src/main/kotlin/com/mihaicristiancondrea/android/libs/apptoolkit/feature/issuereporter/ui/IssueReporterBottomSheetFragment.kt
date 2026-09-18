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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme

/**
 * The container the issue reporter is presented in.
 *
 * It exists because the reporter has more than one entry point and only one of them is inside a
 * composition. Advanced settings could host a Compose `ModalBottomSheet` directly, but the shake
 * gesture is detected by an application-scoped listener that has an `Activity` and no composition
 * to attach to. A fragment can be shown from either, so both go through
 * [IssueReporterLauncher][com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.presentation.IssueReporterLauncher]
 * and reach the same [IssueReporterContent], rather than the feature carrying one presentation for
 * settings and a second one for the gesture.
 *
 * The fragment is deliberately empty otherwise: it is a window, and everything visible inside it is
 * Compose. It is not part of the module's public API; hosts open the reporter through the launcher.
 */
internal class IssueReporterBottomSheetFragment : BottomSheetDialogFragment() {

    /**
     * Hosts the reporter in a `ComposeView` built against `requireContext()`.
     *
     * The context matters. A dialog fragment's inflater carries the dialog's own themed wrapper,
     * and `AppTheme` reaches the window through `LocalView.current.context as Activity`, a cast
     * that wrapper does not survive.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            AppTheme {
                IssueReporterContent()
            }
        }
    }

    /**
     * Opens expanded and stays there.
     *
     * The description field plus a keyboard needs most of the screen, and a half-expanded sheet
     * would put the author's own text behind the IME on first focus. `skipCollapsed` also keeps a
     * downward drag a dismissal rather than a stop at the peek height.
     */
    override fun onStart() {
        super.onStart()
        val bottomSheetDialog = dialog as? BottomSheetDialog ?: return
        bottomSheetDialog.behavior.apply {
            skipCollapsed = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}
