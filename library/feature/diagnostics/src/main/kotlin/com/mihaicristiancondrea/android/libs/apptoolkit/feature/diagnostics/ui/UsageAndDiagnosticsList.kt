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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.links.AppLinks
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.contracts.UsageAndDiagnosticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.states.UsageAndDiagnosticsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.views.dialogs.FirebaseConsentDialog
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val USAGE_DIAGNOSTICS_SCREEN_NAME = "UsageAndDiagnostics"
private const val USAGE_DIAGNOSTICS_SCREEN_CLASS = "UsageAndDiagnosticsList"

private object UsageAndDiagnosticsPreferenceKeys {
    const val USAGE_AND_DIAGNOSTICS: String = "usage_and_diagnostics"
    const val PRIVACY_CHOICES: String = "advanced_privacy_settings"
}

/**
 * Usage and diagnostics settings: the reporting switch, and the privacy choices behind it.
 *
 * The layout is the ads screen's, deliberately. Both screens are one switch over a single
 * preference that opens the consent surface belonging to it (such as the AdMob consent form,
 * or the toolkit's own privacy dialog) so the two settings screens that ask the same kind of
 * question now look like each other.
 *
 * The granular consents used to sit on this screen as an expandable block of switch cards. They are
 * the four the dialog already explains one tab away, so the block was a second, plainer copy of a
 * surface the onboarding flow presents better; opening that dialog from here replaces it, and the
 * choices made here and during onboarding are visibly the same choices.
 *
 * @param paddingValues The padding to apply to the content of the list, typically provided by a Scaffold.
 */
@Composable
fun UsageAndDiagnosticsList(
    paddingValues: PaddingValues,
) {
    val viewModel: UsageAndDiagnosticsViewModel = koinViewModel()
    val screenState: UiStateScreen<UsageAndDiagnosticsUiState> by viewModel.uiState.collectAsStateWithLifecycle()
    val uiState: UsageAndDiagnosticsUiState = screenState.data ?: UsageAndDiagnosticsUiState()

    val firebaseController: FirebaseController = koinInject()

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = USAGE_DIAGNOSTICS_SCREEN_NAME,
        screenClass = USAGE_DIAGNOSTICS_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = USAGE_DIAGNOSTICS_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    var privacyChoicesVisible: Boolean by rememberSaveable { mutableStateOf(value = false) }

    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
    ) {
        item {
            LargeVerticalSpacer()
        }

        item {
            SwitchCardItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SizeConstants.LargeSize),
                title = stringResource(id = R.string.usage_and_diagnostics),
                switchState = rememberUpdatedState(newValue = uiState.usageAndDiagnostics),
                onSwitchToggled = { isChecked ->
                    viewModel.onEvent(UsageAndDiagnosticsEvent.SetUsageAndDiagnostics(isChecked))
                },
                firebaseController = firebaseController,
                ga4EventProvider = { isChecked ->
                    Ga4EventData(
                        name = SettingsAnalytics.Events.PREFERENCE_TOGGLE,
                        params = mapOf(
                            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(
                                USAGE_DIAGNOSTICS_SCREEN_NAME
                            ),
                            SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(
                                UsageAndDiagnosticsPreferenceKeys.USAGE_AND_DIAGNOSTICS
                            ),
                            SettingsAnalytics.Params.ENABLED to AnalyticsValue.Str(isChecked.toString()),
                        ),
                    )
                }
            )
        }

        item {
            Box(modifier = Modifier.padding(horizontal = SizeConstants.SmallSize)) {
                PreferenceItem(
                    title = stringResource(id = R.string.advanced_privacy_settings),
                    summary = stringResource(id = R.string.summary_advanced_privacy_settings),
                    onClick = { privacyChoicesVisible = true },
                    firebaseController = firebaseController,
                    ga4Event = Ga4EventData(
                        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
                        params = mapOf(
                            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(
                                USAGE_DIAGNOSTICS_SCREEN_NAME
                            ),
                            SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(
                                UsageAndDiagnosticsPreferenceKeys.PRIVACY_CHOICES
                            ),
                        ),
                    )
                )
            }
        }

        item {
            InfoMessageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SizeConstants.MediumSize * 2),
                message = stringResource(id = R.string.summary_usage_and_diagnostics),
                learnMoreText = stringResource(id = R.string.learn_more),
                learnMoreUrl = AppLinks.PRIVACY_POLICY,
            )
        }
    }

    if (privacyChoicesVisible) {
        FirebaseConsentDialog(
            state = uiState,
            onDismissRequest = { privacyChoicesVisible = false },
            onAllowAll = {
                viewModel.onEvent(UsageAndDiagnosticsEvent.AllowAllConsent)
                privacyChoicesVisible = false
            },
            onAllowEssentials = {
                viewModel.onEvent(UsageAndDiagnosticsEvent.AllowEssentialConsent)
                privacyChoicesVisible = false
            },
            onConfirmSelection = { privacyChoicesVisible = false },
            onAnalyticsConsentChanged = {
                viewModel.onEvent(UsageAndDiagnosticsEvent.SetAnalyticsConsent(it))
            },
            onAdStorageConsentChanged = {
                viewModel.onEvent(UsageAndDiagnosticsEvent.SetAdStorageConsent(it))
            },
            onAdUserDataConsentChanged = {
                viewModel.onEvent(UsageAndDiagnosticsEvent.SetAdUserDataConsent(it))
            },
            onAdPersonalizationConsentChanged = {
                viewModel.onEvent(UsageAndDiagnosticsEvent.SetAdPersonalizationConsent(it))
            },
        )
    }
}
