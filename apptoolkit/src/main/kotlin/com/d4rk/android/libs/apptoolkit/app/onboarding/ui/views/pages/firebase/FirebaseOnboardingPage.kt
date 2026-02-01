package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.firebase

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
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.UsageAndDiagnosticsViewModel
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.contract.UsageAndDiagnosticsEvent
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.state.UsageAndDiagnosticsUiState
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.OnboardingViewModel
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingEvent
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.state.OnboardingUiState
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.firebase.cards.UsageAndDiagnosticsToggleCard
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.firebase.dialogs.FirebaseConsentDialog
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.firebase.text.PrivacyPolicySection
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraLargeIncreasedVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraLargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FirebaseOnboardingPage() {
    val onboardingViewModel: OnboardingViewModel = koinViewModel()
    val diagnosticsViewModel: UsageAndDiagnosticsViewModel = koinViewModel()

    val onboardingState by onboardingViewModel.uiState.collectAsStateWithLifecycle()
    val onboardingUiState = onboardingState.data ?: OnboardingUiState()

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
                modifier = Modifier.size(size = SizeConstants.ExtraExtraLargeSize + SizeConstants.LargeSize),
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

            GeneralOutlinedButton(
                onClick = {
                    onboardingViewModel.onEvent(OnboardingEvent.ShowCrashlyticsDialog)
                },
                modifier = Modifier.fillMaxWidth(),
                vectorIcon = Icons.Outlined.PrivacyTip,
                iconContentDescription = stringResource(id = R.string.onboarding_crashlytics_show_details_button_cd),
                label = stringResource(id = R.string.onboarding_crashlytics_show_details_button)
            )

            LargeVerticalSpacer()

            PrivacyPolicySection()
        }
    }

    if (onboardingUiState.isCrashlyticsDialogVisible) {
        FirebaseConsentDialog(
            state = diagnosticsUiState,
            onDismissRequest = {
                onboardingViewModel.onEvent(OnboardingEvent.HideCrashlyticsDialog)
            },
            onApplyConsents = { analyticsStorage, adStorage, adUserData, adPersonalization ->
                diagnosticsViewModel.onEvent(
                    UsageAndDiagnosticsEvent.SetAnalyticsConsent(
                        analyticsStorage
                    )
                )
                diagnosticsViewModel.onEvent(UsageAndDiagnosticsEvent.SetAdStorageConsent(adStorage))
                diagnosticsViewModel.onEvent(
                    UsageAndDiagnosticsEvent.SetAdUserDataConsent(
                        adUserData
                    )
                )
                diagnosticsViewModel.onEvent(
                    UsageAndDiagnosticsEvent.SetAdPersonalizationConsent(
                        adPersonalization
                    )
                )
                val shouldEnableUsage: Boolean = analyticsStorage
                diagnosticsViewModel.onEvent(
                    UsageAndDiagnosticsEvent.SetUsageAndDiagnostics(
                        shouldEnableUsage
                    )
                )
                onboardingViewModel.onEvent(OnboardingEvent.HideCrashlyticsDialog)
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
