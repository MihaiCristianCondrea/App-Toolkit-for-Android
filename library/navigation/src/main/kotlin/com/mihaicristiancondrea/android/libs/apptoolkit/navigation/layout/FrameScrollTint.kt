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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf

/**
 * The app bars beside the navigation that follow scrolling, in the order they appeared: the one on
 * top decides the frame's colour. The shell's tabs sit underneath, so a page the navigation opens
 * takes over while it is shown and hands back to the tab when it leaves.
 *
 * Bars join with [FollowScrollWithFrameTint]; the shell reads [current].
 */
@Stable
class FrameScrollTint {
    private val drivers = mutableStateListOf<() -> FrameTint>()

    /** What the bar on top asks for, or [FrameTint.None] when none is shown. */
    val current: FrameTint
        get() = drivers.lastOrNull()?.invoke() ?: FrameTint.None

    internal fun add(driver: () -> FrameTint, underneath: Boolean) {
        if (underneath) drivers.add(0, driver) else drivers.add(driver)
    }

    internal fun remove(driver: () -> FrameTint) {
        drivers.remove(driver)
    }
}
