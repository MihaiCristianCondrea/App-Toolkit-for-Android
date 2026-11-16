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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

/**
 * Tells a screen that the transition bringing it in is far enough along for its entrance to begin,
 * so its content arrives as the screen lands rather than after it. [reached] is read inside a
 * snapshot, so it should read state. The shell provides one for each page and tab.
 */
fun interface EntranceCue {
    /** Whether the screen's entrance may begin. */
    fun reached(): Boolean
}
