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
            text = "Below you’ll find more detailed controls for how your data is used across the app. " +
                    "Your choices here affect what " +
                    "information is collected, how it’s stored, and how it may be shared with service " +
                    "providers such as Google, in accordance with applicable privacy standards and consent " +
                    "requirements.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )

        SmallVerticalSpacer()

        ConsentExpandableItemCard(
            title = "Analytics storage",
            summary = "Allows collecting analytics to improve the app.",
            details = "This lets us measure usage, detect crashes and prioritize enhancements.",
            icon = Icons.Outlined.Analytics,
            checked = state.analyticsConsent,
            onCheckedChange = onAnalyticsConsentChanged,
            onLearnMoreClick = { url ->
                context.openUrl(url)
            }
        )

        ConsentExpandableItemCard(
            title = "Ad storage",
            summary = "Ad storage on device.",
            details = "Stores ad-related info on your device to enable ad delivery and reporting.",
            icon = Icons.Outlined.Storage,
            checked = state.adStorageConsent,
            onCheckedChange = onAdStorageConsentChanged,
            onLearnMoreClick = { url ->
                context.openUrl(url)
            }
        )

        ConsentExpandableItemCard(
            title = "Ad user data",
            summary = "Ad measurement data.",
            details = "Allows sending ad-related user data to help measure ad performance.",
            icon = Icons.Outlined.Info,
            checked = state.adUserDataConsent,
            onCheckedChange = onAdUserDataConsentChanged,
            onLearnMoreClick = { url ->
                context.openUrl(url)
            }
        )

        ConsentExpandableItemCard(
            title = "Ad personalization",
            summary = "Personalized ads.",
            details = "Allows ads to be personalized based on your activity. Turning it off reduces personalization.",
            icon = Icons.Outlined.Campaign,
            checked = state.adPersonalizationConsent,
            onCheckedChange = onAdPersonalizationConsentChanged,
            onLearnMoreClick = { url ->
                context.openUrl(url)
            }
        )
    }
}