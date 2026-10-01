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

import androidx.compose.ui.unit.IntOffset
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.mutableIntStateOf
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.rememberFabScrollBehavior
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.ToolkitFabColumn
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.LocalFabHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.FabHost
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animate
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SinglePaneSceneStrategy
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationTint
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.ContentWidthBox
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.HideOnScrollTopBar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalContentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalPageSnackbarHostState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalTopBarStyleOverride
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.ShellTopAppBar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.TopBarSearch
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.rememberTopBarHideState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.frameTint
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.rememberTopBarScrollBehavior
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.resetTo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.topBarInsets
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.withoutBottom
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar.DefaultSnackbarHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar.LocalScaffoldSnackbars
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar.ScaffoldSnackbars
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellSearch
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellNavDisplay
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellSearch
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DestinationKind
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DrawerEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ContentCardShape
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.FollowScrollWithFrameTint
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalShellLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.besideNavigationTitle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.LocalShellMotion
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ScreenTransition
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ShellMotion
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.ListDetailSceneStrategy
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.ShellEntryInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.topShellInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellBackHandler
import androidx.navigationevent.NavigationEvent
import kotlinx.coroutines.launch
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.LocalShowBottomBarLabels
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonFeedback
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.R

/**
 * What a screen inside the shell can ask of the chrome around it: whether the navigation hides in
 * a drawer, and a way to open it, for screens that draw their own header.
 */
@Immutable
class ShellChromeController(
    val showsMenuButton: Boolean,
    val openNavigation: () -> Unit,
)

/**
 * The shell's snackbar host, set by `ShellHost`, and how many tab scaffolds draw it now. While none
 * does, on a start screen or under a page, `ShellHost` draws it itself, so a message the app shows
 * there is never left waiting on a host nobody draws.
 */
@Stable
internal class ShellSnackbarHost(val hostState: SnackbarHostState) {
    var drawnByTabs: Int by mutableIntStateOf(0)
}

internal val LocalShellSnackbarHost = staticCompositionLocalOf<ShellSnackbarHost?> { null }

val LocalShellChrome = staticCompositionLocalOf { ShellChromeController(showsMenuButton = false, openNavigation = {}) }

/**
 * The shell: app bar, tab content, and the navigation surface [LocalShellLayout] asks for, with
 * the player docked above the bottom edge and the banner on the bottom navigation bar, the only
 * place it shows.
 *
 * The player sits under the drawer and the modal rail, so opening either covers it; on a permanent
 * drawer it docks beside the drawer and covers it only when expanded.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShellChrome(graph: ShellGraph, navigator: ShellNavigator) {
    val settings = LocalShellSettings.current
    val layout = LocalShellLayout.current
    val frame = LocalShellFrame.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val playerExpansion = remember { Animatable(0f) }
    // Read as a yes or no, so the chrome recomposes when the player starts or stops opening, not on
    // every frame of it.
    val playerCollapsed by remember { derivedStateOf { playerExpansion.value == 0f } }
    var bottomDock by remember { mutableStateOf(0.dp) }
    val searches = rememberSaveable(
        saver = listSaver(save = { list -> list.map { it.query } }, restore = { queries -> queries.map(::ShellSearch) }),
    ) { graph.tabs.map { ShellSearch() } }
    // Where each tab screen puts the floating action buttons it declares, by the screen's entry.
    val fabHosts = remember { mutableMapOf<String, FabHost>() }
    // A host lives as long as its screen's entry: once a screen has left every tab's stack, its host
    // goes too, rather than one staying behind for every child ever opened.
    LaunchedEffect(navigator, graph) {
        snapshotFlow {
            graph.tabs.indices.flatMapTo(HashSet()) { tab -> navigator.tabStack(tab).map { tabEntryKey(tab, it) } }
        }.collect { live -> fabHosts.keys.retainAll(live) }
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
                wide -> frame?.openNavigation(layout.mode)
                layout.mode == ShellLayoutMode.BottomBar || layout.mode == ShellLayoutMode.Auto -> scope.launch { drawerState.open() }
            }
        }
    }
    val currentOpenNavigation by rememberUpdatedState(openNavigation)
    val showsMenuButton = layout.mode == ShellLayoutMode.BottomBar
    val chrome = remember(showsMenuButton) { ShellChromeController(showsMenuButton) { currentOpenNavigation() } }
    val highlightedTab = highlightedTab(graph, navigator, layout.listDetail)

    val tintMode = if (wide) settings.navigationTint else NavigationTint.None
    val tinted = tintMode != NavigationTint.None

    val body: @Composable (bottomBar: @Composable () -> Unit) -> Unit = { bottomBar ->
        ShellBody(
            graph = graph,
            navigator = navigator,
            searches = searches,
            fabHosts = fabHosts,
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
                body {}
                playerOverlay()
            } else {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = !navigator.isShowingChild && playerCollapsed,
                    drawerContent = {
                        ModalDrawerSheet(drawerState, windowInsets = DrawerContentInsets) {
                            ShellDrawerContent(graph, highlightedTab, showTabs = false, callbacks)
                        }
                    },
                ) {
                    Box(Modifier.fillMaxSize()) {
                        body {
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
 * The tab to draw as selected, or [NoTab]. A tab reads as selected while its root is on screen: not
 * while a child covers it, but still when the child is the detail open beside it. With
 * [pagesReplaceTab], as beside a rail or drawer where an open page takes the tab's place, not
 * while a page is open either; a bottom bar under a page keeps its tab marked for when it returns.
 */
internal fun highlightedTab(
    graph: ShellGraph,
    navigator: ShellNavigator,
    listDetail: Boolean,
    pagesReplaceTab: Boolean = false,
): Int {
    // A graph of pages only has no tab to mark.
    if (graph.tabs.isEmpty() || (pagesReplaceTab && navigator.pages.size > 1)) return NoTab
    val tabStack = navigator.currentTabStack
    val rootVisible = tabStack.size == 1 || (
        listDetail && tabStack.size == 2 &&
            graph.destination(tabStack[0]).paneRole == PaneRole.List &&
            graph.destination(tabStack[1]).paneRole == PaneRole.Detail
        )
    return if (rootVisible) navigator.currentTabIndex else NoTab
}

/** No tab is shown as selected. */
internal const val NoTab = -1

/** A drawer keeps clear of the top and start; its rows pad the bottom themselves, to scroll behind it. */
internal val DrawerContentInsets: WindowInsets
    @Composable get() = WindowInsets.safeDrawing.only(WindowInsetsSides.Start + WindowInsetsSides.Top)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShellBody(
    graph: ShellGraph,
    navigator: ShellNavigator,
    searches: List<ShellSearch>,
    fabHosts: MutableMap<String, FabHost>,
    callbacks: NavigationCallbacks,
    navigationNamesApp: Boolean,
    showMenuButton: Boolean,
    onMenuClick: () -> Unit,
    playerActive: Boolean,
    playerExpansion: () -> Float,
    onBottomDockChange: (Dp) -> Unit,
    tinted: Boolean,
    bottomBar: @Composable () -> Unit,
) {
    val settings = LocalShellSettings.current
    val layout = LocalShellLayout.current
    val density = LocalDensity.current
    val tabIndex = navigator.currentTabIndex
    val tab = graph.tabs[tabIndex]
    val topKey = navigator.currentTabStack.last()
    val destination = graph.destination(topKey)
    val isChild = destination.kind == DestinationKind.Child
    val style = layout.topBarFor(LocalTopBarStyleOverride.current ?: destination.topBar)
    val topScroll = rememberTopBarScrollBehavior(style)
    val bottomScroll = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    // Extended buttons fold to their icon while the content scrolls down.
    val fabScroll = rememberFabScrollBehavior()
    val hideOnScroll = settings.hideBottomBarOnScroll && !playerActive
    // The app bar slides away too, whatever its style, when the settings ask for it.
    val hideTopBar = settings.hideTopBarOnScroll
    val topHide = rememberTopBarHideState()
    // The bottom bar's height as laid out now, which shrinks as it hides.
    var bottomBarHeight by remember { mutableIntStateOf(0) }
    val bottomInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
    val search = tab.search?.takeIf { !isChild }?.let { TopBarSearch(searches[tabIndex], stringResource(it.hint)) }

    // Every destination starts with its bars as it declares them, whatever the last one scrolled them to.
    LaunchedEffect(topKey, hideOnScroll, hideTopBar) {
        fabScroll.expanded = true
        topHide.show()
        launch { topScroll.resetTo(collapsed = style == TopBarStyle.LargeCollapsed) }
        val offset = bottomScroll.state.heightOffset
        if (offset != 0f) animate(offset, 0f) { value, _ -> bottomScroll.state.heightOffset = value }
    }

    // Beside the navigation, the shared frame colour can follow this bar; a page the navigation
    // opens takes over from it while shown.
    FollowScrollWithFrameTint(underneath = true) { topScroll.frameTint(style) }

    val title = when {
        isChild -> destination.title?.invoke(topKey).orEmpty()
        navigationNamesApp -> stringResource(tab.label)
        else -> stringResource(graph.appTitle)
    }
    val besideNavigation = layout.mode != ShellLayoutMode.BottomBar
    val contentInsets = if (besideNavigation) {
        WindowInsets.safeDrawing.only(WindowInsetsSides.End + WindowInsetsSides.Vertical)
    } else {
        WindowInsets.safeDrawing
    }

    // Content and the floating action button keep clear of the docked player.
    val playerReserve by animateDpAsState(if (playerActive) MiniPlayerReserve else 0.dp, label = "PlayerReserve")

    // The tabs' snackbars, drawn by this scaffold above the bars, the player and the buttons. The
    // host is the shell's, so the app can show its own messages here too.
    val shellSnackbarHost = LocalShellSnackbarHost.current
    val snackbarHostState = shellSnackbarHost?.hostState ?: remember { SnackbarHostState() }
    if (shellSnackbarHost != null) {
        DisposableEffect(shellSnackbarHost) {
            shellSnackbarHost.drawnByTabs++
            onDispose { shellSnackbarHost.drawnByTabs-- }
        }
    }
    val snackbarScope = rememberCoroutineScope()
    val snackbars = remember(snackbarHostState, snackbarScope) { ScaffoldSnackbars(snackbarHostState, snackbarScope) }

    Scaffold(
        modifier = Modifier
            .nestedScroll(topScroll.nestedScrollConnection)
            // After the bar's own behaviour, so a large bar collapses before it slides away.
            .then(if (hideTopBar) Modifier.nestedScroll(topHide.nestedScrollConnection) else Modifier)
            .nestedScroll(fabScroll.nestedScrollConnection)
            .then(if (hideOnScroll) Modifier.nestedScroll(bottomScroll.nestedScrollConnection) else Modifier),
        topBar = {
            val barInsets = topBarInsets(reachesStart = !besideNavigation)
            HideOnScrollTopBar(topHide, enabled = hideTopBar) {
                ShellTopAppBar(
                    style = style,
                    title = title,
                    navigationIcon = {
                        // One button for both roles, so the menu becoming a back arrow crossfades in
                        // place; it slides out only where there is neither, beside a rail or drawer.
                        AnimatedIconButtonDirection(
                            visible = isChild || showMenuButton,
                            icon = ToolkitIcon.Vector(if (isChild) Icons.AutoMirrored.Filled.ArrowBack else Icons.Outlined.Menu),
                            contentDescription = stringResource(
                                if (isChild) CoreUiR.string.go_back else R.string.shell_open_navigation,
                            ),
                            onClick = if (isChild) navigator::goBack else onMenuClick,
                            // Opening the drawer or going back already moves the screen, so the
                            // Toolkit's main app bar plays only the click sound here.
                            feedback = ButtonFeedback(hapticFeedbackType = null),
                        )
                    },
                    actions = {
                        destination.actions?.invoke(this, topKey)
                        OverflowMenu(graph.overflow, callbacks, visible = !isChild)
                    },
                    scrollBehavior = topScroll,
                    // Beside the navigation, a page opened from it takes over this title in place.
                    titleModifier = if (isChild) Modifier else besideNavigationTitle(),
                    windowInsets = barInsets,
                    search = search,
                    // Tinted, the frame behind draws the bar's colour for the bar and the rail together.
                    colors = if (tinted) {
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = Color.Transparent,
                        )
                    } else {
                        null
                    },
                )
            }
        },
        bottomBar = {
            HideOnScrollBottomBar(
                scrollBehavior = if (hideOnScroll) bottomScroll else null,
                modifier = Modifier
                    .onSizeChanged { size ->
                        bottomBarHeight = size.height
                        onBottomDockChange(with(density) { size.height.toDp() })
                    }
                    .graphicsLayer {
                        val expansion = playerExpansion()
                        translationY = size.height * expansion
                        alpha = 1f - expansion
                    },
            ) {
                bottomBar()
            }
        },
        // The destination's own button, above the bars, the banner and the player; it scales out
        // and in as destinations change, and simply is not there for one without a button.
        floatingActionButton = {
            AnimatedContent(
                targetState = tabIndex to topKey,
                contentKey = { (index, key) -> index to key::class },
                transitionSpec = {
                    (scaleIn(initialScale = 0.6f) + fadeIn()) togetherWith (scaleOut(targetScale = 0.6f) + fadeOut())
                },
                label = "FloatingActionButton",
            ) { (index, key) ->
                val shown = graph.destination(key)
                val fab = shown.floatingActionButton
                // The buttons the graph describes, then those the screen itself declares. The host
                // is held here, so a screen's buttons still scale out after its entry has gone.
                val host = remember(index, key) { fabHosts.getOrPut(tabEntryKey(index, key)) { FabHost() } }
                val described = shown.floatingActionButtons?.invoke(key).orEmpty() + host.fabs
                if (fab != null || described.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .padding(bottom = playerReserve)
                            // Material's Scaffold places the buttons above the bottom bar as it is
                            // laid out, and the hiding bar takes the navigation bar's inset with it:
                            // hidden, it left them over the gesture bar. They rise by the part of
                            // the inset the bar no longer covers.
                            .offset { IntOffset(0, -(bottomInsets.getBottom(this) - bottomBarHeight).coerceAtLeast(0)) }
                            .graphicsLayer { alpha = 1f - playerExpansion() },
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        ToolkitFabColumn(described, expanded = fabScroll.expanded)
                        fab?.invoke(key)
                    }
                }
            }
        },
        snackbarHost = {
            DefaultSnackbarHost(
                snackbarState = snackbarHostState,
                // Clear of the player, and of the gesture bar once the bottom bar has hidden, as
                // the floating action buttons are.
                modifier = Modifier
                    .padding(bottom = playerReserve)
                    .offset { IntOffset(0, -(bottomInsets.getBottom(this) - bottomBarHeight).coerceAtLeast(0)) },
            )
        },
        contentWindowInsets = contentInsets,
        containerColor = if (tinted) Color.Transparent else MaterialTheme.colorScheme.background,
        // Explicit, since a transparent container would take its content colour from outside,
        // which is black by default: tab text on the dark theme would be unreadable.
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) { padding ->
        // The tabs reach the bottom of the window, behind the bars, the banner and the player, and
        // receive what covers them as content padding, so their lists scroll under all of it.
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding.withoutBottom())
                .consumeWindowInsets(padding)
                // Framed by the tinted navigation and app bar, the content is a card.
                .then(if (tinted) Modifier.clip(ContentCardShape).background(MaterialTheme.colorScheme.surface) else Modifier),
        ) {
            CompositionLocalProvider(
                LocalContentPadding provides PaddingValues(bottom = padding.calculateBottomPadding() + playerReserve),
                LocalPageSnackbarHostState provides snackbarHostState,
                LocalScaffoldSnackbars provides snackbars,
            ) {
                TabsNavDisplay(graph, navigator, searches, fabHosts)
            }
        }
    }
}

/**
 * The inner display: every tab's stack, of which it shows the first tab's and the selected tab's.
 *
 * Each tab decorates its own stack, so a tab's screens keep their scroll position and view models
 * while another tab is selected. Moving between tabs uses the tab transition; pushing and popping
 * children within one tab uses the activity transition.
 */
@Composable
private fun TabsNavDisplay(
    graph: ShellGraph,
    navigator: ShellNavigator,
    searches: List<ShellSearch>,
    fabHosts: MutableMap<String, FabHost>,
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
                tabEntry(graph, key, tabIndex, searches[tabIndex], fabHosts.getOrPut(tabEntryKey(tabIndex, key)) { FabHost() })
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

/** The transition of the destination on top of [this] scene. */
private fun Scene<*>.transition(graph: ShellGraph, layout: ShellLayoutMode): ScreenTransition {
    val info = topShellInfo ?: return graph.transitions.pages
    return graph.transitions.resolve(info.kind, info.transition, layout)
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

/** A tab screen's entry key: the same child can be open in two tabs at once. */
private fun tabEntryKey(tabIndex: Int, key: NavKey): String = "$tabIndex/$key"

private fun tabEntry(
    graph: ShellGraph,
    key: NavKey,
    tabIndex: Int,
    search: ShellSearch,
    fabHost: FabHost,
): NavEntry<NavKey> {
    val destination = graph.destination(key)
    return NavEntry(
        key = key,
        // The same child can be open in two tabs at once; the tab keeps their states apart.
        contentKey = tabEntryKey(tabIndex, key),
        metadata = ShellEntryInfo(destination.kind, tabIndex, destination.paneRole, destination.transition).toMetadata(),
    ) { entryKey ->
        val provided = if (graph.tabs[tabIndex].search != null) search else null
        CompositionLocalProvider(LocalShellSearch provides provided, LocalFabHost provides fabHost) {
            ContentWidthBox(
                maxWidth = LocalShellLayout.current.maxWidthFor(destination.contentWidth),
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            ) {
                destination.content(entryKey)
            }
        }
    }
}

/**
 * The app's banner, docked on the bottom navigation bar as a full-width strip joined to it. It
 * draws no background of its own.
 */
@Composable
private fun BannerSlot(banner: (@Composable () -> Unit)?, visible: Boolean) {
    if (banner == null) return
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.fillMaxWidth(),
        ) {
            banner()
        }
    }
}

@Composable
internal fun RailOverlay(open: Boolean, onDismiss: () -> Unit, rail: @Composable () -> Unit) {
    // The rail sits at the start edge, so it slides in from the left, or from the right in RTL.
    val fromStart = if (LocalLayoutDirection.current == LayoutDirection.Ltr) -1 else 1
    AnimatedVisibility(visible = open, enter = fadeIn(), exit = fadeOut()) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.32f))
                .pointerInput(Unit) { detectTapGestures { onDismiss() } },
        )
    }
    AnimatedVisibility(
        visible = open,
        enter = slideInHorizontally { fromStart * it } + fadeIn(),
        exit = slideOutHorizontally { fromStart * it } + fadeOut(),
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                ),
        ) {
            rail()
        }
    }
    if (open) ShellBackHandler(onBack = onDismiss)
}
