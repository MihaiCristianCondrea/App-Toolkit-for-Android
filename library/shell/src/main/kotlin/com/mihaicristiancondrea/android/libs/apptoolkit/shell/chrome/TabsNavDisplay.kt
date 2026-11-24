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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome

import androidx.compose.animation.ContentTransform
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigationevent.NavigationEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LocalScreenOpening
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.FabHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.LocalFabHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.ContentWidthBox
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellSearch
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellNavDisplay
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellSearch
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.LocalTopBarScrollMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ArticleTopBarHost
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalArticleTopBarHost
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalShellLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.LocalShellMotion
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ScreenTransition
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ShellMotion
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.ListDetailSceneStrategy
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.ShellEntryInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.topShellInfo
import kotlinx.collections.immutable.ImmutableList

/**
 * The inner display: every tab's stack, of which it shows the first tab's and the selected tab's.
 *
 * Each tab decorates its own stack, so a tab's screens keep their scroll position and view models
 * while another tab is selected. Moving between tabs uses the tab transition; pushing and popping
 * children within one tab uses the activity transition.
 */
@Composable
internal fun TabsNavDisplay(
    graph: ShellGraph,
    navigator: ShellNavigator,
    searches: ImmutableList<ShellSearch>,
    entryHosts: EntryHosts,
) {
    val motion = LocalShellMotion.current
    val layout = LocalShellLayout.current
    val entriesByTab = graph.tabs.indices.map { tabIndex ->
        rememberDecoratedNavEntries(
            backStack = navigator.tabStack(tabIndex),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = { key ->
                val entryKey = tabEntryKey(tabIndex, key)
                tabEntry(
                    graph = graph,
                    key = key,
                    tabIndex = tabIndex,
                    search = searches[tabIndex],
                    fabHost = entryHosts.fabHostFor(entryKey),
                    articleHost = entryHosts.articleHostFor(entryKey),
                    timesOpened = { navigator.timesOpened(tabIndex) },
                )
            },
        )
    }
    // A tab declared as a list shows its detail child beside it on wide windows.
    val strategies = remember(layout.listDetail) {
        listOf(ListDetailSceneStrategy<NavKey>(layout.listDetail, framed = false), SinglePaneSceneStrategy())
    }
    val transitionOf = { scene: Scene<*> -> scene.transition(graph, layout.mode) }
    ShellNavDisplay(
        entries = navigator.tabsInUse.flatMap { entriesByTab[it] },
        sceneStrategies = strategies,
        onBack = { navigator.popTab() },
        transitionSpec = { motion.forTabs(initialState, targetState, back = false, transitionOf) },
        popTransitionSpec = { motion.forTabs(initialState, targetState, back = true, transitionOf) },
        // Back from a child with the activity transition is back between activities; any other
        // back, between tabs or from a sliding child, seeks its own transition with the gesture.
        activityBack = {
            navigator.isShowingChild &&
                graph.transitionOf(navigator.currentTabStack.last(), layout.mode) == ScreenTransition.Activity
        },
        // While a page covers the shell, even one still opening, back is the page's.
        backEnabled = navigator.pages.size == 1,
        predictivePopTransitionSpec = { swipeEdge ->
            val mirrored = motion.followFingerFromRight && swipeEdge == NavigationEvent.EDGE_RIGHT
            motion.forTabs(initialState, targetState, back = true, transitionOf, mirrored)
        },
    )
}

/**
 * Between tabs, the tab transition; between a tab's screens, the transition of the screen that
 * comes or goes: the one arriving going forward, the one leaving going back.
 */
private fun ShellMotion.forTabs(
    from: Scene<*>,
    to: Scene<*>,
    back: Boolean,
    transitionOf: (Scene<*>) -> ScreenTransition,
    mirrored: Boolean = false,
): ContentTransform {
    val fromTab = from.topShellInfo?.tabIndex
    val toTab = to.topShellInfo?.tabIndex
    return when {
        fromTab != null && toTab != null && fromTab != toTab -> tabs.between(forward = toTab > fromTab)
        back -> screens.back(transitionOf(from), mirrored)
        else -> screens.forward(transitionOf(to))
    }
}

private fun tabEntry(
    graph: ShellGraph,
    key: NavKey,
    tabIndex: Int,
    search: ShellSearch,
    fabHost: FabHost,
    articleHost: ArticleTopBarHost,
    timesOpened: () -> Int,
): NavEntry<NavKey> {
    val destination = graph.destination(key)
    return NavEntry(
        key = key,
        // The same child can be open in two tabs at once; the tab keeps their states apart.
        contentKey = tabEntryKey(tabIndex, key),
        metadata = ShellEntryInfo(
            kind = destination.kind,
            tabIndex = tabIndex,
            paneRole = destination.paneRole,
            transition = destination.transition,
        ).toMetadata(),
    ) { entryKey ->
        val provided = if (graph.tabs[tabIndex].search != null) search else null
        CompositionLocalProvider(
            LocalShellSearch provides provided,
            LocalTopBarScrollMode provides destination.topBarScrollMode,
            LocalFabHost provides fabHost,
            LocalArticleTopBarHost provides articleHost,
            // Picking the tab again opens its screens again, for their entrance.
            LocalScreenOpening provides timesOpened(),
        ) {
            ProvideEntranceCue {
                ContentWidthBox(
                    maxWidth = LocalShellLayout.current.maxWidthFor(destination.contentWidth),
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                ) {
                    destination.content(entryKey)
                }
            }
        }
    }
}
