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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.ScaffoldFabs
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import androidx.activity.compose.LocalActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.contracts.FaqAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.contracts.FaqEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.states.FaqUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.content.FaqScreenContent
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openPlayStoreForApp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.lists.GroupedAction
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.lists.GroupedActionList
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.R
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

// The analytics screen name stays "Help": the destination is still "Help & feedback" to the
// user, and changing it would split this screen's history in GA4 at the module rename.
private const val FAQ_SCREEN_NAME: String = "Help"
private const val FAQ_SCREEN_CLASS: String = "FaqScreen"

private object FaqPreferenceKeys {
    const val FEEDBACK: String = "feedback"
    const val REQUEST_FEATURE: String = "request_feature"
    const val LEAVE_REVIEW: String = "leave_review"
}

private object FaqActionNames {
    const val RETRY_LOAD: String = "retry_load"
    const val FEEDBACK_SHEET_OPENED: String = "feedback_sheet_opened"
}

/**
 * Help and feedback: the questions, a contact card and a button that opens the feedback sheet.
 *
 * The body of the help page, which `helpPage()` registers with [FaqMenuActions] in its app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen() {
    val viewModel: FaqViewModel = koinViewModel()
    val firebaseController: FirebaseController = koinInject()

    val context = LocalContext.current
    val activity = LocalActivity.current
    val reviewHost = remember(activity) { activity?.let(::ReviewHost) }
    val paddingValues = contentPadding()

    val showFeedbackBottomSheet = rememberSaveable { mutableStateOf(false) }
    val feedbackBottomSheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)

    val screenState: UiStateScreen<FaqUiState> by viewModel.uiState.collectAsStateWithLifecycle()

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = FAQ_SCREEN_NAME,
        screenClass = FAQ_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = FAQ_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    LaunchedEffect(Unit) {
        viewModel.actionEvent.collect { action ->
            when (action) {
                is FaqAction.OpenUrl -> context.openUrl(action.url)
                is FaqAction.OpenPlayStoreReview -> context.openPlayStoreForApp(context.packageName)
                is FaqAction.ReviewOutcomeReported -> Unit
            }
        }
    }

    // The page frame draws it at the bottom end, above the system bar.
    ScaffoldFabs(
        listOf(
            ToolkitFab(
                icon = ToolkitIcon.Vector(Icons.Outlined.RateReview),
                label = stringResource(id = R.string.feedback),
                onClick = {
                    firebaseController.logGa4Event(faqPreferenceTapEvent(preferenceKey = FaqPreferenceKeys.FEEDBACK))
                    firebaseController.logEvent(faqActionEvent(actionName = FaqActionNames.FEEDBACK_SHEET_OPENED))
                    showFeedbackBottomSheet.value = true
                },
            ),
        ),
    )

    ScreenStateHandler(
        screenState = screenState,
        onLoading = { LoadingScreen() },
        onEmpty = {
            NoDataScreen(
                showRetry = true,
                onRetry = {
                    firebaseController.logEvent(faqActionEvent(actionName = FaqActionNames.RETRY_LOAD))
                    viewModel.onEvent(FaqEvent.LoadFaq)
                },
                paddingValues = paddingValues
            )
        },
        onError = {
            NoDataScreen(
                isError = true,
                showRetry = true,
                onRetry = {
                    firebaseController.logEvent(faqActionEvent(actionName = FaqActionNames.RETRY_LOAD))
                    viewModel.onEvent(FaqEvent.LoadFaq)
                },
                paddingValues = paddingValues
            )
        },
        onSuccess = { data: FaqUiState ->
            FaqScreenContent(
                questions = data.questions,
                paddingValues = paddingValues,
            )
        }
    )

    if (showFeedbackBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showFeedbackBottomSheet.value = false },
            sheetState = feedbackBottomSheetState,
        ) {
            Text(
                text = stringResource(id = R.string.help_feedback_sheet_title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = SizeConstants.ExtraLargeCompactSize)
            )
            Spacer(modifier = Modifier.height(SizeConstants.SmallSize))

            GroupedActionList(
                modifier = Modifier.padding(horizontal = SizeConstants.MediumSize),
                actions = persistentListOf(
                    GroupedAction(
                        title = stringResource(id = R.string.help_feedback_sheet_feature_request_title),
                        description = stringResource(id = R.string.help_feedback_sheet_feature_request_description),
                        icon = Icons.Outlined.Lightbulb,
                        onClick = {
                            firebaseController.logGa4Event(faqPreferenceTapEvent(FaqPreferenceKeys.REQUEST_FEATURE))
                            showFeedbackBottomSheet.value = false
                            viewModel.onEvent(FaqEvent.OpenFeatureRequestForm)
                        },
                    ),
                    GroupedAction(
                        title = stringResource(id = R.string.help_feedback_sheet_review_title),
                        description = stringResource(id = R.string.help_feedback_sheet_review_description),
                        icon = Icons.Outlined.RateReview,
                        onClick = {
                            firebaseController.logGa4Event(faqPreferenceTapEvent(FaqPreferenceKeys.LEAVE_REVIEW))
                            showFeedbackBottomSheet.value = false
                            reviewHost?.let { host ->
                                viewModel.onEvent(FaqEvent.RequestReview(host = host))
                            }
                        },
                    ),
                ),
            )

            Spacer(modifier = Modifier.height(SizeConstants.ExtraLargeCompactSize))
        }
    }
}

private fun faqPreferenceTapEvent(preferenceKey: String): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(FAQ_SCREEN_NAME),
            SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(preferenceKey),
        ),
    )
}

private fun faqActionEvent(actionName: String): AnalyticsEvent {
    return AnalyticsEvent(
        name = SettingsAnalytics.Events.ACTION,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(FAQ_SCREEN_NAME),
            SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(actionName),
        ),
    )
}
