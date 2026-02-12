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

package com.d4rk.android.libs.apptoolkit.app.issuereporter.ui

import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.IssueReportResult
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.providers.DeviceInfoProvider
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.usecases.SendIssueReportUseCase
import com.d4rk.android.libs.apptoolkit.app.issuereporter.ui.contract.IssueReporterEvent
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.FakeFirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import com.google.common.truth.Truth.assertThat
import io.ktor.http.HttpStatusCode
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.CoroutineDispatcher
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
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@OptIn(ExperimentalCoroutinesApi::class)
class IssueReporterViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()

        @JvmStatic
        fun errorCases() = listOf(
            Arguments.of(HttpStatusCode.Unauthorized, R.string.error_unauthorized),
            Arguments.of(HttpStatusCode.Forbidden, R.string.error_forbidden),
            Arguments.of(HttpStatusCode.Gone, R.string.error_gone),
            Arguments.of(HttpStatusCode.UnprocessableEntity, R.string.error_unprocessable),
            Arguments.of(HttpStatusCode.InternalServerError, R.string.snack_report_failed),
        )
    }

    private val githubTarget = GithubTarget("user", "repo")
    private val deviceInfoProvider = object : DeviceInfoProvider {
        override suspend fun capture(): DeviceInfo = mockk()
    }
    private val firebaseController = FakeFirebaseController()

    private inline fun <T> withMainDispatcher(
        dispatcher: CoroutineDispatcher,
        block: () -> T
    ): T {
        Dispatchers.setMain(dispatcher)
        return runCatching { block() }
            .also {
                runCatching { Dispatchers.resetMain() }
            }
            .getOrThrow()
    }

    @Test
    fun `invalid input shows error and does not call use case`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)

        withMainDispatcher(dispatcher) {
            val useCase = mockk<SendIssueReportUseCase>()
            val dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)
            val viewModel = IssueReporterViewModel(
                sendIssueReport = useCase,
                githubTarget = githubTarget,
                githubToken = "",
                deviceInfoProvider = deviceInfoProvider,
                firebaseController = firebaseController,
                dispatchers = dispatchers,
            )
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }

            viewModel.onEvent(IssueReporterEvent.Send)
            advanceUntilIdle()

            val snackbar = viewModel.uiState.value.snackbar!!
            val msg = snackbar.message as UiTextHelper.StringResource
            assertThat(msg.resourceId).isEqualTo(R.string.error_invalid_report)
            confirmVerified(useCase)
        }
    }

    @Test
    fun `send report success updates state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)

        withMainDispatcher(dispatcher) {
            val useCase = mockk<SendIssueReportUseCase>()
            val captured = slot<SendIssueReportUseCase.Params>()
            every { useCase.invoke(capture(captured)) } returns flowOf(IssueReportResult.Success("url"))
            val dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)
            val viewModel = IssueReporterViewModel(
                sendIssueReport = useCase,
                githubTarget = githubTarget,
                githubToken = "token",
                deviceInfoProvider = deviceInfoProvider,
                firebaseController = firebaseController,
                dispatchers = dispatchers,
            )
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
        }
    }

    @Test
    fun `update events modify state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)

        withMainDispatcher(dispatcher) {
            val useCase = mockk<SendIssueReportUseCase>()
            val dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)
            val viewModel = IssueReporterViewModel(
                sendIssueReport = useCase,
                githubTarget = githubTarget,
                githubToken = "",
                deviceInfoProvider = deviceInfoProvider,
                firebaseController = firebaseController,
                dispatchers = dispatchers,
            )
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
        }
    }

    @Test
    fun `device info failure shows error and skips use case`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)

        val failingProvider = object : DeviceInfoProvider {
            override suspend fun capture(): DeviceInfo {
                throw IllegalStateException("boom")
            }
        }

        withMainDispatcher(dispatcher) {
            val useCase = mockk<SendIssueReportUseCase>()
            val dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)
            val viewModel = IssueReporterViewModel(
                sendIssueReport = useCase,
                githubTarget = githubTarget,
                githubToken = "",
                deviceInfoProvider = failingProvider,
                firebaseController = firebaseController,
                dispatchers = dispatchers,
            )
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
            confirmVerified(useCase)
        }
    }

    @ParameterizedTest
    @MethodSource("errorCases")
    fun `send report error maps message`(status: HttpStatusCode, expected: Int) = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)

        withMainDispatcher(dispatcher) {
            val useCase = mockk<SendIssueReportUseCase>()
            every { useCase.invoke(any()) } returns flowOf(IssueReportResult.Error(status, ""))

            val dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)
            val viewModel = IssueReporterViewModel(
                sendIssueReport = useCase,
                githubTarget = githubTarget,
                githubToken = "tok",
                deviceInfoProvider = deviceInfoProvider,
                firebaseController = firebaseController,
                dispatchers = dispatchers,
            )
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }

            viewModel.onEvent(IssueReporterEvent.UpdateTitle("Bug"))
            viewModel.onEvent(IssueReporterEvent.UpdateDescription("Desc"))
            advanceUntilIdle()
            viewModel.onEvent(IssueReporterEvent.Send)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            val snackbar = state.snackbar!!
            assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
            assertThat((snackbar.message as UiTextHelper.StringResource).resourceId).isEqualTo(
                expected
            )
        }
    }
}
