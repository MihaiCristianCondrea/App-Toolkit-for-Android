package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.firebase.dialogs

import android.content.Context
import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.state.UsageAndDiagnosticsUiState
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.firebase.dialogs.pages.AboutPage
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.firebase.dialogs.pages.ConsentPage
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.firebase.dialogs.pages.DetailsPage
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.MediumHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openUrl
import kotlinx.coroutines.launch

@Composable
fun FirebaseConsentDialog(
    state: UsageAndDiagnosticsUiState,
    onDismissRequest: () -> Unit,
    onApplyConsents: (
        analyticsStorage: Boolean,
        adStorage: Boolean,
        adUserData: Boolean,
        adPersonalization: Boolean,
    ) -> Unit,
    onAnalyticsConsentChanged: (Boolean) -> Unit,
    onAdStorageConsentChanged: (Boolean) -> Unit,
    onAdUserDataConsentChanged: (Boolean) -> Unit,
    onAdPersonalizationConsentChanged: (Boolean) -> Unit,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    val context: Context = LocalContext.current
    val tabs = listOf("Consent", "Details", "About")
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current

    val dialogHeight = with(density) {
        (windowInfo.containerSize.height.toDp() * 0.78f).coerceAtLeast(SizeConstants.TwoHundredFiftySixSize)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(dialogHeight),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = SizeConstants.ExtraTinySize,
            shadowElevation = SizeConstants.SmallSize,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(SizeConstants.LargeSize),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Security,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    MediumHorizontalSpacer()
                    Text(
                        text = "Privacy choices", // TODO: move to string resources
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )
                }

                PrimaryTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    divider = { HorizontalDivider(thickness = SizeConstants.ExtraTinySize / 2) },
                ) {
                    tabs.forEachIndexed { index, label ->
                        Tab(
                            modifier = Modifier
                                .bounceClick()
                                .clip(CircleShape),
                            selected = pagerState.currentPage == index,
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                            text = {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                )
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true),
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth(),
                    ) { page ->
                        when (page) {
                            0 -> ConsentPage(
                                onOpenUrl = { url ->
                                    context.openUrl(url)
                                }
                            )

                            1 -> DetailsPage(
                                state = state,
                                onAnalyticsConsentChanged = onAnalyticsConsentChanged,
                                onAdStorageConsentChanged = onAdStorageConsentChanged,
                                onAdUserDataConsentChanged = onAdUserDataConsentChanged,
                                onAdPersonalizationConsentChanged = onAdPersonalizationConsentChanged,
                            )

                            else -> AboutPage()
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraSmallSize),
                ) {
                    GeneralButton(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Allow all", // TODO: string
                        onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                            onApplyConsents(true, true, true, true)
                        },
                    )

                    GeneralOutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Allow essentials", // TODO: string
                        onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                            onApplyConsents(true, false, false, false)
                        },
                    )
                }
            }
        }
    }
}
