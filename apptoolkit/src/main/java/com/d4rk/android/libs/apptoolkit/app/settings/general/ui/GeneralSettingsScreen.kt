package com.d4rk.android.libs.apptoolkit.app.settings.general.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.actions.GeneralSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.model.ui.UiGeneralSettingsScreen
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.GeneralSettingsContentProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.components.navigation.LargeTopAppBarWithScaffold
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSettingsScreen(
    title: String,
    contentKey: String?,
    onBackClicked: () -> Unit
) {
    val viewModel: GeneralSettingsViewModel = koinViewModel()
    val contentProvider: GeneralSettingsContentProvider = koinInject()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(contentKey) {
        viewModel.onEvent(GeneralSettingsEvent.Load(contentKey = contentKey))
    }

    LargeTopAppBarWithScaffold(
        title = title,
        onBackClicked = onBackClicked,
        snackbarHostState = snackbarHostState
    ) { paddingValues: PaddingValues ->
        val screenState: UiStateScreen<UiGeneralSettingsScreen> by viewModel.uiState.collectAsStateWithLifecycle()
        GeneralSettingsContent(
            screenState = screenState,
            contentProvider = contentProvider,
            paddingValues = paddingValues,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun GeneralSettingsContent(
    screenState: UiStateScreen<UiGeneralSettingsScreen>,
    contentProvider: GeneralSettingsContentProvider,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState
) {
    ScreenStateHandler(
        screenState = screenState,
        onLoading = { LoadingScreen() },
        onEmpty = { NoDataScreen(paddingValues = paddingValues) },
        onSuccess = { data: UiGeneralSettingsScreen ->
            contentProvider.ProvideContent(
                contentKey = data.contentKey,
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState
            )
        })
}