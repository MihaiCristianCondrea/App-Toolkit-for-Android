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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuOpen
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.navigation3.ui.NavDisplay
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.navigation.NavigationItemsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.states.MainUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.views.fab.MainFloatingActionButton
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.views.navigation.isDrawerItemSelected
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.AppNavigationEntryContext
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.NavigationManager
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.navigation.handleNavigationItemClick
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.views.dialogs.ChangelogDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.views.navigation.MainTopAppBar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.startActivitySafely
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.navigation.NavigationAnimations
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.navigation.Navigator
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.navigation.entryProviderFor
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.navigation.rememberNavigationEntryDecorators
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.navigation.rememberNavigationState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.hapticDrawerSwipe
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar.DefaultSnackbarHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.window.rememberWindowWidthSizeClass
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.animations.BottomNavTransitions
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.animations.NativeActivityTransitions
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.animations.rememberBottomNavTransitions
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.animations.rememberNativeActivityTransitions
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.BottomBarItem
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDrawerItem
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.StableNavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.isTopLevel
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.NavigationDrawerRoutes
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ui.NavigationDrawerItemContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

/**
 * Hosts the adaptive app shell and pushed navigation destinations.
 *
 * @param entryBuilders Supplies the navigation entries for this shell. Passed in rather than
 * imported so the shell does not have to see the feature modules whose destinations it renders;
 * `:sample:app` is the only module that knows the full set.
 * @param startRoute The persisted top-level destination resolved by the application host before
 * this shell is composed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    startRoute: StableNavKey,
    bottomBarItems: ImmutableList<BottomBarItem<StableNavKey>>,
    fabSupportedRoutes: ImmutableSet<StableNavKey>,
    entryBuilders: (AppNavigationEntryContext) -> List<NavigationEntryBuilder<StableNavKey>>,
    onTitleLookup: @Composable (StableNavKey) -> String,
    onLaunchActivity: (StableNavKey) -> Boolean = { false },
    onNavigationRequested: (String) -> Unit = {},
) {
    val viewModel: MainViewModel = koinViewModel()
    val uiStateScreen by viewModel.uiState.collectAsStateWithLifecycle()
    val uiState = uiStateScreen.data ?: MainUiState()
    val activity = LocalActivity.current

    val navigationState = rememberNavigationState(
        startRoute = startRoute,
        topLevelRoutes = remember(bottomBarItems) { bottomBarItems.map { it.route }.toImmutableSet() }
    )
    val navigator = remember(navigationState) { Navigator(state = navigationState) }

    val windowWidthSizeClass: AppWindowWidthSizeClass = rememberWindowWidthSizeClass()

    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    val nativeActivityTransitions = rememberNativeActivityTransitions()

    val navigationManager: NavigationManager = koinInject()

    LaunchedEffect(navigationManager, navigator, onLaunchActivity) {
        navigationManager.navigationRequests.collect { route ->
            if (!onLaunchActivity(route)) {
                navigator.navigate(route)
            }
        }
    }

    var randomAppHandler: (() -> Unit)? by remember { mutableStateOf(null) }
    val randomAppHandlerState: State<(() -> Unit)?> = rememberUpdatedState(randomAppHandler)
    var showChangelog by remember { mutableStateOf(false) }

    val resolvedEntryBuilders: List<NavigationEntryBuilder<StableNavKey>> =
        remember(windowWidthSizeClass, entryBuilders) {
            entryBuilders(
                AppNavigationEntryContext(
                    paddingValues = PaddingValues(),
                    windowWidthSizeClass = windowWidthSizeClass,
                    onRandomAppHandlerChanged = { _, handler -> randomAppHandler = handler },
                )
            )
        }

    val entryDecorators = rememberNavigationEntryDecorators<StableNavKey>()

    val entryProvider: (StableNavKey) -> NavEntry<StableNavKey> =
        remember(resolvedEntryBuilders) {
            entryProviderFor(resolvedEntryBuilders)
        }

    val sceneStrategy = remember(
        uiState,
        navigator,
        windowWidthSizeClass,
        snackBarHostState,
        randomAppHandlerState,
        nativeActivityTransitions,
        entryProvider,
        onTitleLookup,
        bottomBarItems,
        fabSupportedRoutes,
    ) {
        MainSceneStrategy(
            uiState = uiState,
            navigator = navigator,
            windowWidthSizeClass = windowWidthSizeClass,
            snackBarHostState = snackBarHostState,
            onChangelogRequested = { showChangelog = true },
            randomAppHandlerState = randomAppHandlerState,
            nativeActivityTransitions = nativeActivityTransitions,
            entryProvider = entryProvider,
            onTitleLookup = onTitleLookup,
            onNavigationRequested = onNavigationRequested,
            bottomBarItems = bottomBarItems,
            fabSupportedRoutes = fabSupportedRoutes,
        )
    }

    NavDisplay(
        backStack = navigationState.currentBackStack,
        entryDecorators = entryDecorators,
        entryProvider = entryProvider,
        onBack = {
            if (!navigator.goBack()) {
                activity?.finish()
            }
        },
        sceneStrategies = listOf(sceneStrategy),
        transitionSpec = { NavigationAnimations.default() },
        popTransitionSpec = { NavigationAnimations.default() },
    )

    if (showChangelog) {
        ChangelogDialog(
            onDismiss = { showChangelog = false },
        )
    }
}

/**
 * Selects a persistent shell for top-level tabs and a focused shell for pushed destinations.
 *
 * Each scene owns its motion metadata so pushed screens retain native forward/back behavior,
 * while top-level tab changes are animated only inside the stable main shell. Shell-owned
 * transient callbacks are passed through stable state holders so they cannot replace a scene
 * while a return animation is in progress.
 */
private class MainSceneStrategy(
    private val uiState: MainUiState,
    private val navigator: Navigator<StableNavKey>,
    private val windowWidthSizeClass: AppWindowWidthSizeClass,
    private val snackBarHostState: SnackbarHostState,
    private val onChangelogRequested: () -> Unit,
    private val randomAppHandlerState: State<(() -> Unit)?>,
    private val nativeActivityTransitions: NativeActivityTransitions,
    private val entryProvider: (StableNavKey) -> NavEntry<StableNavKey>,
    private val onTitleLookup: @Composable (StableNavKey) -> String,
    private val onNavigationRequested: (String) -> Unit,
    private val bottomBarItems: ImmutableList<BottomBarItem<StableNavKey>>,
    private val fabSupportedRoutes: ImmutableSet<StableNavKey>,
) : SceneStrategy<StableNavKey> {
    override fun SceneStrategyScope<StableNavKey>.calculateScene(
        entries: List<NavEntry<StableNavKey>>
    ): Scene<StableNavKey>? {
        val currentEntry = entries.lastOrNull() ?: return null
        val currentRoute = checkNotNull(currentEntry.contentKey as? StableNavKey) {
            "Main navigation entries must use their StableNavKey as NavEntry.contentKey."
        }

        return if (currentRoute.isTopLevel) {
            MainShellScene(
                key = "main-shell",
                entry = currentEntry,
                previousEntries = entries.dropLast(1),
                uiState = uiState,
                navigator = navigator,
                windowWidthSizeClass = windowWidthSizeClass,
                snackBarHostState = snackBarHostState,
                onChangelogRequested = onChangelogRequested,
                randomAppHandlerState = randomAppHandlerState,
                onBack = onBack,
                nativeActivityTransitions = nativeActivityTransitions,
                entryProvider = entryProvider,
                onTitleLookup = onTitleLookup,
                onNavigationRequested = onNavigationRequested,
                bottomBarItems = bottomBarItems,
                fabSupportedRoutes = fabSupportedRoutes,
            )
        } else {
            SubScreenScene(
                route = currentRoute,
                entry = currentEntry,
                previousEntries = entries.dropLast(1),
                onBack = onBack,
                nativeActivityTransitions = nativeActivityTransitions,
                entryProvider = entryProvider,
                onTitleLookup = onTitleLookup,
            )
        }
    }
}

/** Shell-owned scene for top-level destinations and their persistent navigation chrome. */
private data class MainShellScene(
    override val key: Any,
    private val entry: NavEntry<StableNavKey>,
    override val previousEntries: List<NavEntry<StableNavKey>>,
    private val uiState: MainUiState,
    private val navigator: Navigator<StableNavKey>,
    private val windowWidthSizeClass: AppWindowWidthSizeClass,
    private val snackBarHostState: SnackbarHostState,
    private val onChangelogRequested: () -> Unit,
    private val randomAppHandlerState: State<(() -> Unit)?>,
    private val onBack: () -> Unit,
    private val nativeActivityTransitions: NativeActivityTransitions,
    private val entryProvider: (StableNavKey) -> NavEntry<StableNavKey>,
    private val onTitleLookup: @Composable (StableNavKey) -> String,
    private val onNavigationRequested: (String) -> Unit,
    private val bottomBarItems: ImmutableList<BottomBarItem<StableNavKey>>,
    private val fabSupportedRoutes: ImmutableSet<StableNavKey>,
) : Scene<StableNavKey> {
    override val entries: List<NavEntry<StableNavKey>> = listOf(entry)
    override val metadata: Map<String, Any> = metadata {
        put(NavDisplay.TransitionKey) { nativeActivityTransitions.forward() }
        put(NavDisplay.PopTransitionKey) { nativeActivityTransitions.pop() }
        put(NavDisplay.PredictivePopTransitionKey) { nativeActivityTransitions.predictivePop() }
    }

    override val content: @Composable () -> Unit = {
        MainShell(
            uiState = uiState,
            navigator = navigator,
            windowWidthSizeClass = windowWidthSizeClass,
            snackBarHostState = snackBarHostState,
            onChangelogRequested = onChangelogRequested,
            randomAppHandlerState = randomAppHandlerState,
            onBack = onBack,
            entryProvider = entryProvider,
            onTitleLookup = onTitleLookup,
            onNavigationRequested = onNavigationRequested,
            bottomBarItems = bottomBarItems,
            fabSupportedRoutes = fabSupportedRoutes,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainShell(
    uiState: MainUiState,
    navigator: Navigator<StableNavKey>,
    windowWidthSizeClass: AppWindowWidthSizeClass,
    snackBarHostState: SnackbarHostState,
    onChangelogRequested: () -> Unit,
    randomAppHandlerState: State<(() -> Unit)?>,
    onBack: () -> Unit,
    entryProvider: (StableNavKey) -> NavEntry<StableNavKey>,
    onTitleLookup: @Composable (StableNavKey) -> String,
    onNavigationRequested: (String) -> Unit,
    bottomBarItems: ImmutableList<BottomBarItem<StableNavKey>>,
    fabSupportedRoutes: ImmutableSet<StableNavKey>,
) {
    val context = LocalContext.current
    val navigationItemsProvider: NavigationItemsProvider = koinInject()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val bottomNavTransitions = rememberBottomNavTransitions()
    val currentRoute = navigator.state.currentBackStack.last()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val modalDrawerEnabled = windowWidthSizeClass == AppWindowWidthSizeClass.Compact
    val randomAppHandler = randomAppHandlerState.value

    // Read through derivedStateOf so the shell recomposes when the FAB should change shape, not on
    // every scroll frame: contentOffset changes continuously while the derived boolean rarely does.
    val isScrollingUp: Boolean by remember(scrollBehavior) {
        derivedStateOf { scrollBehavior.state.contentOffset > -SizeConstants.MediumSize.value }
    }

    val appBarTitle = onTitleLookup(currentRoute)

    val onNavigationDrawerItemClick: (NavigationDrawerItem, DrawerState?, CoroutineScope?) -> Unit =
        { item, state, scope ->
            handleNavigationItemClick(
                context = context,
                item = item,
                drawerState = state,
                coroutineScope = scope,
                onChangelogRequested = onChangelogRequested,
                onInternalNavigationRequested = { route ->
                    onNavigationRequested(route)
                },
            )
        }

    val isFabVisible: Boolean = remember(currentRoute, fabSupportedRoutes) {
        fabSupportedRoutes.contains(currentRoute)
    }
    val isFabExtended: Boolean = isScrollingUp

    val railDrawerItems: Pair<List<NavigationDrawerItem>, List<NavigationDrawerItem>> =
        remember(uiState.navigationDrawerItems) {
            uiState.navigationDrawerItems.partition { item: NavigationDrawerItem ->
                item.route !in BottomDrawerActionRoutes
            }
        }

    val shellContent: @Composable () -> Unit = {
        BackHandler(enabled = drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            if (!modalDrawerEnabled) {
                NavigationRail {
                    bottomBarItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationRailItem(
                            selected = isSelected,
                            onClick = { navigator.navigate(item.route) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.icon,
                                    contentDescription = stringResource(item.title),
                                )
                            },
                            label = { Text(stringResource(item.title)) },
                        )
                    }
                    railDrawerItems.first.forEach { item ->
                        val isSelected = isDrawerItemSelected(item.route, currentRoute, navigationItemsProvider)
                        NavigationRailItem(
                            selected = isSelected,
                            onClick = { onNavigationDrawerItemClick(item, null, null) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.icon,
                                    contentDescription = stringResource(item.title),
                                )
                            },
                            label = { Text(stringResource(item.title)) },
                        )
                    }
                    railDrawerItems.second.forEach { item ->
                        val isSelected = isDrawerItemSelected(item.route, currentRoute, navigationItemsProvider)
                        NavigationRailItem(
                            selected = isSelected,
                            onClick = { onNavigationDrawerItemClick(item, null, null) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.icon,
                                    contentDescription = stringResource(item.title),
                                )
                            },
                            label = { Text(stringResource(item.title)) },
                        )
                    }
                }
            }

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    MainTopAppBar(
                        title = appBarTitle,
                        navigationIcon = when {
                            drawerState.isOpen -> Icons.AutoMirrored.Outlined.MenuOpen
                            modalDrawerEnabled -> Icons.Outlined.Menu
                            else -> null
                        },
                        onNavigationIconClick = {
                            when {
                                drawerState.isOpen -> coroutineScope.launch { drawerState.close() }
                                modalDrawerEnabled -> coroutineScope.launch { drawerState.open() }
                            }
                        },
                        onSupportClick = {
                            onNavigationRequested(NavigationDrawerRoutes.ROUTE_SUPPORT)
                        },
                        showSupportAction = remember(currentRoute, bottomBarItems) {
                            bottomBarItems.any { it.route == currentRoute }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                },
                bottomBar = {
                    if (modalDrawerEnabled) {
                        NavigationBar {
                            bottomBarItems.forEach { item ->
                                val isSelected = currentRoute == item.route
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { navigator.navigate(item.route) },
                                    icon = {
                                        Icon(
                                            imageVector = if (isSelected) item.selectedIcon else item.icon,
                                            contentDescription = stringResource(item.title),
                                        )
                                    },
                                    label = { Text(stringResource(item.title)) },
                                )
                            }
                        }
                    }
                },
                floatingActionButton = {
                    val fabEnabled = isFabVisible && randomAppHandler != null
                    MainFloatingActionButton(
                        visible = fabEnabled,
                        enabled = fabEnabled,
                        expanded = isFabExtended,
                        onClick = { randomAppHandler?.invoke() },
                    )
                },
                snackbarHost = { DefaultSnackbarHost(snackbarState = snackBarHostState) },
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues),
                ) {
                    // Keep the chrome mounted, but let a content-only NavDisplay own tab motion and
                    // predictive-pop progress. This preserves the existing bottom-tab animation style
                    // without making the whole app shell part of the predictive back scene.
                    TopLevelContentNavDisplay(
                        navigator = navigator,
                        entryProvider = entryProvider,
                        onBack = onBack,
                        bottomNavTransitions = bottomNavTransitions,
                        bottomBarItems = bottomBarItems,
                    )
                }
            }
        }
    }

    if (modalDrawerEnabled) {
        ModalNavigationDrawer(
            modifier = Modifier.hapticDrawerSwipe(state = drawerState),
            drawerState = drawerState,
            gesturesEnabled = true,
            drawerContent = {
                ModalDrawerSheet(drawerState = drawerState) {
                    LargeVerticalSpacer()
                    uiState.navigationDrawerItems.forEach { item ->
                        NavigationDrawerItemContent(
                            item = item,
                            selected = isDrawerItemSelected(item.route, currentRoute, navigationItemsProvider),
                            dividerRoutes = persistentSetOf(),
                            handleNavigationItemClick = {
                                onNavigationDrawerItemClick(
                                    item,
                                    drawerState,
                                    coroutineScope
                                )
                            },
                        )
                    }
                }
            },
            content = shellContent,
        )
    } else {
        shellContent()
    }
}

@Composable
private fun TopLevelContentNavDisplay(
    navigator: Navigator<StableNavKey>,
    entryProvider: (StableNavKey) -> NavEntry<StableNavKey>,
    onBack: () -> Unit,
    bottomNavTransitions: BottomNavTransitions,
    bottomBarItems: ImmutableList<BottomBarItem<StableNavKey>>,
) {
    NavDisplay(
        entries = navigator.state.toDecoratedTopLevelEntries(entryProvider),
        onBack = onBack,
        transitionSpec = {
            bottomNavTransitions.betweenTabEntries(
                initialState = initialState,
                targetState = targetState,
                bottomBarItems = bottomBarItems,
            )
        },
        popTransitionSpec = {
            bottomNavTransitions.betweenTabEntries(
                initialState = initialState,
                targetState = targetState,
                bottomBarItems = bottomBarItems,
            )
        },
        predictivePopTransitionSpec = {
            bottomNavTransitions.betweenTabEntries(
                initialState = initialState,
                targetState = targetState,
                bottomBarItems = bottomBarItems,
            )
        },
    )
}

private fun BottomNavTransitions.betweenTabEntries(
    initialState: Any?,
    targetState: Any?,
    bottomBarItems: ImmutableList<BottomBarItem<StableNavKey>>,
) = run {
    val initialTabIndex = tabIndex(initialState, bottomBarItems)
    val targetTabIndex = tabIndex(targetState, bottomBarItems)

    if (initialTabIndex >= 0 && targetTabIndex >= 0) {
        betweenTabs(forward = targetTabIndex >= initialTabIndex)
    } else {
        transition()
    }
}

private fun tabIndex(
    state: Any?,
    bottomBarItems: ImmutableList<BottomBarItem<StableNavKey>>,
): Int {
    val entry: NavEntry<*>? = when (state) {
        // Navigation 3 transition scopes animate Scene objects; read their visible entry so
        // predictive back between bottom tabs can use the same tab-direction motion as taps.
        is Scene<*> -> state.entries.lastOrNull()
        is List<*> -> state.lastOrNull() as? NavEntry<*>
        else -> null
    }
    val route = entry?.contentKey as? StableNavKey
    return bottomBarItems.indexOfFirst { item -> item.route == route }
}

private data class SubScreenScene(
    private val route: StableNavKey,
    private val entry: NavEntry<StableNavKey>,
    override val previousEntries: List<NavEntry<StableNavKey>>,
    private val onBack: () -> Unit,
    private val nativeActivityTransitions: NativeActivityTransitions,
    private val entryProvider: (StableNavKey) -> NavEntry<StableNavKey>,
    private val onTitleLookup: @Composable (StableNavKey) -> String,
) : Scene<StableNavKey> {
    override val key: Any = route
    override val entries: List<NavEntry<StableNavKey>> = listOf(entry)
    override val metadata: Map<String, Any> = metadata {
        put(NavDisplay.TransitionKey) { nativeActivityTransitions.forward() }
        put(NavDisplay.PopTransitionKey) { nativeActivityTransitions.pop() }
        put(NavDisplay.PredictivePopTransitionKey) { nativeActivityTransitions.predictivePop() }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override val content: @Composable () -> Unit = {
        SubScreenShell(
            title = onTitleLookup(route),
            onBack = onBack,
            content = { entry.Content() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubScreenShell(
    title: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    LargeTopAppBarWithScaffold(
        title = title,
        onBackClicked = onBack,
        actions = {},
        scrollBehavior = scrollBehavior,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
        ) {
            content()
        }
    }
}

private val BottomDrawerActionRoutes = persistentSetOf(
    NavigationDrawerRoutes.ROUTE_SETTINGS,
    NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK,
    NavigationDrawerRoutes.ROUTE_SUPPORT,
    NavigationDrawerRoutes.ROUTE_UPDATES,
    NavigationDrawerRoutes.ROUTE_SHARE,
)
