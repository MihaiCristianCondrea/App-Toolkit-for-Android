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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode

/** What the navigation beside the content shares with the shell drawn next to it. */
@Stable
internal class ShellFrameState(railExpanded: Boolean) {
    /** Whether the expanded rail layout shows its rail open, with labels, or collapsed. */
    var railExpanded: Boolean by mutableStateOf(railExpanded)

    /** Whether the rail layout's modal rail is open over the content. */
    var railOverlayOpen: Boolean by mutableStateOf(false)

    /**
     * How far the navigation and the app bar have taken the shared colour, from 0 to 1. The frame
     * draws it; the app bar drives it when the colour follows scrolling.
     */
    val tint: Animatable<Float, AnimationVector1D> = Animatable(0f)

    /** Opens the navigation the way [mode] does: the modal rail, or the expanded rail toggled. */
    fun openNavigation(mode: ShellLayoutMode) {
        when (mode) {
            ShellLayoutMode.Rail -> railOverlayOpen = true
            ShellLayoutMode.ExpandedRail -> railExpanded = !railExpanded
            else -> Unit
        }
    }
}
