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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuOpen
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.d4rk.android.apps.apptoolkit.app.main.ui.navigation.NavigationManager
import com.d4rk.android.apps.apptoolkit.app.main.ui.state.MainUiState
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.fab.MainFloatingActionButton
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.AppNavigationEntryContext
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.RandomAppHandler
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.appNavigationEntryBuilders
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.isDrawerItemSelected
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AdsSettingsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppNavKey
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.ComponentsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.FavoriteAppsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.GeneralSettingsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.HelpRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.LicensesRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.PermissionsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.SettingsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.defaults.MainNavigationDefaults
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.BottomBarItem
import com.d4rk.android.libs.apptoolkit.app.main.ui.navigation.handleNavigationItemClick
import com.d4rk.android.libs.apptoolkit.app.main.ui.views.dialogs.ChangelogDialog
import com.d4rk.android.libs.apptoolkit.app.main.ui.views.navigation.MainTopAppBar
import com.d4rk.android.libs.apptoolkit.app.main.ui.views.navigation.NavigationDrawerItemContent
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.NavigationDrawerRoutes
import com.d4rk.android.libs.apptoolkit.core.di.AppToolkitDiConstants
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationAnimations
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.Navigator
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.entryProviderFor
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.rememberNavigationEntryDecorators
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.rememberNavigationState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.RootContentContainer
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.hapticDrawerSwipe
import com.d4rk.android.libs.apptoolkit.core.ui.views.snackbar.DefaultSnackbarHost
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import com.d4rk.android.libs.apptoolkit.core.ui.window.toAppWindowWidthSizeClass
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) { // FIXME: Unstable parameter 'viewModel' prevents composable from being skippable
    val uiStateScreen by viewModel.uiState.collectAsState()
    val uiState = uiStateScreen.data ?: MainUiState()

    val context = LocalContext.current
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val scrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior()

    val navigationState = rememberNavigationState(
        startRoute = AppsListRoute,
        topLevelRoutes = NavigationRoutes.topLevelRoutes
    )
    val navigator = remember(navigationState) { Navigator(state = navigationState) }

    val currentRoute: AppNavKey = navigator.state.currentRoute
    val isScrollingUp: Boolean =
        scrollBehavior.state.contentOffset > -SizeConstants.MediumSize.value

    val appBarTitleResId: Int = remember(currentRoute) {
        MainNavigationDefaults.bottomBarItems
            .find { item: BottomBarItem<AppNavKey> -> item.route == currentRoute }?.title
            ?: when (currentRoute) {
                is SettingsRoute -> com.d4rk.android.libs.apptoolkit.R.string.settings
                is GeneralSettingsRoute -> com.d4rk.android.libs.apptoolkit.R.string.settings // Or more specific title if needed
                is HelpRoute -> com.d4rk.android.libs.apptoolkit.R.string.help
                is AdsSettingsRoute -> com.d4rk.android.libs.apptoolkit.R.string.ads
                is PermissionsRoute -> com.d4rk.android.libs.apptoolkit.R.string.permissions
                is LicensesRoute -> com.d4rk.android.libs.apptoolkit.R.string.oss_license_title
                else -> com.d4rk.android.libs.apptoolkit.R.string.app_name
            }
    }

    val windowWidthSizeClass: AppWindowWidthSizeClass =
        currentWindowAdaptiveInfo().windowSizeClass.toAppWindowWidthSizeClass()

    val layoutType: NavigationSuiteType = remember(windowWidthSizeClass) {
        when (windowWidthSizeClass) {
            AppWindowWidthSizeClass.Compact -> NavigationSuiteType.NavigationBar
            AppWindowWidthSizeClass.Medium -> NavigationSuiteType.NavigationRail
            else -> NavigationSuiteType.NavigationRail
        }
    }

    val showNavigationSuite: Boolean by remember(currentRoute, layoutType) {
        derivedStateOf {
            val isTopLevel = currentRoute in NavigationRoutes.topLevelRoutes
            val isRail = layoutType == NavigationSuiteType.NavigationRail

            // Always show the navigation suite if it's a rail (tablet global nav)
            // or if we are on a top-level screen.
            isRail || isTopLevel
        }
    }

    val modalDrawerEnabled: Boolean = windowWidthSizeClass == AppWindowWidthSizeClass.Compact
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    val navigationManager: NavigationManager = koinInject()

    LaunchedEffect(navigationManager, navigator) {
        navigationManager.navigationRequests.collect { route ->
            navigator.navigate(route)
        }
    }

    val changelogUrl: String = koinInject(qualifier = named(AppToolkitDiConstants.GITHUB_CHANGELOG))

    var randomAppHandler: (() -> Unit)? by remember { mutableStateOf(null) }
    val onRandomAppHandlerChanged: (AppNavKey, RandomAppHandler?) -> Unit = { _, handler ->
        randomAppHandler = handler
    }

    val isFabVisible: Boolean by remember(currentRoute) {
        derivedStateOf { MainNavigationDefaults.fabSupportedRoutes.contains(currentRoute) }
    }
    val isFabExtended: Boolean by remember(isScrollingUp) {
        derivedStateOf { isScrollingUp }
    }

    val bottomItems: ImmutableList<BottomBarItem<AppNavKey>> = MainNavigationDefaults.bottomBarItems
    var showChangelog by remember { mutableStateOf(false) }

    val appRouteHandlers: Map<String, (NavigationDrawerItem) -> Unit> = remember(navigator) {
        mapOf(
            NavigationRoutes.ROUTE_APPS_LIST to { navigator.navigate(AppsListRoute) },
            NavigationRoutes.ROUTE_FAVORITE_APPS to { navigator.navigate(FavoriteAppsRoute) },
            NavigationRoutes.ROUTE_COMPONENTS to { navigator.navigate(ComponentsRoute) },
        )
    }

    val railDrawerItems: Pair<List<NavigationDrawerItem>, List<NavigationDrawerItem>> =
        remember(uiState.navigationDrawerItems) {
            uiState.navigationDrawerItems.partition { item: NavigationDrawerItem ->
                item.route !in BottomDrawerActionRoutes
            }
        }

    val onNavigationDrawerItemClick: (NavigationDrawerItem, DrawerState?, CoroutineScope?) -> Unit =
        { item, state, scope ->
            handleNavigationItemClick(
                context = context,
                item = item,
                drawerState = state,
                coroutineScope = scope,
                onChangelogRequested = { showChangelog = true },
                onInternalNavigationRequested = { route ->
                    when (route) {
                        NavigationDrawerRoutes.ROUTE_SETTINGS -> navigator.navigate(SettingsRoute)
                        NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK -> navigator.navigate(
                            HelpRoute
                        )
                    }
                },
                additionalHandlers = appRouteHandlers,
            )
        }

    val shellContent: @Composable () -> Unit = {
        NavigationSuiteScaffold(
            modifier = Modifier.imePadding(),
            layoutType = if (showNavigationSuite) layoutType else NavigationSuiteType.None,
            navigationSuiteItems = {
                bottomItems.forEach { item: BottomBarItem<AppNavKey> ->
                    val isSelected: Boolean = navigator.state.currentRoute == item.route
                    item(
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.icon,
                                contentDescription = stringResource(id = item.title),
                            )
                        },
                        label = {
                            Text(text = stringResource(id = item.title))
                        },
                        selected = isSelected,
                        onClick = { navigator.navigate(route = item.route) },
                    )
                }

                if (!modalDrawerEnabled) {
                    railDrawerItems.first.forEach { item: NavigationDrawerItem ->
                        val isSelected: Boolean = isDrawerItemSelected(
                            itemRoute = item.route,
                            currentRoute = currentRoute,
                        )
                        item(
                            selected = isSelected,
                            onClick = { onNavigationDrawerItemClick(item, null, null) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) {
                                        item.selectedIcon
                                    } else {
                                        item.icon
                                    },
                                    contentDescription = stringResource(item.title),
                                )
                            },
                            label = { Text(text = stringResource(item.title)) },
                        )
                    }

                    // We cannot easily add a Spacer with weight(1f) inside NavigationSuiteScope's item list
                    // as it expects discrete items. However, we can add a dummy disabled item if needed,
                    // or just accept that they are listed sequentially.
                    // The user's provided code used a Column inside a label of a single item() as a hack.
                    // Let's try to match the user's intent if possible, but the standard way is just adding items.

                    railDrawerItems.second.forEach { item: NavigationDrawerItem ->
                        val isSelected: Boolean = isDrawerItemSelected(
                            itemRoute = item.route,
                            currentRoute = currentRoute,
                        )
                        item(
                            selected = isSelected,
                            onClick = { onNavigationDrawerItemClick(item, null, null) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) {
                                        item.selectedIcon
                                    } else {
                                        item.icon
                                    },
                                    contentDescription = stringResource(item.title),
                                )
                            },
                            label = { Text(text = stringResource(item.title)) },
                        )
                    }
                }
            },
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                containerColor = Color.Transparent,
                topBar = {
                    MainTopAppBar(
                        title = stringResource(appBarTitleResId),
                        navigationIcon = when {
                            drawerState.isOpen -> Icons.AutoMirrored.Outlined.MenuOpen
                            navigator.canGoBack() -> Icons.AutoMirrored.Outlined.ArrowBack
                            modalDrawerEnabled -> Icons.Outlined.Menu
                            else -> null
                        },
                        onNavigationIconClick = {
                            when {
                                drawerState.isOpen -> coroutineScope.launch { drawerState.close() }
                                navigator.canGoBack() -> navigator.goBack()
                                modalDrawerEnabled -> coroutineScope.launch { drawerState.open() }
                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
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
                snackbarHost = {
                    DefaultSnackbarHost(snackbarState = snackBarHostState)
                },
            ) { paddingValues: PaddingValues ->
                RootContentContainer(
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues),
                ) {
                    val entryBuilders: List<NavigationEntryBuilder<AppNavKey>> =
                        remember(
                            paddingValues,
                            windowWidthSizeClass,
                        ) {
                            val entryContext = AppNavigationEntryContext(
                                paddingValues = PaddingValues(),
                                windowWidthSizeClass = windowWidthSizeClass,
                                onRandomAppHandlerChanged = onRandomAppHandlerChanged,
                            )

                            appNavigationEntryBuilders(
                                context = entryContext,
                                additionalEntryBuilders = persistentListOf(),
                            )
                        }

                    val entryDecorators = rememberNavigationEntryDecorators<AppNavKey>()

                    val entryProvider: (AppNavKey) -> NavEntry<AppNavKey> =
                        remember(entryBuilders) {
                            entryProviderFor(entryBuilders)
                        }

                    NavDisplay(
                        backStack = navigationState.currentBackStack,
                        entryDecorators = entryDecorators,
                        entryProvider = entryProvider,
                        onBack = {
                            if (!drawerState.isOpen && navigator.canGoBack()) {
                                navigator.goBack()
                            }
                        },
                        transitionSpec = { NavigationAnimations.default() },
                        popTransitionSpec = { NavigationAnimations.default() },
                        predictivePopTransitionSpec = { NavigationAnimations.default() },
                    )
                }
            }
        }
    }

    LaunchedEffect(modalDrawerEnabled) {
        if (!modalDrawerEnabled && drawerState.isOpen) {
            drawerState.close()
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

                    uiState.navigationDrawerItems.forEach { item: NavigationDrawerItem ->
                        NavigationDrawerItemContent(
                            item = item,
                            selected = false,
                            dividerRoutes = persistentSetOf(),
                            handleNavigationItemClick = {
                                onNavigationDrawerItemClick(
                                    item,
                                    drawerState,
                                    coroutineScope,
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

    BackHandler(enabled = navigator.canGoBack() || drawerState.isOpen) {
        coroutineScope.launch {
            if (drawerState.isOpen) {
                drawerState.close()
            } else {
                navigator.goBack()
            }
        }
    }

    if (showChangelog) {
        ChangelogDialog(
            changelogUrl = changelogUrl,
            onDismiss = { showChangelog = false },
        )
    }
}

private val BottomDrawerActionRoutes = persistentSetOf(
    NavigationDrawerRoutes.ROUTE_SETTINGS,
    NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK,
    NavigationDrawerRoutes.ROUTE_UPDATES,
    NavigationDrawerRoutes.ROUTE_SHARE,
)
