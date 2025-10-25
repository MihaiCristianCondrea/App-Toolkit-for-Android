package com.d4rk.android.apps.apptoolkit.app.main.ui

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.main.domain.model.ui.UiMainScreen
import com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation.AppNavigationHost
import com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation.NavigationDrawer
import com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation.handleNavigationItemClick
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.BottomBarItem
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.dialogs.ChangelogDialog
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.BottomNavigationBar
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.LeftNavigationRail
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.MainTopAppBar
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.snackbar.DefaultSnackbarHost
import com.d4rk.android.libs.apptoolkit.core.utils.window.rememberWindowWidthSizeClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

@Composable
fun MainScreen() {
    val windowWidthSizeClass: WindowWidthSizeClass = rememberWindowWidthSizeClass()
    val viewModel: MainViewModel = koinViewModel()
    val screenState: UiStateScreen<UiMainScreen> by viewModel.uiState.collectAsStateWithLifecycle()
    if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
        NavigationDrawer(
            screenState = screenState,
            windowWidthSizeClass = windowWidthSizeClass,
        )
    } else {
        MainScaffoldTabletContent(
            screenState = screenState,
            windowWidthSizeClass = windowWidthSizeClass,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffoldContent(
    drawerState: DrawerState,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val navController: NavHostController = rememberNavController()
    val bottomItems: List<BottomBarItem> = remember {
        listOf(
            BottomBarItem(
                route = NavigationRoutes.ROUTE_APPS_LIST,
                icon = Icons.Outlined.Apps,
                selectedIcon = Icons.Rounded.Apps,
                title = R.string.all_apps
            ),
            BottomBarItem(
                route = NavigationRoutes.ROUTE_FAVORITE_APPS,
                icon = Icons.Outlined.StarOutline,
                selectedIcon = Icons.Rounded.Star,
                title = R.string.favorite_apps
            )
        )
    }

    Scaffold(
        modifier = Modifier
            .imePadding()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection), topBar = {
        MainTopAppBar(
            navigationIcon = if (drawerState.isOpen) Icons.AutoMirrored.Outlined.MenuOpen else Icons.Default.Menu,
            onNavigationIconClick = { coroutineScope.launch { drawerState.open() } },
            scrollBehavior = scrollBehavior)
    }, snackbarHost = {
        DefaultSnackbarHost(snackbarState = snackBarHostState)
    }, bottomBar = {
        BottomNavigationBar(navController = navController, items = bottomItems)
    }) { paddingValues ->
        AppNavigationHost(
            navController = navController,
            snackbarHostState = snackBarHostState,
            paddingValues = paddingValues,
            windowWidthSizeClass = windowWidthSizeClass,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffoldTabletContent(
    screenState: UiStateScreen<UiMainScreen>,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var isRailExpanded by rememberSaveable(windowWidthSizeClass) {
        mutableStateOf(value = windowWidthSizeClass == WindowWidthSizeClass.Expanded)
    }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val changelogUrl: String = koinInject(qualifier = named("github_changelog"))
    val buildInfoProvider: BuildInfoProvider = koinInject()
    val dispatchers: DispatcherProvider = koinInject()
    var showChangelog by rememberSaveable { mutableStateOf(false) }

    val uiState: UiMainScreen = screenState.data ?: UiMainScreen()
    val navController: NavHostController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember(backStackEntry) {
        derivedStateOf { backStackEntry?.destination?.route ?: navController.currentDestination?.route }
    }

    val bottomItems: List<BottomBarItem> = remember {
        listOf(
            BottomBarItem(
                route = NavigationRoutes.ROUTE_APPS_LIST,
                icon = Icons.Outlined.Apps,
                selectedIcon = Icons.Rounded.Apps,
                title = R.string.all_apps
            ),
            BottomBarItem(
                route = NavigationRoutes.ROUTE_FAVORITE_APPS,
                icon = Icons.Outlined.StarOutline,
                selectedIcon = Icons.Rounded.Star,
                title = R.string.favorite_apps
            )
        )
    }

    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current

    Scaffold(
        modifier = Modifier
            .imePadding()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection), topBar = {
            MainTopAppBar(
                navigationIcon = if (isRailExpanded) Icons.AutoMirrored.Outlined.MenuOpen else Icons.Default.Menu,
                onNavigationIconClick = {
                    hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                    coroutineScope.launch {
                        isRailExpanded = !isRailExpanded
                    }
                },
                scrollBehavior = scrollBehavior)
        }) { paddingValues ->
        LeftNavigationRail(
            drawerItems = uiState.navigationDrawerItems,
            bottomItems = bottomItems,
            currentRoute = currentRoute,
            isRailExpanded = isRailExpanded,
            paddingValues = paddingValues,
            onBottomItemClick = { item: BottomBarItem ->
                navController.navigate(item.route) {
                    launchSingleTop = true
                    popUpTo(navController.graph.startDestinationId)
                }
            },
            onDrawerItemClick = { item: NavigationDrawerItem ->
                handleNavigationItemClick(
                    context = context,
                    item = item,
                    onChangelogRequested = { showChangelog = true },
                )
            },
            content = {
                AppNavigationHost(
                    navController = navController,
                    snackbarHostState = snackBarHostState,
                    paddingValues = PaddingValues(),
                    windowWidthSizeClass = windowWidthSizeClass,
                )
            })
    }

    if (showChangelog) {
        ChangelogDialog(
            changelogUrl = changelogUrl,
            buildInfoProvider = buildInfoProvider,
            onDismiss = { showChangelog = false },
            dispatchers = dispatchers
        )
    }
}
