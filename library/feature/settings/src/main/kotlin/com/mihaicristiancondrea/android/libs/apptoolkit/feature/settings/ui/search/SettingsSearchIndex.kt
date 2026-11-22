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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.search

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchContext
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.LocalShellCapabilities
import org.koin.compose.getKoin
import java.text.Normalizer
import java.util.Locale

/**
 * Everything the settings search looks through, resolved to text once: the host's own rows, and
 * the rows every settings page registers as a [SettingsSearchProvider].
 *
 * The rows are built on the first real search, not when the settings open: most visits never
 * search, and building means resolving and normalising every row of every settings page.
 */
@Stable
internal class SettingsSearchIndex(buildRows: () -> List<Row>) {

    private val rows: List<Row> by lazy(mode = LazyThreadSafetyMode.NONE, initializer = buildRows)

    /** One searchable row, drawn as [preference], found by [text]. */
    class Row(val preference: SettingsPreference, val text: String)

    /**
     * The rows whose title, summary or page hold every word of [query], ignoring case and
     * accents, in the order the pages list them.
     */
    fun matching(query: String): List<SettingsPreference> {
        val words = query.normalizedForSearch().split(regex = Whitespace).filter { it.isNotEmpty() }
        if (words.isEmpty()) return emptyList()
        return rows.filter { row -> words.all { it in row.text } }.map { it.preference }
    }
}

/**
 * Caches an index for the current settings, the app's graph and capabilities, and the resource
 * configuration; a locale change rebuilds the resolved search text. The providers receive the
 * graph and capabilities the settings pages read, from `ShellHost`.
 */
@Composable
internal fun rememberSettingsSearchIndex(config: SettingsConfig): SettingsSearchIndex {
    val graph = LocalShellGraph.current
    val capabilities = LocalShellCapabilities.current
    val koin = getKoin()
    val resources = LocalResources.current
    val configuration = LocalConfiguration.current
    return remember(config, graph, capabilities, koin, resources, configuration) {
        SettingsSearchIndex {
            searchRows(
                config = config,
                providers = koin.getAll<SettingsSearchProvider>(),
                context = SettingsSearchContext(graph = graph, capabilities = capabilities),
                string = resources::getString,
                onUnregistered = ::logUnregistered,
            )
        }
    }
}

/**
 * The rows the search looks through: the host's own rows, then every provider's, in Koin's order.
 *
 * Every provider lists its rows for [context]. No result navigates to a destination the context's
 * graph does not register. A provider's row for one is left out, and so is a host row that only
 * opens one. A host row with an action keeps it, since the action may handle the click, but loses
 * the unregistered fallback in its search result; the list itself is unchanged. Each such row is
 * reported to [onUnregistered], with whether it was kept.
 *
 * Provider rows with the same title and destination appear once, the first provider's.
 */
internal fun searchRows(
    config: SettingsConfig,
    providers: List<SettingsSearchProvider>,
    context: SettingsSearchContext,
    string: (Int) -> String,
    onUnregistered: (destination: NavKey, title: String?, keptForAction: Boolean) -> Unit = { _, _, _ -> },
): List<SettingsSearchIndex.Row> {
    val graph = context.graph
    val hostRows = config.categories.flatMap { it.preferences }.mapNotNull { preference ->
        val destination = preference.destination
        val searchable = when {
            destination == null || graph.contains(key = destination) -> preference
            preference.action != null -> preference.copy(destination = null)
            else -> null
        }
        if (destination != null && searchable !== preference) {
            onUnregistered(destination, preference.title, searchable != null)
        }
        searchable?.let { row ->
            SettingsSearchIndex.Row(
                preference = row,
                text = searchText(row.title, row.summary),
            )
        }
    }
    val pageRows = providers
        .flatMap { it.entries(context = context) }
        .filter { entry ->
            graph.contains(key = entry.destination).also { registered ->
                if (!registered) {
                    val title = runCatching { string(entry.title) }.getOrNull()
                    onUnregistered(entry.destination, title, false)
                }
            }
        }
        .distinctBy { it.title to it.destination }
        .map { entry ->
            val title = string(entry.title)
            val section = string(entry.section)
            val summary = entry.summary?.let { id -> runCatching { string(id) }.getOrNull() }
            SettingsSearchIndex.Row(
                preference = SettingsPreference(
                    key = "search_${entry.destination}_${entry.title}",
                    title = title,
                    summary = section,
                    destination = entry.destination,
                ),
                text = searchText(title, summary, section),
            )
        }
    return hostRows + pageRows
}

private fun logUnregistered(destination: NavKey, title: String?, keptForAction: Boolean) {
    val outcome = if (keptForAction) "kept for its action, without the fallback" else "left out"
    Log.w(
        LOG_TAG,
        "Settings search result \"$title\" opens ${destination::class.qualifiedName}, which the graph does not register: $outcome.",
    )
}

private const val LOG_TAG: String = "SettingsSearch"

private fun searchText(vararg parts: String?): String =
    parts.filterNotNull().joinToString(separator = " ").normalizedForSearch()

/**
 * Lower case, without accents, so "ecran" finds "Écran". Uses [Locale.ROOT] after NFD
 * decomposition so uppercase `I` folds consistently across locales such as Turkish.
 */
private fun String.normalizedForSearch(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(regex = CombiningMarks, replacement = "")
        .lowercase(locale = Locale.ROOT)
        .trim()

private val CombiningMarks = Regex(pattern = "\\p{Mn}+")
private val Whitespace = Regex(pattern = "\\s+")
