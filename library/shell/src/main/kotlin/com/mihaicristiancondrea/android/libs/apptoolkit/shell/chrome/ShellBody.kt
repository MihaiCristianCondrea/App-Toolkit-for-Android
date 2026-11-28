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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonFeedback
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.ToolkitFabColumn
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.rememberFabScrollBehavior
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.HideOnScrollTopBar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalContentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalPageSnackbarHostState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalTopBarStyleOverride
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalTopBarTint
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.ShellTopAppBar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.TopBarSearch
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.forArticle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.frameTint
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.rememberTopBarHideState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.rememberTopBarScrollBehavior
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.resetTo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.topBarInsets
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.withoutBottom
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar.DefaultSnackbarHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar.LocalScaffoldSnackbars
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar.ScaffoldSnackbars
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellSearch
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DestinationKind
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.LocalShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarScrollMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ContentCardShape
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.FollowScrollWithFrameTint
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.FrameTint
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalShellLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.besideNavigationTitle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.R
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

@Composable
internal fun ShellBody(
    graph: ShellGraph,
    navigator: ShellNavigator,
    searches: ImmutableList<ShellSearch>,
    entryHosts: EntryHosts,
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
    val article = remember(tabIndex, topKey) { entryHosts.articleHostFor(tabEntryKey(tabIndex, topKey)) }
    val style = layout.topBarFor(LocalTopBarStyleOverride.current ?: destination.topBar).forArticle(article)
    // A static bar keeps its style but gets a scroll behavior that never collapses it.
    val reactsToScroll = destination.topBarScrollMode != TopBarScrollMode.Static
    val topScroll = rememberTopBarScrollBehavior(if (reactsToScroll) style else TopBarStyle.Small)
    val bottomScroll = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    // Extended buttons fold to their icon while the content scrolls down.
    val fabScroll = rememberFabScrollBehavior()
    val hideOnScroll = settings.hideBottomBarOnScroll && !playerActive && LocalShellCapabilities.current.hasBottomNavigationTabs
    // The app bar slides away too, whatever its style, when the settings ask for it.
    val hideTopBar = reactsToScroll && settings.hideTopBarOnScroll
    val topHide = rememberTopBarHideState()
    // The bottom bar's height as laid out now, which shrinks as it hides.
    var bottomBarHeight by remember { mutableIntStateOf(0) }
    val bottomInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
    val search = tab.search?.takeIf { !isChild }?.let { TopBarSearch(searches[tabIndex], stringResource(it.hint)) }

    // Every destination starts with its bars as it declares them, whatever the last one scrolled them to.
    LaunchedEffect(topKey, hideOnScroll, hideTopBar) {
        fabScroll.expanded = true
        topHide.show()
        if (reactsToScroll) launch { topScroll.resetTo(collapsed = style == TopBarStyle.LargeCollapsed) }
        val offset = bottomScroll.state.heightOffset
        if (offset != 0f) animate(offset, 0f) { value, _ -> bottomScroll.state.heightOffset = value }
    }

    // Beside the navigation, the shared frame colour can follow this bar; a page the navigation
    // opens takes over from it while shown.
    FollowScrollWithFrameTint(underneath = true) { if (reactsToScroll) topScroll.frameTint(style) else FrameTint.None }

    // Tabs under the bar take its colour; over a tinted frame the bar is see-through and has none.
    val topBarTint: (() -> FrameTint)? = if (reactsToScroll && !tinted) {
        remember(topScroll, style) { { topScroll.frameTint(style) } }
    } else {
        null
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
            // The buttons fold on the scroll the bar takes too, as on a page too short to scroll far.
            .then(if (reactsToScroll) Modifier.nestedScroll(fabScroll.observing(topScroll.nestedScrollConnection)) else Modifier)
            // After the bar's own behaviour, so a large bar collapses before it slides away.
            .then(if (hideTopBar) Modifier.nestedScroll(fabScroll.observing(topHide.nestedScrollConnection)) else Modifier)
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
                        // One destination's actions cross-fade into the next and the row's width
                        // follows, so the buttons that stay glide into place instead of jumping.
                        val motion = MaterialTheme.motionScheme
                        AnimatedContent(
                            targetState = topKey,
                            contentKey = { graph.destination(it) },
                            // Centred on the row and held to its end, so a button entering a row that
                            // was empty grows in place. At the default top start it rode the size
                            // change from the corner, sliding right and down into place.
                            contentAlignment = Alignment.CenterEnd,
                            transitionSpec = {
                                (fadeIn(motion.defaultEffectsSpec()) + scaleIn(motion.defaultSpatialSpec(), initialScale = 0.8f))
                                    .togetherWith(fadeOut(motion.defaultEffectsSpec()) + scaleOut(motion.defaultSpatialSpec(), targetScale = 0.8f))
                                    .using(SizeTransform(clip = false))
                            },
                            label = "ShellActions",
                        ) { key ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                graph.destination(key).actions?.invoke(this, key)
                            }
                        }
                        OverflowMenu(graph.overflow, callbacks, visible = !isChild)
                    },
                    scrollBehavior = topScroll.takeIf { reactsToScroll },
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
                    article = article,
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
                val host = remember(index, key) { entryHosts.fabHostFor(tabEntryKey(index, key)) }
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
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
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
                LocalTopBarTint provides topBarTint,
            ) {
                TabsNavDisplay(graph, navigator, searches, entryHosts)
            }
        }
    }
}
