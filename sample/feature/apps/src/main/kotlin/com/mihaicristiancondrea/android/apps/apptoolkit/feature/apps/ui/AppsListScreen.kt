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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts.HomeEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states.AppListUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.AndroidAppActionLauncher
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.AppActionLauncher
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.AppDetailsBottomSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.analytics.AnalyticsAppActionLauncher
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.analytics.AppInteractionType
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.analytics.logAppInteraction
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.buildOnAppClick
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.buildOnShareClick
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.screens.AppsList
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.screens.loading.HomeLoadingScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.constants.AppAdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logShare
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.ScaffoldFabs
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.window.rememberWindowWidthSizeClass
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellSearch
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

private const val APPS_LIST_INTERACTION_SOURCE: String = "apps_list"
private const val APP_DETAILS_INTERACTION_SOURCE: String = "app_details"

/**
 * The Apps tab: the catalogue grid, searched from the app bar, with native ad cards, the details
 * sheet and the random-app button.
 *
 * Logs each card and sheet tap, and opens and shares apps, since both need a `Context`. A share
 * logs only the recommended `share` event, because a second `app_card_interaction` for it would
 * count every share twice. The favorite callback reads the state at tap time, so its identity stays
 * the same when the favorites change and the cards do not recompose for it.
 */
@Composable
fun AppsListScreen() {
    val viewModel: AppsListViewModel = koinViewModel()
    val state: AppListUiState by viewModel.state.collectAsStateWithLifecycle()
    val telemetryRepository = LocalTelemetry.current
    val context = LocalContext.current
    val adsEnabled: Boolean = rememberAdsEnabled()
    val listAdsConfig: AdsConfig = koinInject(qualifier = named(AppAdsQualifiers.APPS_LIST_NATIVE_AD))
    val detailsAdsConfig: AdsConfig = koinInject(qualifier = named(AppAdsQualifiers.APP_DETAILS_NATIVE_AD))
    val openApp: (AppInfo) -> Unit = buildOnAppClick()
    val shareApp: (AppInfo) -> Unit = buildOnShareClick()
    val appActionLauncher = remember(context) { AndroidAppActionLauncher(context) }
    val selectedApp: AppInfo? = state.selectedApp
    val detailsActionLauncher: AppActionLauncher = remember(appActionLauncher, telemetryRepository, selectedApp) {
        selectedApp?.let { app ->
            AnalyticsAppActionLauncher(
                delegate = appActionLauncher,
                telemetryRepository = telemetryRepository,
                appInfo = app,
                source = APP_DETAILS_INTERACTION_SOURCE,
            )
        } ?: appActionLauncher
    }
    val currentState: AppListUiState by rememberUpdatedState(state)

    TrackScreenView(
        screenName = AppScreenTracking.Screens.APPS_LIST.name,
        screenClass = AppScreenTracking.Screens.APPS_LIST.className,
    )

    TrackScreenState(
        screenName = AppScreenTracking.Screens.APPS_LIST.name,
        state = state.apps,
    )

    val randomAppToOpen: AppInfo? = state.randomAppToOpen
    LaunchedEffect(randomAppToOpen) {
        if (randomAppToOpen != null) {
            openApp(randomAppToOpen)
            viewModel.onEvent(HomeEvent.RandomAppOpened)
        }
    }

    val onFavoriteToggle: (String) -> Unit = remember(viewModel, telemetryRepository) {
        { packageName ->
            val current = currentState
            current.loadedApps.firstOrNull { it.packageName == packageName }?.let { app ->
                telemetryRepository.logAppInteraction(
                    source = APPS_LIST_INTERACTION_SOURCE,
                    appInfo = app,
                    interaction = if (packageName in current.favorites) {
                        AppInteractionType.RemoveFavorite
                    } else {
                        AppInteractionType.AddFavorite
                    },
                )
            }
            viewModel.onEvent(HomeEvent.FavoriteToggled(packageName = packageName))
        }
    }

    AppsListScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onFavoriteToggle = onFavoriteToggle,
        onShareClick = { app ->
            telemetryRepository.logShare(
                method = "system_share",
                contentType = "app",
                itemId = app.packageName,
            )
            shareApp(app)
        },
        onDetailsClosed = { app ->
            telemetryRepository.logAppInteraction(
                source = APPS_LIST_INTERACTION_SOURCE,
                appInfo = app,
                interaction = AppInteractionType.CloseDetailsBottomSheet,
            )
        },
        detailsActionLauncher = detailsActionLauncher,
        windowWidthSizeClass = rememberWindowWidthSizeClass(),
        contentPadding = contentPadding(),
        searchQuery = LocalShellSearch.current?.query.orEmpty(),
        listAdUnitId = listAdsConfig.bannerAdUnitId.takeIf { adsEnabled && it.isNotBlank() },
        detailsAdsConfig = detailsAdsConfig,
    )

    MessageHost(
        viewModel = viewModel,
        onAction = { viewModel.onEvent(HomeEvent.Load) },
    )
}

/**
 * Renders [state]: the grid with its filter chips, the details sheet of the selected app, and the
 * random-app button, which the tab's scaffold draws and scales out while there is no app to open.
 * Holds no ViewModel, injects nothing and opens nothing, so it renders in a preview with plain
 * values.
 *
 * @param onEvent Receives the events [AppsListViewModel] handles.
 * @param onFavoriteToggle A card's or the sheet's favorite button was tapped.
 * @param onShareClick A card's share button was tapped.
 * @param onDetailsClosed The details sheet of this app was dismissed. The content then hides the
 * sheet and sends `HomeEvent.AppDetailsDismissed` itself.
 * @param detailsActionLauncher Opens the system pages and links the details sheet offers.
 * @param contentPadding Padding from the shell, applied inside the grid and the state screens.
 * @param searchQuery The app bar's search text.
 * @param listAdUnitId The grid's native ad unit, or null when no ad should show.
 * @param detailsAdsConfig The details sheet's native ad configuration.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppsListScreenContent(
    state: AppListUiState,
    onEvent: (HomeEvent) -> Unit,
    onFavoriteToggle: (packageName: String) -> Unit,
    onShareClick: (AppInfo) -> Unit,
    onDetailsClosed: (AppInfo) -> Unit,
    detailsActionLauncher: AppActionLauncher,
    windowWidthSizeClass: AppWindowWidthSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    searchQuery: String = "",
    listAdUnitId: String? = null,
    detailsAdsConfig: AdsConfig = AdsConfig(),
) {
    val sheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    ScaffoldFabs(
        listOf(
            ToolkitFab(
                icon = ToolkitIcon.Vector(Icons.Outlined.Casino),
                onClick = { onEvent(HomeEvent.OpenRandomApp) },
                label = stringResource(R.string.open_random_app),
                visible = state.canOpenRandomApp,
            ),
        ),
    )

    ScreenStateHandler(
        state = state.apps,
        modifier = modifier,
        contentPadding = contentPadding,
        onRetry = { onEvent(HomeEvent.Load) },
        onLoading = {
            HomeLoadingScreen(
                paddingValues = contentPadding,
                windowWidthSizeClass = windowWidthSizeClass,
            )
        },
    ) { ready ->
        AppsList(
            allApps = ready.value,
            selectedFilter = state.selectedFilter,
            favorites = state.favorites,
            installedPackages = state.installedPackages,
            contentPadding = contentPadding,
            adUnitId = listAdUnitId,
            onFilterSelected = { filter -> onEvent(HomeEvent.FilterSelected(filter = filter)) },
            onFavoriteToggle = onFavoriteToggle,
            onAppClick = { app -> onEvent(HomeEvent.AppSelected(packageName = app.packageName)) },
            onShareClick = onShareClick,
            windowWidthSizeClass = windowWidthSizeClass,
            searchQuery = searchQuery,
        )
    }

    state.selectedApp?.let { app ->
        val details = state.selectedAppDetails
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = sheetState,
            onDismissRequest = {
                onDetailsClosed(app)
                coroutineScope.launch {
                    sheetState.hide()
                    onEvent(HomeEvent.AppDetailsDismissed)
                }
            },
        ) {
            AppDetailsBottomSheet(
                appInfo = app,
                appDetails = (details as? Loadable.Ready)?.value,
                isDetailsLoading = details is Loadable.Loading,
                hasDetailsError = details is Loadable.Failed,
                isFavorite = app.packageName in state.favorites,
                isAppInstalled = state.selectedAppInstallInfo?.isInstalled,
                installedVersionInfo = state.selectedAppInstallInfo?.versionInfo,
                actionLauncher = detailsActionLauncher,
                onFavoriteClick = { onFavoriteToggle(app.packageName) },
                onRetryDetails = { onEvent(HomeEvent.RetryAppDetails) },
                adsConfig = detailsAdsConfig,
            )
        }
    }
}

private val PreviewApps = persistentListOf(
    AppInfo(
        name = "App Toolkit",
        packageName = "com.example.apptoolkit",
        iconUrl = "",
        shortDescription = "Reusable building blocks for Android apps",
    ),
    AppInfo(
        name = "Smart Cleaner",
        packageName = "com.example.cleaner",
        iconUrl = "",
        shortDescription = "Frees up storage",
    ),
)

@Preview(showBackground = true)
@Composable
private fun AppsListScreenContentPreview() {
    val context = LocalContext.current
    MaterialTheme {
        AppsListScreenContent(
            state = AppListUiState(
                apps = Loadable.Ready(PreviewApps),
                installedPackages = persistentSetOf("com.example.apptoolkit"),
                favorites = persistentSetOf("com.example.cleaner"),
            ),
            onEvent = {},
            onFavoriteToggle = {},
            onShareClick = {},
            onDetailsClosed = {},
            detailsActionLauncher = remember(context) { AndroidAppActionLauncher(context) },
            windowWidthSizeClass = AppWindowWidthSizeClass.Compact,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppsListScreenContentLoadingPreview() {
    val context = LocalContext.current
    MaterialTheme {
        AppsListScreenContent(
            state = AppListUiState(apps = Loadable.Loading),
            onEvent = {},
            onFavoriteToggle = {},
            onShareClick = {},
            onDetailsClosed = {},
            detailsActionLauncher = remember(context) { AndroidAppActionLauncher(context) },
            windowWidthSizeClass = AppWindowWidthSizeClass.Compact,
        )
    }
}
