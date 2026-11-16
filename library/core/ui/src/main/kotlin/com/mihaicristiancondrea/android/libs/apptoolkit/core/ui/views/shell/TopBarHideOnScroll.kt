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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import kotlin.math.roundToInt

/**
 * Whether app bars slide away as content scrolls down, whatever their style. The shell provides
 * it from its settings; `PageScaffold` and the shell's tab scaffold read it.
 */
val LocalHideTopBarOnScroll = staticCompositionLocalOf { false }

@Composable
fun rememberTopBarHideState(): TopBarHideState = remember { TopBarHideState() }

/**
 * Lays out [bar] less the part of it that [state] has slid away, so the scaffold's content moves
 * up with it. The bar leaves entirely, the part behind the status bar too, and the content scrolls
 * on under the status bar, edge to edge. With [enabled] false the bar is laid out whole.
 */
@Composable
fun HideOnScrollTopBar(
    state: TopBarHideState,
    enabled: Boolean,
    bar: @Composable () -> Unit,
) {
    Layout(content = bar, modifier = Modifier.clipToBounds()) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints.copy(minHeight = 0)) }
        val width = placeables.maxOfOrNull { it.width } ?: constraints.minWidth
        val height = placeables.maxOfOrNull { it.height } ?: 0
        val limit = -height.toFloat()
        if (state.limit != limit) state.limit = limit
        val offset = if (enabled) state.offset.coerceIn(limit, 0f).roundToInt() else 0
        layout(width, height + offset) {
            placeables.forEach { it.place(0, offset) }
        }
    }
}
