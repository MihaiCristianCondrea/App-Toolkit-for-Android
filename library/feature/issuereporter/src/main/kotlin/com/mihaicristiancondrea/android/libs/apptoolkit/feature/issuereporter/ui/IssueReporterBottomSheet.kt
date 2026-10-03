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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonMeasurements
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.markdown.MarkdownFormatAction
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportFieldError
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportValidation
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.presentation.IssueReporterPresence
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.analytics.ISSUE_REPORTER_SCREEN_NAME
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.analytics.IssueReporterActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.analytics.issueReporterActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueReporterUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueSubmissionState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views.DeviceInfoSection
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views.IssueReportForm
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views.IssueSubmittedContent
import kotlinx.collections.immutable.ImmutableList
import org.koin.compose.viewmodel.koinViewModel

private const val ISSUE_REPORTER_SCREEN_CLASS = "IssueReporterContent"

/**
 * Shared report sheet for Compose hosts and
 * [IssueReporterLauncher][com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.presentation.IssueReporterLauncher].
 * It opens fully expanded so the editor remains usable above the keyboard.
 *
 * Presence is registered for the composition lifetime to prevent duplicate sheets across entry
 * points. Dismissal, including Done on the confirmation, resets the ViewModel so drafts and
 * completed reports do not reappear; an in-flight submission finishes before its reset. Messages
 * show as toasts so they do not cover the send button.
 *
 * [onDismissRequest] runs after the sheet settles out of view, allowing removal from
 * composition without cutting the exit animation.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IssueReporterBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: IssueReporterViewModel = koinViewModel()
    val state: IssueReporterUiState by viewModel.state.collectAsStateWithLifecycle()
    val messages: ImmutableList<UiMessage> by viewModel.messages.collectAsStateWithLifecycle()
    val telemetryRepository = LocalTelemetry.current

    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
    )

    TrackScreenView(
        screenName = ISSUE_REPORTER_SCREEN_NAME,
        screenClass = ISSUE_REPORTER_SCREEN_CLASS,
    )
    TrackScreenState(
        screenName = ISSUE_REPORTER_SCREEN_NAME,
        state = state.submissionState,
    )

    DisposableEffect(Unit) {
        IssueReporterPresence.onShown()
        onDispose { IssueReporterPresence.onHidden() }
    }

    val dismiss: () -> Unit = {
        viewModel.onEvent(IssueReporterEvent.Reset)
        onDismissRequest()
    }

    ModalBottomSheet(
        onDismissRequest = dismiss,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        IssueReporterBottomSheetContent(
            state = state,
            onEvent = viewModel::onEvent,
            onSend = { viewModel.onEvent(IssueReporterEvent.Send) },
            onDeviceInfoExpandedChange = { expanded ->
                if (expanded) viewModel.onEvent(IssueReporterEvent.RequestDeviceInfo)
                telemetryRepository.logEvent(
                    issueReporterActionEvent(
                        actionName = IssueReporterActionNames.TOGGLE_DEVICE_INFO,
                        params = mapOf("expanded" to AnalyticsValue.Bool(expanded)),
                    ),
                )
            },
            onMarkdownFormat = { action ->
                telemetryRepository.logEvent(
                    issueReporterActionEvent(
                        actionName = IssueReporterActionNames.FORMAT_DESCRIPTION,
                        params = mapOf("format" to AnalyticsValue.Str(action.analyticsName)),
                    ),
                )
            },
            onDone = dismiss,
        )
    }

    MessageToasts(
        messages = messages,
        onShown = viewModel::messageShown,
    )
}

/**
 * The editor while the report is written or sent, and the confirmation once it is filed. The fade
 * is keyed on whether the report was filed, and the navigation bar inset is consumed before the
 * IME padding so the two are not added together.
 *
 * @param onEvent Receives the events [IssueReporterViewModel] handles.
 * @param onSend Files the report.
 * @param onDeviceInfoExpandedChange Called when the device panel opens or closes.
 * @param onMarkdownFormat Called with each formatting action used in the description.
 * @param onDone Closes the reporter from the confirmation.
 */
@Composable
internal fun IssueReporterBottomSheetContent(
    state: IssueReporterUiState,
    onEvent: (IssueReporterEvent) -> Unit,
    onSend: () -> Unit,
    onDeviceInfoExpandedChange: (expanded: Boolean) -> Unit,
    onMarkdownFormat: (MarkdownFormatAction) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val submissionState: IssueSubmissionState = state.submissionState

    SubmissionSucceeded(submissionState = submissionState)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .padding(
                start = SizeConstants.LargeSize,
                end = SizeConstants.LargeSize,
                bottom = SizeConstants.LargeSize,
            ),
    ) {
        AnimatedContent(
            targetState = submissionState is IssueSubmissionState.Submitted,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "IssueReporterSubmission",
        ) { submitted: Boolean ->
            if (submitted) {
                IssueSubmittedContent(onDone = onDone)
            } else {
                IssueReportEditor(
                    state = state,
                    onEvent = onEvent,
                    onSend = onSend,
                    onDeviceInfoExpandedChange = onDeviceInfoExpandedChange,
                    onMarkdownFormat = onMarkdownFormat,
                )
            }
        }
    }
}

/**
 * Editor with a fixed send action. The form wraps content until the sheet fills the screen,
 * then scrolls beneath the button, which is disabled while a send is in flight.
 */
@Composable
private fun IssueReportEditor(
    state: IssueReporterUiState,
    onEvent: (IssueReporterEvent) -> Unit,
    onSend: () -> Unit,
    onDeviceInfoExpandedChange: (expanded: Boolean) -> Unit,
    onMarkdownFormat: (MarkdownFormatAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.MediumSize),
    ) {
        Text(
            text = stringResource(id = R.string.bug_report),
            style = MaterialTheme.typography.headlineSmall,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f, fill = false)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
        ) {
            IssueReportForm(
                data = state,
                onEvent = onEvent,
                onMarkdownFormat = onMarkdownFormat,
            )

            LargeVerticalSpacer()

            DeviceInfoSection(
                deviceInfo = state.deviceInfo,
                onExpandedChange = onDeviceInfoExpandedChange,
            )
        }

        GeneralButton(
            onClick = onSend,
            style = GeneralButtonStyle.Filled,
            enabled = state.submissionState !is IssueSubmissionState.Sending,
            label = stringResource(id = R.string.issue_send),
            icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.BugReport),
            measurements = ButtonMeasurements.Medium,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * Closes the editing session the moment the report lands: the keyboard goes down, focus clears,
 * and a confirm haptic marks a change that is easy to miss on a glance away.
 */
@Composable
private fun SubmissionSucceeded(submissionState: IssueSubmissionState) {
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val focusManager: FocusManager = LocalFocusManager.current
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val submitted: Boolean = submissionState is IssueSubmissionState.Submitted

    LaunchedEffect(submitted) {
        if (!submitted) return@LaunchedEffect
        keyboardController?.hide()
        focusManager.clearFocus(force = true)
        hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.Confirm)
    }
}

/**
 * Shows [messages] as toasts, oldest first, and calls [onShown] with each one's id once its toast
 * is posted. A toast is the system's own window, so it never covers the sheet's send button.
 */
@Composable
private fun MessageToasts(
    messages: ImmutableList<UiMessage>,
    onShown: (id: Long) -> Unit,
) {
    val context: Context = LocalContext.current
    val next: UiMessage? = messages.firstOrNull()

    LaunchedEffect(next?.id) {
        val message: UiMessage = next ?: return@LaunchedEffect
        Toast.makeText(
            context.applicationContext,
            message.text.asString(context = context),
            if (message.isError) Toast.LENGTH_LONG else Toast.LENGTH_SHORT,
        ).show()
        onShown(message.id)
    }
}

@Preview(showBackground = true)
@Composable
private fun IssueReporterBottomSheetContentPreview() {
    MaterialTheme {
        IssueReporterBottomSheetContent(
            state = IssueReporterUiState(
                title = "Sample Bug Title",
                description = "This is a detailed description of the bug in the sample application.",
                email = "user@example.com",
                deviceInfo = Loadable.Ready("Device: Pixel 7\nOS: Android 14\nApp Version: 1.0.0"),
            ),
            onEvent = {},
            onSend = {},
            onDeviceInfoExpandedChange = {},
            onMarkdownFormat = {},
            onDone = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IssueReporterBottomSheetContentInvalidPreview() {
    MaterialTheme {
        IssueReporterBottomSheetContent(
            state = IssueReporterUiState(
                title = "Crash",
                description = "",
                email = "user at example",
                fieldErrors = IssueReportValidation(
                    titleError = IssueReportFieldError.TooShort(minimumLength = 10),
                    descriptionError = IssueReportFieldError.Missing,
                    emailError = IssueReportFieldError.InvalidEmail,
                ),
            ),
            onEvent = {},
            onSend = {},
            onDeviceInfoExpandedChange = {},
            onMarkdownFormat = {},
            onDone = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IssueReporterBottomSheetContentSubmittedPreview() {
    MaterialTheme {
        IssueReporterBottomSheetContent(
            state = IssueReporterUiState(
                submissionState = IssueSubmissionState.Submitted(issueUrl = "https://github.com/example/1"),
            ),
            onEvent = {},
            onSend = {},
            onDeviceInfoExpandedChange = {},
            onMarkdownFormat = {},
            onDone = {},
        )
    }
}
