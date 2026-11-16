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


/** The app bar a tab, child or page is drawn under. */
enum class TopBarStyle {
    /** Title at the start, stays in place. */
    Small,

    /** Title centred, stays in place. */
    CenterAligned,

    /**
     * Tall title that collapses into a small bar as the content scrolls. On a window too short for
     * it, such as a phone in landscape, the shell draws a small bar instead.
     */
    Large,

    /**
     * A [Large] bar that opens already collapsed, as a small bar, so the content starts right
     * under it. Pulling the content down from its top grows the bar to its full size. Suits pages
     * opened to act on, such as settings, more than pages opened to read.
     */
    LargeCollapsed,

    /** No app bar. The screen draws its own header, such as a search field. */
    Hidden,
    ;

    /** Whether this is a large bar, collapsed at first or not. */
    val isLarge: Boolean get() = this == Large || this == LargeCollapsed
}
