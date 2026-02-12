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

package com.d4rk.android.libs.apptoolkit.core.ui.window

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.window.core.layout.WindowSizeClass

/**
 * Application-specific extension of the window width breakpoints.
 *
 * The default Material breakpoints only expose up to the expanded bucket. This enum adds the
 * recommended Large and ExtraLarge classes so layouts can react to wider viewports such as tablets,
 * desktop windows, or connected displays.
 */
enum class AppWindowWidthSizeClass {
    Compact,
    Medium,
    Expanded,
    Large,
    ExtraLarge
}

/**
 * Remembers the current [WindowAdaptiveInfo], opting into the Large and ExtraLarge width
 * breakpoints.
 */
@Composable
fun rememberWindowAdaptiveInfo(
    supportLargeAndXLargeWidth: Boolean = true,
): WindowAdaptiveInfo =
    currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = supportLargeAndXLargeWidth)

/**
 * Remembers the current [WindowSizeClass] calculated from the active window metrics.
 */
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val adaptiveInfo: WindowAdaptiveInfo = rememberWindowAdaptiveInfo()
    return remember(adaptiveInfo) { adaptiveInfo.windowSizeClass }
}

/**
 * Returns the current [AppWindowWidthSizeClass] calculated from the active window metrics.
 */
@Composable
fun rememberWindowWidthSizeClass(): AppWindowWidthSizeClass {
    val windowSizeClass: WindowSizeClass = rememberWindowSizeClass()
    return remember(windowSizeClass) { windowSizeClass.toAppWindowWidthSizeClass() }
}

/**
 * Maps the raw [WindowSizeClass] breakpoints to the extended [AppWindowWidthSizeClass] values.
 */
fun WindowSizeClass.toAppWindowWidthSizeClass(): AppWindowWidthSizeClass = when {
    isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXTRA_LARGE_LOWER_BOUND) -> AppWindowWidthSizeClass.ExtraLarge
    isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_LARGE_LOWER_BOUND) -> AppWindowWidthSizeClass.Large
    isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> AppWindowWidthSizeClass.Expanded
    isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> AppWindowWidthSizeClass.Medium
    else -> AppWindowWidthSizeClass.Compact
}
