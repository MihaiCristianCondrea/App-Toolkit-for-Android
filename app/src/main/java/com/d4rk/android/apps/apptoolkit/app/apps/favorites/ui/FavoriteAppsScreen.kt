package com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.actions.FavoriteAppsEvent
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.ui.UiHomeScreen
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.components.AppsList
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.components.screens.loading.HomeLoadingScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.ScreenStateHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoriteAppsScreen(paddingValues: PaddingValues) {
    val viewModel: FavoriteAppsViewModel = koinViewModel()
    val screenState: UiStateScreen<UiHomeScreen> by viewModel.uiState.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val onFavoriteToggle: (String) -> Unit = remember(viewModel) { { pkg -> viewModel.toggleFavorite(pkg) } }

    ScreenStateHandler(
        screenState = screenState,
        onLoading = { HomeLoadingScreen(paddingValues = paddingValues) },
        onEmpty = {
            NoDataScreen(
                textMessage = R.string.no_apps_added_to_favorites,
                icon = Icons.Outlined.Android
            )
        },
        onSuccess = { uiHomeScreen ->
            AppsList(
                uiHomeScreen = uiHomeScreen,
                favorites = favorites,
                paddingValues = paddingValues,
                onFavoriteToggle = onFavoriteToggle
            )
        },
        onError = {
            NoDataScreen(
                showRetry = true,
                onRetry = { viewModel.onEvent(FavoriteAppsEvent.LoadFavorites) },
                isError = true
            )
        }
    )
}