package com.d4rk.android.apps.apptoolkit.app.apps.list.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views.AppDetailsBottomSheet
import com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views.buildOnAppClick
import com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views.buildOnShareClick
import com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views.screens.AppsList
import com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views.screens.loading.HomeLoadingScreen
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.contract.HomeAction
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.contract.HomeEvent
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state.AppListUiState
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.RandomAppHandler
import com.d4rk.android.apps.apptoolkit.core.utils.constants.logging.APPS_LIST_LOG_TAG
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.ui.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.ads.rememberAdsEnabled
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openPlayStoreForApp
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.packagemanager.isAppInstalled
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
fun AppsListRoute(
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

    val appDetailsAdsConfig: AdsConfig = koinInject(qualifier = named("app_details_native_ad"))
    val dispatchers: DispatcherProvider = koinInject()

    val onFavoriteToggle: (String) -> Unit =
        remember(viewModel) { { pkg -> viewModel.toggleFavorite(pkg) } }
    val onRetry: () -> Unit = remember(viewModel) { { viewModel.onEvent(HomeEvent.FetchApps) } }

    val buildAppClick = buildOnAppClick(dispatchers)
    val buildShareClick = buildOnShareClick()
    val openApp: (AppInfo) -> Unit = remember(dispatchers) { buildAppClick }
    val onShareClick: (AppInfo) -> Unit = remember { buildShareClick }

    val onOpenInPlayStore: (AppInfo) -> Unit = remember(context) {
        { appInfo ->
            if (appInfo.packageName.isNotEmpty()) {
                val opened = context.openPlayStoreForApp(appInfo.packageName)
                if (!opened) {
                    android.util.Log.w(
                        APPS_LIST_LOG_TAG,
                        "Unable to open Play Store for ${appInfo.packageName}"
                    )
                }
            }
        }
    }

    var selectedApp: AppInfo? by remember { mutableStateOf(null) }
    var isSelectedAppInstalled: Boolean? by remember { mutableStateOf(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedApp?.packageName) {
        isSelectedAppInstalled = selectedApp?.let { app ->
            if (app.packageName.isNotEmpty()) {
                withContext(dispatchers.io) {
                    context.isAppInstalled(app.packageName)
                }
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
                onFavoriteClick = { onFavoriteToggle(app.packageName) },
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
