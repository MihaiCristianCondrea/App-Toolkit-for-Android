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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.ads.ui.state.AdsSettingsUiState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.platform.ConsentFormHelper
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/** Compose screen displaying ad preferences. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdsSettingsScreen() {
    val viewModel: AdsSettingsViewModel = koinViewModel()
    val screenState: UiStateScreen<AdsSettingsUiState> by viewModel.uiState.collectAsStateWithLifecycle()

    val activity = LocalActivity.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val consentInfo = remember(context) {
        UserMessagingPlatform.getConsentInformation(context)
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
                            title = stringResource(id = R.string.display_ads),
                            switchState = rememberUpdatedState(data.adsEnabled)
                        ) { isChecked: Boolean ->
                            viewModel.onEvent(AdsSettingsEvent.SetAdsEnabled(isChecked))
                        }
                    }

                    item {
                        Box(modifier = Modifier.padding(horizontal = SizeConstants.SmallSize)) {
                            PreferenceItem(
                                title = stringResource(id = R.string.personalized_ads),
                                enabled = data.adsEnabled,
                                summary = stringResource(id = R.string.summary_ads_personalized_ads),
                                onClick = {
                                    activity?.let {
                                        coroutineScope.launch {
                                            ConsentFormHelper.showConsentForm(
                                                activity = activity,
                                                consentInfo = consentInfo
                                            )
                                        }
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
