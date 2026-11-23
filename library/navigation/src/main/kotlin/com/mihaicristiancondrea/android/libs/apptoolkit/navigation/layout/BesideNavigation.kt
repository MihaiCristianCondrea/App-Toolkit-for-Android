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
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Set by the shell while its pages open beside a rail or a permanent drawer.
 *
 * The page the navigation opened stands in for a tab there, so it is drawn like one: no back
 * button (the navigation beside it is the way out), the tab's small app bar, and, when the
 * navigation and the app bar share a colour, the same frame colour behind the bar and the content
 * in a card.
 *
 * @param tinted Whether the navigation and the app bar share [frameColor]; the content then sits
 * in a card of [ContentCardShape].
 * @param scrollTint Set while the shared colour follows scrolling: where the app bars beside the
 * navigation say how far to take it. Null while the colour is fixed.
 * @param frameColor The colour behind the navigation right now, which may be animating. Last, so
 * it can be passed as a trailing lambda.
 */
@Stable
class BesideNavigation(
    val tinted: Boolean,
    val scrollTint: FrameScrollTint? = null,
    val frameColor: () -> Color,
)

/** The shell's pages open beside the navigation; null when they cover the window. */
val LocalBesideNavigation = compositionLocalOf<BesideNavigation?> { null }
