package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.components.ConsentSectionHeader
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.components.ConsentToggleCard
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.components.ExpandableConsentSectionHeader
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.contract.UsageAndDiagnosticsEvent
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.state.UsageAndDiagnosticsUiState
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.SwitchCardItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UsageAndDiagnosticsList(
    paddingValues: PaddingValues,
) {
    val viewModel: UsageAndDiagnosticsViewModel = koinViewModel()
    val screenState: UiStateScreen<UsageAndDiagnosticsUiState> =
        viewModel.uiState.collectAsStateWithLifecycle().value
    val uiState = screenState.data ?: UsageAndDiagnosticsUiState()

    var advancedSettingsExpanded by remember { mutableStateOf(false) }

    LazyColumn(contentPadding = paddingValues, modifier = Modifier.fillMaxSize()) {
        item {
            val usageState = rememberUpdatedState(newValue = uiState.usageAndDiagnostics)
            SwitchCardItem(
                title = stringResource(id = R.string.usage_and_diagnostics),
                switchState = usageState,
            ) { isChecked ->
                viewModel.onEvent(UsageAndDiagnosticsEvent.SetUsageAndDiagnostics(isChecked))
            }
        }

        item {
            ExpandableConsentSectionHeader(
                title = stringResource(id = R.string.advanced_privacy_settings),
                expanded = advancedSettingsExpanded,
                onToggle = { advancedSettingsExpanded = !advancedSettingsExpanded },
            )
        }

        item {
            AnimatedVisibility(
                visible = advancedSettingsExpanded,
                enter = expandVertically(expandFrom = Alignment.Top),
                exit = shrinkVertically(shrinkTowards = Alignment.Top),
            ) {
                Column(modifier = Modifier.padding(horizontal = SizeConstants.SmallSize)) {
                    ConsentSectionHeader(title = stringResource(id = R.string.consent_category_analytics_title))
                    ConsentToggleCard(
                        title = stringResource(id = R.string.consent_analytics_storage_title),
                        description = stringResource(id = R.string.consent_analytics_storage_description),
                        switchState = uiState.analyticsConsent,
                        icon = Icons.Outlined.Analytics,
                        onCheckedChange = { isChecked ->
                            viewModel.onEvent(UsageAndDiagnosticsEvent.SetAnalyticsConsent(isChecked))
                        },
                    )

                    SmallVerticalSpacer()

                    ConsentSectionHeader(title = stringResource(id = R.string.consent_category_advertising_title))
                    ConsentToggleCard(
                        title = stringResource(id = R.string.consent_ad_storage_title),
                        description = stringResource(id = R.string.consent_ad_storage_description),
                        switchState = uiState.adStorageConsent,
                        icon = Icons.Outlined.Storage,
                        onCheckedChange = { isChecked ->
                            viewModel.onEvent(UsageAndDiagnosticsEvent.SetAdStorageConsent(isChecked))
                        },
                    )

                    SmallVerticalSpacer()

                    ConsentToggleCard(
                        title = stringResource(id = R.string.consent_ad_user_data_title),
                        description = stringResource(id = R.string.consent_ad_user_data_description),
                        switchState = uiState.adUserDataConsent,
                        icon = Icons.AutoMirrored.Outlined.Send,
                        onCheckedChange = { isChecked ->
                            viewModel.onEvent(UsageAndDiagnosticsEvent.SetAdUserDataConsent(isChecked))
                        },
                    )

                    SmallVerticalSpacer()

                    ConsentToggleCard(
                        title = stringResource(id = R.string.consent_ad_personalization_title),
                        description = stringResource(id = R.string.consent_ad_personalization_description),
                        switchState = uiState.adPersonalizationConsent,
                        icon = Icons.Outlined.Campaign,
                        onCheckedChange = { isChecked ->
                            viewModel.onEvent(UsageAndDiagnosticsEvent.SetAdPersonalizationConsent(isChecked))
                        },
                    )
                }
            }
        }

        item {
            InfoMessageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = SizeConstants.MediumSize * 2),
                message = stringResource(id = R.string.summary_usage_and_diagnostics),
                learnMoreText = stringResource(id = R.string.learn_more),
                learnMoreUrl = AppLinks.PRIVACY_POLICY,
            )
        }
    }
}
