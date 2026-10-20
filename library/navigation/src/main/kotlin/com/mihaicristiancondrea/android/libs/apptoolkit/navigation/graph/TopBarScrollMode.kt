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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph

import androidx.compose.runtime.staticCompositionLocalOf

/** How a destination's app bar answers its content scrolling, whatever its [TopBarStyle]. */
enum class TopBarScrollMode {
    /** The bar follows its style: it collapses, tints and hides on scroll as the shell's settings say. */
    Default,

    /**
     * The bar stays expanded, visible and untinted while the content scrolls, beside the navigation
     * too. A large style stays expanded, `TopBarStyle.LargeCollapsed` included; a small window still
     * shrinks it, and `TopBarStyle.Hidden` stays hidden. The content, the bottom bar, the floating
     * action buttons and an article's reading progress still follow the scroll.
     */
    Static,
}

/** The [TopBarScrollMode] of the destination around this point, for a page that draws its own `PageScaffold`. */
val LocalTopBarScrollMode = staticCompositionLocalOf { TopBarScrollMode.Default }
