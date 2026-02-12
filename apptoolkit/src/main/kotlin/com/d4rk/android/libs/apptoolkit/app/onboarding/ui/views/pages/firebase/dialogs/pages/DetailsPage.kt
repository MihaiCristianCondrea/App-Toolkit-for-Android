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

package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.firebase.dialogs.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.state.UsageAndDiagnosticsUiState
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.firebase.dialogs.ConsentExpandableItemCard
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openUrl

@Composable
fun DetailsPage(
    state: UsageAndDiagnosticsUiState,
    onAnalyticsConsentChanged: (Boolean) -> Unit,
    onAdStorageConsentChanged: (Boolean) -> Unit,
    onAdUserDataConsentChanged: (Boolean) -> Unit,
    onAdPersonalizationConsentChanged: (Boolean) -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
    ) {
        Text(
            text = stringResource(R.string.onboarding_crashlytics_details_intro),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )

        SmallVerticalSpacer()

        ConsentExpandableItemCard(
            title = stringResource(R.string.consent_analytics_storage_title),
            summary = stringResource(R.string.consent_analytics_storage_description_short),
            details = stringResource(R.string.consent_analytics_storage_description),
            icon = Icons.Outlined.Analytics,
            checked = state.analyticsConsent,
            onCheckedChange = onAnalyticsConsentChanged,
            onLearnMoreClick = { url -> context.openUrl(url) }
        )

        ConsentExpandableItemCard(
            title = stringResource(R.string.consent_ad_storage_title),
            summary = stringResource(R.string.consent_ad_storage_description_short),
            details = stringResource(R.string.consent_ad_storage_description),
            icon = Icons.Outlined.Storage,
            checked = state.adStorageConsent,
            onCheckedChange = onAdStorageConsentChanged,
            onLearnMoreClick = { url -> context.openUrl(url) }
        )

        ConsentExpandableItemCard(
            title = stringResource(R.string.consent_ad_user_data_title),
            summary = stringResource(R.string.consent_ad_user_data_description_short),
            details = stringResource(R.string.consent_ad_user_data_description),
            icon = Icons.Outlined.Info,
            checked = state.adUserDataConsent,
            onCheckedChange = onAdUserDataConsentChanged,
            onLearnMoreClick = { url -> context.openUrl(url) }
        )

        ConsentExpandableItemCard(
            title = stringResource(R.string.consent_ad_personalization_title),
            summary = stringResource(R.string.consent_ad_personalization_description_short),
            details = stringResource(R.string.consent_ad_personalization_description),
            icon = Icons.Outlined.Campaign,
            checked = state.adPersonalizationConsent,
            onCheckedChange = onAdPersonalizationConsentChanged,
            onLearnMoreClick = { url -> context.openUrl(url) }
        )
    }
}
