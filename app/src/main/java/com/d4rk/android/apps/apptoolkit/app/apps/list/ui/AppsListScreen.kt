package com.d4rk.android.apps.apptoolkit.app.apps.list.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.components.rememberAdsConfig
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.components.rememberAdsEnabled
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ScreenHelper
import org.koin.compose.getKoin
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.actions.HomeEvent
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.ui.UiHomeScreen
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.components.AppsList
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.components.screens.loading.HomeLoadingScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.ScreenStateHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppsListScreen(paddingValues: PaddingValues) {
    val viewModel: AppsListViewModel = koinViewModel()
    val screenState: UiStateScreen<UiHomeScreen> by viewModel.uiState.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val isTabletOrLandscape = remember(context) { ScreenHelper.isLandscapeOrTablet(context) }
    val koin = getKoin()
    val adsConfig = rememberAdsConfig(koin, isTabletOrLandscape)
    val adsEnabled = rememberAdsEnabled(koin)

    ScreenStateHandler(
        screenState = screenState, onLoading = {
            HomeLoadingScreen(paddingValues = paddingValues)
        },
        onEmpty = {
            NoDataScreen()
        },
        onSuccess = { uiHomeScreen ->
            AppsList(
                uiHomeScreen = uiHomeScreen,
                favorites = favorites,
                paddingValues = paddingValues,
                adsConfig = adsConfig,
                adsEnabled = adsEnabled,
                onFavoriteToggle = { pkg -> viewModel.toggleFavorite(pkg) }
            )
        },
        onError = {
            NoDataScreen(showRetry = true, onRetry = {
                viewModel.onEvent(HomeEvent.FetchApps)
            }, isError = true)
        }
    )
}