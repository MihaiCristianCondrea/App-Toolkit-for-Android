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
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.BannerStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationTint
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.ContentWidthBox
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalContentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalTopBarStyleOverride
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.ShellTopAppBar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.TopBarSearch
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.rememberTopBarScrollBehavior
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.resetTo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.topBarInsets
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.withoutBottom
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellSearch
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellNavDisplay
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellSearch
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DestinationKind
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DrawerEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalShellLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.LocalShellMotion
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ScreenTransition
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ShellMotion
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.ListDetailSceneStrategy
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.ShellEntryInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.topShellInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellBackHandler
import androidx.navigationevent.NavigationEvent
import kotlinx.coroutines.flow.collectLatest
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

val LocalShellChrome = staticCompositionLocalOf { ShellChromeController(showsMenuButton = false, openNavigation = {}) }

/**
 * The shell: app bar, tab content, and the navigation surface [LocalShellLayout] asks for, with
 * the banner and player docked above the bottom edge.
 *
 * The player sits under the drawer and the modal rail, so opening either covers it; on a permanent
 * drawer it docks beside the drawer and covers it only when expanded.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShellChrome(graph: ShellGraph, navigator: ShellNavigator) {
    val settings = LocalShellSettings.current
    val layout = LocalShellLayout.current
    val context = LocalContext.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var railExpanded by rememberSaveable { mutableStateOf(true) }
    var railOverlayOpen by rememberSaveable { mutableStateOf(false) }
    val playerExpansion = remember { Animatable(0f) }
    var bottomDock by remember { mutableStateOf(0.dp) }
    var railWidth by remember { mutableStateOf(0.dp) }
    val searches = rememberSaveable(
        saver = listSaver(save = { list -> list.map { it.query } }, restore = { queries -> queries.map(::ShellSearch) }),
    ) { graph.tabs.map { ShellSearch() } }
    // Where each tab screen puts the floating action buttons it declares, by the screen's entry.
    val fabHosts = remember { mutableMapOf<String, FabHost>() }

    val player = graph.player?.takeIf { settings.accessoryMode.showsPlayer }
    val playerActive = player?.isActive?.invoke() == true
    val banner = graph.banner?.takeIf { settings.accessoryMode.showsBanner }

    LaunchedEffect(layout.mode) {
        railOverlayOpen = false
        drawerState.snapTo(DrawerValue.Closed)
    }

    val closeOverlays: () -> Unit = {
        railOverlayOpen = false
        if (drawerState.isOpen) scope.launch { drawerState.close() }
    }
    val callbacks = NavigationCallbacks(
        onTabClick = { index ->
            closeOverlays()
            navigator.selectTab(index)
        },
        onEntryClick = { entry ->
            closeOverlays()
            when (entry) {
                is DrawerEntry.Link -> navigator.navigate(entry.key)
                is DrawerEntry.Action -> entry.onClick(context)
                DrawerEntry.Spacer -> Unit
            }
        },
    )
    val openNavigation: () -> Unit = {
        when (layout.mode) {
            ShellLayoutMode.BottomBar -> scope.launch { drawerState.open() }
            ShellLayoutMode.Rail -> railOverlayOpen = true
            ShellLayoutMode.ExpandedRail -> railExpanded = !railExpanded
            ShellLayoutMode.PermanentDrawer, ShellLayoutMode.Auto -> Unit
        }
    }
    val chrome = ShellChromeController(showsMenuButton = layout.mode == ShellLayoutMode.BottomBar, openNavigation)

    // A tab reads as selected while its root is on screen: not while a child covers it, but still
    // when the child is the detail open beside it.
    val tabStack = navigator.currentTabStack
    val rootVisible = tabStack.size == 1 || (
        layout.listDetail && tabStack.size == 2 &&
            graph.destination(tabStack[0]).paneRole == PaneRole.List &&
            graph.destination(tabStack[1]).paneRole == PaneRole.Detail
        )
    val highlightedTab = if (rootVisible) navigator.currentTabIndex else NoTab

    // Beside a rail or a permanent drawer, the navigation and the app bar can share one colour,
    // drawn once behind both, so they read as one frame around the content.
    val wide = layout.mode == ShellLayoutMode.Rail ||
        layout.mode == ShellLayoutMode.ExpandedRail ||
        layout.mode == ShellLayoutMode.PermanentDrawer
    val tintMode = if (wide) settings.navigationTint else NavigationTint.None
    val tint = remember { Animatable(0f) }
    LaunchedEffect(tintMode) {
        when (tintMode) {
            NavigationTint.None -> tint.snapTo(0f)
            NavigationTint.Always -> tint.snapTo(1f)
            NavigationTint.OnScroll -> Unit // ShellBody follows the app bar.
        }
    }
    val tinted = tintMode != NavigationTint.None
    val untintedColor = MaterialTheme.colorScheme.surface
    val tintedColor = MaterialTheme.colorScheme.surfaceContainer
    val frameColor = if (tinted) Color.Transparent else untintedColor

    val body: @Composable (bottomBar: @Composable () -> Unit) -> Unit = { bottomBar ->
        ShellBody(
            graph = graph,
            navigator = navigator,
            searches = searches,
            fabHosts = fabHosts,
            callbacks = callbacks,
            // With the app named in the navigation beside it, the app bar names the tab instead.
            navigationNamesApp = layout.mode == ShellLayoutMode.PermanentDrawer ||
                (layout.mode == ShellLayoutMode.ExpandedRail && railExpanded),
            showMenuButton = chrome.showsMenuButton,
            onMenuClick = openNavigation,
            playerActive = playerActive,
            playerExpansion = { playerExpansion.value },
            onBottomDockChange = { bottomDock = it },
            tinted = tinted,
            followScrollTint = if (tintMode == NavigationTint.OnScroll) tint else null,
            bottomBar = bottomBar,
        )
    }
    val playerOverlay: @Composable () -> Unit = {
        if (player != null) {
            ShellPlayerOverlay(
                player = player,
                active = playerActive,
                expansion = playerExpansion,
                dockBottom = bottomDock,
                dockStart = if (layout.mode == ShellLayoutMode.BottomBar) 0.dp else railWidth,
            )
        }
    }
    val wideBottomBar: @Composable () -> Unit = {
        Column(Modifier.windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))) {
            BannerSlot(banner, visible = !playerActive, settings.bannerStyle, navigationBelow = false)
        }
    }

    CompositionLocalProvider(LocalShellChrome provides chrome) {
        Box(
            Modifier
                .fillMaxSize()
                .then(
                    if (tinted) {
                        Modifier.drawBehind { drawRect(lerp(untintedColor, tintedColor, tint.value)) }
                    } else {
                        Modifier
                    },
                ),
        ) {
            when (layout.mode) {
                ShellLayoutMode.BottomBar, ShellLayoutMode.Auto -> ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = !navigator.isShowingChild && playerExpansion.value == 0f,
                    drawerContent = {
                        ModalDrawerSheet(drawerState, windowInsets = DrawerContentInsets) {
                            ShellDrawerContent(graph, highlightedTab, showTabs = false, callbacks)
                        }
                    },
                ) {
                    Box(Modifier.fillMaxSize()) {
                        body {
                            Column {
                                BannerSlot(banner, visible = !playerActive, settings.bannerStyle, navigationBelow = true)
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

                ShellLayoutMode.Rail, ShellLayoutMode.ExpandedRail -> {
                    Row(Modifier.fillMaxSize()) {
                        ShellRail(
                            graph = graph,
                            selectedIndex = highlightedTab,
                            expanded = layout.mode == ShellLayoutMode.ExpandedRail && railExpanded,
                            callbacks = callbacks,
                            onMenuClick = openNavigation,
                            modifier = Modifier.onSizeChanged { railWidth = with(density) { it.width.toDp() } },
                            containerColor = frameColor,
                        )
                        Box(Modifier.weight(1f)) { body(wideBottomBar) }
                    }
                    playerOverlay()
                    if (layout.mode == ShellLayoutMode.Rail) {
                        RailOverlay(open = railOverlayOpen, onDismiss = { railOverlayOpen = false }) {
                            ShellRail(
                                graph = graph,
                                selectedIndex = highlightedTab,
                                expanded = true,
                                callbacks = callbacks,
                                onMenuClick = { railOverlayOpen = false },
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            )
                        }
                    }
                }

                ShellLayoutMode.PermanentDrawer -> {
                    PermanentNavigationDrawer(
                        drawerContent = {
                            PermanentDrawerSheet(
                                Modifier
                                    .width(PermanentDrawerWidth)
                                    .onSizeChanged { railWidth = with(density) { it.width.toDp() } },
                                drawerContainerColor = if (tinted) frameColor else DrawerDefaults.standardContainerColor,
                                windowInsets = DrawerContentInsets,
                            ) {
                                ShellDrawerContent(graph, highlightedTab, showTabs = true, callbacks)
                            }
                        },
                    ) {
                        body(wideBottomBar)
                    }
                    playerOverlay()
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

/** No tab is shown as selected. */
private const val NoTab = -1

/** The content's corner beside a tinted rail or drawer, under the app bar. */
private val ContentCardShape = RoundedCornerShape(topStart = 24.dp)

/** A drawer keeps clear of the top and start; its rows pad the bottom themselves, to scroll behind it. */
private val DrawerContentInsets: WindowInsets
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
    followScrollTint: Animatable<Float, AnimationVector1D>?,
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
    val hideOnScroll = settings.hideBottomBarOnScroll && !playerActive
    val search = tab.search?.takeIf { !isChild }?.let { TopBarSearch(searches[tabIndex], stringResource(it.hint)) }

    // Every destination starts with its bars as it declares them, whatever the last one scrolled them to.
    LaunchedEffect(topKey, hideOnScroll) {
        launch { topScroll.resetTo(collapsed = style == TopBarStyle.LargeCollapsed) }
        val offset = bottomScroll.state.heightOffset
        if (offset != 0f) animate(offset, 0f) { value, _ -> bottomScroll.state.heightOffset = value }
    }

    // The shared frame colour follows the app bar the way Material tints the bar itself: a large
    // bar blends as it collapses, the others switch, with a spring, once content scrolls under.
    if (followScrollTint != null) {
        LaunchedEffect(topScroll, style, followScrollTint) {
            snapshotFlow {
                when {
                    style.isLarge -> topScroll.state.collapsedFraction
                    topScroll.state.overlappedFraction > 0.01f -> 1f
                    else -> 0f
                }
            }.collectLatest { target ->
                if (style.isLarge) {
                    followScrollTint.snapTo(target)
                } else {
                    followScrollTint.animateTo(target, spring(stiffness = Spring.StiffnessMediumLow))
                }
            }
        }
    }

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

    Scaffold(
        modifier = Modifier
            .nestedScroll(topScroll.nestedScrollConnection)
            .then(if (hideOnScroll) Modifier.nestedScroll(bottomScroll.nestedScrollConnection) else Modifier),
        topBar = {
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
                windowInsets = topBarInsets(reachesStart = !besideNavigation),
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
        },
        bottomBar = {
            HideOnScrollBottomBar(
                scrollBehavior = if (hideOnScroll) bottomScroll else null,
                modifier = Modifier
                    .onSizeChanged { size -> onBottomDockChange(with(density) { size.height.toDp() }) }
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
                // The buttons the graph describes, then those the screen itself declares.
                val described = shown.floatingActionButtons?.invoke(key).orEmpty() +
                    fabHosts.getOrPut(tabEntryKey(index, key)) { FabHost() }.fabs
                if (fab != null || described.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .padding(bottom = playerReserve)
                            .graphicsLayer { alpha = 1f - playerExpansion() },
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        ToolkitFabColumn(described)
                        fab?.invoke(key)
                    }
                }
            }
        },
        contentWindowInsets = contentInsets,
        containerColor = if (tinted) Color.Transparent else MaterialTheme.colorScheme.background,
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
 * The app's banner, in the container its [style] asks for: a card floating over the content with
 * the mini player's corners and margins, or a strip joined to the navigation bar. Automatically,
 * it docks onto a bottom navigation bar and floats where [navigationBelow] is false, beside a
 * rail or drawer. The banner itself draws no background.
 */
@Composable
private fun BannerSlot(
    banner: (@Composable () -> Unit)?,
    visible: Boolean,
    style: BannerStyle,
    navigationBelow: Boolean,
) {
    if (banner == null) return
    val floating = style == BannerStyle.Floating || (style == BannerStyle.Automatic && !navigationBelow)
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
    ) {
        when (floating) {
            true -> Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 3.dp,
                shadowElevation = 6.dp,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth(),
            ) {
                banner()
            }

            false -> Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.fillMaxWidth(),
            ) {
                banner()
            }
        }
    }
}

@Composable
private fun RailOverlay(open: Boolean, onDismiss: () -> Unit, rail: @Composable () -> Unit) {
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
