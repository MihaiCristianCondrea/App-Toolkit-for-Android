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

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellGraph
import java.text.Normalizer
import org.koin.compose.getKoin

/**
 * Everything the settings search looks through, resolved to text once: the host's own rows, and
 * the rows every settings page registers as a [SettingsSearchProvider].
 */
@Immutable
internal class SettingsSearchIndex(private val rows: List<Row>) {

    /** One searchable row, drawn as [preference], found by [text]. */
    class Row(val preference: SettingsPreference, val text: String)

    /**
     * The rows whose title, summary or page hold every word of [query], ignoring case and
     * accents, in the order the pages list them.
     */
    fun matching(query: String): List<SettingsPreference> {
        val words = query.normalizedForSearch().split(' ').filter { it.isNotEmpty() }
        if (words.isEmpty()) return emptyList()
        return rows.filter { row -> words.all { it in row.text } }.map { it.preference }
    }
}

@Composable
internal fun rememberSettingsSearchIndex(config: SettingsConfig): SettingsSearchIndex {
    val graph = LocalShellGraph.current
    val koin = getKoin()
    val resources = LocalContext.current.resources
    // Resolved again when the language changes.
    val configuration = LocalConfiguration.current
    return remember(config, graph, koin, configuration) {
        val hostRows = config.categories.flatMap { it.preferences }.map { preference ->
            SettingsSearchIndex.Row(preference, searchText(preference.title, preference.summary))
        }
        val pageRows = koin.getAll<SettingsSearchProvider>()
            .flatMap { it.entries(graph) }
            .distinctBy { it.title to it.destination }
            .map { entry ->
                val title = resources.getString(entry.title)
                val section = resources.getString(entry.section)
                val summary = entry.summary?.let(resources::safeString)
                SettingsSearchIndex.Row(
                    preference = SettingsPreference(
                        key = "search_${entry.destination}_${entry.title}",
                        title = title,
                        // Where the setting lives, as its result's summary.
                        summary = section,
                        destination = entry.destination,
                    ),
                    text = searchText(title, summary, section),
                )
            }
        SettingsSearchIndex(hostRows + pageRows)
    }
}

private fun Resources.safeString(id: Int): String? = runCatching { getString(id) }.getOrNull()

private fun searchText(vararg parts: String?): String =
    parts.filterNotNull().joinToString(" ").normalizedForSearch()

/** Lower case, without accents, so "ecran" finds "Écran". */
private fun String.normalizedForSearch(): String =
    Normalizer.normalize(lowercase(), Normalizer.Form.NFD).replace(CombiningMarks, "").trim()

private val CombiningMarks = Regex("\\p{Mn}+")
