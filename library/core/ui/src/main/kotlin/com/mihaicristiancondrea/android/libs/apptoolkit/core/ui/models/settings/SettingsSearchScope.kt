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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings

import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph

/**
 * The rows of one settings page, as [settingsSearchProvider] declares them: each row takes the
 * page's section and destination unless it names its own destination.
 */
@SettingsSearchDsl
class SettingsSearchScope internal constructor(
    private val context: SettingsSearchContext,
    @param:StringRes private val section: Int,
    private val pageDestination: NavKey,
) {
    private val entries = mutableListOf<SettingsSearchEntry>()

    /** The app's graph, for rows that depend on which destinations it registers. */
    val graph: ShellGraph
        get() = context.graph

    /** What the app's declared shell can show, for rows the page only shows in some apps. */
    val capabilities: ShellCapabilities
        get() = context.capabilities

    /**
     * A row the search finds.
     *
     * @param title The row's title, as the page shows it.
     * @param summary The row's summary, searched as well as the title.
     * @param destination Where the result goes: the page by default, or the page the row opens.
     */
    fun preference(
        @StringRes title: Int,
        @StringRes summary: Int? = null,
        destination: NavKey = pageDestination,
    ) {
        entries += SettingsSearchEntry(title = title, section = section, destination = destination, summary = summary)
    }

    internal fun build(): List<SettingsSearchEntry> = entries.toList()
}

/**
 * The searchable rows of one settings page, for the settings search: bind the result in Koin as a
 * [SettingsSearchProvider] with a name of its own.
 *
 * ```
 * val readerSettingsSearch = settingsSearchProvider(R.string.reader_settings, ReaderSettingsRoute) {
 *     preference(R.string.font_size, summary = R.string.font_size_summary)
 *     if (capabilities.hasMultipleTabs) preference(R.string.start_tab)
 *     preference(R.string.reader_theme, destination = ReaderThemeRoute)
 * }
 * ```
 *
 * [preferences] runs each time the search builds its index, with the app's [SettingsSearchContext],
 * so a row is left out with plain Kotlin conditions over [SettingsSearchScope.capabilities] or
 * [SettingsSearchScope.graph]. Rows whose destination the graph does not register are left out of
 * the results.
 *
 * @param section The page's title, shown under each result.
 * @param destination The page itself, where results go unless a row names another destination.
 */
fun settingsSearchProvider(
    @StringRes section: Int,
    destination: NavKey,
    preferences: SettingsSearchScope.() -> Unit,
): SettingsSearchProvider = SettingsSearchProvider { context ->
    SettingsSearchScope(context, section, destination).apply(preferences).build()
}

/**
 * The destinations of this provider's rows in [context] that its graph does not register. The
 * search leaves such rows out; a test asserts this is empty to catch a page missing from the graph.
 */
fun SettingsSearchProvider.unregisteredDestinations(context: SettingsSearchContext): List<NavKey> =
    entries(context).map { it.destination }.filterNot(context.graph::contains).distinct()