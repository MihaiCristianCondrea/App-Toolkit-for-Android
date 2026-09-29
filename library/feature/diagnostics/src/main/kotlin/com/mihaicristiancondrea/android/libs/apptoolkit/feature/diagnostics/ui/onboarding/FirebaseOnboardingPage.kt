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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.onboarding

import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ExtraLargeIncreasedVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ExtraLargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.UsageAndDiagnosticsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.contracts.UsageAndDiagnosticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.states.UsageAndDiagnosticsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.onboarding.cards.UsageAndDiagnosticsToggleCard
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.views.dialogs.FirebaseConsentDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.onboarding.text.PrivacyPolicySection
import org.koin.compose.viewmodel.koinViewModel

/**
 * The usage and diagnostics page of onboarding: the consent toggle, the detailed consent dialog
 * and the privacy policy link.
 *
 * An app adds it to its onboarding as an `OnboardingPage.CustomPage` from its `OnboardingProvider`.
 * It lives here, with the usage and diagnostics settings it edits, so onboarding does not depend on
 * this feature.
 *
 * @param isSelected Whether this page is currently selected in the onboarding pager; the details
 *   dialog only shows on the selected page.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FirebaseOnboardingPage(isSelected: Boolean) {
    val diagnosticsViewModel: UsageAndDiagnosticsViewModel = koinViewModel()
    // Shown the first time the page is reached, as onboarding always has; saved across rotation.
    var isDetailsDialogVisible by rememberSaveable { mutableStateOf(true) }

    val diagnosticsState by diagnosticsViewModel.uiState.collectAsStateWithLifecycle()
    val diagnosticsUiState = diagnosticsState.data ?: UsageAndDiagnosticsUiState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SizeConstants.LargeSize)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Outlined.Analytics,
                contentDescription = null,
                modifier = Modifier.size(size = SizeConstants.LauncherIconSize + SizeConstants.LargeSize),
                tint = MaterialTheme.colorScheme.primary
            )

            ExtraLargeVerticalSpacer()

            Text(
                text = stringResource(R.string.onboarding_crashlytics_title),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            LargeVerticalSpacer()

            Text(
                text = stringResource(R.string.onboarding_crashlytics_description),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = SizeConstants.LargeSize)
            )

            ExtraLargeIncreasedVerticalSpacer()
            SmallVerticalSpacer()

            UsageAndDiagnosticsToggleCard(
                switchState = diagnosticsUiState.usageAndDiagnostics,
                onCheckedChange = { isChecked ->
                    diagnosticsViewModel.onEvent(
                        UsageAndDiagnosticsEvent.SetUsageAndDiagnostics(isChecked)
                    )
                },
            )

            LargeVerticalSpacer()

            GeneralButton(
                style = GeneralButtonStyle.Outlined,
                onClick = {
                    isDetailsDialogVisible = true
                },
                modifier = Modifier.fillMaxWidth(),
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.PrivacyTip),
                contentDescription = stringResource(id = R.string.onboarding_crashlytics_show_details_button_cd),
                label = stringResource(id = R.string.onboarding_crashlytics_show_details_button)
            )

            LargeVerticalSpacer()

            PrivacyPolicySection()
        }
    }

    if (isSelected && isDetailsDialogVisible) {
        FirebaseConsentDialog(
            state = diagnosticsUiState,
            onDismissRequest = {
                isDetailsDialogVisible = false
            },
            onAllowAll = {
                diagnosticsViewModel.onEvent(UsageAndDiagnosticsEvent.AllowAllConsent)
                isDetailsDialogVisible = false
            },
            onAllowEssentials = {
                diagnosticsViewModel.onEvent(UsageAndDiagnosticsEvent.AllowEssentialConsent)
                isDetailsDialogVisible = false
            },
            onConfirmSelection = {
                isDetailsDialogVisible = false
            },

            onAnalyticsConsentChanged = {
                diagnosticsViewModel.onEvent(UsageAndDiagnosticsEvent.SetAnalyticsConsent(it))
            },
            onAdStorageConsentChanged = {
                diagnosticsViewModel.onEvent(UsageAndDiagnosticsEvent.SetAdStorageConsent(it))
            },
            onAdUserDataConsentChanged = {
                diagnosticsViewModel.onEvent(UsageAndDiagnosticsEvent.SetAdUserDataConsent(it))
            },
            onAdPersonalizationConsentChanged = {
                diagnosticsViewModel.onEvent(UsageAndDiagnosticsEvent.SetAdPersonalizationConsent(it))
            },
        )
    }
}

