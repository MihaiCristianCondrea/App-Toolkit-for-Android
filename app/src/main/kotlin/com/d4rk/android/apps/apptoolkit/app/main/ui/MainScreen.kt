package com.d4rk.android.apps.apptoolkit.app.main.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.apps.apptoolkit.app.logging.FAB_LOG_TAG
import com.d4rk.android.apps.apptoolkit.app.main.ui.components.fab.MainFloatingActionButton
import com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation.AppNavigationHost
import com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation.NavigationDrawer
import com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation.RandomAppHandler
import com.d4rk.android.libs.apptoolkit.app.main.ui.navigation.handleNavigationItemClick
import com.d4rk.android.apps.apptoolkit.app.main.ui.states.MainUiState
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppNavKey
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.toNavKeyOrDefault
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.BottomBarItem
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.dialogs.ChangelogDialog
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.BottomNavigationBar
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.HideOnScrollBottomBar
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.LeftNavigationRail
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.MainTopAppBar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.components.ads.BottomAppBarNativeAdBanner
import com.d4rk.android.libs.apptoolkit.core.ui.components.snackbar.DefaultSnackbarHost
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.utils.window.rememberWindowWidthSizeClass
import com.d4rk.android.apps.apptoolkit.core.data.datastore.DataStore
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationState
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.Navigator
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.rememberNavigationState
import com.d4rk.android.libs.apptoolkit.data.datastore.startupDestinationFlow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

/**
 * The main entry point composable for the application's UI.
 *
 * This function acts as a router, determining the top-level layout based on the device's
 * screen width. It observes the [WindowWidthSizeClass] and delegates the UI construction
 * to either [NavigationDrawer] for compact screens (phones) or [MainScaffoldTabletContent]
 * for larger screens (tablets and desktops).
 *
 * It retrieves the main view model and collects the UI state to be passed down to its
 * children.
 *
 * @see NavigationDrawer for the compact width implementation.
 * @see MainScaffoldTabletContent for the medium/expanded width implementation.
 * @see rememberWindowWidthSizeClass
 */
@Composable
fun MainScreen() {
    val windowWidthSizeClass: WindowWidthSizeClass = rememberWindowWidthSizeClass()
    val viewModel: MainViewModel = koinViewModel()
    val screenState: UiStateScreen<MainUiState> by viewModel.uiState.collectAsStateWithLifecycle()

    val bottomItems: ImmutableList<BottomBarItem<AppNavKey>> = MainNavigationDefaults.bottomBarItems
    val dataStore: DataStore = koinInject()
    val startupRoute: AppNavKey by dataStore
        .startupDestinationFlow(
            defaultRoute = NavigationRoutes.ROUTE_APPS_LIST,
            mapToKey = { value -> value.toNavKeyOrDefault() }
        )
        .collectAsStateWithLifecycle(initialValue = NavigationRoutes.ROUTE_APPS_LIST.toNavKeyOrDefault())
    val navigationState: NavigationState<AppNavKey> = rememberNavigationState(
        startRoute = startupRoute,
        topLevelRoutes = NavigationRoutes.topLevelRoutes
    )
    val navigator: Navigator<AppNavKey> = remember(startupRoute) { Navigator(navigationState) }

    if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
        NavigationDrawer(
            uiState = screenState.data ?: MainUiState(),
            windowWidthSizeClass = windowWidthSizeClass,
            bottomItems = bottomItems,
            navigationState = navigationState,
            navigator = navigator,
        )
    } else {
        MainScaffoldTabletContent(
            uiState = screenState.data ?: MainUiState(),
            windowWidthSizeClass = windowWidthSizeClass,
            bottomItems = bottomItems,
            navigationState = navigationState,
            navigator = navigator,
        )
    }
}

/**
 * Composable function that sets up the main UI structure for compact screen sizes (phones).
 *
 * This function builds the layout using a `Scaffold`, which includes a top app bar, a bottom
 * navigation bar, a floating action button (FAB), and the main content area managed by a
 * `NavHost`. It handles the visibility and state of the FAB based on the current navigation
 * route and scroll behavior.
 *
 * @param drawerState The state of the navigation drawer, used to open it and to determine the
 *   icon for the top app bar's navigation button.
 * @param windowWidthSizeClass The window size class, used to pass down to child composables
 *   like the `AppNavigationHost`.
 * @param bottomItems A list of items to be displayed in the bottom navigation bar.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainScaffoldContent(
    drawerState: DrawerState,
    windowWidthSizeClass: WindowWidthSizeClass,
    bottomItems: ImmutableList<BottomBarItem<AppNavKey>>,
    navigationState: NavigationState<AppNavKey>,
    navigator: Navigator<AppNavKey>,
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val bottomAppBarScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val currentRoute: AppNavKey =
        navigationState.backStacks[navigationState.topLevelRoute]?.lastOrNull()
            ?: navigationState.topLevelRoute

    val isFabVisible: Boolean = currentRoute in MainNavigationDefaults.fabSupportedRoutes
    var isFabExtended by remember { mutableStateOf(true) }

    val randomAppHandlers = remember { mutableStateMapOf<AppNavKey, RandomAppHandler>() }
    val randomAppHandler: RandomAppHandler? = randomAppHandlers[currentRoute]

    LaunchedEffect(scrollBehavior) {
        snapshotFlow { scrollBehavior.state.contentOffset >= 0f }
            .distinctUntilChanged()
            .collect { shouldExtend ->
                if (isFabExtended != shouldExtend) isFabExtended = shouldExtend
            }
    }

    LaunchedEffect(isFabVisible, currentRoute) {
        if (isFabVisible) isFabExtended = true
        Log.d(
            FAB_LOG_TAG,
            "[Phone] visibility update route=$currentRoute visible=$isFabVisible handlerPresent=${randomAppHandler != null}"
        )
    }

    Scaffold(
        modifier = Modifier
            .imePadding()
            .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MainTopAppBar(
                navigationIcon = if (drawerState.isOpen) Icons.AutoMirrored.Outlined.MenuOpen else Icons.Default.Menu,
                onNavigationIconClick = { coroutineScope.launch { drawerState.open() } },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { DefaultSnackbarHost(snackbarState = snackBarHostState) },
        bottomBar = {
            val adsConfig: AdsConfig = koinInject(qualifier = named("bottom_nav_bar_native_ad"))
            HideOnScrollBottomBar(scrollBehavior = bottomAppBarScrollBehavior) {
                BottomAppBarNativeAdBanner(
                    adUnitId = adsConfig.bannerAdUnitId
                )
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    items = bottomItems,
                    onNavigate = navigator::navigate
                )
            }
        },
        floatingActionButton = {
            val navPads =
                WindowInsets.navigationBars.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
                    .asPaddingValues()
            val layoutDir = LocalLayoutDirection.current
            val state = bottomAppBarScrollBehavior.state
            val collapseFraction =
                if (state.heightOffsetLimit == 0f) 0f else (state.heightOffset / state.heightOffsetLimit).coerceIn(
                    0f,
                    1f
                )
            MainFloatingActionButton(
                modifier = Modifier.padding(
                    start = (navPads.calculateLeftPadding(layoutDir).value * collapseFraction).dp,
                    end = (navPads.calculateRightPadding(layoutDir).value * collapseFraction).dp,
                    bottom = (navPads.calculateBottomPadding().value * collapseFraction).dp
                ),
                visible = isFabVisible && randomAppHandler != null,
                expanded = isFabExtended,
                onClick = { randomAppHandler?.invoke() },
            )
        }
    ) { paddingValues ->
        AppNavigationHost(
            modifier = Modifier
                .consumeWindowInsets(paddingValues),
            navigationState = navigationState,
            navigator = navigator,
            paddingValues = paddingValues,
            windowWidthSizeClass = windowWidthSizeClass,
            onRandomAppHandlerChanged = { route: AppNavKey, handler ->
                if (handler == null) randomAppHandlers.remove(route) else randomAppHandlers[route] = handler
            },
        )
    }
}

/**
 * A composable function that defines the main layout structure for tablet-sized screens.
 * It uses a `Scaffold` with a `MainTopAppBar`, a `LeftNavigationRail`, and a `MainFloatingActionButton`.
 * The navigation rail's expansion state is managed based on the window size and user interaction.
 *
 * This layout is responsible for handling the overall navigation, displaying the main content via
 * `AppNavigationHost`, and coordinating UI elements like the FAB visibility and extension state based
 * on the current navigation route and scroll behavior.
 *
 * @param uiState The current state of the main screen, containing data for the navigation drawer.
 * @param windowWidthSizeClass The class representing the width of the window, used to determine
 *   the initial state of the navigation rail (expanded or collapsed).
 * @param bottomItems The list of items to be displayed at the bottom of the `LeftNavigationRail`,
 *   acting as the primary navigation destinations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffoldTabletContent(
    uiState: MainUiState,
    windowWidthSizeClass: WindowWidthSizeClass,
    bottomItems: ImmutableList<BottomBarItem<AppNavKey>>,
    navigationState: NavigationState<AppNavKey>,
    navigator: Navigator<AppNavKey>,
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val isRailExpanded = rememberSaveable(windowWidthSizeClass) {
        mutableStateOf(windowWidthSizeClass == WindowWidthSizeClass.Expanded)
    }

    val context: Context = LocalContext.current
    remember { SnackbarHostState() }
    val changelogUrl: String = koinInject(qualifier = named("github_changelog"))

    val showChangelog = rememberSaveable { mutableStateOf(false) }

    val currentRoute: AppNavKey =
        navigationState.backStacks[navigationState.topLevelRoute]?.lastOrNull()
            ?: navigationState.topLevelRoute

    val isFabVisible: Boolean = currentRoute in MainNavigationDefaults.fabSupportedRoutes
    var isFabExtended by remember { mutableStateOf(true) }

    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val randomAppHandlers = remember { mutableStateMapOf<AppNavKey, RandomAppHandler>() }
    val randomAppHandler: RandomAppHandler? = randomAppHandlers[currentRoute]

    LaunchedEffect(scrollBehavior) {
        snapshotFlow { scrollBehavior.state.contentOffset >= 0f }
            .distinctUntilChanged()
            .collect { shouldExtend ->
                if (isFabExtended != shouldExtend) isFabExtended = shouldExtend
            }
    }

    LaunchedEffect(isFabVisible, currentRoute) {
        if (isFabVisible) isFabExtended = true
        Log.d(
            FAB_LOG_TAG,
            "[Tablet] visibility update route=$currentRoute visible=$isFabVisible handlerPresent=${randomAppHandler != null}"
        )
    }

    Scaffold(
        modifier = Modifier
            .imePadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MainTopAppBar(
                navigationIcon = if (isRailExpanded.value) Icons.AutoMirrored.Outlined.MenuOpen else Icons.Default.Menu,
                onNavigationIconClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                    isRailExpanded.value = !isRailExpanded.value
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            MainFloatingActionButton(
                visible = isFabVisible && randomAppHandler != null,
                expanded = isFabExtended,
                onClick = { randomAppHandler?.invoke() },
            )
        }
    ) { paddingValues ->
        LeftNavigationRail(
            drawerItems = uiState.navigationDrawerItems,
            bottomItems = bottomItems,
            currentRoute = currentRoute,
            isRailExpanded = isRailExpanded.value,
            paddingValues = paddingValues,
            onBottomItemClick = { item ->
                navigator.navigate(item.route)
            },
            onDrawerItemClick = { item ->
                handleNavigationItemClick(
                    context = context,
                    item = item,
                    onChangelogRequested = { showChangelog.value = true },
                )
            },
            content = {
                AppNavigationHost(
                    navigationState = navigationState,
                    navigator = navigator,
                    paddingValues = PaddingValues(),
                    windowWidthSizeClass = windowWidthSizeClass,
                    onRandomAppHandlerChanged = { route: AppNavKey, handler ->
                        if (handler == null) randomAppHandlers.remove(route) else randomAppHandlers[route] = handler
                    },
                )
            }
        )
    }

    if (showChangelog.value) {
        ChangelogDialog(
            changelogUrl = changelogUrl,
            onDismiss = { showChangelog.value = false }
        )
    }
}
