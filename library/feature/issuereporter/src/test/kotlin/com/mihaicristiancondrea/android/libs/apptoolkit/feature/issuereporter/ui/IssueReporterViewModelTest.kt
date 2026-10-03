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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.GenericErrorText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.exceptions.IssueReportRejectedException
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.repositories.IssueReportHistoryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.repositories.IssueReporterRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.mappers.toPlainText
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.DeviceInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportFieldError
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportRefusal
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportValidation
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.Report
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.github.GithubTarget
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.usecases.ValidateIssueReportUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueReporterUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueSubmissionState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class IssueReporterViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()

        private const val ISSUE_URL = "https://github.com/user/repo/issues/1"
        private const val TITLE = "Crash when opening settings"
        private const val DESCRIPTION = "The app closes as soon as I open the settings page from the drawer."
        private const val EMAIL = "me@example.com"
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private fun createViewModel(
        repository: IssueReporterRepository = FakeIssueReporterRepository(),
        historyRepository: IssueReportHistoryRepository = FakeIssueReportHistoryRepository(),
        githubToken: String = "token",
    ): IssueReporterViewModel = IssueReporterViewModel(
        repository = repository,
        historyRepository = historyRepository,
        validateReport = ValidateIssueReportUseCase(),
        githubTarget = GithubTarget(username = "user", repository = "repo"),
        githubToken = githubToken,
        telemetryRepository = telemetryRepository,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private fun IssueReporterViewModel.writeDraft(
        title: String = TITLE,
        description: String = DESCRIPTION,
        email: String = EMAIL,
    ) {
        onEvent(IssueReporterEvent.UpdateTitle(title))
        onEvent(IssueReporterEvent.UpdateDescription(description))
        onEvent(IssueReporterEvent.UpdateEmail(email))
    }

    private fun IssueReporterViewModel.onlyMessage(): UiMessage = messages.value.single()

    private val UiMessage.resourceId: Int
        get() = (text as UiTextHelper.StringResource).resourceId

    private fun sendAttempts(): List<AnalyticsEvent> = telemetryRepository.loggedEvents.filter { event ->
        event.params["action_name"] == AnalyticsValue.Str("send_issue")
    }

    private fun AnalyticsEvent.validationFailure(): AnalyticsValue? = params["validation_failure"]

    @Test
    fun `the sheet opens with an empty draft and no message`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        assertEquals(IssueReporterUiState(), viewModel.state.value)
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `typing updates the draft`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()

        viewModel.onEvent(IssueReporterEvent.UpdateTitle("T"))
        viewModel.onEvent(IssueReporterEvent.UpdateDescription("D"))
        viewModel.onEvent(IssueReporterEvent.UpdateEmail("E"))
        advance()

        val state = viewModel.state.value
        assertEquals("T", state.title)
        assertEquals("D", state.description)
        assertEquals("E", state.email)
    }

    @Test
    fun `a blank report shows an error under every field and nothing is sent`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = FakeIssueReporterRepository()
            val history = FakeIssueReportHistoryRepository()
            val viewModel = createViewModel(repository = repository, historyRepository = history)

            viewModel.onEvent(IssueReporterEvent.Send)
            advance()

            assertEquals(
                IssueReportValidation(
                    titleError = IssueReportFieldError.Missing,
                    descriptionError = IssueReportFieldError.Missing,
                    emailError = IssueReportFieldError.Missing,
                ),
                viewModel.state.value.fieldErrors,
            )
            assertTrue(viewModel.messages.value.isEmpty())
            assertTrue(repository.sentReports.isEmpty())
            assertEquals(0, history.checks)
            assertEquals(IssueSubmissionState.Editing, viewModel.state.value.submissionState)
        }

    @Test
    fun `a report without an email is not sent`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeIssueReporterRepository()
        val viewModel = createViewModel(repository = repository)
        viewModel.writeDraft(email = "")

        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        assertEquals(
            IssueReportValidation(emailError = IssueReportFieldError.Missing),
            viewModel.state.value.fieldErrors,
        )
        assertTrue(repository.sentReports.isEmpty())
    }

    @Test
    fun `a malformed email is not sent`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeIssueReporterRepository()
        val viewModel = createViewModel(repository = repository)
        viewModel.writeDraft(email = "achrafachrafachref 123@gmail.com")

        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        assertEquals(IssueReportFieldError.InvalidEmail, viewModel.state.value.fieldErrors.emailError)
        assertTrue(repository.sentReports.isEmpty())
    }

    /** The shape of the garbage reports filed as #714 and #715. */
    @Test
    fun `a short unrelated report is not sent`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeIssueReporterRepository()
        val viewModel = createViewModel(repository = repository)
        viewModel.writeDraft(title = "Hello", description = "Good app")

        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        val errors = viewModel.state.value.fieldErrors
        assertEquals(IssueReportFieldError.TooShort(minimumLength = 10), errors.titleError)
        assertEquals(IssueReportFieldError.TooShort(minimumLength = 40), errors.descriptionError)
        assertTrue(repository.sentReports.isEmpty())
    }

    @Test
    fun `editing a field clears only its own error`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        viewModel.onEvent(IssueReporterEvent.UpdateEmail("m"))
        advance()

        val errors = viewModel.state.value.fieldErrors
        assertNull(errors.emailError)
        assertEquals(IssueReportFieldError.Missing, errors.titleError)
        assertEquals(IssueReportFieldError.Missing, errors.descriptionError)
    }

    @Test
    fun `a report in another script is sent`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeIssueReporterRepository()
        val viewModel = createViewModel(repository = repository)
        viewModel.writeDraft(
            title = "應用程式開啟設定時閃退",
            description = "每次我從主畫面開啟設定頁面時，應用程式都會立即關閉。重新安裝後問題仍然存在，請協助檢查這個錯誤。",
        )

        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        assertEquals("應用程式開啟設定時閃退", repository.sentReports.single().title)
    }

    @Test
    fun `sending files the trimmed draft, records it and shows the confirmation`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = FakeIssueReporterRepository()
            val history = FakeIssueReportHistoryRepository()
            val viewModel = createViewModel(repository = repository, historyRepository = history)
            viewModel.writeDraft(title = "  $TITLE  ", description = "\n$DESCRIPTION\n", email = " $EMAIL ")

            viewModel.onEvent(IssueReporterEvent.Send)
            advance()

            val state = viewModel.state.value
            assertEquals(IssueSubmissionState.Submitted(issueUrl = ISSUE_URL), state.submissionState)
            assertEquals(IssueReportValidation(), state.fieldErrors)
            assertTrue(viewModel.messages.value.isEmpty())
            val report = repository.sentReports.single()
            assertEquals(TITLE, report.title)
            assertTrue(report.getDescription().contains(DESCRIPTION))
            assertTrue(report.getDescription().contains("*Reported by [$EMAIL](mailto:$EMAIL)*"))
            assertEquals(listOf<String?>("token"), repository.sentTokens)
            assertEquals(listOf(TITLE to DESCRIPTION), history.recorded)
        }

    @Test
    fun `a report inside the cooldown is held back with a message`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeIssueReporterRepository()
        val history = FakeIssueReportHistoryRepository(refusal = IssueReportRefusal.COOLDOWN)
        val viewModel = createViewModel(repository = repository, historyRepository = history)
        viewModel.writeDraft()

        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        val message = viewModel.onlyMessage()
        assertEquals(R.string.error_report_cooldown, message.resourceId)
        assertTrue(message.isError)
        assertTrue(repository.sentReports.isEmpty())
        assertTrue(history.recorded.isEmpty())
        assertEquals(IssueSubmissionState.Editing, viewModel.state.value.submissionState)
        assertEquals(TITLE, viewModel.state.value.title)
    }

    @Test
    fun `a duplicate report is held back with a message`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeIssueReporterRepository()
        val viewModel = createViewModel(
            repository = repository,
            historyRepository = FakeIssueReportHistoryRepository(refusal = IssueReportRefusal.DUPLICATE),
        )
        viewModel.writeDraft()

        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        assertEquals(R.string.error_report_duplicate, viewModel.onlyMessage().resourceId)
        assertTrue(repository.sentReports.isEmpty())
    }

    @Test
    fun `a second report right after the first is held back`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeIssueReporterRepository()
        val history = FakeIssueReportHistoryRepository()
        val viewModel = createViewModel(repository = repository, historyRepository = history)
        viewModel.writeDraft()
        viewModel.onEvent(IssueReporterEvent.Send)
        advance()
        viewModel.onEvent(IssueReporterEvent.Reset)
        advance()

        history.refusal = IssueReportRefusal.COOLDOWN
        viewModel.writeDraft(title = "Another crash in settings")
        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        assertEquals(1, repository.sentReports.size)
        assertEquals(R.string.error_report_cooldown, viewModel.onlyMessage().resourceId)
    }

    @Test
    fun `each send attempt is logged with what stopped it, and never the text`() =
        runTest(dispatcherExtension.testDispatcher) {
            val history = FakeIssueReportHistoryRepository()
            val viewModel = createViewModel(historyRepository = history)

            viewModel.writeDraft(email = "achrafachrafachref 123@gmail.com")
            viewModel.onEvent(IssueReporterEvent.Send)
            advance()

            viewModel.writeDraft(description = "Too short")
            viewModel.onEvent(IssueReporterEvent.Send)
            advance()

            history.refusal = IssueReportRefusal.DUPLICATE
            viewModel.writeDraft()
            viewModel.onEvent(IssueReporterEvent.Send)
            advance()

            history.refusal = null
            viewModel.onEvent(IssueReporterEvent.Send)
            advance()

            val attempts = sendAttempts()
            assertEquals(
                listOf("email_invalid", "description_too_short", "duplicate", "none").map { AnalyticsValue.Str(it) },
                attempts.map { it.validationFailure() },
            )
            assertTrue(attempts.none { "has_email" in it.params })
            val logged: String = telemetryRepository.loggedEvents.joinToString { it.params.values.toString() }
            assertTrue(EMAIL !in logged && TITLE !in logged && "achraf" !in logged)
        }

    @Test
    fun `a blank token is sent as none`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeIssueReporterRepository()
        val viewModel = createViewModel(repository = repository, githubToken = "")
        viewModel.writeDraft()

        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        assertEquals(listOf<String?>(null), repository.sentTokens)
    }

    @Test
    fun `the sheet shows sending until the answer arrives`() = runTest(dispatcherExtension.testDispatcher) {
        val gate = CompletableDeferred<Unit>()
        val viewModel = createViewModel(repository = FakeIssueReporterRepository(sendGate = gate))
        viewModel.writeDraft()

        viewModel.onEvent(IssueReporterEvent.Send)
        advance()
        assertEquals(IssueSubmissionState.Sending, viewModel.state.value.submissionState)

        gate.complete(Unit)
        advance()
        assertEquals(IssueSubmissionState.Submitted(issueUrl = ISSUE_URL), viewModel.state.value.submissionState)
    }

    @Test
    fun `a second send while one is in flight is ignored`() = runTest(dispatcherExtension.testDispatcher) {
        val gate = CompletableDeferred<Unit>()
        val repository = FakeIssueReporterRepository(sendGate = gate)
        val viewModel = createViewModel(repository = repository)
        viewModel.writeDraft()

        viewModel.onEvent(IssueReporterEvent.Send)
        viewModel.onEvent(IssueReporterEvent.Send)
        gate.complete(Unit)
        advance()

        assertEquals(1, repository.sentReports.size)
    }

    @Test
    fun `a rejected token shows its own text and keeps the draft`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeIssueReporterRepository(
            sendFailure = IssueReportRejectedException(IssueReportRejectedException.Reason.UNAUTHORIZED),
        )
        val viewModel = createViewModel(repository = repository)
        viewModel.writeDraft()

        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        val state = viewModel.state.value
        assertEquals(IssueSubmissionState.Failed, state.submissionState)
        assertEquals(TITLE, state.title)
        val message = viewModel.onlyMessage()
        assertEquals(R.string.error_unauthorized, message.resourceId)
        assertTrue(message.isError)
    }

    @Test
    fun `a timeout shows the shared timeout text`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeIssueReporterRepository(
            sendFailure = NetworkException(reason = NetworkException.Reason.TIMEOUT),
        )
        val viewModel = createViewModel(repository = repository)
        viewModel.writeDraft()

        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        assertEquals(CoreUiR.string.screen_error_timeout, viewModel.onlyMessage().resourceId)
    }

    @Test
    fun `a failure with no text of its own shows the reporter's text and is reported`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = FakeIssueReporterRepository(
                sendFailure = NetworkException(reason = NetworkException.Reason.CLIENT),
            )
            val viewModel = createViewModel(repository = repository)
            viewModel.writeDraft()

            viewModel.onEvent(IssueReporterEvent.Send)
            advance()

            assertEquals(R.string.snack_report_failed, viewModel.onlyMessage().resourceId)
            assertTrue(telemetryRepository.loggedEvents.any { it.name == "vm_op_error" })
        }

    @Test
    fun `a failed device capture fails the send without sending`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeIssueReporterRepository(captureFailure = IllegalStateException("boom"))
        val viewModel = createViewModel(repository = repository)
        viewModel.writeDraft()

        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        assertEquals(IssueSubmissionState.Failed, viewModel.state.value.submissionState)
        assertEquals(R.string.snack_report_failed, viewModel.onlyMessage().resourceId)
        assertTrue(repository.sentReports.isEmpty())
    }

    @Test
    fun `sending again after a failure shows the confirmation`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeIssueReporterRepository(
            sendFailure = NetworkException(reason = NetworkException.Reason.NO_INTERNET),
        )
        val viewModel = createViewModel(repository = repository)
        viewModel.writeDraft()
        viewModel.onEvent(IssueReporterEvent.Send)
        advance()
        viewModel.messageShown(viewModel.onlyMessage().id)

        repository.sendFailure = null
        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        assertEquals(IssueSubmissionState.Submitted(issueUrl = ISSUE_URL), viewModel.state.value.submissionState)
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `opening the device panel captures the details once`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeIssueReporterRepository()
        val viewModel = createViewModel(repository = repository)

        viewModel.onEvent(IssueReporterEvent.RequestDeviceInfo)
        viewModel.onEvent(IssueReporterEvent.RequestDeviceInfo)
        advance()

        assertEquals(Loadable.Ready(deviceInfo().toPlainText()), viewModel.state.value.deviceInfo)
        assertEquals(1, repository.captures)
    }

    @Test
    fun `a failed device capture shows in the panel and runs again on the next opening`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = FakeIssueReporterRepository(captureFailure = IllegalStateException("boom"))
            val viewModel = createViewModel(repository = repository)

            viewModel.onEvent(IssueReporterEvent.RequestDeviceInfo)
            advance()
            assertEquals(Loadable.Failed(message = GenericErrorText), viewModel.state.value.deviceInfo)

            repository.captureFailure = null
            viewModel.onEvent(IssueReporterEvent.RequestDeviceInfo)
            advance()
            assertEquals(Loadable.Ready(deviceInfo().toPlainText()), viewModel.state.value.deviceInfo)
        }

    @Test
    fun `reset clears a submitted report`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        viewModel.writeDraft()
        viewModel.onEvent(IssueReporterEvent.RequestDeviceInfo)
        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        viewModel.onEvent(IssueReporterEvent.Reset)
        advance()

        assertEquals(IssueReporterUiState(), viewModel.state.value)
    }

    @Test
    fun `reset clears a message still waiting`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            historyRepository = FakeIssueReportHistoryRepository(refusal = IssueReportRefusal.COOLDOWN),
        )
        viewModel.writeDraft()
        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        viewModel.onEvent(IssueReporterEvent.Reset)
        advance()

        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `reset asked for during a send is applied once the report lands`() =
        runTest(dispatcherExtension.testDispatcher) {
            val gate = CompletableDeferred<Unit>()
            val repository = FakeIssueReporterRepository(sendGate = gate)
            val viewModel = createViewModel(repository = repository)
            viewModel.writeDraft()

            viewModel.onEvent(IssueReporterEvent.Send)
            viewModel.onEvent(IssueReporterEvent.Reset)
            advance()
            assertEquals(IssueSubmissionState.Sending, viewModel.state.value.submissionState)

            gate.complete(Unit)
            advance()
            assertEquals(1, repository.sentReports.size)
            assertEquals(IssueReporterUiState(), viewModel.state.value)
        }

    @Test
    fun `reset asked for during a failing send also drops its message`() =
        runTest(dispatcherExtension.testDispatcher) {
            val gate = CompletableDeferred<Unit>()
            val repository = FakeIssueReporterRepository(
                sendGate = gate,
                sendFailure = NetworkException(reason = NetworkException.Reason.SERVER),
            )
            val viewModel = createViewModel(repository = repository)
            viewModel.writeDraft()

            viewModel.onEvent(IssueReporterEvent.Send)
            viewModel.onEvent(IssueReporterEvent.Reset)
            gate.complete(Unit)
            advance()

            assertEquals(IssueReporterUiState(), viewModel.state.value)
            assertTrue(viewModel.messages.value.isEmpty())
        }

    @Test
    fun `submission states keep the screen_state labels`() {
        assertEquals("success", IssueSubmissionState.Editing.trackingLabel)
        assertEquals("loading", IssueSubmissionState.Sending.trackingLabel)
        assertEquals("error", IssueSubmissionState.Failed.trackingLabel)
        assertEquals("success", IssueSubmissionState.Submitted(issueUrl = ISSUE_URL).trackingLabel)
    }

    @Test
    fun `a shown message leaves the queue`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            historyRepository = FakeIssueReportHistoryRepository(refusal = IssueReportRefusal.COOLDOWN),
        )
        viewModel.writeDraft()
        viewModel.onEvent(IssueReporterEvent.Send)
        advance()

        viewModel.messageShown(viewModel.onlyMessage().id)

        assertNull(viewModel.messages.value.firstOrNull())
    }

    private fun deviceInfo(): DeviceInfo = DeviceInfo(
        appVersionName = "1.0.0",
        appVersionCode = 1L,
        buildVersion = "build",
        releaseVersion = "16",
        sdkVersion = 36,
        buildId = "id",
        brand = "brand",
        manufacturer = "manufacturer",
        device = "device",
        model = "model",
        product = "product",
        hardware = "hardware",
        abis = listOf("arm64-v8a"),
        abis32Bit = emptyList(),
        abis64Bit = listOf("arm64-v8a"),
    )

    private inner class FakeIssueReporterRepository(
        var sendFailure: Throwable? = null,
        var captureFailure: Throwable? = null,
        private val sendGate: CompletableDeferred<Unit>? = null,
    ) : IssueReporterRepository {
        val sentReports: MutableList<Report> = mutableListOf()
        val sentTokens: MutableList<String?> = mutableListOf()
        var captures: Int = 0

        override suspend fun captureDeviceInfo(): DeviceInfo {
            captures++
            captureFailure?.let { throw it }
            return deviceInfo()
        }

        override suspend fun sendReport(report: Report, target: GithubTarget, token: String?): String {
            sendGate?.await()
            sendFailure?.let { throw it }
            sentReports += report
            sentTokens += token
            return ISSUE_URL
        }
    }

    private class FakeIssueReportHistoryRepository(
        var refusal: IssueReportRefusal? = null,
    ) : IssueReportHistoryRepository {
        val recorded: MutableList<Pair<String, String>> = mutableListOf()
        var checks: Int = 0

        override suspend fun refusalFor(title: String, description: String): IssueReportRefusal? {
            checks++
            return refusal
        }

        override suspend fun recordSubmission(title: String, description: String) {
            recorded += title to description
        }
    }
}
