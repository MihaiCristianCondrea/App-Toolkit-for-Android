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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutPolicy
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class SettingsSearchScopeTest {

    private data object Home : NavKey
    private data object Second : NavKey
    private data object Reader : NavKey
    private data object ReaderTheme : NavKey
    private data object Missing : NavKey

    private fun graph(tabs: Int = 1): ShellGraph = shellGraph(appTitle = 0) {
        val icon = ToolkitIcon.Vector(Icons.Outlined.Home)
        if (tabs >= 1) tab(Home, 0, icon) {}
        if (tabs >= 2) tab(Second, 0, icon) {}
        page<Reader> {}
        page<ReaderTheme> {}
        if (tabs == 0) start(Reader)
    }

    private fun context(tabs: Int = 1, policy: ShellLayoutPolicy = ShellLayoutPolicy()): SettingsSearchContext {
        val graph = graph(tabs)
        return SettingsSearchContext(graph, ShellCapabilities.of(graph, policy))
    }

    @Test
    fun `rows take the page's section and destination`() {
        val provider = settingsSearchProvider(section = SECTION, destination = Reader) {
            preference(title = 1, summary = 2)
            preference(title = 3)
        }

        assertEquals(
            listOf(
                SettingsSearchEntry(title = 1, section = SECTION, destination = Reader, summary = 2),
                SettingsSearchEntry(title = 3, section = SECTION, destination = Reader, summary = null),
            ),
            provider.entries(context()),
        )
    }

    @Test
    fun `a row can open its own destination`() {
        val provider = settingsSearchProvider(section = SECTION, destination = Reader) {
            preference(title = 1)
            preference(title = 2, destination = ReaderTheme)
        }

        assertEquals(listOf<NavKey>(Reader, ReaderTheme), provider.entries(context()).map { it.destination })
        assertTrue(provider.entries(context()).all { it.section == SECTION })
    }

    @Test
    fun `conditions read the app's capabilities each time the rows are asked for`() {
        val provider = settingsSearchProvider(section = SECTION, destination = Reader) {
            preference(title = 1)
            if (capabilities.hasTabs) preference(title = 2)
            if (capabilities.hasMultipleTabs) preference(title = 3)
        }

        assertEquals(listOf(1), provider.entries(context(tabs = 0)).map { it.title })
        assertEquals(listOf(1, 2), provider.entries(context(tabs = 1)).map { it.title })
        assertEquals(listOf(1, 2, 3), provider.entries(context(tabs = 2)).map { it.title })
    }

    @Test
    fun `conditions see the layout policy the context was built with`() {
        val provider = settingsSearchProvider(section = SECTION, destination = Reader) {
            if (capabilities.usesBottomNavigation) preference(title = 1)
        }

        assertEquals(listOf(1), provider.entries(context(tabs = 2)).map { it.title })
        assertEquals(emptyList(), provider.entries(context(tabs = 2, policy = ShellLayoutPolicy(railFrom = 0.dp))))
    }

    @Test
    fun `the scope reads the graph and capabilities of the context it was given`() {
        val context = context(tabs = 2)
        var seenGraph: ShellGraph? = null
        var seenCapabilities: ShellCapabilities? = null
        val provider = settingsSearchProvider(section = SECTION, destination = Reader) {
            seenGraph = graph
            seenCapabilities = capabilities
        }

        provider.entries(context)

        assertSame(context.graph, seenGraph)
        assertSame(context.capabilities, seenCapabilities)
    }

    @Test
    fun `a context built from a graph alone uses the default layout policy`() {
        val graph = graph(tabs = 2)

        assertEquals(ShellCapabilities.of(graph, ShellLayoutPolicy()), SettingsSearchContext(graph).capabilities)
    }

    @Test
    fun `asking twice does not repeat rows`() {
        val provider = settingsSearchProvider(section = SECTION, destination = Reader) { preference(title = 1) }

        provider.entries(context())

        assertEquals(1, provider.entries(context()).size)
    }

    @Test
    fun `a provider written by hand receives the same context`() {
        val provider = SettingsSearchProvider { context ->
            if (context.capabilities.hasTabs) listOf(SettingsSearchEntry(title = 1, section = SECTION, destination = Reader)) else emptyList()
        }

        assertEquals(listOf(SettingsSearchEntry(title = 1, section = SECTION, destination = Reader)), provider.entries(context(tabs = 1)))
        assertEquals(emptyList(), provider.entries(context(tabs = 0)))
    }

    @Test
    fun `destinations missing from the graph are reported once each`() {
        val provider = settingsSearchProvider(section = SECTION, destination = Reader) {
            preference(title = 1)
            preference(title = 2, destination = Missing)
            preference(title = 3, destination = Missing)
        }

        assertEquals(listOf<NavKey>(Missing), provider.unregisteredDestinations(context()))
    }

    @Test
    fun `a provider whose pages are all registered reports none`() {
        val provider = settingsSearchProvider(section = SECTION, destination = Reader) {
            preference(title = 1, destination = ReaderTheme)
        }

        assertEquals(emptyList(), provider.unregisteredDestinations(context()))
    }

    private companion object {
        const val SECTION = 100
    }
}
