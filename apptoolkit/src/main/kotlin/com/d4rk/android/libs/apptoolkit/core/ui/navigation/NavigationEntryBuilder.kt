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

package com.d4rk.android.libs.apptoolkit.core.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider


/**
 * Typed Navigation 3 entry builder (DSL installer).
 */
typealias NavigationEntryBuilder<T> = EntryProviderScope<T>.() -> Unit

/**
 * Public, stable type for an Entry Provider in Nav3:
 * a function that maps a key -> NavEntry.
 *
 * Nav3 defines an Entry Provider exactly like this. :contentReference[oaicite:1]{index=1}
 */
typealias NavigationEntryProvider<T> = (T) -> NavEntry<T>

/**
 * Combine multiple [NavigationEntryBuilder] installers into a single entry provider.
 *
 * Important: Do NOT expose androidx.navigation3.runtime.EntryProvider here — in some versions
 * it’s not accessible (private-in-file), and Kotlin 2.4+ will hard-error on inline leakage.
 */
fun <T : NavKey> entryProviderFor(
    builders: Iterable<NavigationEntryBuilder<T>>
): NavigationEntryProvider<T> =
    entryProvider {
        builders.forEach { it() }
    }