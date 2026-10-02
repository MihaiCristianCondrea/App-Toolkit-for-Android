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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.repositories.IssueReporterRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.di.GithubToken
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.mappers.toPlainText
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.github.GithubTarget
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.mappers.toReport
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.mappers.toSendFailedMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueReporterUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueSubmissionState
import kotlinx.coroutines.Job

/**
 * Owns the report draft and submission state. Form fields survive submission and are cleared on
 * dismissal; a reset requested during sending is deferred until that send finishes. Success is
 * rendered by the confirmation state rather than a transient message.
 *
 * [IssueReporterRepository] is main-safe, so this needs no dispatcher.
 */
class IssueReporterViewModel(
    private val repository: IssueReporterRepository,
    private val githubTarget: GithubTarget,
    @param:GithubToken private val githubToken: String,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<IssueReporterUiState, IssueReporterEvent>(
    initialState = IssueReporterUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "IssueReporter",
    viewModelName = "IssueReporterViewModel",
) {
    private var sendJob: Job? = null
    private var deviceInfoJob: Job? = null
    private var resetAfterSend: Boolean = false

    override fun handleEvent(event: IssueReporterEvent) {
        when (event) {
            is IssueReporterEvent.UpdateTitle -> setState { copy(title = event.value) }
            is IssueReporterEvent.UpdateDescription -> setState { copy(description = event.value) }
            is IssueReporterEvent.UpdateEmail -> setState { copy(email = event.value) }
            IssueReporterEvent.RequestDeviceInfo -> loadDeviceInfo()
            IssueReporterEvent.Send -> sendReport()
            IssueReporterEvent.Reset -> resetReport()
        }
    }

    /**
     * Captures the panel's device details once per report. A failed capture shows in the panel and
     * runs again the next time the panel opens.
     */
    private fun loadDeviceInfo() {
        val deviceInfo: Loadable<String> = currentState.deviceInfo
        if (deviceInfo is Loadable.Ready || deviceInfo is Loadable.Loading) return

        deviceInfoJob = deviceInfoJob.restart {
            launchReport(
                action = Actions.LOAD_DEVICE_INFO,
                onError = { error -> setState { copy(deviceInfo = error.toFailed()) } },
            ) {
                setState { copy(deviceInfo = Loadable.Loading) }
                val text: String = repository.captureDeviceInfo().toPlainText()
                setState { copy(deviceInfo = Loadable.Ready(text)) }
            }
        }
    }

    /**
     * Files the draft as it stands when sent. A blank title or description is refused with a
     * message, and a send already in flight makes this a no-op. A failure returns to the editor
     * with the draft intact.
     */
    private fun sendReport() {
        if (sendJob?.isActive == true) return

        val draft: IssueReporterUiState = currentState
        if (draft.title.isBlank() || draft.description.isBlank()) {
            showMessage(UiMessage(text = InvalidReportText, isError = true))
            return
        }

        sendJob = launchReport(
            action = Actions.SEND_REPORT,
            extra = mapOf(
                ExtraKeys.HAS_TITLE to draft.title.isNotBlank().toString(),
                ExtraKeys.HAS_DESCRIPTION to draft.description.isNotBlank().toString(),
            ),
            onError = { error ->
                setState { copy(submissionState = IssueSubmissionState.Failed) }
                showMessage(error.toSendFailedMessage())
                applyPendingReset()
            },
        ) {
            setState { copy(submissionState = IssueSubmissionState.Sending) }
            val issueUrl: String = repository.sendReport(
                report = draft.toReport(deviceInfo = repository.captureDeviceInfo()),
                target = githubTarget,
                token = githubToken.takeIf { it.isNotBlank() },
            )
            setState { copy(submissionState = IssueSubmissionState.Submitted(issueUrl = issueUrl)) }
            applyPendingReset()
        }
    }

    /**
     * Clears the report and any message still waiting. A send in flight is left to finish, since
     * closing the sheet does not take the report back, and the reset runs once it lands.
     */
    private fun resetReport() {
        if (sendJob?.isActive == true) {
            resetAfterSend = true
            return
        }
        applyReset()
    }

    private fun applyPendingReset() {
        if (!resetAfterSend) return
        resetAfterSend = false
        applyReset()
    }

    private fun applyReset() {
        deviceInfoJob?.cancel()
        setState { IssueReporterUiState() }
        messages.value.forEach { message -> messageShown(message.id) }
    }

    private object Actions {
        const val SEND_REPORT: String = "sendReport"
        const val LOAD_DEVICE_INFO: String = "loadDeviceInfo"
    }

    private object ExtraKeys {
        const val HAS_TITLE: String = "hasTitle"
        const val HAS_DESCRIPTION: String = "hasDescription"
    }

    private companion object {
        val InvalidReportText = UiTextHelper.StringResource(R.string.error_invalid_report)
    }
}
