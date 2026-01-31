package com.d4rk.android.libs.apptoolkit.app.settings.general.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.app.settings.general.ui.contract.GeneralSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.general.ui.state.GeneralSettingsUiState
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.GeneralSettingsContentProvider
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.d4rk.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val GENERAL_SETTINGS_SCREEN_NAME = "GeneralSettings"
private const val GENERAL_SETTINGS_SCREEN_CLASS = "GeneralSettingsScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSettingsScreen(
    title: String,
    contentKey: String?,
    onBackClicked: () -> Unit,
) {
    val viewModel: GeneralSettingsViewModel = koinViewModel()
    val contentProvider: GeneralSettingsContentProvider = koinInject()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

    val firebaseController: FirebaseController = koinInject()
    TrackScreenView(
        firebaseController = firebaseController,
        screenName = GENERAL_SETTINGS_SCREEN_NAME,
        screenClass = GENERAL_SETTINGS_SCREEN_CLASS,
    )

    LaunchedEffect(contentKey) {
        viewModel.onEvent(GeneralSettingsEvent.Load(contentKey = contentKey))
    }

    LargeTopAppBarWithScaffold(
        title = title,
        onBackClicked = onBackClicked,
        snackbarHostState = snackbarHostState,
    ) { paddingValues: PaddingValues ->
        val screenState: UiStateScreen<GeneralSettingsUiState> by viewModel.uiState.collectAsStateWithLifecycle()

        TrackScreenState(
            firebaseController = firebaseController,
            screenName = GENERAL_SETTINGS_SCREEN_NAME,
            screenState = screenState.screenState,
        )

        GeneralSettingsContent(
            screenState = screenState,
            contentProvider = contentProvider,
            paddingValues = paddingValues,
            snackbarHostState = snackbarHostState,
        )
    }
}

@Composable
fun GeneralSettingsContent(
    screenState: UiStateScreen<GeneralSettingsUiState>,
    contentProvider: GeneralSettingsContentProvider,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
) {
    ScreenStateHandler(
        screenState = screenState,
        onLoading = { LoadingScreen() },
        onEmpty = { NoDataScreen(paddingValues = paddingValues) },
        onSuccess = { data: GeneralSettingsUiState ->
            contentProvider.ProvideContent(
                contentKey = data.contentKey,
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
            )
        },
    )
}