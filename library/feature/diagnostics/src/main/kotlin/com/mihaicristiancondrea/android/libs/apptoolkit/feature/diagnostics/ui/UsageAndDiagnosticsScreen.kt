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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.links.AppLinks
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.domain.models.UsageAndDiagnosticsSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.contracts.UsageAndDiagnosticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.states.UsageAndDiagnosticsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.views.dialogs.FirebaseConsentDialog
import org.koin.compose.viewmodel.koinViewModel

private const val USAGE_DIAGNOSTICS_SCREEN_NAME = "UsageAndDiagnostics"
private const val USAGE_DIAGNOSTICS_SCREEN_CLASS = "UsageAndDiagnosticsScreen"

private object UsageAndDiagnosticsPreferenceKeys {
    const val USAGE_AND_DIAGNOSTICS: String = "usage_and_diagnostics"
    const val PRIVACY_CHOICES: String = "advanced_privacy_settings"
}

/**
 * Usage and diagnostics settings: the reporting switch, and a row that opens
 * [FirebaseConsentDialog], the same privacy choices onboarding shows. The layout matches the ads
 * settings screen, which is also one switch over the consent surface it belongs to.
 *
 * Owns [UsageAndDiagnosticsViewModel], tracking and its messages, such as a failed write.
 */
@Composable
fun UsageAndDiagnosticsScreen() {
    val viewModel: UsageAndDiagnosticsViewModel = koinViewModel()
    val state: UsageAndDiagnosticsUiState by viewModel.state.collectAsStateWithLifecycle()

    TrackScreenView(
        screenName = USAGE_DIAGNOSTICS_SCREEN_NAME,
        screenClass = USAGE_DIAGNOSTICS_SCREEN_CLASS,
    )

    TrackScreenState(
        screenName = USAGE_DIAGNOSTICS_SCREEN_NAME,
        state = state.settings,
    )

    UsageAndDiagnosticsScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        contentPadding = contentPadding(),
    )

    MessageHost(viewModel = viewModel)
}

/**
 * The reporting switch, the privacy choices row and the privacy note for [state], or its loading
 * or failure state. The privacy choices dialog's visibility is saved here, so it survives
 * rotation.
 *
 * @param onEvent Receives the events [UsageAndDiagnosticsViewModel] handles.
 * @param contentPadding Padding from the shell, applied inside the list and the state screens.
 */
@Composable
internal fun UsageAndDiagnosticsScreenContent(
    state: UsageAndDiagnosticsUiState,
    onEvent: (UsageAndDiagnosticsEvent) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    var privacyChoicesVisible: Boolean by rememberSaveable { mutableStateOf(value = false) }

    ScreenStateHandler(
        state = state.settings,
        modifier = modifier,
        contentPadding = contentPadding,
        onRetry = { onEvent(UsageAndDiagnosticsEvent.Load) },
    ) { ready ->
        val settings: UsageAndDiagnosticsSettings = ready.value

        UsageAndDiagnosticsList(
            settings = settings,
            onEvent = onEvent,
            onOpenPrivacyChoices = { privacyChoicesVisible = true },
            contentPadding = contentPadding,
        )

        if (privacyChoicesVisible) {
            FirebaseConsentDialog(
                settings = settings,
                onDismissRequest = { privacyChoicesVisible = false },
                onAllowAll = {
                    onEvent(UsageAndDiagnosticsEvent.AllowAllConsent)
                    privacyChoicesVisible = false
                },
                onAllowEssentials = {
                    onEvent(UsageAndDiagnosticsEvent.AllowEssentialConsent)
                    privacyChoicesVisible = false
                },
                onConfirmSelection = { privacyChoicesVisible = false },
                onAnalyticsConsentChanged = {
                    onEvent(UsageAndDiagnosticsEvent.SetAnalyticsConsent(it))
                },
                onAdStorageConsentChanged = {
                    onEvent(UsageAndDiagnosticsEvent.SetAdStorageConsent(it))
                },
                onAdUserDataConsentChanged = {
                    onEvent(UsageAndDiagnosticsEvent.SetAdUserDataConsent(it))
                },
                onAdPersonalizationConsentChanged = {
                    onEvent(UsageAndDiagnosticsEvent.SetAdPersonalizationConsent(it))
                },
            )
        }
    }
}

@Composable
private fun UsageAndDiagnosticsList(
    settings: UsageAndDiagnosticsSettings,
    onEvent: (UsageAndDiagnosticsEvent) -> Unit,
    onOpenPrivacyChoices: () -> Unit,
    contentPadding: PaddingValues,
) {
    LazyColumn(
        contentPadding = contentPadding,
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
                switchState = rememberUpdatedState(newValue = settings.usageAndDiagnostics),
                onSwitchToggled = { isChecked ->
                    onEvent(UsageAndDiagnosticsEvent.SetUsageAndDiagnostics(isChecked))
                },
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
                    onClick = onOpenPrivacyChoices,
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
}

@Preview(showBackground = true)
@Composable
private fun UsageAndDiagnosticsScreenContentPreview() {
    MaterialTheme {
        UsageAndDiagnosticsScreenContent(
            state = UsageAndDiagnosticsUiState(
                settings = Loadable.Ready(
                    UsageAndDiagnosticsSettings(
                        usageAndDiagnostics = true,
                        analyticsConsent = true,
                        adStorageConsent = true,
                        adUserDataConsent = false,
                        adPersonalizationConsent = false,
                    )
                ),
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsageAndDiagnosticsScreenContentLoadingPreview() {
    MaterialTheme {
        UsageAndDiagnosticsScreenContent(
            state = UsageAndDiagnosticsUiState(settings = Loadable.Loading),
            onEvent = {},
        )
    }
}
