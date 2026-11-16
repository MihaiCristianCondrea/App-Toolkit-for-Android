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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

/**
 * How far an app bar has slid away, for [HideOnScrollTopBar]. Attach [nestedScrollConnection] to
 * the scaffold after the bar's own scroll behaviour, so a large bar collapses first and only then
 * slides away; scrolling back up brings it back at once, as Material's enter-always bars do.
 */
@Stable
class TopBarHideState {
    /** How far the bar has moved up, from 0 to [limit]. */
    var offset: Float by mutableFloatStateOf(0f)
        private set

    /** The furthest the bar can move up: all of its height, the status bar's part included. */
    var limit: Float by mutableFloatStateOf(0f)
        internal set

    val nestedScrollConnection: NestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val previous = offset
            offset = (offset + available.y).coerceIn(limit, 0f)
            // What the bar took, the content does not scroll: its own height gives the room.
            return Offset(0f, offset - previous)
        }
    }

    /** Brings the bar back in full, as a newly opened destination starts. */
    fun show() {
        offset = 0f
    }
}
