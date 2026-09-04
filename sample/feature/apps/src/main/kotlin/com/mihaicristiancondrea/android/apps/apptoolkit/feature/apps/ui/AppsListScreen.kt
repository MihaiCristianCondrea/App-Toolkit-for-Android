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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.domain.models.RandomAppHandler
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts.HomeAction
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts.HomeEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states.AppListUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.AndroidAppActionLauncher
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.AppDetailsBottomSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.analytics.AppInteractionType
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.analytics.logAppInteraction
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.buildOnAppClick
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.buildOnShareClick
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.screens.AppsList
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.screens.loading.HomeLoadingScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.constants.AppAdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

/**
 * A route-level composable that orchestrates the display of the apps list screen.
 *
 * This function is responsible for:
 * - Observing state from the [AppsListViewModel].
 * - Handling UI state (loading, success, error, empty).
 * - Managing user interactions such as toggling favorites, clicking on an app to view details,
 *   sharing an app, and retrying data fetching.
 * - Displaying a modal bottom sheet for app details when an app is selected.
 * - Registering a handler for opening a random app, which can be triggered by a parent composable
 *   (e.g., a Floating Action Button in the main scaffold).
 *
 * @param paddingValues The padding values to be applied to the screen content, typically from a
 *   [Scaffold].
 * @param windowWidthSizeClass The window width size class, used to adapt the layout for
 *   different screen sizes.
 * @param onRegisterRandomAppHandler A callback to register or unregister the "open random app"
 *   action. It passes a [RandomAppHandler] lambda when the action is available (i.e., when
 *   there are apps to choose from), and `null` otherwise.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsListScreen(
    paddingValues: PaddingValues,
    windowWidthSizeClass: AppWindowWidthSizeClass,
    onRegisterRandomAppHandler: (RandomAppHandler?) -> Unit,
) {
    val viewModel: AppsListViewModel = koinViewModel()

    val screenState: UiStateScreen<AppListUiState> by viewModel.uiState.collectAsStateWithLifecycle()
    val favoritesRaw: Set<String> by viewModel.favorites.collectAsStateWithLifecycle()
    val favorites = remember(favoritesRaw) { favoritesRaw.toImmutableSet() } // ✅ stable
    val canOpenRandomApp by viewModel.canOpenRandomApp.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val adsEnabled = rememberAdsEnabled()

    val appDetailsAdsConfig: AdsConfig =
        koinInject(qualifier = named(AppAdsQualifiers.APP_DETAILS_NATIVE_AD))
    val firebaseController: FirebaseController = koinInject()

    val onFavoriteToggle: (String) -> Unit =
        remember(viewModel, firebaseController, screenState.data?.apps, favorites) {
            { pkg ->
                val app = screenState.data?.apps?.firstOrNull { it.packageName == pkg }
                val wasFavorite = favorites.contains(pkg)
                if (app != null) {
                    firebaseController.logAppInteraction(
                        source = "apps_list",
                        appInfo = app,
                        interaction = if (wasFavorite) AppInteractionType.RemoveFavorite else AppInteractionType.AddFavorite,
                    )
                }
                viewModel.toggleFavorite(pkg)
            }
        }
    val onRetry: () -> Unit = remember(viewModel) { { viewModel.onEvent(HomeEvent.FetchApps) } }

    val buildAppClick = buildOnAppClick()
    val buildShareClick = buildOnShareClick()
    val openApp: (AppInfo) -> Unit = remember { buildAppClick }
    val onShareClick: (AppInfo) -> Unit = remember(buildShareClick, firebaseController) {
        { app ->
            firebaseController.logAppInteraction(
                source = "apps_list",
                appInfo = app,
                interaction = AppInteractionType.Share,
                interactionContext = "grid_share"
            )
            buildShareClick(app)
        }
    }

    val selectedApp: AppInfo? = screenState.data?.selectedApp
    val selectedAppDetails = screenState.data?.selectedAppDetails
    val isAppDetailsLoading = screenState.data?.isAppDetailsLoading == true
    val hasAppDetailsError = screenState.data?.hasAppDetailsError == true
    val selectedAppInstallInfo = screenState.data?.selectedAppInstallInfo
    val appActionLauncher = remember(context) { AndroidAppActionLauncher(context) }

    val sheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    selectedApp?.let { app ->
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = sheetState,
            onDismissRequest = {
                firebaseController.logAppInteraction(
                    source = "apps_list",
                    appInfo = app,
                    interaction = AppInteractionType.CloseDetailsBottomSheet
                )
                coroutineScope.launch {
                    sheetState.hide()
                    viewModel.onEvent(HomeEvent.AppDetailsDismissed)
                }
            }
        ) {
            AppDetailsBottomSheet(
                appInfo = app,
                appDetails = selectedAppDetails,
                isDetailsLoading = isAppDetailsLoading,
                hasDetailsError = hasAppDetailsError,
                isFavorite = favorites.contains(app.packageName),
                isAppInstalled = selectedAppInstallInfo?.isInstalled,
                installedVersionInfo = selectedAppInstallInfo?.versionInfo,
                actionLauncher = appActionLauncher,
                onFavoriteClick = { onFavoriteToggle(app.packageName) },
                onRetryDetails = { viewModel.onEvent(HomeEvent.RetryAppDetails) },
                adsConfig = appDetailsAdsConfig
            )
        }
    }

    val randomAppHandler: RandomAppHandler =
        remember(viewModel) { { viewModel.onEvent(HomeEvent.OpenRandomApp) } }

    val registerHandler by rememberUpdatedState(onRegisterRandomAppHandler)

    LaunchedEffect(canOpenRandomApp) {
        registerHandler(if (canOpenRandomApp) randomAppHandler else null)
    }

    LaunchedEffect(viewModel) {
        viewModel.actionEvent.collectLatest { action ->
            when (action) {
                is HomeAction.OpenRandomApp -> {
                    if (sheetState.isVisible) sheetState.hide()
                    viewModel.onEvent(HomeEvent.AppDetailsDismissed)
                    openApp(action.app)
                }
            }
        }
    }

    ScreenStateHandler(
        screenState = screenState,
        onLoading = {
            HomeLoadingScreen(
                paddingValues = paddingValues,
                windowWidthSizeClass = windowWidthSizeClass,
            )
        },
        onEmpty = { NoDataScreen(paddingValues = paddingValues) },
        onError = {
            NoDataScreen(
                showRetry = true,
                onRetry = onRetry,
                isError = true,
                paddingValues = paddingValues
            )
        },
        onSuccess = { uiHomeScreen ->
            AppsList(
                uiHomeScreen = uiHomeScreen,
                favorites = favorites,
                installedPackages = uiHomeScreen.installedPackages,
                paddingValues = paddingValues,
                adsEnabled = adsEnabled,
                onFilterSelected = { filter -> viewModel.onEvent(HomeEvent.FilterSelected(filter)) },
                onFavoriteToggle = onFavoriteToggle,
                onAppClick = { app ->
                    firebaseController.logAppInteraction(
                        source = "apps_list",
                        appInfo = app,
                        interaction = AppInteractionType.OpenDetailsBottomSheet
                    )
                    viewModel.onEvent(HomeEvent.AppSelected(app.packageName))
                },
                onShareClick = onShareClick,
                onFirstVisibleAppChanged = { firstVisibleApp ->
                    firebaseController.logAppInteraction(
                        source = "apps_list",
                        appInfo = firstVisibleApp,
                        interaction = AppInteractionType.GridAppImpression
                    )
                },
                windowWidthSizeClass = windowWidthSizeClass,
            )
        }
    )
}
