package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.firebase.dialogs

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.state.UsageAndDiagnosticsUiState
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.MediumHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.switches.CustomSwitch
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun FirebaseConsentDialog(
    state: UsageAndDiagnosticsUiState,
    onDismissRequest: () -> Unit,
    onAcknowledge: (Boolean) -> Unit,
    onAnalyticsConsentChanged: (Boolean) -> Unit,
    onAdStorageConsentChanged: (Boolean) -> Unit,
    onAdUserDataConsentChanged: (Boolean) -> Unit,
    onAdPersonalizationConsentChanged: (Boolean) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false),
        icon = {
            Icon(
                imageVector = Icons.Outlined.Analytics,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.onboarding_crashlytics_title_dialog),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(state = rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_crashlytics_description_dialog),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(
                        horizontal = SizeConstants.SmallSize, vertical = SizeConstants.SmallSize
                    )
                )

                SmallVerticalSpacer()

                ConsentToggleItem(
                    title = stringResource(id = R.string.consent_analytics_storage_title),
                    description = stringResource(id = R.string.consent_analytics_storage_description_short),
                    icon = Icons.Outlined.Analytics,
                    switchState = state.analyticsConsent,
                    onCheckedChange = {
                        onAnalyticsConsentChanged(it)
                    },
                )
                SmallVerticalSpacer()
                ConsentToggleItem(
                    title = stringResource(id = R.string.consent_ad_storage_title),
                    description = stringResource(id = R.string.consent_ad_storage_description_short),
                    icon = Icons.Outlined.Storage,
                    switchState = state.adStorageConsent,
                    onCheckedChange = {
                        onAdStorageConsentChanged(it)
                    },
                )
                SmallVerticalSpacer()
                ConsentToggleItem(
                    title = stringResource(id = R.string.consent_ad_user_data_title),
                    description = stringResource(id = R.string.consent_ad_user_data_description_short),
                    icon = Icons.AutoMirrored.Outlined.Send,
                    switchState = state.adUserDataConsent,
                    onCheckedChange = {
                        onAdUserDataConsentChanged(it)
                    },
                )
                SmallVerticalSpacer()
                ConsentToggleItem(
                    title = stringResource(id = R.string.consent_ad_personalization_title),
                    description = stringResource(id = R.string.consent_ad_personalization_description_short),
                    icon = Icons.Outlined.Campaign,
                    switchState = state.adPersonalizationConsent,
                    onCheckedChange = {
                        onAdPersonalizationConsentChanged(it)
                    },
                )
            }
        },
        confirmButton = {
            GeneralOutlinedButton(
                onClick = {
                    val overallConsent: Boolean =
                        state.analyticsConsent && state.adStorageConsent && state.adUserDataConsent && state.adPersonalizationConsent
                    onAcknowledge(overallConsent)
                },
                label = stringResource(id = R.string.button_acknowledge_consents),
                vectorIcon = Icons.Outlined.Check,
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    )
}

@Composable
fun ConsentToggleItem(
    title: String,
    description: String,
    icon: ImageVector,
    switchState: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .bounceClick()
            .clip(RoundedCornerShape(SizeConstants.MediumSize))
            .clickable(onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                onCheckedChange(!switchState)
            }),
        shape = RoundedCornerShape(SizeConstants.MediumSize),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = SizeConstants.ExtraTinySize / 2)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = SizeConstants.MediumSize,

                    vertical = SizeConstants.SmallSize + SizeConstants.ExtraTinySize
                ), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(SizeConstants.ExtraLargeSize)
            )
            MediumHorizontalSpacer()
            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            MediumHorizontalSpacer()

            CustomSwitch(
                checked = switchState,
                onCheckedChange = { isChecked ->
                    onCheckedChange(isChecked)
                },
                checkIcon = Icons.Filled.Done,
                uncheckIcon = Icons.Filled.Block
            )
        }
    }
}