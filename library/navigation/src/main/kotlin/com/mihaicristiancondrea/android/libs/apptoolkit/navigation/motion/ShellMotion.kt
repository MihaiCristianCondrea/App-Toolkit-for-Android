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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

/** The shell's transitions, scaled by the developer options' animation speed. */
@Immutable
class ShellMotion internal constructor(
    internal val activity: ActivityTransitions,
    /** The transitions between tabs, and between a tab and the page beside the navigation. */
    val tabs: TabTransitions,
    /** The transitions of children and pages, by [ScreenTransition]. */
    val screens: ScreenTransitions,
    /** The developer options' animation speed: 1 is normal, 0 plays no motion. */
    val durationScale: Float,
    /**
     * Whether a back swipe from the right edge mirrors one from the left, following the finger,
     * instead of Android's default shrink in place.
     */
    val followFingerFromRight: Boolean = false,
)

/**
 * The shell's [ShellMotion] for [tabStyle], scaled by [durationScale], with every slide mirrored in
 * right-to-left layouts. Rebuilt only when an input or the density changes.
 *
 * @param followFingerFromRight See [ShellMotion.followFingerFromRight].
 */
@Composable
fun rememberShellMotion(
    tabStyle: TabTransitionStyle,
    durationScale: Float,
    followFingerFromRight: Boolean = false,
): ShellMotion {
    val density = LocalDensity.current
    val direction = if (LocalLayoutDirection.current == LayoutDirection.Rtl) -1 else 1
    return remember(density, direction, tabStyle, durationScale, followFingerFromRight) {
        with(density) {
            val activity = ActivityTransitions(
                enteringOffsetPx = ActivityMotion.EnteringStartOffset.roundToPx(),
                displayMarginPx = ActivityMotion.DisplayBoundsMargin.roundToPx(),
                durationScale = durationScale,
                direction = direction,
            )
            ShellMotion(
                activity = activity,
                tabs = TabTransitions(tabStyle, durationScale, direction),
                screens = ScreenTransitions(activity, durationScale, direction),
                durationScale = durationScale,
                followFingerFromRight = followFingerFromRight,
            )
        }
    }
}

/** The shell's motion. Only available inside `ShellHost`. */
val LocalShellMotion = staticCompositionLocalOf<ShellMotion> {
    error("ShellMotion is only available inside ShellHost.")
}
