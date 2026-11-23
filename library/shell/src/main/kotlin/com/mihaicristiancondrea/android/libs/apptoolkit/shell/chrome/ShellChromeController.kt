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

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * What a screen inside the shell can ask of the chrome around it: whether the navigation hides in
 * a drawer, and a way to open it, for screens that draw their own header.
 *
 * The shell creates the controller; read it from [LocalShellChrome].
 *
 * @property showsMenuButton Whether the navigation is a modal drawer, so a screen drawing its own
 * header shows a menu button. False beside a rail or a permanent drawer, where it is always on
 * screen, and in the bottom bar layouts that have no drawer to open.
 * @property openNavigation Opens the navigation drawer or rail. Nothing in the Toolkit calls it,
 * since its own screens use the shell's app bar; it is for a screen that draws its own header.
 */
@Immutable
class ShellChromeController internal constructor(
    val showsMenuButton: Boolean,
    val openNavigation: () -> Unit,
)

/**
 * The [ShellChromeController] of the shell the screen is in. Outside `ShellHost`, such as in a
 * preview, it shows no menu button and its [ShellChromeController.openNavigation] does nothing.
 */
val LocalShellChrome = staticCompositionLocalOf {
    ShellChromeController(showsMenuButton = false, openNavigation = {})
}
