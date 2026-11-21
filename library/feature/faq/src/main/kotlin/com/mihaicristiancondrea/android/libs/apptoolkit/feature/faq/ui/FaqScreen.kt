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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ads.AdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.links.AppLinks
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openPlayStoreForApp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.sendEmailToDeveloper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics.FaqAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics.FaqActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics.FaqContentTypes
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics.FaqItemIds
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics.faqActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics.faqQuestionSelectEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics.faqSelectContentEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.contracts.FaqEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.states.FaqUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.lists.FaqList
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.sheets.FaqFeedbackSheet
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

@Composable
fun FaqScreen() {
    val viewModel: FaqViewModel = koinViewModel()
    val state: FaqUiState by viewModel.state.collectAsStateWithLifecycle()
    val telemetry = LocalTelemetry.current
    val adsConfig: AdsConfig = koinInject(qualifier = named(name = AdsQualifiers.HELP_NATIVE_AD))
    val adsEnabled: Boolean = rememberAdsEnabled()
    val context = LocalContext.current
    val activity = LocalActivity.current
    val reviewHost: ReviewHost? = remember(key1 = activity) { activity?.let(block = ::ReviewHost) }
    var showFeedbackSheet: Boolean by rememberSaveable { mutableStateOf(value = false) }
    var feedbackActionSelected: Boolean by remember { mutableStateOf(value = false) }

    LaunchedEffect(key1 = state.openStoreListing) {
        if (state.openStoreListing) {
            telemetry.logEvent(event = faqActionEvent(actionName = FaqActionNames.STORE_LISTING_OPENED))
            context.openPlayStoreForApp(packageName = context.packageName)
            viewModel.onEvent(event = FaqEvent.StoreListingOpened)
        }
    }

    ScreenHost(
        screen = FaqAnalytics.SCREEN,
        status = state.questions,
        viewModel = viewModel,
        fabs = listOf(
            element = ToolkitFab(
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.RateReview),
                label = stringResource(id = R.string.feedback),
                onClick = {
                    feedbackActionSelected = false
                    telemetry.logEvent(event = faqActionEvent(actionName = FaqActionNames.FEEDBACK_SHEET_OPENED))
                    showFeedbackSheet = true
                },
            ),
        ),
    ) {
        FaqScreenContent(
            state = state,
            showFeedbackSheet = showFeedbackSheet,
            onFeedbackSheetDismissed = {
                if (!feedbackActionSelected) {
                    telemetry.logEvent(event = faqActionEvent(actionName = FaqActionNames.FEEDBACK_SHEET_DISMISSED))
                }
                showFeedbackSheet = false
            },
            onRetry = {
                telemetry.logEvent(event = faqActionEvent(actionName = FaqActionNames.RETRY_LOAD))
                viewModel.onEvent(event = FaqEvent.Load)
            },
            onRequestFeature = {
                feedbackActionSelected = true
                telemetry.logEvent(
                    event = faqSelectContentEvent(
                        contentType = FaqContentTypes.FEEDBACK_OPTION,
                        itemId = FaqItemIds.REQUEST_FEATURE,
                    ),
                )
                context.openUrl(url = AppLinks.FEATURE_REQUESTS_FORM)
            },
            onLeaveReview = {
                feedbackActionSelected = true
                telemetry.logEvent(
                    event = faqSelectContentEvent(
                        contentType = FaqContentTypes.FEEDBACK_OPTION,
                        itemId = FaqItemIds.LEAVE_REVIEW,
                    ),
                )
                reviewHost?.let { host -> viewModel.onEvent(event = FaqEvent.RequestReview(host = host)) }
            },
            onQuestionToggled = { question, position, expanded ->
                telemetry.logEvent(
                    event = faqQuestionSelectEvent(
                        faqId = question.id.value,
                        position = position,
                        expanded = expanded,
                    ),
                )
            },
            onShowMoreQuestions = {
                telemetry.logEvent(
                    event = faqActionEvent(actionName = FaqActionNames.SHOW_MORE_QUESTIONS),
                )
            },
            onContactUs = {
                telemetry.logEvent(
                    event = faqSelectContentEvent(
                        contentType = FaqContentTypes.SUPPORT,
                        itemId = FaqItemIds.CONTACT_US,
                    ),
                )
                context.sendEmailToDeveloper(applicationNameRes = CommonR.string.app_name)
            },
            contentPadding = contentPadding(),
            adUnitId = adsConfig.bannerAdUnitId.takeIf { adsEnabled && it.isNotBlank() },
        )
    }
}

/** @param adUnitId Native ad unit, or `null` to omit the ad slot. */
@Composable
internal fun FaqScreenContent(
    state: FaqUiState,
    showFeedbackSheet: Boolean,
    onFeedbackSheetDismissed: () -> Unit,
    onRetry: () -> Unit,
    onRequestFeature: () -> Unit,
    onLeaveReview: () -> Unit,
    onQuestionToggled: (question: FaqItem, position: Int, expanded: Boolean) -> Unit,
    onShowMoreQuestions: () -> Unit,
    onContactUs: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    adUnitId: String? = null,
) {
    ScreenStateHandler(
        state = state.questions,
        modifier = modifier,
        contentPadding = contentPadding,
        onRetry = onRetry,
        retryWhenEmpty = true,
    ) { ready ->
        FaqList(
            questions = ready.value,
            contentPadding = contentPadding,
            adUnitId = adUnitId,
            onQuestionToggled = onQuestionToggled,
            onShowMoreQuestions = onShowMoreQuestions,
            onContactUs = onContactUs,
        )
    }

    if (showFeedbackSheet) {
        FaqFeedbackSheet(
            onDismiss = onFeedbackSheetDismissed,
            onRequestFeature = onRequestFeature,
            onLeaveReview = onLeaveReview,
        )
    }
}
