package com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.IssueReportResult
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.Report
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.ExtraInfo
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.repository.IssueReporterRepository
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.google.common.truth.Truth.assertThat
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class SendIssueReportUseCaseTest {

    private val dispatcher = StandardTestDispatcher()
    private val dispatchers = TestDispatchers(dispatcher)

    private val params = SendIssueReportUseCase.Params(
        report = Report("t", "d", mockk(), ExtraInfo(), null),
        target = GithubTarget("user", "repo"),
        token = null
    )

    @Test
    fun `invoke success returns success`() = runTest(dispatcher) {
        val repository = mockk<IssueReporterRepository>()
        coEvery {
            repository.sendReport(
                any(),
                any(),
                any()
            )
        } returns IssueReportResult.Success("url")

        val useCase = SendIssueReportUseCase(repository, dispatchers)
        val result = useCase(params).first()

        assertThat(result).isInstanceOf(IssueReportResult.Success::class.java)
        assertThat((result as IssueReportResult.Success).url).isEqualTo("url")
    }

    @Test
    fun `invoke error maps exception`() = runTest(dispatcher) {
        val repository = mockk<IssueReporterRepository>()
        coEvery { repository.sendReport(any(), any(), any()) } throws IllegalStateException("boom")

        val useCase = SendIssueReportUseCase(repository, dispatchers)
        val result = useCase(params).first()

        assertThat(result).isInstanceOf(IssueReportResult.Error::class.java)
        val error = result as IssueReportResult.Error
        assertThat(error.status).isEqualTo(HttpStatusCode.InternalServerError)
        assertThat(error.message).isEqualTo("boom")
    }

    @Test
    fun `invoke cancellation rethrows`() = runTest(dispatcher) {
        val repository = mockk<IssueReporterRepository>()
        coEvery {
            repository.sendReport(
                any(),
                any(),
                any()
            )
        } throws CancellationException("cancel")

        val useCase = SendIssueReportUseCase(repository, dispatchers)

        assertFailsWith<CancellationException> {
            useCase(params).first()
        }
    }
}

