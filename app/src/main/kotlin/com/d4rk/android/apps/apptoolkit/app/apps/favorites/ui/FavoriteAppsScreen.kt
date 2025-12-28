package com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.apps.common.AppDetailsBottomSheet
import com.d4rk.android.apps.apptoolkit.app.apps.common.buildOnAppClick
import com.d4rk.android.apps.apptoolkit.app.apps.common.buildOnShareClick
import com.d4rk.android.apps.apptoolkit.app.apps.common.screens.AppsList
import com.d4rk.android.apps.apptoolkit.app.apps.common.screens.loading.HomeLoadingScreen
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.contract.FavoriteAppsAction
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.contract.FavoriteAppsEvent
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state.AppListUiState
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.RandomAppHandler
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.apps.apptoolkit.core.logging.FAVORITES_LOG_TAG
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.ui.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.ads.rememberAdsEnabled
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openPlayStoreForApp
import com.d4rk.android.libs.apptoolkit.core.utils.platform.AppInfoHelper
import com.d4rk.android.libs.apptoolkit.core.utils.window.AppWindowWidthSizeClass
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

/**
 * A composable that represents the "Favorite Apps" screen.
 * It displays a list of apps that the user has marked as favorites.
 * This route also manages the state for showing app details in a bottom sheet
 * and handles the logic for opening a random favorite app.
 *
 * @param paddingValues The padding values to be applied to the content, typically from a [Scaffold].
 * @param windowWidthSizeClass The width size class of the window to adapt the layout.
 * @param onRegisterRandomAppHandler A callback to register or unregister the "open random app" action.
 * This is used to enable or disable the random app feature in the main UI based on whether there
 * are any favorite apps available.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteAppsRoute(
    paddingValues: PaddingValues,
    windowWidthSizeClass: AppWindowWidthSizeClass,
    onRegisterRandomAppHandler: (RandomAppHandler?) -> Unit,
) {
    val viewModel: FavoriteAppsViewModel = koinViewModel()

    val screenState: UiStateScreen<AppListUiState> by viewModel.uiState.collectAsStateWithLifecycle()
    val favoritesRaw: Set<String> by viewModel.favorites.collectAsStateWithLifecycle()
    val favorites: ImmutableSet<String> =
        remember(favoritesRaw) { favoritesRaw.toImmutableSet() } // stable :contentReference[oaicite:3]{index=3}
    val canOpenRandomApp: Boolean by viewModel.canOpenRandomApp.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val adsEnabled = rememberAdsEnabled()

    val appDetailsAdsConfig: AdsConfig = koinInject(qualifier = named("app_details_native_ad"))
    val dispatchers: DispatcherProvider = koinInject()

    val onFavoriteToggle: (String) -> Unit =
        remember(viewModel) { { pkg -> viewModel.toggleFavorite(pkg) } }
    val onRetry: () -> Unit =
        remember(viewModel) { { viewModel.onEvent(FavoriteAppsEvent.LoadFavorites) } }

    val buildAppClick = buildOnAppClick(dispatchers)
    val buildShareClick = buildOnShareClick()

    val openApp: (AppInfo) -> Unit = remember(dispatchers) { buildAppClick }
    val onShareClick: (AppInfo) -> Unit = remember { buildShareClick }

    val appInfoHelper = remember(dispatchers) { AppInfoHelper(dispatchers) }
    val onOpenInPlayStore: (AppInfo) -> Unit = remember(context) {
        { appInfo ->
            if (appInfo.packageName.isNotEmpty()) {
                context.openPlayStoreForApp(appInfo.packageName)
            }
        }
    }

    var selectedApp: AppInfo? by remember { mutableStateOf(null) }
    var isSelectedAppInstalled: Boolean? by remember { mutableStateOf(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedApp?.packageName) {
        isSelectedAppInstalled = selectedApp?.let { app ->
            if (app.packageName.isNotEmpty()) appInfoHelper.isAppInstalled(
                context,
                app.packageName
            ) else false
        }
    }

    selectedApp?.let { app ->
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = sheetState,
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                    selectedApp = null
                }
            }
        ) {
            AppDetailsBottomSheet(
                appInfo = app,
                isFavorite = favorites.contains(app.packageName),
                isAppInstalled = isSelectedAppInstalled,
                onShareClick = { onShareClick(app) },
                onOpenAppClick = {
                    coroutineScope.launch {
                        selectedApp = null
                        openApp(app)
                    }
                },
                onOpenInPlayStoreClick = {
                    coroutineScope.launch {
                        selectedApp = null
                        onOpenInPlayStore(app)
                    }
                },
                onFavoriteClick = { onFavoriteToggle(app.packageName) },
                adsConfig = appDetailsAdsConfig
            )
        }
    }

    val randomAppHandler: RandomAppHandler = remember(viewModel) {
        { viewModel.onEvent(FavoriteAppsEvent.OpenRandomApp) }
    }

    val registerHandler by rememberUpdatedState(onRegisterRandomAppHandler)

    LaunchedEffect(canOpenRandomApp) {
        val handler = if (canOpenRandomApp) randomAppHandler else null
        Log.d(
            FAVORITES_LOG_TAG,
            "canOpenRandomApp=$canOpenRandomApp -> handlerRegistered=${handler != null}"
        )
        Log.d(
            FAVORITES_LOG_TAG,
            "Requesting handler update route=${NavigationRoutes.ROUTE_FAVORITE_APPS}"
        )
        registerHandler(handler)
    }

    LaunchedEffect(viewModel) {
        viewModel.actionEvent.collectLatest { action ->
            when (action) {
                is FavoriteAppsAction.OpenRandomApp -> {
                    if (sheetState.isVisible) sheetState.hide()
                    selectedApp = null
                    isSelectedAppInstalled = null
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
        onEmpty = {
            NoDataScreen(
                textMessage = R.string.no_apps_added_to_favorites,
                icon = Icons.Outlined.Android,
                paddingValues = paddingValues
            )
        },
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
                paddingValues = paddingValues,
                adsEnabled = adsEnabled,
                onFavoriteToggle = onFavoriteToggle,
                onAppClick = { app -> selectedApp = app },
                onShareClick = onShareClick,
                windowWidthSizeClass = windowWidthSizeClass,
            )
        }
    )
}
