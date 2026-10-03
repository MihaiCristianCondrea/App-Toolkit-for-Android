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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

/**
 * Collapses extended FAB labels when content scrolls forward and expands them on reverse
 * scrolling. Only consumed motion counts, so overscroll at either end does not change
 * [expanded].
 *
 * Attach [nestedScrollConnection] to content and pass [expanded] to [ToolkitFabColumn].
 */
@Stable
class FabScrollBehavior {
    /** Whether extended buttons show their label. */
    var expanded: Boolean by mutableStateOf(true)

    val nestedScrollConnection: NestedScrollConnection = object : NestedScrollConnection {
        override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
            when {
                consumed.y < -ScrollThreshold -> expanded = false
                consumed.y > ScrollThreshold -> expanded = true
            }
            return Offset.Zero
        }
    }
}

@Composable
fun rememberFabScrollBehavior(): FabScrollBehavior = remember { FabScrollBehavior() }

/** Pixels of scrolling below which a jitter is ignored. */
private const val ScrollThreshold = 1f
