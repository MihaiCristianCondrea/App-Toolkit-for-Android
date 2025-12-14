package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.components.pages

import android.content.Context
import android.content.Intent
import android.view.SoundEffectConstants
import android.view.View
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.onboarding.utils.helpers.CrashlyticsOnboardingStateManager
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.OutlinedIconButtonWithText
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraLargeIncreasedVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraLargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.MediumHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.MediumVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.switches.CustomSwitch
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ConsentManagerHelper
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CrashlyticsOnboardingPageTab() {
    val context: Context = LocalContext.current
    val configProvider = koinInject<BuildInfoProvider>()
    val dataStore: CommonDataStore = CommonDataStore.getInstance(context = context)
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val switchState: State<Boolean> =
        dataStore.usageAndDiagnostics(default = !configProvider.isDebugBuild)
            .collectAsStateWithLifecycle(initialValue = !configProvider.isDebugBuild)
    val showCrashlyticsDialog = CrashlyticsOnboardingStateManager.showCrashlyticsDialog

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
                    fontWeight = FontWeight.Bold, fontSize = 30.sp, textAlign = TextAlign.Center
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
                switchState = switchState.value, onCheckedChange = { isChecked ->
                    coroutineScope.launch {
                        dataStore.saveUsageAndDiagnostics(isChecked = isChecked)
                    }
                })

            LargeVerticalSpacer()

            OutlinedIconButtonWithText(
                onClick = { CrashlyticsOnboardingStateManager.openDialog() },
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Outlined.PrivacyTip,
                iconContentDescription = stringResource(id = R.string.onboarding_crashlytics_show_details_button_cd),
                label = stringResource(id = R.string.onboarding_crashlytics_show_details_button)
            )

            LargeVerticalSpacer()

            LearnMoreSection()
        }
    }

    if (showCrashlyticsDialog) {
        CrashlyticsConsentDialog(onDismissRequest = { CrashlyticsOnboardingStateManager.dismissDialog() },
            onAcknowledge = { CrashlyticsOnboardingStateManager.dismissDialog() })
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UsageAndDiagnosticsToggleCard(
    switchState: Boolean, onCheckedChange: (Boolean) -> Unit
) {

    val hapticFeedback : HapticFeedback = LocalHapticFeedback.current
    val view : View = LocalView.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick()
            .clip(RoundedCornerShape(SizeConstants.LargeSize))
            .clickable(onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                onCheckedChange(!switchState)
            }),
        shape = RoundedCornerShape(SizeConstants.LargeSize),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = SizeConstants.ExtraSmallSize - SizeConstants.ExtraTinySize / 2)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = SizeConstants.LargeIncreasedSize,
                    vertical = SizeConstants.LargeSize
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(
                    text = stringResource(id = R.string.usage_and_diagnostics),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier.animateContentSize(),
                    text = if (switchState) stringResource(id = R.string.onboarding_crashlytics_enabled_desc)
                    else stringResource(id = R.string.onboarding_crashlytics_disabled_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            LargeHorizontalSpacer()

            CustomSwitch(
                checked = switchState,
                onCheckedChange = { isChecked ->
                    onCheckedChange(isChecked)
                },
                checkIcon = Icons.Filled.Analytics,
                uncheckIcon = Icons.Filled.Policy
            )
        }
    }
}

@Composable
fun LearnMoreSection() {
    val context: Context = LocalContext.current
    val appContext = remember(context) { context.applicationContext }

    // ✅ Compose-native resource read (fixes the lint)
    val errorText: String = stringResource(id = R.string.error)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalDivider(
            modifier = Modifier.padding(vertical = SizeConstants.LargeSize),
            thickness = SizeConstants.ExtraTinySize / 4
        )

        Text(
            text = stringResource(R.string.onboarding_crashlytics_privacy_info),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = SizeConstants.LargeIncreasedSize + SizeConstants.ExtraSmallSize)
        )

        MediumVerticalSpacer()

        OutlinedIconButtonWithText(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, AppLinks.PRIVACY_POLICY.toUri())
                intent.resolveActivity(context.packageManager)?.let {
                    runCatching { context.startActivity(intent) }
                } ?: Toast.makeText(
                    appContext,
                    errorText, // ✅ no LocalContext.getString()
                    Toast.LENGTH_SHORT
                ).show()
            },
            icon = Icons.AutoMirrored.Filled.Launch,
            iconContentDescription = null,
            label = stringResource(id = R.string.learn_more_privacy_policy)
        )
    }
}


@Composable
fun CrashlyticsConsentDialog(
    onDismissRequest: () -> Unit,
    onAcknowledge: () -> Unit,
) {
    val context: Context = LocalContext.current
    val configProvider: BuildInfoProvider = koinInject()
    val dataStore: CommonDataStore = CommonDataStore.getInstance(context = context)
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val initialConsentValue: Boolean = !configProvider.isDebugBuild

    val analyticsState: State<Boolean> =
        dataStore.analyticsConsent(default = initialConsentValue)
            .collectAsStateWithLifecycle(initialValue = initialConsentValue)
    val adStorageState: State<Boolean> =
        dataStore.adStorageConsent(default = initialConsentValue)
            .collectAsStateWithLifecycle(initialValue = initialConsentValue)
    val adUserDataState: State<Boolean> =
        dataStore.adUserDataConsent(default = initialConsentValue)
            .collectAsStateWithLifecycle(initialValue = initialConsentValue)
    val adPersonalizationState: State<Boolean> =
        dataStore.adPersonalizationConsent(default = initialConsentValue)
            .collectAsStateWithLifecycle(initialValue = initialConsentValue)

    fun updateConsentState(type: String, value: Boolean) {
        coroutineScope.launch {
            when (type) {
                DataStoreNamesConstants.DATA_STORE_ANALYTICS_CONSENT -> dataStore.saveAnalyticsConsent(
                    isGranted = value
                )

                DataStoreNamesConstants.DATA_STORE_AD_STORAGE_CONSENT -> dataStore.saveAdStorageConsent(
                    isGranted = value
                )

                DataStoreNamesConstants.DATA_STORE_AD_USER_DATA_CONSENT -> dataStore.saveAdUserDataConsent(
                    isGranted = value
                )

                DataStoreNamesConstants.DATA_STORE_AD_PERSONALIZATION_CONSENT -> dataStore.saveAdPersonalizationConsent(
                    value
                )
            }
        }
    }

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
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
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
                    switchState = analyticsState.value,
                    onCheckedChange = {
                        updateConsentState(
                            type = DataStoreNamesConstants.DATA_STORE_ANALYTICS_CONSENT,
                            value = it
                        )
                    })
                SmallVerticalSpacer()
                ConsentToggleItem(
                    title = stringResource(id = R.string.consent_ad_storage_title),
                    description = stringResource(id = R.string.consent_ad_storage_description_short),
                    icon = Icons.Outlined.Storage,
                    switchState = adStorageState.value,
                    onCheckedChange = {
                        updateConsentState(
                            type = DataStoreNamesConstants.DATA_STORE_AD_STORAGE_CONSENT,
                            value = it
                        )
                    })
                SmallVerticalSpacer()
                ConsentToggleItem(
                    title = stringResource(id = R.string.consent_ad_user_data_title),
                    description = stringResource(id = R.string.consent_ad_user_data_description_short),
                    icon = Icons.AutoMirrored.Outlined.Send,
                    switchState = adUserDataState.value,
                    onCheckedChange = {
                        updateConsentState(
                            type = DataStoreNamesConstants.DATA_STORE_AD_USER_DATA_CONSENT,
                            value = it
                        )
                    })
                SmallVerticalSpacer()
                ConsentToggleItem(
                    title = stringResource(id = R.string.consent_ad_personalization_title),
                    description = stringResource(id = R.string.consent_ad_personalization_description_short),
                    icon = Icons.Outlined.Campaign,
                    switchState = adPersonalizationState.value,
                    onCheckedChange = {
                        updateConsentState(
                            type = DataStoreNamesConstants.DATA_STORE_AD_PERSONALIZATION_CONSENT,
                            value = it
                        )
                    })
            }
        },
        confirmButton = {
            OutlinedIconButtonWithText(
                onClick = {
                    coroutineScope.launch {
                        val overallConsent: Boolean =
                            analyticsState.value && adStorageState.value && adUserDataState.value && adPersonalizationState.value
                        dataStore.saveUsageAndDiagnostics(isChecked = overallConsent)

                        ConsentManagerHelper.updateConsent(
                            analyticsGranted = analyticsState.value,
                            adStorageGranted = adStorageState.value,
                            adUserDataGranted = adUserDataState.value,
                            adPersonalizationGranted = adPersonalizationState.value
                        )
                    }
                    onAcknowledge()
                },
                label = stringResource(id = R.string.button_acknowledge_consents),
                icon = Icons.Outlined.Check,
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

    val hapticFeedback : HapticFeedback = LocalHapticFeedback.current
    val view : View = LocalView.current

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
                ), verticalAlignment = Alignment.CenterVertically) {
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