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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonMeasurements
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueReporterUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueSubmissionState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.ISSUE_REPORTER_SCREEN_NAME
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.IssueReporterActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.issueReporterActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views.DeviceInfoSection
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views.IssueReportForm
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views.IssueSubmittedContent
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val ISSUE_REPORTER_SCREEN_CLASS = "IssueReporterContent"

/**
 * The whole issue reporter, as content a container can place anywhere.
 *
 * It owns no window and no navigation. Presentation belongs to whatever shows it, today the modal
 * sheet in [IssueReporterBottomSheet]; this composable only renders state and forwards intent to
 * [IssueReporterViewModel]. That is what lets settings and the shake gesture share one
 * implementation instead of one screen each.
 *
 * The reporter is a small state machine, and the sheet shows exactly one of its states. Editing and
 * sending are the form, with a persistent send button rather than the floating one the full screen
 * used: a floating action button inside another floating surface reads as an unrelated second
 * layer. Submitting replaces all of it with [IssueSubmittedContent], so the sheet shrinks to a
 * confirmation instead of leaving the author reading a form they have already finished with.
 *
 * Failures go the other way, back to editing with the report intact, and say so through a toast. A
 * snackbar belongs to the surface hosting it, and this one is a sheet: it would land inside the
 * sheet, over the send button that produced it, and vanish with the sheet if the author dismissed
 * it on the way. A toast is the system's own window.
 *
 * [onDone] is how the confirmation closes the reporter, so a container passes its own dismissal.
 */
@Composable
fun IssueReporterContent(
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IssueReporterViewModel = koinViewModel(),
) {
    val firebaseController: FirebaseController = koinInject()

    val uiStateScreen: UiStateScreen<IssueReporterUiState> by viewModel.uiState.collectAsStateWithLifecycle()
    val data: IssueReporterUiState = uiStateScreen.data ?: IssueReporterUiState()
    val submissionState: IssueSubmissionState = data.submissionState

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = ISSUE_REPORTER_SCREEN_NAME,
        screenClass = ISSUE_REPORTER_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = ISSUE_REPORTER_SCREEN_NAME,
        screenState = uiStateScreen.screenState,
    )

    SubmissionSucceeded(submissionState = submissionState)

    // navigationBarsPadding() comes first on purpose: it consumes the navigation bar inset, so the
    // imePadding() after it adds only what the keyboard needs on top, instead of both insets
    // stacking into a gap above the send button whenever the keyboard is open.
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
            // Keyed on the kind of state, not the state itself, so a Submitted value carrying a
            // different URL does not restart the transition.
            targetState = submissionState is IssueSubmissionState.Submitted,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "IssueReporterSubmission",
        ) { submitted: Boolean ->
            if (submitted && submissionState is IssueSubmissionState.Submitted) {
                IssueSubmittedContent(
                    onDone = onDone,
                )
            } else {
                IssueReportEditor(
                    data = data,
                    firebaseController = firebaseController,
                    isSending = submissionState is IssueSubmissionState.Sending,
                    onEvent = viewModel::onEvent,
                )
            }
        }

        ScreenMessageToast(
            snackbar = uiStateScreen.snackbar,
            onShown = { viewModel.onEvent(IssueReporterEvent.DismissSnackbar) },
        )
    }
}

/** The report being written: the form, what will be attached, and the action that files it. */
@Composable
private fun IssueReportEditor(
    data: IssueReporterUiState,
    firebaseController: FirebaseController,
    isSending: Boolean,
    onEvent: (IssueReporterEvent) -> Unit,
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
                // The sheet wraps its content while it fits and stops growing once it fills the
                // screen, at which point the report scrolls under a send button that stays put.
                .weight(weight = 1f, fill = false)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
        ) {
            IssueReportForm(
                data = data,
                firebaseController = firebaseController,
                onEvent = onEvent,
            )

            LargeVerticalSpacer()

            DeviceInfoSection(
                deviceInfoText = data.deviceInfoText,
                firebaseController = firebaseController,
                onExpandRequested = { onEvent(IssueReporterEvent.RequestDeviceInfo) },
            )
        }

        GeneralButton(
            onClick = {
                firebaseController.logEvent(
                    issueReporterActionEvent(
                        actionName = IssueReporterActionNames.SEND_ISSUE,
                        params = mapOf(
                            "title_length" to AnalyticsValue.LongVal(data.title.length.toLong()),
                            "description_length" to AnalyticsValue.LongVal(data.description.length.toLong()),
                            "has_email" to AnalyticsValue.Bool(data.email.isNotBlank()),
                        ),
                    )
                )
                onEvent(IssueReporterEvent.Send)
            },
            style = GeneralButtonStyle.Filled,
            enabled = !isSending,
            label = stringResource(id = R.string.issue_send),
            icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.BugReport),
            measurements = ButtonMeasurements.Medium,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * Closes the editing session the moment the report lands.
 *
 * The keyboard and the focused field belong to a form that is about to be replaced, so leaving them
 * up would cover the confirmation with an input for something already sent. The haptic is the other
 * half of that: the sheet shrinking is easy to miss on a glance away from the screen.
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
 * Shows each screen message once, as a toast.
 *
 * [UiSnackbar.timeStamp] is the identity of a message, not its content: the reporter can fail the
 * same way twice in a row, and keying on the text would swallow the second one. Acknowledging it
 * through [onShown] is what lets the next identical message through.
 */
@Composable
private fun ScreenMessageToast(
    snackbar: UiSnackbar?,
    onShown: () -> Unit,
) {
    val context: Context = LocalContext.current
    val appContext: Context = remember(context) { context.applicationContext }

    LaunchedEffect(snackbar?.timeStamp) {
        val message: UiTextHelper = snackbar?.message ?: return@LaunchedEffect
        val text: String = message.asString(context = context)
        if (text.isBlank()) return@LaunchedEffect

        Toast.makeText(
            appContext,
            text,
            if (snackbar.isError) Toast.LENGTH_LONG else Toast.LENGTH_SHORT,
        ).show()
        onShown()
    }
}

@Preview(showBackground = true)
@Composable
private fun IssueReportEditorPreview() {
    val dummyData = IssueReporterUiState(
        title = "Sample Bug Title",
        description = "This is a detailed description of the bug encounter in the sample application.",
        email = "user@example.com",
        deviceInfoText = "Device: Pixel 7\nOS: Android 14\nApp Version: 1.0.0",
    )

    MaterialTheme {
        Column(modifier = Modifier.padding(all = SizeConstants.LargeSize)) {
            IssueReportEditor(
                data = dummyData,
                firebaseController = PreviewFirebaseController,
                isSending = false,
                onEvent = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IssueSubmittedContentPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(all = SizeConstants.LargeSize)) {
            IssueSubmittedContent(
                onDone = {},
            )
        }
    }
}

/** Does nothing, so the previews can render composables that report what the author does. */
private object PreviewFirebaseController : FirebaseController {
    override fun updateConsent(analyticsGranted: Boolean, adStorageGranted: Boolean, adUserDataGranted: Boolean, adPersonalizationGranted: Boolean) = Unit
    override fun setAnalyticsEnabled(enabled: Boolean) = Unit
    override fun setCrashlyticsEnabled(enabled: Boolean) = Unit
    override fun setPerformanceEnabled(enabled: Boolean) = Unit
    override fun logBreadcrumb(message: String, attributes: Map<String, String>) = Unit
    override fun reportViewModelError(viewModelName: String, action: String, throwable: Throwable, extraKeys: Map<String, String>) = Unit
    override fun recordNonFatal(throwable: Throwable, attributes: Map<String, String>) = Unit
    override fun logEvent(event: AnalyticsEvent) = Unit
    override fun logScreenView(screenName: String, screenClass: String?) = Unit
    override fun setUserProperty(name: String, value: String?) = Unit
}
