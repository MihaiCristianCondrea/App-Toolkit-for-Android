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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.repositories.IssueReportHistoryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.repositories.IssueReporterRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.di.GithubToken
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.mappers.toPlainText
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportRefusal
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportValidation
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.github.GithubTarget
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.usecases.ValidateIssueReportUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.analytics.ISSUE_REPORTER_SCREEN_NAME
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.analytics.IssueReporterActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.analytics.IssueReporterParams
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.analytics.analyticsName
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.analytics.failureName
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.analytics.issueReporterActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.mappers.toMessage
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
 * A send passes three gates before it reaches GitHub: [validateReport], whose errors show under the
 * fields, then the cooldown and the duplicate check of [historyRepository], which show as a
 * message. Each attempt is logged once as `send_issue` with the gate that stopped it.
 *
 * Both repositories are main-safe, so this needs no dispatcher.
 */
class IssueReporterViewModel(
    private val repository: IssueReporterRepository,
    private val historyRepository: IssueReportHistoryRepository,
    private val validateReport: ValidateIssueReportUseCase,
    private val githubTarget: GithubTarget,
    @param:GithubToken private val githubToken: String,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<IssueReporterUiState, IssueReporterEvent>(
    initialState = IssueReporterUiState(),
    telemetryRepository = telemetryRepository,
    screenName = ISSUE_REPORTER_SCREEN_NAME,
    viewModelName = "IssueReporterViewModel",
) {
    private var sendJob: Job? = null
    private var deviceInfoJob: Job? = null
    private var resetAfterSend: Boolean = false

    override fun handleEvent(event: IssueReporterEvent) {
        when (event) {
            is IssueReporterEvent.UpdateTitle -> setState {
                copy(title = event.value, fieldErrors = fieldErrors.copy(titleError = null))
            }

            is IssueReporterEvent.UpdateDescription -> setState {
                copy(description = event.value, fieldErrors = fieldErrors.copy(descriptionError = null))
            }

            is IssueReporterEvent.UpdateEmail -> setState {
                copy(email = event.value, fieldErrors = fieldErrors.copy(emailError = null))
            }

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
     * Files the draft, trimmed, as it stands when sent. An invalid draft shows its field errors
     * and a report held back by the cooldown or as a duplicate shows a message; neither is sent.
     * A send already in flight makes this a no-op. A failure returns to the editor with the draft
     * intact.
     */
    private fun sendReport() {
        if (sendJob?.isActive == true) return

        val draft: IssueReporterUiState = currentState
        val validation: IssueReportValidation = validateReport(
            title = draft.title,
            description = draft.description,
            email = draft.email,
        )
        setState { copy(fieldErrors = validation) }
        if (!validation.isValid) {
            logSendAttempt(draft = draft, failure = validation.failureName())
            return
        }

        sendJob = launchReport(
            action = Actions.SEND_REPORT,
            onError = { error ->
                setState { copy(submissionState = IssueSubmissionState.Failed) }
                showMessage(error.toSendFailedMessage())
                applyPendingReset()
            },
        ) {
            val title: String = draft.title.trim()
            val description: String = draft.description.trim()
            val refusal: IssueReportRefusal? = historyRepository.refusalFor(title = title, description = description)
            logSendAttempt(draft = draft, failure = refusal?.analyticsName)
            if (refusal != null) {
                showMessage(refusal.toMessage())
                applyPendingReset()
                return@launchReport
            }

            setState { copy(submissionState = IssueSubmissionState.Sending) }
            val issueUrl: String = repository.sendReport(
                report = draft.toReport(deviceInfo = repository.captureDeviceInfo()),
                target = githubTarget,
                token = githubToken.takeIf { it.isNotBlank() },
            )
            historyRepository.recordSubmission(title = title, description = description)
            setState { copy(submissionState = IssueSubmissionState.Submitted(issueUrl = issueUrl)) }
            applyPendingReset()
        }
    }

    /**
     * Logs a tap on Send with the gate that stopped it, or [IssueReporterParams.VALIDATION_PASSED].
     * Only lengths and the failure's name are sent, never the text or the email.
     */
    private fun logSendAttempt(draft: IssueReporterUiState, failure: String?) {
        telemetryRepository.logEvent(
            issueReporterActionEvent(
                actionName = IssueReporterActionNames.SEND_ISSUE,
                params = mapOf(
                    IssueReporterParams.TITLE_LENGTH to AnalyticsValue.LongVal(draft.title.length.toLong()),
                    IssueReporterParams.DESCRIPTION_LENGTH to
                        AnalyticsValue.LongVal(draft.description.length.toLong()),
                    IssueReporterParams.VALIDATION_FAILURE to
                        AnalyticsValue.Str(failure ?: IssueReporterParams.VALIDATION_PASSED),
                ),
            ),
        )
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
}
