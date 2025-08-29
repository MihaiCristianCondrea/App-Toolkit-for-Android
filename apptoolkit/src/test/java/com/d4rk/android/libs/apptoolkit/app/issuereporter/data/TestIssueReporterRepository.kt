package com.d4rk.android.libs.apptoolkit.app.issuereporter.data

import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.repository.IssueReporterRepository
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.IssueReportResult
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.Report
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.ExtraInfo
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.AppDispatchers
import com.google.common.truth.Truth.assertThat
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.net.SocketTimeoutException
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class TestIssueReporterRepository {

    private fun testDispatchers(scheduler: TestCoroutineScheduler) = object : AppDispatchers {
        private val dispatcher = StandardTestDispatcher(scheduler)
        override val io = dispatcher
        override val default = dispatcher
        override val main = dispatcher
    }

    @Test
    fun `sendReport returns success`() = runTest {
        println("\uD83D\uDE80 [TEST] repository success")
        var capturedRequest: HttpRequestData? = null
        val engine = MockEngine { request ->
            capturedRequest = request
            val body = """{"html_url":"https://example.com/issue/1"}"""
            respond(content = body, status = HttpStatusCode.Created)
        }
        val client = HttpClient(engine) {
            install(ContentNegotiation) { json() }
        }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("title", "desc", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), "me@test.com")
        val target = GithubTarget("user", "repo")
        val result = repository.sendReport(report, target, token = "token123")

        assertThat(result).isInstanceOf(IssueReportResult.Success::class.java)
        assertThat((result as IssueReportResult.Success).url).isEqualTo("https://example.com/issue/1")
        assertThat(capturedRequest?.headers?.get(HttpHeaders.Authorization)).isEqualTo("Bearer token123")
        println("\uD83C\uDFC1 [TEST DONE] repository success")
    }

    @Test
    fun `sendReport returns error`() = runTest {
        println("\uD83D\uDE80 [TEST] repository error")
        val engine = MockEngine { respond("fail", HttpStatusCode.BadRequest) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("t", "d", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), null)
        val target = GithubTarget("user", "repo")
        val result = repository.sendReport(report, target)
        assertThat(result).isInstanceOf(IssueReportResult.Error.BadRequest::class.java)
        val error = result as IssueReportResult.Error.BadRequest
        assertThat(error.message).isEqualTo("fail")
        println("\uD83C\uDFC1 [TEST DONE] repository error")
    }

    @Test
    fun `sendReport without token omits header`() = runTest {
        var capturedRequest: HttpRequestData? = null
        val engine = MockEngine { request ->
            capturedRequest = request
            respond("""{"html_url":"https://example.com/issue/2"}""", HttpStatusCode.Created)
        }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("t", "d", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), null)
        val target = GithubTarget("user", "repo")

        val result = repository.sendReport(report, target, token = null)

        assertThat(result).isInstanceOf(IssueReportResult.Success::class.java)
        assertThat(capturedRequest?.headers?.get(HttpHeaders.Authorization)).isNull()
    }

    @Test
    fun `sendReport network exception`() = runTest {
        val engine = MockEngine { throw SocketTimeoutException("timeout") }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("t", "d", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), null)
        val target = GithubTarget("user", "repo")
        val result = repository.sendReport(report, target)
        assertThat(result).isInstanceOf(IssueReportResult.Error.Network::class.java)
    }

    @Test
    fun `sendReport malformed json`() = runTest {
        val engine = MockEngine { respond("{", HttpStatusCode.Created) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("t", "d", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), null)
        val target = GithubTarget("user", "repo")
        val result = repository.sendReport(report, target)
        assertThat(result).isInstanceOf(IssueReportResult.Error.Serialization::class.java)
    }

    @Test
    fun `sendReport unsupported status`() = runTest {
        val engine = MockEngine { respond("weird", HttpStatusCode.PaymentRequired) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("t", "d", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), null)
        val target = GithubTarget("user", "repo")

        val result = repository.sendReport(report, target)
        assertThat(result).isInstanceOf(IssueReportResult.Error.Unknown::class.java)
        val error = result as IssueReportResult.Error.Unknown
        assertThat(error.status).isEqualTo(HttpStatusCode.PaymentRequired)
        assertThat(error.message).isEqualTo("weird")
    }

    @Test
    fun `sendReport includes accept header`() = runTest {
        var capturedRequest: HttpRequestData? = null
        val engine = MockEngine { request ->
            capturedRequest = request
            respond("""{"html_url":"https://ex.com/1"}""", HttpStatusCode.Created)
        }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("t", "d", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), null)
        val target = GithubTarget("user", "repo")

        repository.sendReport(report, target)
        assertThat(capturedRequest?.headers?.get(HttpHeaders.Accept)).isEqualTo("application/vnd.github+json")
    }

    @Test
    fun `sendReport handles bad gateway`() = runTest {
        val engine = MockEngine { respond("broke", HttpStatusCode.BadGateway) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("t", "d", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), null)
        val target = GithubTarget("user", "repo")

        val result = repository.sendReport(report, target)
        assertThat(result).isInstanceOf(IssueReportResult.Error.Unknown::class.java)
        val error = result as IssueReportResult.Error.Unknown
        assertThat(error.status).isEqualTo(HttpStatusCode.BadGateway)
        assertThat(error.message).isEqualTo("broke")
    }

    @Test
    fun `sendReport handles teapot`() = runTest {
        val engine = MockEngine { respond("hot", HttpStatusCode.fromValue(418)) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("t", "d", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), null)
        val target = GithubTarget("user", "repo")

        val result = repository.sendReport(report, target)
        assertThat(result).isInstanceOf(IssueReportResult.Error.Unknown::class.java)
        val error = result as IssueReportResult.Error.Unknown
        assertThat(error.status).isEqualTo(HttpStatusCode.fromValue(418))
        assertThat(error.message).isEqualTo("hot")
    }

    @Test
    fun `sendReport null pointer exception`() = runTest {
        val engine = MockEngine { throw NullPointerException("boom") }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("t", "d", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), null)
        val target = GithubTarget("user", "repo")

        assertFailsWith<NullPointerException> {
            repository.sendReport(report, target)
        }
    }

    @Test
    fun `sendReport illegal state exception`() = runTest {
        val engine = MockEngine { throw IllegalStateException("illegal") }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("t", "d", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), null)
        val target = GithubTarget("user", "repo")

        assertFailsWith<IllegalStateException> {
            repository.sendReport(report, target)
        }
    }

    @Test
    fun `sendReport unauthorized`() = runTest {
        val engine = MockEngine { respond("unauth", HttpStatusCode.Unauthorized) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("t", "d", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), null)
        val target = GithubTarget("user", "repo")

        val result = repository.sendReport(report, target)
        assertThat(result).isInstanceOf(IssueReportResult.Error.Unauthorized::class.java)
        val error = result as IssueReportResult.Error.Unauthorized
        assertThat(error.message).isEqualTo("unauth")
    }

    @Test
    fun `sendReport forbidden`() = runTest {
        val engine = MockEngine { respond("stop", HttpStatusCode.Forbidden) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("t", "d", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), null)
        val target = GithubTarget("user", "repo")

        val result = repository.sendReport(report, target)
        assertThat(result).isInstanceOf(IssueReportResult.Error.Forbidden::class.java)
        val error = result as IssueReportResult.Error.Forbidden
        assertThat(error.message).isEqualTo("stop")
    }

    @Test
    fun `sendReport created without url`() = runTest {
        val engine = MockEngine { respond("{}", HttpStatusCode.Created) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository: IssueReporterRepository = DefaultIssueReporterRepository(client, testDispatchers(testScheduler))
        val report = Report("t", "d", com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo.create(android.app.Application()), ExtraInfo(), null)
        val target = GithubTarget("user", "repo")

        val result = repository.sendReport(report, target)
        assertThat(result).isInstanceOf(IssueReportResult.Success::class.java)
        val success = result as IssueReportResult.Success
        assertThat(success.url).isEmpty()
    }
}

