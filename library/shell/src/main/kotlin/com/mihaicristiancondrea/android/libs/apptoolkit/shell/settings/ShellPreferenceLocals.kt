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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings

import androidx.compose.runtime.staticCompositionLocalOf
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.data.repositories.ShellPreferencesRepository

/**
 * The [ShellPreferencesRepository] the shell reads its settings from: the one passed to
 * `ShellHost`, or its own. A screen that changes a shell setting, such as the developer options,
 * writes through it. Reading it outside `ShellHost` throws.
 */
val LocalShellPreferences = staticCompositionLocalOf<ShellPreferencesRepository> {
    error("ShellPreferencesRepository is only available inside ShellHost.")
}

/**
 * The shell settings in effect now, updated as they change. Outside `ShellHost` it holds the
 * Toolkit's defaults.
 */
val LocalShellSettings = staticCompositionLocalOf { ShellSettings() }
