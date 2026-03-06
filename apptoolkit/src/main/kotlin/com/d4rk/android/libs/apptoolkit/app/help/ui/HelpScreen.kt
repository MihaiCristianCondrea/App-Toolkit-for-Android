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

package com.d4rk.android.libs.apptoolkit.app.help.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ContactSupport
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.help.ui.contract.HelpAction
import com.d4rk.android.libs.apptoolkit.app.help.ui.contract.HelpEvent
import com.d4rk.android.libs.apptoolkit.app.help.ui.state.HelpUiState
import com.d4rk.android.libs.apptoolkit.app.help.ui.views.content.HelpScreenContent
import com.d4rk.android.libs.apptoolkit.app.help.ui.views.dropdowns.HelpScreenMenuActions
import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewHost
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsEvent
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsValue
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedExtendedFloatingActionButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.d4rk.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.utils.constants.analytics.SettingsAnalytics
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.findActivity
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openPlayStoreForApp
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openUrl
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val HELP_SCREEN_NAME: String = "Help"
private const val HELP_SCREEN_CLASS: String = "HelpScreen"

private object HelpPreferenceKeys {
    const val FEEDBACK: String = "feedback"
    const val CONTACT_US: String = "contact_us"
    const val REQUEST_FEATURE: String = "request_feature"
    const val LEAVE_REVIEW: String = "leave_review"
}

private object HelpActionNames {
    const val BACK_CLICK: String = "back_click"
    const val RETRY_LOAD: String = "retry_load"
    const val FEEDBACK_SHEET_OPENED: String = "feedback_sheet_opened"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    config: AppVersionInfo,
) {
    val viewModel: HelpViewModel = koinViewModel()
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
    val feedbackBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val screenState: UiStateScreen<HelpUiState> by viewModel.uiState.collectAsStateWithLifecycle()

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = HELP_SCREEN_NAME,
        screenClass = HELP_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = HELP_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    LaunchedEffect(Unit) {
        viewModel.actionEvent.collect { action ->
            when (action) {
                is HelpAction.OpenUrl -> context.openUrl(action.url)
                is HelpAction.OpenPlayStoreReview -> context.openPlayStoreForApp(context.packageName)
                is HelpAction.ReviewOutcomeReported -> Unit
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

    LargeTopAppBarWithScaffold(
        title = stringResource(id = R.string.help),
        onBackClicked = {
            firebaseController.logEvent(helpActionEvent(actionName = HelpActionNames.BACK_CLICK))
            activity?.finish()
        },
        actions = {
            HelpScreenMenuActions(
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
                    firebaseController.logEvent(helpActionEvent(actionName = HelpActionNames.FEEDBACK_SHEET_OPENED))
                    showFeedbackBottomSheet.value = true
                },
                firebaseController = firebaseController,
                ga4Event = helpPreferenceTapEvent(preferenceKey = HelpPreferenceKeys.FEEDBACK),
                text = { Text(text = stringResource(id = R.string.feedback)) },
                icon = { Icon(imageVector = Icons.Outlined.RateReview, contentDescription = null) },
            )
        }
    ) { paddingValues ->
        ScreenStateHandler(
            screenState = screenState,
            onLoading = { LoadingScreen() },
            onEmpty = {
                NoDataScreen(
                    showRetry = true,
                    onRetry = {
                        firebaseController.logEvent(helpActionEvent(actionName = HelpActionNames.RETRY_LOAD))
                        viewModel.onEvent(HelpEvent.LoadFaq)
                    },
                    paddingValues = paddingValues
                )
            },
            onError = {
                NoDataScreen(
                    isError = true,
                    showRetry = true,
                    onRetry = {
                        firebaseController.logEvent(helpActionEvent(actionName = HelpActionNames.RETRY_LOAD))
                        viewModel.onEvent(HelpEvent.LoadFaq)
                    },
                    paddingValues = paddingValues
                )
            },
            onSuccess = { data: HelpUiState ->
                HelpScreenContent(
                    questions = data.questions,
                    paddingValues = paddingValues,
                    firebaseController = firebaseController,
                )
            }
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
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            FeedbackListItem(
                title = stringResource(id = R.string.help_feedback_sheet_feature_request_title),
                description = stringResource(id = R.string.help_feedback_sheet_feature_request_description),
                icon = { Icon(imageVector = Icons.Outlined.Lightbulb, contentDescription = null) },
                onClick = {
                    firebaseController.logGa4Event(helpPreferenceTapEvent(HelpPreferenceKeys.REQUEST_FEATURE))
                    showFeedbackBottomSheet.value = false
                    viewModel.onEvent(HelpEvent.OpenFeatureRequestForm)
                }
            )

            FeedbackListItem(
                title = stringResource(id = R.string.help_feedback_sheet_contact_title),
                description = stringResource(id = R.string.help_feedback_sheet_contact_description),
                icon = { Icon(imageVector = Icons.AutoMirrored.Outlined.ContactSupport, contentDescription = null) },
                onClick = {
                    firebaseController.logGa4Event(helpPreferenceTapEvent(HelpPreferenceKeys.CONTACT_US))
                    showFeedbackBottomSheet.value = false
                    viewModel.onEvent(HelpEvent.OpenContactPage)
                }
            )

            FeedbackListItem(
                title = stringResource(id = R.string.help_feedback_sheet_review_title),
                description = stringResource(id = R.string.help_feedback_sheet_review_description),
                icon = { Icon(imageVector = Icons.Outlined.RateReview, contentDescription = null) },
                onClick = {
                    firebaseController.logGa4Event(helpPreferenceTapEvent(HelpPreferenceKeys.LEAVE_REVIEW))
                    showFeedbackBottomSheet.value = false
                    reviewHost?.let { host ->
                        viewModel.onEvent(HelpEvent.RequestReview(host = host))
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FeedbackListItem(
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(shape = MaterialTheme.shapes.large)
            .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        icon()
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title)
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}

private fun helpPreferenceTapEvent(preferenceKey: String): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(HELP_SCREEN_NAME),
            SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(preferenceKey),
        ),
    )
}

private fun helpActionEvent(actionName: String): AnalyticsEvent {
    return AnalyticsEvent(
        name = SettingsAnalytics.Events.ACTION,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(HELP_SCREEN_NAME),
            SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(actionName),
        ),
    )
}
