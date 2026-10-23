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

/**
 * Where a destination is shown, and therefore how it enters, leaves and answers the back gesture.
 *
 * The shell has two levels. The outer level holds the shell itself and every [Page], and moves
 * between them the way Android moves between activities. The inner level lives inside the shell,
 * under its app bar and above its navigation bar or rail, and holds the [Tab] roots and their
 * [Child] destinations.
 */
enum class DestinationKind {
    /** A root of the shell, selected from the bottom bar, the rail or the drawer. */
    Tab,

    /**
     * Pushed on top of the current tab, inside the shell. The navigation bar stays; the app bar
     * shows a back arrow and the child's title.
     */
    Child,

    /**
     * Covers the whole shell, bars included, and leaves with the system back animation, the way a
     * separate activity would. Settings, help and anything opened from the drawer are pages.
     */
    Page,
}
