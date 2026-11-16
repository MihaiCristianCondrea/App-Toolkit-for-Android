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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.showcase

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * One app in an [AppShowcase].
 *
 * @property key Stable identity, the package name.
 * @property installed Whether the app is on this device: its card then opens it, and its action
 * reads as "open" rather than as the store.
 */
@Immutable
data class ShowcaseApp(
    val key: String,
    val name: String,
    val category: String,
    val description: String,
    val iconUrl: String,
    val installed: Boolean = false,
)
