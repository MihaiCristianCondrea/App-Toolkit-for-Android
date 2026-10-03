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

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * [ShellPreferences] kept in memory, for tests, previews, and apps that do not persist the
 * shell's settings. Starts from [initial].
 */
class InMemoryShellPreferences(initial: ShellSettings = ShellSettings()) : ShellPreferences {

    private val state = MutableStateFlow(initial)

    override val settings: StateFlow<ShellSettings> = state

    override val lastKnown: ShellSettings get() = state.value

    override suspend fun update(transform: (ShellSettings) -> ShellSettings) {
        state.update(transform)
    }
}
