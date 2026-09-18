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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.contracts.FaqAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.contracts.FaqEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.states.FaqUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.content.FaqScreenContent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.dropdowns.FaqScreenMenuActions
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.findActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openPlayStoreForApp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.AppVersionInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedExtendedFloatingActionButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.lists.GroupedAction
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.lists.GroupedActionList
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.R
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.distinctUntilChanged
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
    const val BACK_CLICK: String = "back_click"
    const val RETRY_LOAD: String = "retry_load"
    const val FEEDBACK_SHEET_OPENED: String = "feedback_sheet_opened"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(
    config: AppVersionInfo,
    isEmbedded: Boolean = false,
) {
    val viewModel: FaqViewModel = koinViewModel()
    val firebaseController: FirebaseController = koinInject()

    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val reviewHost = remember(activity) {
        activity?.let { hostActivity ->
            object : ReviewHost {
                override val activity = hostActivity
            }
        }
    }

    val scrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())
    val isFabExtended = rememberSaveable { mutableStateOf(true) }
    val showDialog = rememberSaveable { mutableStateOf(false) }
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

    LaunchedEffect(scrollBehavior) {
        snapshotFlow { scrollBehavior.state.contentOffset >= 0f }
            .distinctUntilChanged()
            .collect { extended ->
                isFabExtended.value = extended
            }
    }

    val content: @Composable (PaddingValues) -> Unit = { paddingValues ->
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
    }

    if (isEmbedded) {
        Box(modifier = Modifier.fillMaxSize()) {
            content(PaddingValues())
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(all = SizeConstants.LargeSize)
            ) {
                AnimatedExtendedFloatingActionButton(
                    visible = true,
                    expanded = isFabExtended.value,
                    onClick = {
                        firebaseController.logEvent(faqActionEvent(actionName = FaqActionNames.FEEDBACK_SHEET_OPENED))
                        showFeedbackBottomSheet.value = true
                    },
                    firebaseController = firebaseController,
                    ga4Event = faqPreferenceTapEvent(preferenceKey = FaqPreferenceKeys.FEEDBACK),
                    text = { Text(text = stringResource(id = R.string.feedback)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.RateReview,
                            contentDescription = null
                        )
                    },
                )
            }
        }
    } else {
        LargeTopAppBarWithScaffold(
            title = stringResource(id = R.string.help),
            onBackClicked = {
                firebaseController.logEvent(faqActionEvent(actionName = FaqActionNames.BACK_CLICK))
                activity?.finish()
            },
            actions = {
                FaqScreenMenuActions(
                    config = config,
                    showDialog = showDialog.value,
                    onShowDialogChange = { showDialog.value = it }
                )
            },
            scrollBehavior = scrollBehavior,
            floatingActionButton = {
                AnimatedExtendedFloatingActionButton(
                    visible = true,
                    expanded = isFabExtended.value,
                    onClick = {
                        firebaseController.logEvent(faqActionEvent(actionName = FaqActionNames.FEEDBACK_SHEET_OPENED))
                        showFeedbackBottomSheet.value = true
                    },
                    firebaseController = firebaseController,
                    ga4Event = faqPreferenceTapEvent(preferenceKey = FaqPreferenceKeys.FEEDBACK),
                    text = { Text(text = stringResource(id = R.string.feedback)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.RateReview,
                            contentDescription = null
                        )
                    },
                )
            },
            content = content
        )
    }

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
