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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DrawerEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.BesideNavigation
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.FrameScrollTint
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalBesideNavigation
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalBesideNavigationTransitions
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalShellLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationTint
import kotlinx.coroutines.flow.collectLatest

/** Whether this layout keeps the navigation on screen beside the content: a rail or a permanent drawer. */
internal val ShellLayoutMode.keepsNavigationBeside: Boolean
    get() = this == ShellLayoutMode.Rail || this == ShellLayoutMode.ExpandedRail || this == ShellLayoutMode.PermanentDrawer

/** What the navigation beside the content shares with the shell drawn next to it. */
@Stable
internal class ShellFrameState(railExpanded: Boolean) {
    /** Whether the expanded rail layout shows its rail open, with labels, or collapsed. */
    var railExpanded: Boolean by mutableStateOf(railExpanded)

    /** Whether the rail layout's modal rail is open over the content. */
    var railOverlayOpen: Boolean by mutableStateOf(false)

    /**
     * How far the navigation and the app bar have taken the shared colour, from 0 to 1. The frame
     * draws it; the app bar drives it when the colour follows scrolling.
     */
    val tint: Animatable<Float, AnimationVector1D> = Animatable(0f)

    /** Opens the navigation the way [mode] does: the modal rail, or the expanded rail toggled. */
    fun openNavigation(mode: ShellLayoutMode) {
        when (mode) {
            ShellLayoutMode.Rail -> railOverlayOpen = true
            ShellLayoutMode.ExpandedRail -> railExpanded = !railExpanded
            else -> Unit
        }
    }
}

/**
 * The frame's state for the layout [mode] the window has now. The expanded rail starts collapsed,
 * and collapses again whenever the layout changes, as a rotation does: it is opened for the
 * window it was opened in. Within one layout it keeps what its button left it at, across
 * recreation and process death.
 */
@Composable
internal fun rememberShellFrameState(mode: ShellLayoutMode): ShellFrameState {
    var railExpanded by rememberSaveable { mutableStateOf(false) }
    var expandedIn by rememberSaveable { mutableStateOf(mode) }
    val state = remember { ShellFrameState(railExpanded && expandedIn == mode) }
    LaunchedEffect(mode) {
        if (mode != expandedIn) {
            state.railExpanded = false
            expandedIn = mode
        }
    }
    LaunchedEffect(state.railExpanded) { railExpanded = state.railExpanded }
    return state
}

/** The frame around the displays, for the chrome inside them; null outside `ShellHost`. */
internal val LocalShellFrame = staticCompositionLocalOf<ShellFrameState?> { null }

/**
 * Where the shell's displays are drawn. Beside a rail or a permanent drawer, the navigation is part
 * of this frame rather than of the shell's own screen, so every page opens in the space next to
 * it, like a tab, and the navigation stays where it is: the destination chosen in it stays in view
 * and marked, and choosing another replaces it. Below that width, and on start screens, the frame
 * adds nothing: the chrome draws a bottom bar and a modal drawer, and pages cover them.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun ShellFrame(
    graph: ShellGraph,
    navigator: ShellNavigator,
    state: ShellFrameState,
    content: @Composable () -> Unit,
) {
    val layout = LocalShellLayout.current
    val settings = LocalShellSettings.current
    val context = LocalContext.current
    val framed = layout.mode.keepsNavigationBeside && navigator.inShell

    LaunchedEffect(layout.mode) { state.railOverlayOpen = false }

    // Beside a rail or a permanent drawer, the navigation and the app bar can share one colour,
    // drawn once behind both, so they read as one frame around the content.
    val tintMode = if (framed) settings.navigationTint else NavigationTint.None
    // Every app bar beside the navigation that follows scrolling, the tabs' and the pages'; the
    // one on top drives the colour.
    val scrollTint = remember { FrameScrollTint() }
    LaunchedEffect(tintMode) {
        when (tintMode) {
            NavigationTint.None -> state.tint.snapTo(0f)
            NavigationTint.Always -> state.tint.snapTo(1f)
            // The bar on top drives it the way Material tints a bar: a large bar blends as it
            // collapses, the others switch, with a spring, once content scrolls under them.
            NavigationTint.OnScroll -> snapshotFlow { scrollTint.current }.collectLatest { target ->
                if (target.snap) {
                    state.tint.snapTo(target.amount)
                } else {
                    state.tint.animateTo(target.amount, spring(stiffness = Spring.StiffnessMediumLow))
                }
            }
        }
    }
    val tinted = tintMode != NavigationTint.None
    val followsScroll = tintMode == NavigationTint.OnScroll
    val untintedColor = MaterialTheme.colorScheme.surface
    val tintedColor = MaterialTheme.colorScheme.surfaceContainer
    val frameColor = if (tinted) Color.Transparent else untintedColor
    val besideNavigation = remember(framed, tinted, followsScroll, untintedColor, tintedColor) {
        if (framed) {
            BesideNavigation(tinted = tinted, scrollTint = scrollTint.takeIf { followsScroll }) {
                if (tinted) lerp(untintedColor, tintedColor, state.tint.value) else untintedColor
            }
        } else {
            null
        }
    }

    // The navigation replaces what is beside it instead of stacking on it: a tab closes the open
    // pages, and an entry swaps them for its own page.
    val callbacks = NavigationCallbacks(
        onTabClick = { index ->
            state.railOverlayOpen = false
            navigator.closePages()
            navigator.selectTab(index)
        },
        onEntryClick = { entry ->
            state.railOverlayOpen = false
            when (entry) {
                is DrawerEntry.Link -> if (navigator.pages != listOf(navigator.pages.first(), entry.key)) {
                    navigator.closePages()
                    navigator.navigate(entry.key)
                }
                is DrawerEntry.Action -> entry.onClick(context)
                DrawerEntry.Spacer -> Unit
            }
        },
    )
    val highlightedTab = highlightedTab(graph, navigator, layout.listDetail, pagesReplaceTab = true)
    // The page the navigation opened, at the bottom of the pages, stays marked while pages opened
    // from it stack above.
    val selectedEntry = navigator.pages.getOrNull(1)

    Box(
        Modifier
            .fillMaxSize()
            .then(
                if (tinted) {
                    Modifier.drawBehind { drawRect(lerp(untintedColor, tintedColor, state.tint.value)) }
                } else {
                    Modifier
                },
            ),
    ) {
        Row(Modifier.fillMaxSize()) {
            if (framed) {
                when (layout.mode) {
                    ShellLayoutMode.PermanentDrawer -> PermanentDrawerSheet(
                        Modifier.width(PermanentDrawerWidth),
                        drawerContainerColor = if (tinted) frameColor else DrawerDefaults.standardContainerColor,
                        windowInsets = DrawerContentInsets,
                    ) {
                        ShellDrawerContent(graph, highlightedTab, showTabs = true, callbacks, selectedEntry)
                    }

                    else -> ShellRail(
                        graph = graph,
                        selectedIndex = highlightedTab,
                        expanded = layout.mode == ShellLayoutMode.ExpandedRail && state.railExpanded,
                        callbacks = callbacks,
                        onMenuClick = { state.openNavigation(layout.mode) },
                        containerColor = frameColor,
                        selectedEntry = selectedEntry,
                    )
                }
            }
            Box(
                Modifier
                    .weight(1f)
                    // The navigation already keeps clear of the start edge's cutout and bars.
                    .then(if (framed) Modifier.consumeWindowInsets(WindowInsets.safeDrawing.only(WindowInsetsSides.Start)) else Modifier),
            ) {
                // Always there, whatever the layout, so a window crossing from one layout to
                // another keeps what is shown; its titles only pair up beside the navigation.
                SharedTransitionLayout(Modifier.fillMaxSize()) {
                    CompositionLocalProvider(
                        LocalShellFrame provides state,
                        // Pages the navigation opens beside it are drawn like tabs.
                        LocalBesideNavigation provides besideNavigation,
                        LocalBesideNavigationTransitions provides this,
                        content = content,
                    )
                }
            }
        }
        if (framed && layout.mode == ShellLayoutMode.Rail) {
            RailOverlay(open = state.railOverlayOpen, onDismiss = { state.railOverlayOpen = false }) {
                ShellRail(
                    graph = graph,
                    selectedIndex = highlightedTab,
                    expanded = true,
                    callbacks = callbacks,
                    onMenuClick = { state.railOverlayOpen = false },
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    selectedEntry = selectedEntry,
                )
            }
        }
    }
}
