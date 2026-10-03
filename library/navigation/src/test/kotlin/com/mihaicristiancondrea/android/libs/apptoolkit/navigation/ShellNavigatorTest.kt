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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ShellNavigatorTest {

    private data object First : NavKey
    private data object Second : NavKey
    private data object Third : NavKey
    private data class Child(val id: Int) : NavKey
    private data object Page : NavKey
    private data object OtherPage : NavKey
    private data object Detail : NavKey
    private data object OtherDetail : NavKey

    private val icon = ToolkitIcon.Vector(Icons.Outlined.Home)

    private val graph = ShellGraphBuilder(appTitle = 0).apply {
        tab(First, 0, icon) {}
        tab(Second, 0, icon) {}
        tab(Third, 0, icon) {}
        child<Child>(title = { "" }) {}
        page<Page> {}
        page<OtherPage> {}
        page<Detail>(paneRole = PaneRole.Detail) {}
        page<OtherDetail>(paneRole = PaneRole.Detail) {}
        page<SettingsRoute>(paneRole = PaneRole.List) {}
    }.build()

    private lateinit var pages: MutableList<NavKey>
    private lateinit var tabs: List<MutableList<NavKey>>
    private var exits = 0
    private lateinit var navigator: ShellNavigator

    @BeforeEach
    fun setUp() {
        pages = mutableListOf(ShellHomeRoute)
        tabs = graph.tabs.map { mutableListOf(it.key) }
        exits = 0
        navigator = ShellNavigator(graph, pages, tabs, mutableListOf(0)) { exits++ }
    }

    @Test
    fun `the graph starts on its first tab unless told otherwise`() {
        assertEquals(First, graph.start)
        assertEquals(listOf<NavKey>(First, Second, Third), graph.startOptions)
    }

    @Test
    fun `a start screen has no shell under it, and back from it leaves`() {
        val startPages = mutableListOf<NavKey>(Page)
        val startNavigator = ShellNavigator(graph, startPages, tabs, mutableListOf(0)) { exits++ }

        assertFalse(startNavigator.inShell)
        startNavigator.goBack()
        assertEquals(1, exits)
    }

    @Test
    fun `entering the shell replaces the start screen for good`() {
        val startPages = mutableListOf<NavKey>(Page)
        val startNavigator = ShellNavigator(graph, startPages, tabs, mutableListOf(0)) { exits++ }

        startNavigator.navigate(Second)

        assertEquals(listOf<NavKey>(ShellHomeRoute), startPages)
        assertTrue(startNavigator.inShell)
        assertEquals(1, startNavigator.currentTabIndex)
        startNavigator.goBack() // Second → First
        startNavigator.goBack() // First → leaves, never back to the start screen
        assertEquals(1, exits)
    }

    @Test
    fun `a start screen hands over to the next, and back from that one leaves`() {
        val startPages = mutableListOf<NavKey>(Page)
        val startNavigator = ShellNavigator(graph, startPages, tabs, mutableListOf(0)) { exits++ }

        startNavigator.continueStart(OtherPage)

        assertEquals(listOf<NavKey>(OtherPage), startPages)
        assertFalse(startNavigator.inShell)
        startNavigator.goBack()
        assertEquals(1, exits)
    }

    @Test
    fun `continuing the start from the shell opens a page`() {
        navigator.continueStart(Page)

        assertEquals(listOf(ShellHomeRoute, Page), pages)
    }

    @Test
    fun `the current key is the top page, or the current tab's top screen`() {
        assertEquals(First, navigator.currentKey)
        navigator.navigate(Child(1))
        assertEquals(Child(1), navigator.currentKey)
        navigator.navigate(Page)
        assertEquals(Page, navigator.currentKey)
        navigator.goBack()
        assertEquals(Child(1), navigator.currentKey)
    }

    @Test
    fun `a start screen can open pages over itself`() {
        val startPages = mutableListOf<NavKey>(Page)
        val startNavigator = ShellNavigator(graph, startPages, tabs, mutableListOf(0)) { exits++ }

        startNavigator.navigate(OtherPage)
        startNavigator.goBack()

        assertEquals(listOf<NavKey>(Page), startPages)
        assertEquals(0, exits)
    }

    @Test
    fun `a tab key selects its tab and keeps the first tab under it`() {
        navigator.navigate(Third)

        assertEquals(2, navigator.currentTabIndex)
        assertEquals(listOf(0, 2), navigator.tabsInUse)
    }

    @Test
    fun `a child is pushed on the current tab only`() {
        navigator.navigate(Second)
        navigator.navigate(Child(1))

        assertEquals(listOf(Second, Child(1)), navigator.currentTabStack)
        assertEquals(listOf<NavKey>(First), tabs[0])
    }

    @Test
    fun `back pops the child, then returns to the first tab, then leaves`() {
        navigator.navigate(Second)
        navigator.navigate(Child(1))

        navigator.goBack()
        assertEquals(listOf<NavKey>(Second), navigator.currentTabStack)

        navigator.goBack()
        assertEquals(0, navigator.currentTabIndex)
        assertEquals(0, exits)

        navigator.goBack()
        assertEquals(1, exits)
    }

    @Test
    fun `back from a tab root returns to the tab visited before it`() {
        navigator.navigate(Second)
        navigator.navigate(Third)
        assertEquals(listOf(0, 1, 2), navigator.tabsInUse)

        navigator.goBack()
        assertEquals(1, navigator.currentTabIndex)

        navigator.goBack()
        assertEquals(0, navigator.currentTabIndex)
        assertEquals(0, exits)

        navigator.goBack()
        assertEquals(1, exits)
    }

    @Test
    fun `a tab visited again moves to the top of the history, once`() {
        navigator.navigate(Second)
        navigator.navigate(Third)
        navigator.navigate(Second)

        assertEquals(listOf(0, 2, 1), navigator.tabsInUse)
    }

    @Test
    fun `the first tab is part of the history like any other`() {
        navigator.navigate(Second)
        navigator.navigate(Third)
        navigator.navigate(First)
        assertEquals(listOf(1, 2, 0), navigator.tabsInUse)

        navigator.goBack()
        assertEquals(2, navigator.currentTabIndex)
        navigator.goBack()
        assertEquals(1, navigator.currentTabIndex)
        // The history is spent away from the first tab: back goes there before leaving.
        navigator.goBack()
        assertEquals(0, navigator.currentTabIndex)
        assertEquals(0, exits)
        navigator.goBack()
        assertEquals(1, exits)
    }

    @Test
    fun `each tab keeps its stack while another is selected`() {
        navigator.navigate(Child(1))
        navigator.navigate(Second)
        navigator.navigate(First)

        assertEquals(listOf(First, Child(1)), navigator.currentTabStack)
    }

    @Test
    fun `selecting the current tab returns it to its root`() {
        navigator.navigate(Child(1))
        navigator.navigate(Child(2))

        navigator.selectTab(0)

        assertEquals(listOf<NavKey>(First), navigator.currentTabStack)
    }

    @Test
    fun `pages stack above the shell and close before anything in it`() {
        navigator.navigate(Child(1))
        navigator.navigate(Page)
        navigator.navigate(OtherPage)

        assertEquals(listOf(ShellHomeRoute, Page, OtherPage), navigator.pages)

        navigator.goBack()
        navigator.goBack()
        assertEquals(listOf<NavKey>(ShellHomeRoute), navigator.pages)
        assertEquals(listOf(First, Child(1)), navigator.currentTabStack)
    }

    @Test
    fun `opening the page already on top does nothing`() {
        navigator.navigate(Page)
        navigator.navigate(Page)

        assertEquals(listOf(ShellHomeRoute, Page), navigator.pages)
    }

    @Test
    fun `a detail replaces the detail already open`() {
        navigator.navigate(SettingsRoute)
        navigator.navigate(Detail)
        navigator.navigate(OtherDetail)

        assertEquals(listOf(ShellHomeRoute, SettingsRoute, OtherDetail), navigator.pages)
    }

    @Test
    fun `closing a list closes the detail beside it`() {
        navigator.navigate(SettingsRoute)
        navigator.navigate(Detail)

        navigator.close(SettingsRoute)

        assertEquals(listOf<NavKey>(ShellHomeRoute), navigator.pages)
    }

    @Test
    fun `a tab or child opened from a page closes the pages first`() {
        navigator.navigate(Page)
        navigator.navigate(Child(3))

        assertEquals(listOf<NavKey>(ShellHomeRoute), navigator.pages)
        assertEquals(listOf(First, Child(3)), navigator.currentTabStack)

        navigator.navigate(Page)
        navigator.navigate(Second)
        assertEquals(listOf<NavKey>(ShellHomeRoute), navigator.pages)
        assertEquals(1, navigator.currentTabIndex)
    }

    @Test
    fun `the shell is never popped`() {
        assertFalse(navigator.popPage())
        navigator.navigate(Page)
        assertTrue(navigator.popPage())
        assertEquals(listOf<NavKey>(ShellHomeRoute), navigator.pages)
    }

    @Test
    fun `navigating home closes every page`() {
        navigator.navigate(Page)
        navigator.navigate(OtherPage)

        navigator.navigate(ShellHomeRoute)

        assertEquals(listOf<NavKey>(ShellHomeRoute), navigator.pages)
    }

    @Test
    fun `closing the start screen with its back button leaves, as back does`() {
        val startPages = mutableListOf<NavKey>(Page)
        val startNavigator = ShellNavigator(graph, startPages, tabs, mutableListOf(0)) { exits++ }

        startNavigator.close(Page)

        assertEquals(1, exits)
        assertEquals(listOf<NavKey>(Page), startPages)
    }

    @Test
    fun `a graph of pages only starts on its page, opens the pages it links to, and is left from it`() {
        val entry = ShellGraphBuilder(appTitle = 0).apply {
            page<Page> {}
            page<OtherPage> {}
            start(Page)
        }.build()
        val entryPages = mutableListOf<NavKey>(entry.start)
        val entryNavigator = ShellNavigator(entry, entryPages, emptyList(), mutableListOf()) { exits++ }

        assertTrue(entry.tabs.isEmpty())
        assertEquals(null, entry.startTab)
        entryNavigator.navigate(OtherPage)
        entryNavigator.goBack()
        assertEquals(listOf<NavKey>(Page), entryPages)
        entryNavigator.goBack()
        assertEquals(1, exits)
    }

    @Test
    fun `a graph of pages only has no shell to enter, so entering it leaves`() {
        val entry = ShellGraphBuilder(appTitle = 0).apply {
            page<Page> {}
            start(Page)
        }.build()
        val entryNavigator = ShellNavigator(entry, mutableListOf(Page), emptyList(), mutableListOf()) { exits++ }

        entryNavigator.enterShell()

        assertEquals(1, exits)
    }

    @Test
    fun `a graph without tabs needs a page to start on`() {
        assertThrows<IllegalArgumentException> {
            ShellGraphBuilder(appTitle = 0).apply { page<Page> {} }.build()
        }
    }
}
