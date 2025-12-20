package com.d4rk.android.libs.apptoolkit.app.issuereporter.ui

import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.IssueReportResult
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.providers.DeviceInfoProvider
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.usecases.SendIssueReportUseCase
import com.d4rk.android.libs.apptoolkit.app.issuereporter.ui.contract.IssueReporterEvent
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import com.google.common.truth.Truth.assertThat
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@OptIn(ExperimentalCoroutinesApi::class)
class IssueReporterViewModelTest {

    private val githubTarget = GithubTarget("user", "repo")
    private val deviceInfoProvider = object : DeviceInfoProvider {
        override suspend fun capture(): DeviceInfo = mockk()
    }

    @Test
    fun `invalid input shows error and does not call use case`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val useCase = mockk<SendIssueReportUseCase>()
            val viewModel = IssueReporterViewModel(useCase, githubTarget, "", deviceInfoProvider)
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }

            viewModel.onEvent(IssueReporterEvent.Send)
            advanceUntilIdle()

            val snackbar = viewModel.uiState.value.snackbar!!
            val msg = snackbar.message as UiTextHelper.StringResource
            assertThat(msg.resourceId).isEqualTo(R.string.error_invalid_report)
            verify(exactly = 0) { useCase.invoke(any()) } // FIXME: Flow is constructed but not used
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `send report success updates state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val useCase = mockk<SendIssueReportUseCase>()
            val captured = slot<SendIssueReportUseCase.Params>()
            every { useCase.invoke(capture(captured)) } returns flowOf(IssueReportResult.Success("url"))
            val viewModel = IssueReporterViewModel(useCase, githubTarget, "token", deviceInfoProvider)
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }

            viewModel.onEvent(IssueReporterEvent.UpdateTitle("Bug"))
            viewModel.onEvent(IssueReporterEvent.UpdateDescription("Desc"))
            advanceUntilIdle()
            viewModel.onEvent(IssueReporterEvent.Send)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            val snackbar = state.snackbar!!
            assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
            assertThat(state.data?.issueUrl).isEqualTo("url")
            assertThat((snackbar.message as UiTextHelper.StringResource).resourceId)
                .isEqualTo(R.string.snack_report_success)
            assertThat(captured.captured.token).isEqualTo("token")
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `update events modify state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val useCase = mockk<SendIssueReportUseCase>()
            val viewModel = IssueReporterViewModel(useCase, githubTarget, "", deviceInfoProvider)
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }

            viewModel.onEvent(IssueReporterEvent.UpdateTitle("T"))
            viewModel.onEvent(IssueReporterEvent.UpdateDescription("D"))
            viewModel.onEvent(IssueReporterEvent.UpdateEmail("E"))
            viewModel.onEvent(IssueReporterEvent.SetAnonymous(true))
            advanceUntilIdle()

            val data = viewModel.uiState.value.data!!
            assertThat(data.title).isEqualTo("T")
            assertThat(data.description).isEqualTo("D")
            assertThat(data.email).isEqualTo("E")
            assertThat(data.anonymous).isTrue()
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `device info failure shows error and skips use case`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        val failingProvider = object : DeviceInfoProvider {
            override suspend fun capture(): DeviceInfo {
                throw IllegalStateException("boom")
            }
        }
        try {
            val useCase = mockk<SendIssueReportUseCase>()
            val viewModel = IssueReporterViewModel(useCase, githubTarget, "", failingProvider)
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }

            viewModel.onEvent(IssueReporterEvent.UpdateTitle("Bug"))
            viewModel.onEvent(IssueReporterEvent.UpdateDescription("Desc"))
            advanceUntilIdle()
            viewModel.onEvent(IssueReporterEvent.Send)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            val snackbar = state.snackbar!!
            assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
            assertThat((snackbar.message as UiTextHelper.StringResource).resourceId)
                .isEqualTo(R.string.snack_report_failed)
            verify(exactly = 0) { useCase.invoke(any()) } // FIXME: Flow is constructed but not used
        } finally {
            Dispatchers.resetMain()
        }
    }

    companion object {
        @JvmStatic
        fun errorCases() = listOf(
            Arguments.of(HttpStatusCode.Unauthorized, R.string.error_unauthorized),
            Arguments.of(HttpStatusCode.Forbidden, R.string.error_forbidden),
            Arguments.of(HttpStatusCode.Gone, R.string.error_gone),
            Arguments.of(HttpStatusCode.UnprocessableEntity, R.string.error_unprocessable),
            Arguments.of(HttpStatusCode.InternalServerError, R.string.snack_report_failed),
        )
    }

    @ParameterizedTest
    @MethodSource("errorCases")
    fun `send report error maps message`(status: HttpStatusCode, expected: Int) = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val useCase = mockk<SendIssueReportUseCase>()
            every { useCase.invoke(any()) } returns flowOf(IssueReportResult.Error(status, ""))
            val viewModel = IssueReporterViewModel(useCase, githubTarget, "tok", deviceInfoProvider)
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }

            viewModel.onEvent(IssueReporterEvent.UpdateTitle("Bug"))
            viewModel.onEvent(IssueReporterEvent.UpdateDescription("Desc"))
            advanceUntilIdle()
            viewModel.onEvent(IssueReporterEvent.Send)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            val snackbar = state.snackbar!!
            assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
            assertThat((snackbar.message as UiTextHelper.StringResource).resourceId).isEqualTo(expected)
        } finally {
            Dispatchers.resetMain()
        }
    }
}
