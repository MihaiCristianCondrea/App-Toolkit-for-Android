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