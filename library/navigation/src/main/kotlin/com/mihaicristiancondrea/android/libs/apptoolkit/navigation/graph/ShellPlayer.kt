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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * A player that docks above the navigation bar and expands to cover the whole shell.
 *
 * The shell owns the gesture, the position and the hand-off with the bars: the mini player sits on
 * the navigation bar, the bar slides away as the player is dragged up, back collapses it, and the
 * content underneath is padded so nothing hides behind it. The app only draws the two states.
 *
 * @param isActive Read while composing, so it can be backed by snapshot state. The player enters
 * when it turns true and sinks away when it turns false.
 * @param mini The collapsed row. It fills a fixed-height pill; tapping the pill outside the app's
 * own buttons expands it.
 * @param expanded The full-screen player. It is given the call that collapses it again, for a
 * close or chevron button.
 */
@Immutable
class ShellPlayer(
    val isActive: @Composable () -> Boolean,
    val mini: @Composable () -> Unit,
    val expanded: @Composable (collapse: () -> Unit) -> Unit,
)
