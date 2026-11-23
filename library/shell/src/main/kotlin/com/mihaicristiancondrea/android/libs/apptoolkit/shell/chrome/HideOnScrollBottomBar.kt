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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import kotlin.math.roundToInt

/**
 * Keeps the bottom bar in step with [scrollBehavior], sliding it down out of the window as the
 * content scrolls down and back up as it scrolls up. The reported height shrinks with it, so the
 * content above grows into the space instead of leaving a gap.
 */
@Composable
internal fun HideOnScrollBottomBar(
    scrollBehavior: BottomAppBarScrollBehavior?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            val offset = scrollBehavior?.state?.let { state ->
                state.heightOffsetLimit = -placeable.height.toFloat()
                state.heightOffset
            } ?: 0f
            val height = (placeable.height + offset).roundToInt().coerceAtLeast(0)
            layout(placeable.width, height) { placeable.place(0, 0) }
        },
    ) {
        content()
    }
}
