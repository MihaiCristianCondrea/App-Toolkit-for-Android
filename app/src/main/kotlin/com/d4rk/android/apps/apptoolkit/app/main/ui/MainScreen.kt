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

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuOpen
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.d4rk.android.apps.apptoolkit.app.main.ui.state.MainUiState
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.fab.MainFloatingActionButton
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.AppNavigationEntryContext
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.RandomAppHandler
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.appNavigationEntryBuilders
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.drawerItemClickEvent
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.isDrawerItemSelected
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppNavKey
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.ComponentsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.FavoriteAppsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.toNavKeyOrDefault
import com.d4rk.android.apps.apptoolkit.app.main.utils.defaults.MainNavigationDefaults
import com.d4rk.android.apps.apptoolkit.core.data.local.datastore.DatastoreInterface
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.BottomBarItem
import com.d4rk.android.libs.apptoolkit.app.main.ui.navigation.handleNavigationItemClick
import com.d4rk.android.libs.apptoolkit.app.main.ui.views.dialogs.ChangelogDialog
import com.d4rk.android.libs.apptoolkit.app.main.ui.views.navigation.MainTopAppBar
import com.d4rk.android.libs.apptoolkit.app.main.ui.views.navigation.NavigationDrawerItemContent
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.NavigationDrawerRoutes
import com.d4rk.android.libs.apptoolkit.core.di.AppToolkitDiConstants
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationAnimations
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationState
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.Navigator
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.entryProviderFor
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.rememberNavigationEntryDecorators
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.rememberNavigationState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.RootContentContainer
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.hapticDrawerSwipe
import com.d4rk.android.libs.apptoolkit.core.ui.views.snackbar.DefaultSnackbarHost
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import com.d4rk.android.libs.apptoolkit.core.ui.window.rememberWindowWidthSizeClass
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
fun MainScreen() {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val layoutType: NavigationSuiteType = NavigationSuiteScaffoldDefaults.navigationSuiteType(
        adaptiveInfo = adaptiveInfo,
    )
    val drawerEnabled: Boolean = layoutType.usesModalDrawer
    val horizontalNavigation: Boolean = layoutType.usesNavigationBar

    val context: Context = LocalContext.current
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val windowWidthSizeClass: AppWindowWidthSizeClass = rememberWindowWidthSizeClass()
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    val viewModel: MainViewModel = koinViewModel()
    val screenState: UiStateScreen<MainUiState> by viewModel.uiState.collectAsStateWithLifecycle()

    val dataStore: DatastoreInterface = koinInject()
    val firebaseController: FirebaseController = koinInject()
    val changelogUrl: String = koinInject(
        qualifier = named(AppToolkitDiConstants.GITHUB_CHANGELOG),
    )

    val startupRoute: AppNavKey by dataStore
        .startupDestinationFlow(defaultRoute = NavigationRoutes.ROUTE_APPS_LIST) { value: String ->
            value.toNavKeyOrDefault()
        }
        .collectAsStateWithLifecycle(
            initialValue = NavigationRoutes.ROUTE_APPS_LIST.toNavKeyOrDefault(),
        )

    val navigationState: NavigationState<AppNavKey> = rememberNavigationState(
        startRoute = startupRoute,
        topLevelRoutes = NavigationRoutes.topLevelRoutes,
    )

    val navigator: Navigator<AppNavKey> = remember(navigationState) {
        Navigator(navigationState)
    }

    var showChangelog: Boolean by rememberSaveable {
        mutableStateOf(value = false)
    }

    val uiState: MainUiState = screenState.data ?: MainUiState()
    val bottomItems: ImmutableList<BottomBarItem<AppNavKey>> = MainNavigationDefaults.bottomBarItems
    val currentRoute: AppNavKey = navigationState.currentRoute

    val appRouteHandlers: Map<String, (NavigationDrawerItem) -> Unit> = remember(navigator) {
        mapOf(
            NavigationRoutes.ROUTE_APPS_LIST to { navigator.navigate(AppsListRoute) },
            NavigationRoutes.ROUTE_FAVORITE_APPS to { navigator.navigate(FavoriteAppsRoute) },
            NavigationRoutes.ROUTE_COMPONENTS to { navigator.navigate(ComponentsRoute) },
        )
    }

    val onNavigationDrawerItemClick: (NavigationDrawerItem, DrawerState?, CoroutineScope?) -> Unit =
        remember(context, firebaseController, appRouteHandlers) {
            { item: NavigationDrawerItem,
              targetDrawerState: DrawerState?,
              targetCoroutineScope: CoroutineScope? ->

                firebaseController.logEvent(drawerItemClickEvent(route = item.route))

                handleNavigationItemClick(
                    context = context,
                    item = item,
                    drawerState = targetDrawerState,
                    coroutineScope = targetCoroutineScope,
                    onChangelogRequested = { showChangelog = true },
                    additionalHandlers = appRouteHandlers,
                )
            }
        }

    val appBarTitleResId: Int = remember(
        currentRoute,
        bottomItems,
        uiState.navigationDrawerItems,
    ) {
        bottomItems.firstOrNull { item: BottomBarItem<AppNavKey> ->
            item.route == currentRoute
        }?.title ?: uiState.navigationDrawerItems.firstOrNull { item: NavigationDrawerItem ->
            isDrawerItemSelected(
                itemRoute = item.route,
                currentRoute = currentRoute,
            )
        }?.title ?: bottomItems.first().title
    }

    val randomAppHandlers = remember {
        mutableStateMapOf<AppNavKey, RandomAppHandler>()
    }

    val onRandomAppHandlerChanged: (AppNavKey, RandomAppHandler?) -> Unit = remember {
        { route: AppNavKey, handler: RandomAppHandler? ->
            if (handler == null) {
                randomAppHandlers.remove(route)
            } else {
                randomAppHandlers[route] = handler
            }
        }
    }

    val randomAppHandler: RandomAppHandler? = randomAppHandlers[currentRoute]
    val isFabVisible: Boolean = currentRoute in MainNavigationDefaults.fabSupportedRoutes

    LaunchedEffect(currentRoute, isFabVisible) {
        if (isFabVisible) {
            scrollBehavior.state.contentOffset = 0f
        }
    }

    val isFabExtended: Boolean by remember(
        isFabVisible,
        horizontalNavigation,
        scrollBehavior,
    ) {
        derivedStateOf {
            isFabVisible &&
                    horizontalNavigation &&
                    scrollBehavior.state.contentOffset >= 0f
        }
    }

    val railDrawerItems: Pair<List<NavigationDrawerItem>, List<NavigationDrawerItem>> =
        remember(uiState.navigationDrawerItems) {
            uiState.navigationDrawerItems.partition { item: NavigationDrawerItem ->
                item.route !in BottomDrawerActionRoutes
            }
        }

    val bottomNavigationItem: @Composable (BottomBarItem<AppNavKey>) -> Unit =
        { item: BottomBarItem<AppNavKey> ->

            NavigationSuiteItem(
                selected = item.route == currentRoute,
                onClick = { navigator.navigate(item.route) },
                icon = {
                    Icon(
                        imageVector = if (item.route == currentRoute) {
                            item.selectedIcon
                        } else {
                            item.icon
                        },
                        contentDescription = stringResource(item.title),
                    )
                },
                label = { Text(text = stringResource(item.title)) },
                navigationSuiteType = layoutType,
            )
        }

    val drawerNavigationItem: @Composable (NavigationDrawerItem) -> Unit =
        { item: NavigationDrawerItem ->

            NavigationSuiteItem(
                selected = isDrawerItemSelected(
                    itemRoute = item.route,
                    currentRoute = currentRoute,
                ),
                onClick = { onNavigationDrawerItemClick(item, null, null) },
                icon = {
                    Icon(
                        imageVector = if (
                            isDrawerItemSelected(
                                itemRoute = item.route,
                                currentRoute = currentRoute,
                            )
                        ) {
                            item.selectedIcon
                        } else {
                            item.icon
                        },
                        contentDescription = stringResource(item.title),
                )
                },
                label = { Text(text = stringResource(item.title)) },
                navigationSuiteType = layoutType,
            )
        }

    val shellContent: @Composable () -> Unit = {
        NavigationSuiteScaffold(
            modifier = Modifier.imePadding(),
            navigationSuiteType = layoutType,
            navigationItems = {
                if (horizontalNavigation) {
                    bottomItems.forEach { item: BottomBarItem<AppNavKey> ->
                        bottomNavigationItem(item)
                    }
                } else {
                    Column(modifier = Modifier.fillMaxHeight()) {
                        bottomItems.forEach { item: BottomBarItem<AppNavKey> ->
                            bottomNavigationItem(item)
                        }

                        railDrawerItems.first.forEach { item: NavigationDrawerItem ->
                            drawerNavigationItem(item)
                        }

                        Spacer(modifier = Modifier.weight(weight = 1f))

                        railDrawerItems.second.forEach { item: NavigationDrawerItem ->
                            drawerNavigationItem(item)
                        }
                    }
                }
            },
            primaryActionContent = {
                Box(
                    modifier = if (horizontalNavigation) {
                        Modifier
                    } else {
                        Modifier.fillMaxWidth()
                    },
                    contentAlignment = Alignment.Center,
                ) {
                    MainFloatingActionButton(
                        visible = isFabVisible && randomAppHandler != null,
                        expanded = isFabExtended,
                        onClick = { randomAppHandler?.invoke() },
                    )
                }
            },
            primaryActionContentHorizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                topBar = {
                    MainTopAppBar(
                        title = stringResource(appBarTitleResId),
                        navigationIcon = if (drawerState.isOpen) {
                            Icons.AutoMirrored.Outlined.MenuOpen
                        } else {
                            Icons.Default.Menu
                        },
                        onNavigationIconClick = {
                            if (drawerEnabled) {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }
                        },
                        scrollBehavior = scrollBehavior,
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
                    windowWidthSizeClass = windowWidthSizeClass,
                ) {
                    val entryBuilders: List<NavigationEntryBuilder<AppNavKey>> =
                        remember(
                            paddingValues,
                            windowWidthSizeClass,
                            onRandomAppHandlerChanged,
                        ) {
                            val entryContext = AppNavigationEntryContext(
                                paddingValues = paddingValues,
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

    LaunchedEffect(drawerEnabled) {
        if (!drawerEnabled && drawerState.isOpen) {
            drawerState.close()
        }
    }

    if (drawerEnabled) {
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
                            selected = isDrawerItemSelected(
                                itemRoute = item.route,
                                currentRoute = currentRoute,
                            ),
                            dividerRoutes = DrawerDividerRoutes,
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

    if (showChangelog) {
        ChangelogDialog(
            changelogUrl = changelogUrl,
            onDismiss = { showChangelog = false },
        )
    }
}

private val NavigationSuiteType.usesNavigationBar: Boolean
    get() = this == NavigationSuiteType.ShortNavigationBarCompact ||
            this == NavigationSuiteType.ShortNavigationBarMedium ||
            this == NavigationSuiteType.NavigationBar

private val NavigationSuiteType.usesModalDrawer: Boolean
    get() = usesNavigationBar

private val DrawerDividerRoutes = persistentSetOf(
    NavigationRoutes.ROUTE_COMPONENTS,
)

private val BottomDrawerActionRoutes = persistentSetOf(
    NavigationDrawerRoutes.ROUTE_SETTINGS,
    NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK,
    NavigationDrawerRoutes.ROUTE_UPDATES,
    NavigationDrawerRoutes.ROUTE_SHARE,
)