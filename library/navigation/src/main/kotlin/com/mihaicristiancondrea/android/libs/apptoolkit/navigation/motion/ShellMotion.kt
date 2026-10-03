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
    val activity: ActivityTransitions,
    val tabs: TabTransitions,
    val screens: ScreenTransitions,
    val durationScale: Float,
    /**
     * Whether a back swipe from the right edge mirrors one from the left, following the finger,
     * as it does by default, instead of Android's shrink in place.
     */
    val followFingerFromRight: Boolean = true,
)

@Composable
fun rememberShellMotion(
    tabStyle: TabTransitionStyle,
    durationScale: Float,
    followFingerFromRight: Boolean = true,
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

val LocalShellMotion = staticCompositionLocalOf<ShellMotion> {
    error("ShellMotion is only available inside ShellHost.")
}
