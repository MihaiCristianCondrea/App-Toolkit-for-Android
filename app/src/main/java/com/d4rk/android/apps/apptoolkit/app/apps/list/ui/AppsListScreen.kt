package com.d4rk.android.apps.apptoolkit.app.apps.list.ui

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.apps.apptoolkit.app.apps.common.AppDetailsBottomSheet
import com.d4rk.android.apps.apptoolkit.app.apps.common.buildOnAppClick
import com.d4rk.android.apps.apptoolkit.app.apps.common.buildOnShareClick
import com.d4rk.android.apps.apptoolkit.app.apps.common.screens.AppsList
import com.d4rk.android.apps.apptoolkit.app.apps.common.screens.loading.HomeLoadingScreen
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.actions.HomeAction
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.actions.HomeEvent
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.ui.UiHomeScreen
import com.d4rk.android.apps.apptoolkit.app.logging.APPS_LIST_LOG_TAG
import com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation.RandomAppHandler
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.ads.rememberAdsEnabled
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.AppInfoHelper
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsListRoute(
    paddingValues: PaddingValues,
    windowWidthSizeClass: WindowWidthSizeClass,
    onRegisterRandomAppHandler: (RandomAppHandler?) -> Unit,
) {
    val viewModel: AppsListViewModel = koinViewModel()
    val screenState: UiStateScreen<UiHomeScreen> by viewModel.uiState.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val canOpenRandomApp by viewModel.canOpenRandomApp.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val adsEnabled = rememberAdsEnabled()
    val appDetailsAdsConfig: AdsConfig = koinInject(qualifier = named("app_details_native_ad"))
    val onFavoriteToggle: (String) -> Unit = remember(viewModel) { { pkg -> viewModel.toggleFavorite(pkg) } }
    val onRetry: () -> Unit = remember(viewModel) { { viewModel.onEvent(HomeEvent.FetchApps) } }
    val dispatchers: DispatcherProvider = koinInject()
    val openApp: (AppInfo) -> Unit = buildOnAppClick(dispatchers, context)
    val appInfoHelper = remember(dispatchers) { AppInfoHelper(dispatchers) }
    val onOpenInPlayStore: (AppInfo) -> Unit = remember(context) {
        { appInfo ->
            if (appInfo.packageName.isNotEmpty()) {
                IntentsHelper.openPlayStoreForApp(context, appInfo.packageName)
            }
        }
    }
    val onShareClick: (AppInfo) -> Unit = buildOnShareClick(context)
    var selectedApp: AppInfo? by remember { mutableStateOf(null) }
    var isSelectedAppInstalled: Boolean? by remember { mutableStateOf(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedApp?.packageName, context) {
        isSelectedAppInstalled = null
        isSelectedAppInstalled = selectedApp?.let { app ->
            if (app.packageName.isNotEmpty()) {
                appInfoHelper.isAppInstalled(context, app.packageName)
            } else {
                false
            }
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
                onFavoriteClick = {
                    coroutineScope.launch {
                        onFavoriteToggle(app.packageName)
                    }
                },
                adsConfig = appDetailsAdsConfig
            )
        }
    }

    val randomAppHandler = remember(viewModel) {
        { viewModel.onEvent(HomeEvent.OpenRandomApp) }
    }

    LaunchedEffect(canOpenRandomApp, randomAppHandler) {
        val handler = if (canOpenRandomApp) randomAppHandler else null
        Log.d(
            APPS_LIST_LOG_TAG,
            "canOpenRandomApp=$canOpenRandomApp -> handlerRegistered=${handler != null}"
        )
        Log.d(
            APPS_LIST_LOG_TAG,
            "Requesting handler update route=${NavigationRoutes.ROUTE_APPS_LIST}"
        )
        onRegisterRandomAppHandler(handler)
    }

    LaunchedEffect(viewModel) {
        viewModel.actionEvent.collectLatest { action ->
            when (action) {
                is HomeAction.OpenRandomApp -> {
                    if (sheetState.isVisible) {
                        sheetState.hide()
                    }
                    selectedApp = null
                    isSelectedAppInstalled = null
                    openApp(action.app)
                }
            }
        }
    }

    AppsListScreen(
        screenState = screenState,
        favorites = favorites,
        paddingValues = paddingValues,
        adsEnabled = adsEnabled,
        onFavoriteToggle = onFavoriteToggle,
        onAppClick = { app -> selectedApp = app },
        onShareClick = onShareClick,
        onRetry = onRetry,
        windowWidthSizeClass = windowWidthSizeClass,
    )
}

@Composable
fun AppsListScreen(
    screenState: UiStateScreen<UiHomeScreen>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    adsEnabled: Boolean,
    onFavoriteToggle: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onShareClick: (AppInfo) -> Unit,
    onRetry: () -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    ScreenStateHandler(
        screenState = screenState,
        onLoading = {
            HomeLoadingScreen(
                paddingValues = paddingValues,
                windowWidthSizeClass = windowWidthSizeClass,
            )
        },
        onEmpty = { NoDataScreen(paddingValues = paddingValues) },
        onSuccess = { uiHomeScreen ->
            AppsList(
                uiHomeScreen = uiHomeScreen,
                favorites = favorites,
                paddingValues = paddingValues,
                adsEnabled = adsEnabled,
                onFavoriteToggle = onFavoriteToggle,
                onAppClick = onAppClick,
                onShareClick = onShareClick,
                windowWidthSizeClass = windowWidthSizeClass,
            )
        },
        onError = {
            NoDataScreen(
                showRetry = true,
                onRetry = onRetry,
                isError = true,
                paddingValues = paddingValues
            )
        }
    )
}
