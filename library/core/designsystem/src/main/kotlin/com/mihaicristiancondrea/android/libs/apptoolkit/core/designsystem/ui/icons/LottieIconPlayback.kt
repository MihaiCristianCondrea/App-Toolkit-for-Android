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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons

/** Keeps click replays distinct from selection changes, including clicks before JSON finishes loading. */
internal class LottieIconPlayback(private var restingAtEnd: Boolean) {
    private var lastClickCount = 0
    private var targetAtEnd = restingAtEnd

    fun target(atEnd: Boolean, clickCount: Int, mode: ToolkitIconReplayMode): LottiePlaybackTarget {
        val clicked = clickCount != lastClickCount
        val selectionChanged = atEnd != restingAtEnd
        targetAtEnd = when {
            clicked && mode == ToolkitIconReplayMode.Restart -> true
            selectionChanged -> atEnd
            clicked -> !targetAtEnd
            else -> targetAtEnd
        }
        restingAtEnd = atEnd
        lastClickCount = clickCount
        return LottiePlaybackTarget(if (targetAtEnd) 1f else 0f, clicked && mode == ToolkitIconReplayMode.Restart)
    }
}
