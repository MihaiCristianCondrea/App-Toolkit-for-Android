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

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.Layout
import kotlin.math.roundToInt

/**
 * Whether app bars slide away as content scrolls down, whatever their style. The shell provides
 * it from its settings; `PageScaffold` and the shell's tab scaffold read it.
 */
val LocalHideTopBarOnScroll = staticCompositionLocalOf { false }

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

    /** The furthest the bar can move up: its height, less the status bar strip it keeps. */
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

@Composable
fun rememberTopBarHideState(): TopBarHideState = remember { TopBarHideState() }

/**
 * Lays out [bar] less the part of it that [state] has slid away, so the scaffold's content moves
 * up with it. [windowInsets] is what the bar keeps clear of; its top stays, so the status bar
 * keeps the bar's colour behind it. With [enabled] false the bar is laid out whole.
 */
@Composable
fun HideOnScrollTopBar(
    state: TopBarHideState,
    enabled: Boolean,
    windowInsets: WindowInsets,
    bar: @Composable () -> Unit,
) {
    Layout(content = bar, modifier = Modifier.clipToBounds()) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints.copy(minHeight = 0)) }
        val width = placeables.maxOfOrNull { it.width } ?: constraints.minWidth
        val height = placeables.maxOfOrNull { it.height } ?: 0
        val keep = windowInsets.getTop(this).coerceAtMost(height)
        val limit = -(height - keep).toFloat()
        if (state.limit != limit) state.limit = limit
        val offset = if (enabled) state.offset.coerceIn(limit, 0f).roundToInt() else 0
        layout(width, height + offset) {
            placeables.forEach { it.place(0, offset) }
        }
    }
}
