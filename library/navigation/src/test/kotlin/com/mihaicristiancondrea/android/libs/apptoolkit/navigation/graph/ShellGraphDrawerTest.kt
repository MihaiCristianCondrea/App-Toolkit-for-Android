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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShellGraphDrawerTest {

    private data object Home : NavKey
    private data object Library : NavKey
    private data object Downloads : NavKey

    private val icon = ToolkitIcon.Vector(Icons.Outlined.Home)

    private fun graph(drawer: ShellGraphBuilder.() -> Unit): ShellGraph = ShellGraphBuilder(appTitle = 0).apply {
        tab(Home, 0, icon) {}
        page<Library> {}
        page<Downloads> {}
        page<SettingsRoute> {}
        drawer()
    }.build()

    private fun List<DrawerEntry>.keys(): List<Any> = map { (it as? DrawerEntry.Link)?.key ?: it }

    @Test
    fun `the footer comes last and is pinned to the bottom, whatever order it was given in`() {
        val graph = graph {
            drawer {
                footer { settings() }
                link(Library, 0, icon)
            }
            drawer { link(Downloads, 0, icon) }
        }

        assertEquals(listOf(Library, Downloads, DrawerEntry.Spacer, SettingsRoute), graph.drawer.keys())
    }

    @Test
    fun `an app's own spacer keeps its bottom entries, and the footer follows them`() {
        val graph = graph {
            drawer {
                link(Library, 0, icon)
                spacer()
                link(Downloads, 0, icon)
                footer { settings() }
            }
        }

        assertEquals(listOf(Library, DrawerEntry.Spacer, Downloads, SettingsRoute), graph.drawer.keys())
    }

    @Test
    fun `a drawer without a footer is left as listed`() {
        val graph = graph { drawer { link(Library, 0, icon) } }

        assertEquals(listOf<Any>(Library), graph.drawer.keys())
    }
}
