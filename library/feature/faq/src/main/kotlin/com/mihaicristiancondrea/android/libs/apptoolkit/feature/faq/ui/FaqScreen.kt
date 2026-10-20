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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqId
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics.FAQ_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics.FaqActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics.FaqPreferenceKeys
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics.faqActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics.faqPreferenceTapEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.contracts.FaqEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.states.FaqUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.lists.FaqList
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.sheets.FaqFeedbackSheet
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

/**
 * Help and feedback: the questions, a contact card and a button that opens the feedback sheet.
 *
 * The body of the help page, which `helpPage()` registers with `FaqMenuActions` in its app bar.
 * This is the stateful half. It owns the [FaqViewModel], tracks the screen, logs each tap, opens
 * links, mail and the store listing, and hands the rendering to [FaqScreenContent].
 *
 * The feedback button stays while the questions load or fail: feedback does not need them.
 */
@Composable
fun FaqScreen() {
    val viewModel: FaqViewModel = koinViewModel()
    val state: FaqUiState by viewModel.state.collectAsStateWithLifecycle()
    val telemetry = LocalTelemetry.current
    val adsConfig: AdsConfig = koinInject(qualifier = named(AdsQualifiers.HELP_NATIVE_AD))
    val adsEnabled: Boolean = rememberAdsEnabled()
    val context = LocalContext.current
    val activity = LocalActivity.current
    val reviewHost: ReviewHost? = remember(activity) { activity?.let(::ReviewHost) }
    var showFeedbackSheet: Boolean by rememberSaveable { mutableStateOf(value = false) }

    LaunchedEffect(state.openStoreListing) {
        if (state.openStoreListing) {
            context.openPlayStoreForApp(context.packageName)
            viewModel.onEvent(FaqEvent.StoreListingOpened)
        }
    }

    ScreenHost(
        screen = FAQ_SCREEN,
        status = state.questions,
        viewModel = viewModel,
        fabs = listOf(
            ToolkitFab(
                icon = ToolkitIcon.Vector(Icons.Outlined.RateReview),
                label = stringResource(id = R.string.feedback),
                onClick = {
                    telemetry.logGa4Event(faqPreferenceTapEvent(preferenceKey = FaqPreferenceKeys.FEEDBACK))
                    telemetry.logEvent(faqActionEvent(actionName = FaqActionNames.FEEDBACK_SHEET_OPENED))
                    showFeedbackSheet = true
                },
            ),
        ),
    ) {
        FaqScreenContent(
            state = state,
            showFeedbackSheet = showFeedbackSheet,
            onFeedbackSheetDismissed = { showFeedbackSheet = false },
            onRetry = {
                telemetry.logEvent(faqActionEvent(actionName = FaqActionNames.RETRY_LOAD))
                viewModel.onEvent(FaqEvent.Load)
            },
            onRequestFeature = {
                telemetry.logGa4Event(faqPreferenceTapEvent(preferenceKey = FaqPreferenceKeys.REQUEST_FEATURE))
                context.openUrl(AppLinks.FEATURE_REQUESTS_FORM)
            },
            onLeaveReview = {
                telemetry.logGa4Event(faqPreferenceTapEvent(preferenceKey = FaqPreferenceKeys.LEAVE_REVIEW))
                reviewHost?.let { host -> viewModel.onEvent(FaqEvent.RequestReview(host = host)) }
            },
            onQuestionToggled = { question, position, expanded ->
                telemetry.logGa4Event(
                    faqPreferenceTapEvent(
                        preferenceKey = FaqPreferenceKeys.FAQ_ITEM,
                        faqId = question.id.value,
                        faqPosition = position,
                        expanded = expanded,
                    )
                )
            },
            onShowMoreQuestions = {
                telemetry.logGa4Event(
                    faqPreferenceTapEvent(preferenceKey = FaqPreferenceKeys.SHOW_MORE_QUESTIONS)
                )
            },
            onContactUs = {
                telemetry.logEvent(
                    faqPreferenceTapEvent(preferenceKey = FaqPreferenceKeys.CONTACT_US).toAnalyticsEvent()
                )
                context.sendEmailToDeveloper(applicationNameRes = CommonR.string.app_name)
            },
            contentPadding = contentPadding(),
            adUnitId = adsConfig.bannerAdUnitId.takeIf { adsEnabled && it.isNotBlank() },
        )
    }
}

/**
 * Renders the help page for [state]: the questions, the contact card and, while
 * [showFeedbackSheet], the feedback sheet. Reports every tap through a callback and does nothing
 * else with it.
 *
 * This is the stateless half of [FaqScreen]: it holds no ViewModel, injects nothing, logs nothing
 * and opens nothing, so it renders in a preview or a test with plain values. The state it keeps is
 * visual: which questions are expanded and whether all of them show.
 *
 * @param showFeedbackSheet Whether the feedback sheet is open; [FaqScreen]'s feedback button opens
 * it.
 * @param onFeedbackSheetDismissed The sheet closed, dismissed or after one of its rows was tapped.
 * @param onRetry Loads the questions again after a failure or an empty result.
 * @param onRequestFeature The sheet's feature request row was tapped.
 * @param onLeaveReview The sheet's review row was tapped.
 * @param onQuestionToggled A question was expanded or collapsed, with its position in the list.
 * @param onShowMoreQuestions The rest of the questions were revealed.
 * @param onContactUs The contact card was tapped.
 * @param contentPadding Padding from the shell, applied inside the list and the state screens.
 * @param adUnitId The native ad slot's unit, or `null` when no ad should show.
 */
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
        // Nothing to show is still worth another try: the catalog may have been updated.
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

@Preview(showBackground = true)
@Composable
private fun FaqScreenContentPreview() {
    MaterialTheme {
        FaqScreenContent(
            state = FaqUiState(
                questions = Loadable.Ready(
                    persistentListOf(
                        FaqItem(
                            id = FaqId("preview-1"),
                            question = "How do I change the theme?",
                            answer = "Open Settings, then Theme, and pick a palette.",
                        ),
                        FaqItem(
                            id = FaqId("preview-2"),
                            question = "Does the app work offline?",
                            answer = "Yes. Everything but the remote questions works offline.",
                        ),
                    )
                ),
            ),
            showFeedbackSheet = false,
            onFeedbackSheetDismissed = {},
            onRetry = {},
            onRequestFeature = {},
            onLeaveReview = {},
            onQuestionToggled = { _, _, _ -> },
            onShowMoreQuestions = {},
            onContactUs = {},
        )
    }
}
