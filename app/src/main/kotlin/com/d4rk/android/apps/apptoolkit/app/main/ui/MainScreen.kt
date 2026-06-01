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

package com.d4rk.android.apps.apptoolkit.app.main.ui

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.navigation3.ui.NavDisplay
import com.d4rk.android.apps.apptoolkit.app.main.ui.navigation.NavigationManager
import com.d4rk.android.apps.apptoolkit.app.main.ui.state.MainUiState
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.fab.MainFloatingActionButton
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.AppNavigationEntryContext
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.appNavigationEntryBuilders
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.isDrawerItemSelected
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.ComponentsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.FavoriteAppsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.ToolkitTilesRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.defaults.MainNavigationDefaults
import com.d4rk.android.libs.apptoolkit.app.help.ui.views.dropdowns.HelpScreenMenuActions
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.BottomBarItem
import com.d4rk.android.libs.apptoolkit.app.main.ui.navigation.handleNavigationItemClick
import com.d4rk.android.libs.apptoolkit.app.main.ui.views.dialogs.ChangelogDialog
import com.d4rk.android.libs.apptoolkit.app.main.ui.views.navigation.MainTopAppBar
import com.d4rk.android.libs.apptoolkit.app.main.ui.views.navigation.NavigationDrawerItemContent
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.AdsSettingsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.GeneralSettingsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.HelpRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.LibraryExtrasRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.LicensesRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.NavigationDrawerRoutes
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.PermissionsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.SettingsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.SupportRoute
import com.d4rk.android.libs.apptoolkit.core.di.AppToolkitDiConstants
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.Navigator
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.entryProviderFor
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.rememberNavigationEntryDecorators
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.rememberNavigationState
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.hapticDrawerSwipe
import com.d4rk.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.ui.views.snackbar.DefaultSnackbarHost
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import com.d4rk.android.libs.apptoolkit.core.ui.window.toAppWindowWidthSizeClass
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.navigation.animations.NativeActivityTransitions
import com.d4rk.android.libs.apptoolkit.navigation.animations.rememberBottomNavTransitions
import com.d4rk.android.libs.apptoolkit.navigation.animations.rememberNativeActivityTransitions
import com.d4rk.android.libs.apptoolkit.navigation.models.isTopLevel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

/** Hosts the adaptive app shell and pushed navigation destinations. */
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = koinViewModel()
    val uiStateScreen by viewModel.uiState.collectAsState()

    MainScreenContent(uiState = uiStateScreen.data ?: MainUiState())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(uiState: MainUiState) {
    val activity = LocalActivity.current

    val navigationState = rememberNavigationState(
        startRoute = AppsListRoute,
        topLevelRoutes = NavigationRoutes.topLevelRoutes
    )
    val navigator = remember(navigationState) { Navigator(state = navigationState) }

    val windowWidthSizeClass: AppWindowWidthSizeClass =
        currentWindowAdaptiveInfo().windowSizeClass.toAppWindowWidthSizeClass()

    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    val nativeActivityTransitions = rememberNativeActivityTransitions()

    val navigationManager: NavigationManager = koinInject()

    LaunchedEffect(navigationManager, navigator) {
        navigationManager.navigationRequests.collect { route ->
            navigator.navigate(route)
        }
    }

    val changelogUrl: String = koinInject(qualifier = named(AppToolkitDiConstants.GITHUB_CHANGELOG))

    var randomAppHandler: (() -> Unit)? by remember { mutableStateOf(null) }
    val randomAppHandlerState: State<(() -> Unit)?> = rememberUpdatedState(randomAppHandler)
    var showChangelog by remember { mutableStateOf(false) }

    val entryBuilders: List<NavigationEntryBuilder<StableNavKey>> =
        remember(windowWidthSizeClass) {
            val entryContext = AppNavigationEntryContext(
                paddingValues = PaddingValues(),
                windowWidthSizeClass = windowWidthSizeClass,
                onRandomAppHandlerChanged = { _, handler -> randomAppHandler = handler },
            )

            appNavigationEntryBuilders(
                context = entryContext,
                additionalEntryBuilders = persistentListOf(),
            )
        }

    val entryDecorators = rememberNavigationEntryDecorators<StableNavKey>()

    val entryProvider: (StableNavKey) -> NavEntry<StableNavKey> =
        remember(entryBuilders) {
            entryProviderFor(entryBuilders)
        }

    val sceneStrategy = remember(
        uiState,
        navigator,
        windowWidthSizeClass,
        snackBarHostState,
        randomAppHandlerState,
        nativeActivityTransitions,
    ) {
        MainSceneStrategy(
            uiState = uiState,
            navigator = navigator,
            windowWidthSizeClass = windowWidthSizeClass,
            snackBarHostState = snackBarHostState,
            onChangelogRequested = { showChangelog = true },
            randomAppHandlerState = randomAppHandlerState,
            nativeActivityTransitions = nativeActivityTransitions,
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
    )

    if (showChangelog) {
        ChangelogDialog(
            changelogUrl = changelogUrl,
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
            )
        } else {
            SubScreenScene(
                key = currentRoute,
                entry = currentEntry,
                previousEntries = entries.dropLast(1),
                onBack = onBack,
                nativeActivityTransitions = nativeActivityTransitions,
            )
        }
    }
}

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
            entry = entry,
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
    entry: NavEntry<StableNavKey>,
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val bottomNavTransitions = rememberBottomNavTransitions()
    val appRouteHandlers: Map<String, (NavigationDrawerItem) -> Unit> = remember(navigator) {
        mapOf(
            NavigationRoutes.ROUTE_APPS_LIST to { navigator.navigate(AppsListRoute) },
            NavigationRoutes.ROUTE_FAVORITE_APPS to { navigator.navigate(FavoriteAppsRoute) },
            NavigationRoutes.ROUTE_TOOLKIT_TILES to { navigator.navigate(ToolkitTilesRoute) },
            NavigationRoutes.ROUTE_COMPONENTS to { navigator.navigate(ComponentsRoute) },
        )
    }
    val context = LocalContext.current
    val currentRoute = navigator.state.currentBackStack.last()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val modalDrawerEnabled = windowWidthSizeClass == AppWindowWidthSizeClass.Compact
    val randomAppHandler = randomAppHandlerState.value

    val isScrollingUp: Boolean =
        scrollBehavior.state.contentOffset > -SizeConstants.MediumSize.value

    val appBarTitleResId: Int = remember(currentRoute) {
        MainNavigationDefaults.bottomBarItems
            .find { item: BottomBarItem<StableNavKey> -> item.route == currentRoute }?.title
            ?: com.d4rk.android.libs.apptoolkit.R.string.app_name
    }

    val onNavigationDrawerItemClick: (NavigationDrawerItem, DrawerState?, CoroutineScope?) -> Unit =
        { item, state, scope ->
            handleNavigationItemClick(
                context = context,
                item = item,
                drawerState = state,
                coroutineScope = scope,
                onChangelogRequested = onChangelogRequested,
                onInternalNavigationRequested = { route ->
                    when (route) {
                        NavigationDrawerRoutes.ROUTE_SETTINGS -> navigator.navigate(SettingsRoute)
                        NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK -> navigator.navigate(
                            HelpRoute
                        )

                        NavigationDrawerRoutes.ROUTE_SUPPORT -> navigator.navigate(SupportRoute)
                    }
                },
                additionalHandlers = appRouteHandlers,
            )
        }

    val isFabVisible: Boolean by remember(currentRoute) {
        derivedStateOf { MainNavigationDefaults.fabSupportedRoutes.contains(currentRoute) }
    }
    val isFabExtended: Boolean by remember(isScrollingUp) {
        derivedStateOf { isScrollingUp }
    }

    val bottomItems: ImmutableList<BottomBarItem<StableNavKey>> =
        MainNavigationDefaults.bottomBarItems

    val railDrawerItems: Pair<List<NavigationDrawerItem>, List<NavigationDrawerItem>> =
        remember(uiState.navigationDrawerItems) {
            uiState.navigationDrawerItems.partition { item: NavigationDrawerItem ->
                item.route !in BottomDrawerActionRoutes
            }
        }

    val shellContent: @Composable () -> Unit = {
        BackHandler(enabled = true) {
            if (drawerState.isOpen) {
                coroutineScope.launch { drawerState.close() }
            } else {
                onBack()
            }
        }

        Row(modifier = Modifier
            .fillMaxSize()
            .imePadding()) {
            if (!modalDrawerEnabled) {
                NavigationRail {
                    bottomItems.forEach { item ->
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
                        val isSelected = isDrawerItemSelected(item.route, currentRoute)
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
                        val isSelected = isDrawerItemSelected(item.route, currentRoute)
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
                containerColor = Color.Transparent,
                topBar = {
                    MainTopAppBar(
                        title = stringResource(appBarTitleResId),
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
                        onSupportClick = { navigator.navigate(SupportRoute) },
                        showSupportAction = currentRoute == AppsListRoute,
                        scrollBehavior = scrollBehavior,
                    )
                },
                bottomBar = {
                    if (modalDrawerEnabled) {
                        NavigationBar {
                            bottomItems.forEach { item ->
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
                    // Preserve each tab entry during the fade while leaving app chrome mounted.
                    AnimatedContent(
                        targetState = entry,
                        contentKey = { targetEntry -> targetEntry.contentKey },
                        transitionSpec = {
                            val initialIndex = MainNavigationDefaults.bottomBarItems.indexOfFirst {
                                it.route == (initialState.contentKey as? StableNavKey)
                            }
                            val targetIndex = MainNavigationDefaults.bottomBarItems.indexOfFirst {
                                it.route == (targetState.contentKey as? StableNavKey)
                            }
                            val isBottomBarRouteSwitch = initialIndex >= 0 && targetIndex >= 0

                            if (isBottomBarRouteSwitch) {
                                bottomNavTransitions.betweenTabs(forward = targetIndex >= initialIndex)
                            } else {
                                bottomNavTransitions.transition()
                            }
                        },
                        label = "TabAnimation",
                    ) { targetEntry ->
                        targetEntry.Content()
                    }
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
                            selected = false,
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

private data class SubScreenScene(
    override val key: Any,
    private val entry: NavEntry<StableNavKey>,
    override val previousEntries: List<NavEntry<StableNavKey>>,
    private val onBack: () -> Unit,
    private val nativeActivityTransitions: NativeActivityTransitions,
) : Scene<StableNavKey> {
    override val entries: List<NavEntry<StableNavKey>> = listOf(entry)
    override val metadata: Map<String, Any> = metadata {
        put(NavDisplay.TransitionKey) { nativeActivityTransitions.forward() }
        put(NavDisplay.PopTransitionKey) { nativeActivityTransitions.pop() }
        put(NavDisplay.PredictivePopTransitionKey) { nativeActivityTransitions.predictivePop() }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override val content: @Composable () -> Unit = {
        SubScreenShell(
            currentRoute = key as StableNavKey,
            onBack = onBack,
            content = { entry.Content() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubScreenShell(
    currentRoute: StableNavKey,
    onBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    val scrollBehavior = when (currentRoute) {
        is HelpRoute, is LicensesRoute -> TopAppBarDefaults.enterAlwaysScrollBehavior()
        else -> TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    }
    var showHelpVersionDialog by rememberSaveable(currentRoute) { mutableStateOf(false) }
    val appBarTitle: String = when (currentRoute) {
        is SettingsRoute -> stringResource(com.d4rk.android.libs.apptoolkit.R.string.settings)
        is GeneralSettingsRoute -> currentRoute.title
        is HelpRoute -> stringResource(com.d4rk.android.libs.apptoolkit.R.string.help)
        is AdsSettingsRoute -> stringResource(com.d4rk.android.libs.apptoolkit.R.string.ads)
        is PermissionsRoute -> stringResource(com.d4rk.android.libs.apptoolkit.R.string.permissions)
        is LicensesRoute -> stringResource(com.d4rk.android.libs.apptoolkit.R.string.oss_license_title)
        is SupportRoute -> stringResource(com.d4rk.android.libs.apptoolkit.R.string.support_us)
        is LibraryExtrasRoute -> stringResource(com.d4rk.android.libs.apptoolkit.R.string.app_name)
        is ComponentsRoute -> stringResource(com.d4rk.android.apps.apptoolkit.R.string.components_title)
        else -> stringResource(com.d4rk.android.libs.apptoolkit.R.string.app_name)
    }

    LargeTopAppBarWithScaffold(
        title = appBarTitle,
        onBackClicked = onBack,
        actions = {
            if (currentRoute is HelpRoute) {
                val config: AppVersionInfo = koinInject()
                HelpScreenMenuActions(
                    config = config,
                    showDialog = showHelpVersionDialog,
                    onShowDialogChange = { showHelpVersionDialog = it },
                )
            }
        },
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
