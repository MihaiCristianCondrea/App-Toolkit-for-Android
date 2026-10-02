/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui

import android.content.Context
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.links.AppLinks
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.R
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.contracts.AdsSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.models.AdsPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.states.AdsSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val ADS_SETTINGS_SCREEN_NAME = "AdsSettings"
private const val ADS_SETTINGS_SCREEN_CLASS = "AdsSettingsScreen"

private object AdsPreferenceKeys {
    const val DISPLAY_ADS: String = "display_ads"
    const val REDUCE_ADS: String = "reduce_ads"
    const val PERSONALIZED_ADS: String = "personalized_ads"
    const val LEARN_MORE: String = "learn_more"
}

/**
 * The ad preferences page, which `adsSettingsPage()` registers and the privacy page opens. Debug
 * builds show the display ads switch, release builds the reduce ads switch.
 *
 * Owns [AdsSettingsViewModel], tracking and its messages, such as a failed write or consent
 * request. The personalized ads row opens the UMP privacy form from the current activity, and
 * Learn more opens the AdMob help center.
 */
@Composable
fun AdsSettingsScreen() {
    val viewModel: AdsSettingsViewModel = koinViewModel()
    val state: AdsSettingsUiState by viewModel.state.collectAsStateWithLifecycle()
    val telemetryRepository = LocalTelemetry.current
    val buildInfoProvider: BuildInfoProvider = koinInject()
    val context: Context = LocalContext.current
    val activity = LocalActivity.current
    val consentHost: ConsentHost? = remember(activity) { activity?.let(::ConsentHost) }

    TrackScreenView(
        screenName = ADS_SETTINGS_SCREEN_NAME,
        screenClass = ADS_SETTINGS_SCREEN_CLASS,
    )

    TrackScreenState(
        screenName = ADS_SETTINGS_SCREEN_NAME,
        state = state.preferences,
    )

    AdsSettingsScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        showDisplayAdsSwitch = buildInfoProvider.isDebugBuild,
        onOpenPrivacyForm = {
            consentHost?.let { host -> viewModel.onEvent(AdsSettingsEvent.RequestConsent(host)) }
        },
        onLearnMore = {
            telemetryRepository.logEvent(
                AnalyticsEvent(
                    name = SettingsAnalytics.Events.PREFERENCE_VIEW,
                    params = adsPreferenceParams(preferenceKey = AdsPreferenceKeys.LEARN_MORE),
                )
            )
            context.openUrl(url = AppLinks.ADS_HELP_CENTER)
        },
        contentPadding = contentPadding(),
    )

    MessageHost(viewModel = viewModel)
}

/**
 * The ads switch, the personalized ads row and the ads note for [state], or its loading or
 * failure state.
 *
 * @param onEvent Receives the events [AdsSettingsViewModel] handles.
 * @param showDisplayAdsSwitch Shows the display ads switch instead of the reduce ads switch.
 * @param onOpenPrivacyForm The personalized ads row was tapped.
 * @param onLearnMore The note's Learn more was tapped.
 * @param contentPadding Padding from the shell, applied inside the list and the state screens.
 */
@Composable
internal fun AdsSettingsScreenContent(
    state: AdsSettingsUiState,
    onEvent: (AdsSettingsEvent) -> Unit,
    showDisplayAdsSwitch: Boolean,
    onOpenPrivacyForm: () -> Unit,
    onLearnMore: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    ScreenStateHandler(
        state = state.preferences,
        modifier = modifier,
        contentPadding = contentPadding,
        onRetry = { onEvent(AdsSettingsEvent.Load) },
    ) { ready ->
        AdsSettingsList(
            preferences = ready.value,
            onEvent = onEvent,
            showDisplayAdsSwitch = showDisplayAdsSwitch,
            onOpenPrivacyForm = onOpenPrivacyForm,
            onLearnMore = onLearnMore,
            contentPadding = contentPadding,
        )
    }
}

@Composable
private fun AdsSettingsList(
    preferences: AdsPreferences,
    onEvent: (AdsSettingsEvent) -> Unit,
    showDisplayAdsSwitch: Boolean,
    onOpenPrivacyForm: () -> Unit,
    onLearnMore: () -> Unit,
    contentPadding: PaddingValues,
) {
    val switchKey: String = if (showDisplayAdsSwitch) AdsPreferenceKeys.DISPLAY_ADS else AdsPreferenceKeys.REDUCE_ADS

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.LargeSize),
    ) {
        item {
            SwitchCardItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SizeConstants.LargeSize),
                title = stringResource(id = if (showDisplayAdsSwitch) R.string.display_ads else R.string.reduce_ads),
                switchState = rememberUpdatedState(
                    newValue = if (showDisplayAdsSwitch) preferences.adsEnabled else preferences.reduceAds,
                ),
                onSwitchToggled = { isChecked: Boolean ->
                    onEvent(
                        if (showDisplayAdsSwitch) {
                            AdsSettingsEvent.SetAdsEnabled(isChecked)
                        } else {
                            AdsSettingsEvent.SetReduceAds(isChecked)
                        },
                    )
                },
                ga4EventProvider = { isChecked ->
                    Ga4EventData(
                        name = SettingsAnalytics.Events.PREFERENCE_TOGGLE,
                        params = adsPreferenceParams(preferenceKey = switchKey) +
                            (SettingsAnalytics.Params.ENABLED to AnalyticsValue.Str(isChecked.toString())),
                    )
                },
            )
        }

        item {
            Box(modifier = Modifier.padding(horizontal = SizeConstants.SmallSize)) {
                PreferenceItem(
                    title = stringResource(id = R.string.personalized_ads),
                    enabled = preferences.adsEnabled,
                    summary = stringResource(id = R.string.summary_ads_personalized_ads),
                    onClick = onOpenPrivacyForm,
                    ga4Event = Ga4EventData(
                        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
                        params = adsPreferenceParams(preferenceKey = AdsPreferenceKeys.PERSONALIZED_ADS),
                    ),
                )
            }
        }

        item {
            InfoMessageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SizeConstants.MediumSize * 2),
                message = stringResource(id = R.string.summary_ads),
                learnMoreText = stringResource(id = R.string.learn_more),
                learnMoreAction = onLearnMore,
            )
        }
    }
}

/** The `screen` and `preference_key` parameters every ads settings GA4 event carries. */
private fun adsPreferenceParams(preferenceKey: String): Map<String, AnalyticsValue> = mapOf(
    SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(ADS_SETTINGS_SCREEN_NAME),
    SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(preferenceKey),
)

@Preview(showBackground = true)
@Composable
private fun AdsSettingsScreenContentPreview() {
    MaterialTheme {
        AdsSettingsScreenContent(
            state = AdsSettingsUiState(
                preferences = Loadable.Ready(AdsPreferences(adsEnabled = true, reduceAds = false)),
            ),
            onEvent = {},
            showDisplayAdsSwitch = false,
            onOpenPrivacyForm = {},
            onLearnMore = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AdsSettingsScreenContentLoadingPreview() {
    MaterialTheme {
        AdsSettingsScreenContent(
            state = AdsSettingsUiState(preferences = Loadable.Loading),
            onEvent = {},
            showDisplayAdsSwitch = false,
            onOpenPrivacyForm = {},
            onLearnMore = {},
        )
    }
}
