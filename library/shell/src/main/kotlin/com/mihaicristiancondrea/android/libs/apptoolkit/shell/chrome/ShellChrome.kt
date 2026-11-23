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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.LocalShowBottomBarLabels
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.frameTint
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellBackHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellSearch
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DrawerEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.LocalShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalShellLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationTint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

/**
 * The shell: app bar, tab content, and the navigation surface [LocalShellLayout] asks for, with
 * the player docked above the bottom edge and the banner on the bottom navigation bar, the only
 * place it shows.
 *
 * The player sits under the drawer and the modal rail, so opening either covers it; on a permanent
 * drawer it docks beside the drawer and covers it only when expanded.
 */
@Composable
internal fun ShellChrome(graph: ShellGraph, navigator: ShellNavigator) {
    val settings = LocalShellSettings.current
    val layout = LocalShellLayout.current
    val frame = LocalShellFrame.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val playerExpansion = rememberPlayerExpansion()
    // Read as a yes or no, so the chrome recomposes when the player starts or stops opening, not on
    // every frame of it.
    val playerCollapsed by remember { derivedStateOf { playerExpansion.value == 0f } }
    var bottomDock by remember { mutableStateOf(0.dp) }
    // An immutable list, so the bodies it is passed to can skip; each search is itself stable.
    val searches: ImmutableList<ShellSearch> = rememberSaveable(
        saver = listSaver(
            save = { list -> list.map { it.query } },
            restore = { queries -> queries.map(::ShellSearch).toImmutableList() },
        ),
    ) { graph.tabs.map { ShellSearch() }.toImmutableList() }
    // Where each tab screen puts the floating action buttons and article bar it declares, by the
    // screen's entry.
    val entryHosts = remember { EntryHosts() }
    // A host lives as long as its screen's entry: once a screen has left every tab's stack, its host
    // goes too, rather than one staying behind for every child ever opened.
    LaunchedEffect(navigator, graph) {
        snapshotFlow {
            graph.tabs.indices.flatMapTo(HashSet()) { tab -> navigator.tabStack(tab).map { tabEntryKey(tab, it) } }
        }.collect { live -> entryHosts.retainOnly(live) }
    }

    val player = graph.player?.takeIf { settings.accessoryMode.showsPlayer }
    val playerActive = player?.isActive?.invoke() == true
    val banner = graph.banner?.takeIf { settings.accessoryMode.showsBanner }

    LaunchedEffect(layout.mode) {
        drawerState.snapTo(DrawerValue.Closed)
    }

    // Beside a rail or a permanent drawer the navigation belongs to the frame around the displays
    // (ShellFrame), so pages open next to it; below, the chrome draws the bottom bar and the
    // modal drawer itself, and pages cover them.
    val wide = frame != null && layout.mode.keepsNavigationBeside
    // Kept across recompositions: the bars and drawers they are passed to can then skip, and a new
    // controller would recompose everything under LocalShellChrome, a static local, on every
    // navigation.
    val callbacks = remember(navigator, drawerState, scope, context) {
        NavigationCallbacks(
            onTabClick = { index ->
                if (drawerState.isOpen) scope.launch { drawerState.close() }
                navigator.selectTab(index)
            },
            onEntryClick = { entry ->
                if (drawerState.isOpen) scope.launch { drawerState.close() }
                when (entry) {
                    is DrawerEntry.Link -> navigator.navigate(entry.key)
                    is DrawerEntry.Action -> entry.onClick(context)
                    DrawerEntry.Spacer -> Unit
                }
            },
        )
    }
    val openNavigation: () -> Unit = remember(wide, frame, layout.mode, drawerState, scope) {
        {
            when {
                wide -> frame.openNavigation(layout.mode)
                layout.mode == ShellLayoutMode.BottomBar -> scope.launch { drawerState.open() }
            }
        }
    }
    val currentOpenNavigation by rememberUpdatedState(openNavigation)
    val showsMenuButton = layout.mode == ShellLayoutMode.BottomBar
    val chrome = remember(showsMenuButton) { ShellChromeController(showsMenuButton = showsMenuButton, openNavigation = { currentOpenNavigation() }) }
    val highlightedTab = highlightedTab(graph, navigator, layout.listDetail)

    val tintMode = if (wide) settings.navigationTint else NavigationTint.None
    val tinted = tintMode != NavigationTint.None

    val body: @Composable (bottomBar: @Composable () -> Unit) -> Unit = { bottomBar ->
        ShellBody(
            graph = graph,
            navigator = navigator,
            searches = searches,
            entryHosts = entryHosts,
            callbacks = callbacks,
            // With the app named in the navigation beside it, the app bar names the tab instead.
            navigationNamesApp = layout.mode == ShellLayoutMode.PermanentDrawer ||
                (layout.mode == ShellLayoutMode.ExpandedRail && frame?.railExpanded == true),
            showMenuButton = chrome.showsMenuButton,
            onMenuClick = openNavigation,
            playerActive = playerActive,
            playerExpansion = { playerExpansion.value },
            onBottomDockChange = { bottomDock = it },
            tinted = tinted,
            bottomBar = bottomBar,
        )
    }
    val playerOverlay: @Composable () -> Unit = {
        if (player != null) {
            ShellPlayerOverlay(
                player = player,
                active = playerActive,
                expansion = playerExpansion,
                // Read where the player lays out: the hiding bottom bar changes it on every frame.
                dockBottom = { bottomDock },
                // Beside the navigation the chrome already starts after it.
                dockStart = 0.dp,
            )
        }
    }

    // The same shared colour the frame draws behind the navigation, drawn again behind the app bar
    // here, since the display between the two paints its own background.
    val untintedColor = MaterialTheme.colorScheme.surface
    val tintedColor = MaterialTheme.colorScheme.surfaceContainer
    val frameTint = frame?.tint
    CompositionLocalProvider(LocalShellChrome provides chrome) {
        Box(
            Modifier
                .fillMaxSize()
                .then(
                    if (tinted && frameTint != null) {
                        Modifier.drawBehind { drawRect(lerp(untintedColor, tintedColor, frameTint.value)) }
                    } else {
                        Modifier
                    },
                ),
        ) {
            if (wide) {
                // The banner docks on the bottom navigation bar only: beside a rail or drawer
                // there is none to dock on, so the app shows none.
                // The slot is as tall as the system bar, so the player docks above it.
                body { SystemBottomInset() }
                playerOverlay()
            } else {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = !navigator.isShowingChild && playerCollapsed,
                    drawerContent = {
                        ModalDrawerSheet(drawerState, windowInsets = DrawerContentInsets) {
                            ShellDrawerContent(
                                graph = graph,
                                selectedIndex = highlightedTab,
                                showTabs = false,
                                callbacks = callbacks,
                            )
                        }
                    },
                ) {
                    Box(Modifier.fillMaxSize()) {
                        body {
                            if (LocalShellCapabilities.current.hasBottomNavigationTabs) {
                                Column {
                                    BannerSlot(banner, visible = !playerActive)
                                    ShellNavigationBar(
                                        tabs = graph.tabs,
                                        selectedIndex = highlightedTab,
                                        callbacks = callbacks,
                                        short = settings.navigationBarStyle == NavigationBarStyle.Short,
                                        alwaysShowLabels = LocalShowBottomBarLabels.current,
                                    )
                                }
                            } else {
                                // Keep the content, FAB and player clear of Android's navigation area.
                                SystemBottomInset()
                            }
                        }
                        playerOverlay()
                    }
                }
            }

            // Composed only while needed, so they register after the displays' handlers and win.
            // None of them may act while a page is on top: the shell stays composed while a page
            // opens over it, and back then belongs to the page.
            val shellOnTop = navigator.pages.size == 1
            if (shellOnTop && drawerState.isOpen) {
                ShellBackHandler { scope.launch { drawerState.close() } }
            }
            val search = searches[navigator.currentTabIndex]
            if (shellOnTop && !navigator.isShowingChild && graph.tabs[navigator.currentTabIndex].search != null && search.query.isNotEmpty()) {
                ShellBackHandler { search.query = "" }
            }
        }
    }
}

/**
 * An empty bottom bar slot as tall as the system's bottom inset, which the content, the buttons
 * and the docked player rest on where there is no bottom bar to do it.
 */
@Composable
private fun SystemBottomInset() {
    Box(Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)))
}
