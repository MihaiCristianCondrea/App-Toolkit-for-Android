package com.d4rk.android.libs.apptoolkit.app.ads.ui

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.ads.domain.actions.AdsSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.ads.domain.model.ui.UiAdsSettingsScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.components.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.PreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.SwitchCardItem
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ConsentFormHelper
import com.google.android.ump.ConsentInformation
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.launch

/** Compose screen displaying ad preferences. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdsSettingsScreen(
    activity: Activity,
    viewModel: AdsSettingsViewModel
) { // FIXME: Unstable parameter 'activity' prevents composable from being skippable && Parameter 'viewModel' has runtime-determined stability
    val screenState: UiStateScreen<UiAdsSettingsScreen> by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    LargeTopAppBarWithScaffold(
        title = stringResource(id = R.string.ads),
        onBackClicked = { activity.finish() }
    ) { paddingValues: PaddingValues ->
        ScreenStateHandler(
            screenState = screenState,
            onLoading = { LoadingScreen() },
            onEmpty = { NoDataScreen(paddingValues = paddingValues) },
            onError = { NoDataScreen(isError = true, paddingValues = paddingValues) },
            onSuccess = { data: UiAdsSettingsScreen ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = paddingValues)
                ) {
                    item {
                        SwitchCardItem(
                            title = stringResource(id = R.string.display_ads),
                            switchState = rememberUpdatedState(newValue = data.adsEnabled)
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
                                    coroutineScope.launch {
                                        val consentInfo: ConsentInformation = UserMessagingPlatform.getConsentInformation(activity)
                                        ConsentFormHelper.showConsentForm(activity = activity , consentInfo = consentInfo)
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
