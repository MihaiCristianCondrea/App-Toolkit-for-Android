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

/**
 * What a screen inside the shell can ask of the chrome around it: whether the navigation hides in
 * a drawer, and a way to open it, for screens that draw their own header.
 */
@Immutable
class ShellChromeController(
    val showsMenuButton: Boolean,
    /**
     * Opens the navigation drawer or rail. Nothing in the Toolkit calls it, since its own screens
     * use the shell's app bar; it is the host-facing way for a screen that draws its own header
     * to open the navigation, documented in the module README.
     */
    @Suppress("unused")
    val openNavigation: () -> Unit,
)
