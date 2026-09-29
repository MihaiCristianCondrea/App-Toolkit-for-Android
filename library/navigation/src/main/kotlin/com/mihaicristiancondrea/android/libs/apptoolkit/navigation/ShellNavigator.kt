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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.toMutableStateList
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DestinationKind
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellTab

/**
 * The shell's navigation state and the only way screens move through it.
 *
 * There is one outer stack, [pages], whose bottom is [ShellHomeRoute], the shell, and one inner
 * stack per tab. Every tab keeps its own stack while another is selected, so returning to a tab
 * finds it where it was left.
 *
 * An app can instead start on a screen of its own, a start page such as a welcome or sign-in
 * screen: the outer stack then begins with that page and no shell under it, so back from it
 * leaves the app. Navigating to a tab or a child, or [enterShell], replaces it with the shell.
 *
 * Screens call [navigate] with any registered key and the navigator decides where it goes from the
 * key's [DestinationKind]: tabs are selected, children are pushed on the current tab, pages are
 * pushed on the outer stack. Back follows one order everywhere, gesture or button: close the top
 * page, then the top child of the current tab, then return to the tab visited before, then leave
 * the app from the first tab.
 *
 * Tabs are remembered in the order they were visited, each once, like a back stack of tabs: Apps,
 * Games, Search, Apps goes back to Search, then Games, then Apps. Revisiting a tab moves it to the
 * top, the first tab included. The app is always left from the first tab: when the history runs
 * out on another tab, back returns to the first before it leaves.
 *
 * The per-tab stacks and the "exit through the first tab" rule are adapted from the multiple back
 * stacks recipe in [android/nav3-recipes](https://github.com/android/nav3-recipes), Apache License
 * 2.0. The tab history, outer page stack, destination kinds and single-detail rule are the shell's.
 */
@Stable
class ShellNavigator internal constructor(
    private val graph: ShellGraph,
    private val pageStack: MutableList<NavKey>,
    private val tabStacks: List<MutableList<NavKey>>,
    private val tabHistory: MutableList<Int>,
    private val onExit: () -> Unit,
) {
    init {
        require(tabStacks.size == graph.tabs.size) { "Every tab needs a back stack." }
        if (tabHistory.isEmpty()) tabHistory += 0
    }

    /** The index of the selected tab in [ShellGraph.tabs]. */
    val currentTabIndex: Int get() = tabHistory.last()

    val currentTab: ShellTab get() = graph.tabs[currentTabIndex]

    /** The outer stack: [ShellHomeRoute], or a start page, followed by every open page. */
    val pages: List<NavKey> get() = pageStack

    /** Whether the shell is on the outer stack, rather than a start page in its place. */
    val inShell: Boolean get() = pageStack.first() == ShellHomeRoute

    /**
     * The destination on top: the top page, or, with the shell on top, the current tab's top
     * screen. What a person is looking at, for analytics and the like.
     */
    val currentKey: NavKey get() = pageStack.last().takeIf { it != ShellHomeRoute } ?: currentTabStack.last()

    /** The stack of the selected tab, its root first. */
    val currentTabStack: List<NavKey> get() = tabStacks[currentTabIndex]

    fun tabStack(index: Int): List<NavKey> = tabStacks[index]

    /**
     * The tabs whose stacks the inner display holds, in the order back will return through them,
     * the selected one last: the history, with the first tab under it when it is not in it, since
     * back ends there.
     */
    val tabsInUse: List<Int> get() = if (0 in tabHistory) tabHistory.toList() else listOf(0) + tabHistory

    /** Whether the top of the shell, pages excluded, is a child rather than a tab root. */
    val isShowingChild: Boolean get() = currentTabStack.size > 1

    fun navigate(key: NavKey) {
        if (key == ShellHomeRoute) {
            enterShell()
            return
        }
        val destination = graph.destination(key)
        when (destination.kind) {
            DestinationKind.Tab -> {
                enterShell()
                selectTab(graph.tabIndexOf(key))
            }

            DestinationKind.Child -> {
                enterShell()
                val stack = tabStacks[currentTabIndex]
                if (stack.last() != key) stack += key
            }

            DestinationKind.Page -> {
                val top = pageStack.last()
                when {
                    top == key -> Unit
                    // A detail opened from its list replaces the detail already beside the list
                    // rather than stacking on it, so back returns to the list in one step.
                    destination.paneRole == PaneRole.Detail && top.isDetail() -> pageStack[pageStack.lastIndex] = key
                    else -> pageStack += key
                }
            }
        }
    }

    /**
     * Selects [index], moving it to the top of the tab history, or returns an already selected tab
     * to its root.
     */
    fun selectTab(index: Int) {
        require(index in graph.tabs.indices) { "There is no tab $index." }
        when (index) {
            currentTabIndex -> {
                val stack = tabStacks[index]
                while (stack.size > 1) stack.removeAt(stack.lastIndex)
            }

            else -> {
                tabHistory.remove(index)
                tabHistory += index
            }
        }
    }

    /** Back, from an app bar or any button. Leaves the app once there is nothing left to close. */
    fun goBack() {
        if (!popPage() && !(inShell && popTab())) onExit()
    }

    /**
     * Shows the shell, closing every page, and replacing a start page with the shell for good: a
     * start screen calls this, or navigates to a tab, when it is done.
     */
    fun enterShell() {
        when {
            inShell -> closePages()
            // A graph of pages only has no shell to enter: the entry point is done.
            graph.tabs.isEmpty() -> onExit()
            else -> replaceStart(ShellHomeRoute)
        }
    }

    /**
     * Replaces the start screen, and any page open over it, with [next], another start screen:
     * a welcome that hands over to a permission request, then to onboarding. Back from [next]
     * leaves the app, as from the first. Once the shell is showing, it opens [next] as a page.
     */
    fun continueStart(next: NavKey) {
        if (inShell) navigate(next) else replaceStart(next)
    }

    /** Makes [key] the whole outer stack, never leaving it empty on the way. */
    private fun replaceStart(key: NavKey) {
        pageStack += key
        while (pageStack.size > 1) pageStack.removeAt(0)
    }

    /**
     * Closes the page [key] and every page opened above it. A list page uses this for its back
     * button, so the detail open beside it closes with it. Closing the start page, the bottom of
     * the stack when no shell is under it, leaves the app, as back does.
     */
    fun close(key: NavKey) {
        val index = pageStack.lastIndexOf(key)
        when {
            index < 0 -> Unit
            index == 0 -> if (!inShell) onExit()
            else -> while (pageStack.size > index) pageStack.removeAt(pageStack.lastIndex)
        }
    }

    /** Closes every page, showing the shell. */
    fun closePages() {
        while (pageStack.size > 1) pageStack.removeAt(pageStack.lastIndex)
    }

    /** Closes the top page. False when only the shell is left. The outer display calls it on back. */
    fun popPage(): Boolean {
        if (pageStack.size <= 1) return false
        pageStack.removeAt(pageStack.lastIndex)
        return true
    }

    /**
     * Pops the current tab's top child, or returns from a tab's root to the tab visited before it.
     * False when the first tab's root is showing. The inner display calls it on back.
     */
    fun popTab(): Boolean {
        val stack = tabStacks[currentTabIndex]
        return when {
            stack.size > 1 -> {
                stack.removeAt(stack.lastIndex)
                true
            }

            tabHistory.size > 1 -> {
                tabHistory.removeAt(tabHistory.lastIndex)
                true
            }

            // The history ran out away from the first tab: the app is left from the first tab.
            currentTabIndex != 0 -> {
                tabHistory[0] = 0
                true
            }

            else -> false
        }
    }

    private fun NavKey.isDetail(): Boolean =
        this != ShellHomeRoute && graph.destination(this).paneRole == PaneRole.Detail
}

/**
 * Creates the navigator with stacks that survive configuration changes and process death. Every
 * key on them must be `@Serializable`.
 *
 * @param start Where a fresh launch opens: a tab, showing the shell on it, or a page, shown on its
 * own as a start screen. Defaults to the graph's start.
 */
@Composable
fun rememberShellNavigator(graph: ShellGraph, onExit: () -> Unit, start: NavKey = graph.start): ShellNavigator {
    // Only the first launch reads [start]; afterwards the saved stacks win.
    val startsOnTab = graph.tabIndexOf(start) >= 0
    val pageStack = rememberNavBackStack(if (startsOnTab) ShellHomeRoute else start)
    val tabStacks = graph.tabs.map { tab -> rememberNavBackStack(tab.key) }
    val tabHistory = rememberSaveable(
        saver = listSaver(save = { it.toList() }, restore = { it.toMutableStateList() }),
    ) { mutableStateListOf(if (startsOnTab) graph.tabIndexOf(start) else 0) }
    val currentOnExit by rememberUpdatedState(onExit)
    return remember(graph, pageStack, tabStacks) {
        ShellNavigator(graph, pageStack, tabStacks, tabHistory) { currentOnExit() }
    }
}

val LocalShellNavigator = staticCompositionLocalOf<ShellNavigator> {
    error("ShellNavigator is only available inside ShellHost.")
}

/** The graph the shell is showing. */
val LocalShellGraph = staticCompositionLocalOf<ShellGraph> {
    error("ShellGraph is only available inside ShellHost.")
}

/** The page being drawn, or null inside the shell's tabs. */
val LocalPageKey = staticCompositionLocalOf<NavKey?> { null }
