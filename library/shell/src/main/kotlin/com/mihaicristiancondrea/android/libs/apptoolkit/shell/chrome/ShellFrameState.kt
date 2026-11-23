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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode

/** What the navigation beside the content shares with the shell drawn next to it. */
@Stable
internal class ShellFrameState(railExpanded: Boolean, initialTint: Float = 0f) {
    /** Whether the expanded rail layout shows its rail open, with labels, or collapsed. */
    var railExpanded: Boolean by mutableStateOf(railExpanded)

    /** Whether the rail layout's modal rail is open over the content. */
    var railOverlayOpen: Boolean by mutableStateOf(false)

    /**
     * How far the navigation and the app bar have taken the shared colour, from 0 to 1. The frame
     * draws it; the app bar drives it when the colour follows scrolling. It starts at the
     * `initialTint` the first frame needs, so a navigation tint that is always on is not drawn
     * untinted before the frame settles it.
     */
    val tint: Animatable<Float, AnimationVector1D> = Animatable(initialTint)

    /** Opens the navigation the way [mode] does: the modal rail, or the expanded rail toggled. */
    fun openNavigation(mode: ShellLayoutMode) {
        when (mode) {
            ShellLayoutMode.Rail -> railOverlayOpen = true
            ShellLayoutMode.ExpandedRail -> railExpanded = !railExpanded
            else -> Unit
        }
    }
}

/**
 * The frame's state for the layout [mode] the window has now. The expanded rail starts collapsed,
 * and collapses again whenever the layout changes, as a rotation does: it is opened for the
 * window it was opened in. Within one layout it keeps what its button left it at, across
 * recreation and process death. [initialTint] is the frame tint of the first frame.
 */
@Composable
internal fun rememberShellFrameState(mode: ShellLayoutMode, initialTint: Float): ShellFrameState {
    var railExpanded by rememberSaveable { mutableStateOf(false) }
    var expandedIn by rememberSaveable { mutableStateOf(mode) }
    val state = remember {
        ShellFrameState(railExpanded = railExpanded && expandedIn == mode, initialTint = initialTint)
    }
    LaunchedEffect(mode) {
        if (mode != expandedIn) {
            state.railExpanded = false
            expandedIn = mode
        }
    }
    LaunchedEffect(state.railExpanded) { railExpanded = state.railExpanded }
    return state
}
