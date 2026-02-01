package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.firebase.dialogs.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun ConsentPage(
    onOpenUrl: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Your privacy matters. Before we collect any usage or advertising data, " +
                    "please choose the types of data you consent to. Your choices affect how we " +
                    "measure app performance, improve features, and tailor advertising, while still " +
                    "respecting your privacy.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "These choices help us comply with privacy standards like GDPR and CCPA " +
                    "while giving you control over how your data is used. You can change these " +
                    "settings later in the app’s privacy preferences at any time.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        SmallVerticalSpacer()

        InfoMessageSection(
            message = "If you’d like to learn more about how your data is used and what each " + "consent option means, check the documentation below.",
            modifier = Modifier.fillMaxWidth(),
            learnMoreText = "Learn more about consent and data usage",
            learnMoreUrl = "https://developers.google.com/tag-platform/security/guides/app-consent",
        )
    }
}
