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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.views.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.links.AppLinks
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.data.models.UsageAndDiagnosticsSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.analytics.diagnosticsPrivacyChoicesEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.analytics.diagnosticsToggleEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.contracts.UsageAndDiagnosticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

/**
 * Renders the usage and diagnostics switch card, privacy choices entry, and policy summary.
 */
@Composable
internal fun UsageAndDiagnosticsList(
    settings: UsageAndDiagnosticsSettings,
    onEvent: (UsageAndDiagnosticsEvent) -> Unit,
    onOpenPrivacyChoices: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(space = MaterialTheme.spacing.large),
    ) {
        animatedItem {
            LargeVerticalSpacer()
        }

        animatedItem {
            SwitchCardItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.large),
                title = stringResource(id = R.string.usage_and_diagnostics),
                switchState = rememberUpdatedState(newValue = settings.usageAndDiagnostics),
                onSwitchToggled = { isChecked ->
                    onEvent(UsageAndDiagnosticsEvent.SetUsageAndDiagnostics(enabled = isChecked))
                },
                ga4EventProvider = { isChecked ->
                    diagnosticsToggleEvent(enabled = isChecked)
                },
            )
        }

        animatedItem {
            Box(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small)) {
                PreferenceItem(
                    title = stringResource(id = R.string.advanced_privacy_settings),
                    summary = stringResource(id = R.string.summary_advanced_privacy_settings),
                    onClick = onOpenPrivacyChoices,
                    ga4Event = diagnosticsPrivacyChoicesEvent(),
                )
            }
        }

        animatedItem {
            InfoMessageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.extraLargeCompact),
                message = stringResource(id = R.string.summary_usage_and_diagnostics),
                learnMoreText = stringResource(id = CoreUiR.string.learn_more),
                learnMoreUrl = AppLinks.PRIVACY_POLICY,
            )
        }
    }
}
