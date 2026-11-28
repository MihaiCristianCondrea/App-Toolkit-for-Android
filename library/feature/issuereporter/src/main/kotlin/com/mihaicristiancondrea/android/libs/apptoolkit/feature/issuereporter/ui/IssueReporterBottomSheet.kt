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

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageToastHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.StateContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.markdown.MarkdownFormatAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.analytics.ISSUE_REPORTER_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.analytics.issueReporterFormatDescriptionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.analytics.issueReporterToggleDeviceInfoEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueReporterUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueSubmissionState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.IssueReporterPresence
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views.IssueReportEditor
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views.IssueSubmittedContent
import kotlinx.collections.immutable.ImmutableList
import org.koin.compose.viewmodel.koinViewModel

/**
 * Shared report sheet for Compose hosts and
 * [IssueReporterLauncher][com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.IssueReporterLauncher].
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueReporterBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: IssueReporterViewModel = koinViewModel()
    val state: IssueReporterUiState by viewModel.state.collectAsStateWithLifecycle()
    val messages: ImmutableList<UiMessage> by viewModel.messages.collectAsStateWithLifecycle()
    val telemetry = LocalTelemetry.current

    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
    )

    DisposableEffect(Unit) {
        IssueReporterPresence.onShown()
        onDispose { IssueReporterPresence.onHidden() }
    }

    val dismiss: () -> Unit = {
        viewModel.onEvent(event = IssueReporterEvent.Reset)
        onDismissRequest()
    }

    // A sheet over the current screen is a screen of its own: it reports its visits and its status.
    ScreenHost(screen = ISSUE_REPORTER_SCREEN, status = state.submissionState) {
        ModalBottomSheet(
            onDismissRequest = dismiss,
            sheetState = sheetState,
            modifier = modifier,
        ) {
            IssueReporterBottomSheetContent(
                state = state,
                onEvent = viewModel::onEvent,
                onSend = { viewModel.onEvent(event = IssueReporterEvent.Send) },
                onDeviceInfoExpandedChange = { expanded ->
                    if (expanded) viewModel.onEvent(event = IssueReporterEvent.RequestDeviceInfo)
                    telemetry.logEvent(event = issueReporterToggleDeviceInfoEvent(expanded = expanded))
                },
                onMarkdownFormat = { action ->
                    telemetry.logEvent(
                        event = issueReporterFormatDescriptionEvent(format = action.analyticsName),
                    )
                },
                onDone = dismiss,
            )
        }
    }

    MessageToastHost(
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
                start = MaterialTheme.spacing.large,
                end = MaterialTheme.spacing.large,
                bottom = MaterialTheme.spacing.large,
            ),
    ) {
        StateContent(
            state = submissionState is IssueSubmissionState.Submitted,
            transition = fadeIn() togetherWith fadeOut(),
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
