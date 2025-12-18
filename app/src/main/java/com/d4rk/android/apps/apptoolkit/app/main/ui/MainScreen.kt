package com.d4rk.android.apps.apptoolkit.app.main.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.runtime.derivedStateOf
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.d4rk.android.apps.apptoolkit.app.logging.FAB_LOG_TAG
import com.d4rk.android.apps.apptoolkit.app.main.domain.model.ui.UiMainScreen
import com.d4rk.android.apps.apptoolkit.app.main.ui.components.fab.MainFloatingActionButton
import com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation.AppNavigationHost
import com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation.NavigationDrawer
import com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation.RandomAppHandler
import com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation.handleNavigationItemClick
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.BottomBarItem
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.dialogs.ChangelogDialog
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.BottomNavigationBar
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.HideOnScrollBottomBar
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.LeftNavigationRail
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.MainTopAppBar
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.StableNavController
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.ads.BottomAppBarNativeAdBanner
import com.d4rk.android.libs.apptoolkit.core.ui.components.snackbar.DefaultSnackbarHost
import com.d4rk.android.libs.apptoolkit.core.utils.window.rememberWindowWidthSizeClass
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
    val screenState: UiStateScreen<UiMainScreen> by viewModel.uiState.collectAsStateWithLifecycle()

    val bottomItems: ImmutableList<BottomBarItem> = MainNavigationDefaults.bottomBarItems

    if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
        NavigationDrawer(
            uiState = screenState.data ?: UiMainScreen(),
            windowWidthSizeClass = windowWidthSizeClass,
            bottomItems = bottomItems,
        )
    } else {
        MainScaffoldTabletContent(
            uiState = screenState.data ?: UiMainScreen(),
            windowWidthSizeClass = windowWidthSizeClass,
            bottomItems = bottomItems,
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
    bottomItems: ImmutableList<BottomBarItem>,
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val bottomAppBarScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val navController: NavHostController = rememberNavController()
    val stableNavController = remember(navController) { StableNavController(navController) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.route
    val normalizedRoute: String? = currentRoute.normalizeRoute()

    val isFabVisible: Boolean = normalizedRoute in MainNavigationDefaults.fabSupportedRoutes
    var isFabExtended by remember { mutableStateOf(true) }

    val randomAppHandlers = remember { mutableStateMapOf<String, RandomAppHandler>() }
    val randomAppHandler: RandomAppHandler? = normalizedRoute?.let(randomAppHandlers::get)

    LaunchedEffect(scrollBehavior) {
        snapshotFlow { scrollBehavior.state.contentOffset >= 0f }
            .distinctUntilChanged()
            .collect { shouldExtend ->
                if (isFabExtended != shouldExtend) isFabExtended = shouldExtend
            }
    }

    LaunchedEffect(isFabVisible, normalizedRoute) {
        if (isFabVisible) isFabExtended = true
        Log.d(
            FAB_LOG_TAG,
            "[Phone] visibility update raw=$currentRoute normalized=$normalizedRoute visible=$isFabVisible handlerPresent=${randomAppHandler != null}"
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
                    modifier = Modifier.fillMaxWidth(),
                    adUnitId = adsConfig.bannerAdUnitId
                )
                BottomNavigationBar(
                    navController = stableNavController,
                    items = bottomItems
                )
            }
        },
        floatingActionButton = {
            MainFloatingActionButton(
                visible = isFabVisible && randomAppHandler != null,
                expanded = isFabExtended,
                onClick = { randomAppHandler?.invoke() },
            )
        }
    ) { paddingValues ->
        AppNavigationHost(
            navController = stableNavController,
            snackbarHostState = snackBarHostState,
            paddingValues = paddingValues,
            windowWidthSizeClass = windowWidthSizeClass,
            onRandomAppHandlerChanged = { route, handler ->
                val normalized = route.normalizeRoute()
                if (normalized != null) {
                    if (handler == null) randomAppHandlers.remove(normalized)
                    else randomAppHandlers[normalized] = handler
                }
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
    uiState: UiMainScreen,
    windowWidthSizeClass: WindowWidthSizeClass,
    bottomItems: ImmutableList<BottomBarItem>,
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val isRailExpanded = rememberSaveable(windowWidthSizeClass) {
        mutableStateOf(windowWidthSizeClass == WindowWidthSizeClass.Expanded)
    }

    val context: Context = LocalContext.current
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val changelogUrl: String = koinInject(qualifier = named("github_changelog"))

    val showChangelog = rememberSaveable { mutableStateOf(false) }

    val navController: NavHostController = rememberNavController()
    val stableNavController = remember(navController) { StableNavController(navController) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember(backStackEntry) {
        derivedStateOf {
            backStackEntry?.destination?.route ?: navController.currentDestination?.route
        }
    }

    val normalizedRoute = currentRoute.normalizeRoute()
    val isFabVisible: Boolean = normalizedRoute in MainNavigationDefaults.fabSupportedRoutes
    var isFabExtended by remember { mutableStateOf(true) }

    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val randomAppHandlers = remember { mutableStateMapOf<String, RandomAppHandler>() }
    val randomAppHandler: RandomAppHandler? = normalizedRoute?.let(randomAppHandlers::get)

    LaunchedEffect(scrollBehavior) {
        snapshotFlow { scrollBehavior.state.contentOffset >= 0f }
            .distinctUntilChanged()
            .collect { shouldExtend ->
                if (isFabExtended != shouldExtend) isFabExtended = shouldExtend
            }
    }

    LaunchedEffect(isFabVisible, normalizedRoute) {
        if (isFabVisible) isFabExtended = true
        Log.d(
            FAB_LOG_TAG,
            "[Tablet] visibility update raw=$currentRoute normalized=$normalizedRoute visible=$isFabVisible handlerPresent=${randomAppHandler != null}"
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
                navController.navigate(item.route) {
                    launchSingleTop = true
                    popUpTo(navController.graph.startDestinationId)
                }
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
                    navController = stableNavController,
                    snackbarHostState = snackBarHostState,
                    paddingValues = PaddingValues(),
                    windowWidthSizeClass = windowWidthSizeClass,
                    onRandomAppHandlerChanged = { route, handler ->
                        route.normalizeRoute()?.let { appIdentifier ->
                            if (handler == null) randomAppHandlers.remove(appIdentifier)
                            else randomAppHandlers[appIdentifier] = handler
                        }
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