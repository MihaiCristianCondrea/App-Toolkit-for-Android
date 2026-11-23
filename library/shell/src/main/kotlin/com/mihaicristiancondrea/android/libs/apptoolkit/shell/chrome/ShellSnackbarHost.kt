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

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * The shell's snackbar host, set by `ShellHost`, and how many tab scaffolds draw it now. While none
 * does, on a start screen or under a page, `ShellHost` draws it itself, so a message the app shows
 * there is never left waiting on a host nobody draws.
 */
@Stable
internal class ShellSnackbarHost(val hostState: SnackbarHostState) {
    var drawnByTabs: Int by mutableIntStateOf(0)
}

/** The shell's snackbar host while inside `ShellHost`, null outside it. */
internal val LocalShellSnackbarHost = staticCompositionLocalOf<ShellSnackbarHost?> { null }
