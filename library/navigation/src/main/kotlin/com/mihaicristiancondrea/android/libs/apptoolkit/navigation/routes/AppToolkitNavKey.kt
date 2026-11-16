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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes

import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * The Toolkit's own destinations. `toolkitGraph { }` in `:library:apptoolkit` registers a page for
 * each, which an app can replace by registering the same key itself.
 *
 * Every key is `@Serializable`: the shell's back stacks save their keys through
 * kotlinx.serialization, so they survive rotation and process death.
 */
@Immutable
sealed interface AppToolkitNavKey : NavKey
