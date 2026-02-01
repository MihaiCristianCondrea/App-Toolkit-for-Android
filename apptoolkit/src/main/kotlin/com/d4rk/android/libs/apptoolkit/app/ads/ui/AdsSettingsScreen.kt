package com.d4rk.android.libs.apptoolkit.app.ads.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.ads.ui.state.AdsSettingsUiState
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val ADS_SETTINGS_SCREEN_NAME = "AdsSettings"
private const val ADS_SETTINGS_SCREEN_CLASS = "AdsSettingsScreen"

/** Compose screen displaying ad preferences. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdsSettingsScreen() {
    val viewModel: AdsSettingsViewModel = koinViewModel()
    val screenState: UiStateScreen<AdsSettingsUiState> by viewModel.uiState.collectAsStateWithLifecycle()

    val firebaseController: FirebaseController = koinInject()

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = ADS_SETTINGS_SCREEN_NAME,
        screenClass = ADS_SETTINGS_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = ADS_SETTINGS_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    val activity = LocalActivity.current
    val consentHost = remember(activity) {
        activity?.let {
            object : ConsentHost {
                override val activity = it
            }
        }
    }

    LargeTopAppBarWithScaffold(
        title = stringResource(id = R.string.ads),
        onBackClicked = remember(activity) { { activity?.finish() } }
    ) { paddingValues: PaddingValues ->
        ScreenStateHandler(
            screenState = screenState,
            onLoading = { LoadingScreen() },
            onEmpty = { NoDataScreen(paddingValues = paddingValues) },
            onError = { NoDataScreen(isError = true, paddingValues = paddingValues) },
            onSuccess = { data: AdsSettingsUiState ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = paddingValues)
                ) {
                    item {
                        SwitchCardItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = SizeConstants.MediumSize * 2),
                            title = stringResource(id = R.string.display_ads),
                            switchState = rememberUpdatedState(data.adsEnabled),
                            onSwitchToggled = { isChecked: Boolean ->
                                viewModel.onEvent(AdsSettingsEvent.SetAdsEnabled(isChecked))
                            }
                        )
                    }

                    item {
                        Box(modifier = Modifier.padding(horizontal = SizeConstants.SmallSize)) {
                            PreferenceItem(
                                title = stringResource(id = R.string.personalized_ads),
                                enabled = data.adsEnabled,
                                summary = stringResource(id = R.string.summary_ads_personalized_ads),
                                onClick = {
                                    consentHost?.let { host ->
                                        viewModel.onEvent(AdsSettingsEvent.RequestConsent(host))
                                    }
                                }
                            )
                        }
                    }

                    item {
                        InfoMessageSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = SizeConstants.MediumSize * 2),
                            message = stringResource(id = R.string.summary_ads),
                            learnMoreText = stringResource(id = R.string.learn_more),
                            learnMoreUrl = AppLinks.ADS_HELP_CENTER
                        )
                    }
                }
            }
        )
    }
}