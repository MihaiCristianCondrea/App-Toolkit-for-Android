package com.d4rk.android.libs.apptoolkit.app.issuereporter.data.repository

import com.d4rk.android.libs.apptoolkit.app.issuereporter.data.remote.IssueReporterRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.IssueReportResult
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.Report
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.ExtraInfo
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.repository.IssueReporterRepository
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.google.common.truth.Truth.assertThat
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.net.SocketTimeoutException
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class TestIssueReporterRepository {

    private fun testDispatchers(scheduler: TestCoroutineScheduler) =
            TestDispatchers(StandardTestDispatcher(scheduler))

    private fun createRepository(
        client: HttpClient,
        scheduler: TestCoroutineScheduler,
    ): IssueReporterRepository = IssueReporterRepositoryImpl(
        remoteDataSource = IssueReporterRemoteDataSource(client),
        dispatchers = testDispatchers(scheduler),
    )

    // ✅ Pure-JVM fixture: no Android calls
    private fun report(
        title: String = "t",
        desc: String = "d",
        email: String? = null,
    ): Report = Report(
        title ,
        desc ,
        mockk(relaxed = true) ,
        ExtraInfo() ,
        email ,
    )

    private val target = GithubTarget("user", "repo")

    @Test
    fun `sendReport returns success`() = runTest {
        var capturedRequest: HttpRequestData? = null
        val engine = MockEngine { request ->
            capturedRequest = request
            respond("""{"html_url":"https://example.com/issue/1"}""", HttpStatusCode.Created)
        }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository = createRepository(client = client, scheduler = testScheduler)

        val result = repository.sendReport(report("title", "desc", "me@test.com"), target, token = "token123")

        assertThat(result).isInstanceOf(IssueReportResult.Success::class.java)
        assertThat((result as IssueReportResult.Success).url).isEqualTo("https://example.com/issue/1")
        assertThat(capturedRequest?.headers?.get(HttpHeaders.Authorization)).isEqualTo("Bearer token123")
    }

    @Test
    fun `sendReport returns error`() = runTest {
        val engine = MockEngine { respond("fail", HttpStatusCode.BadRequest) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository = createRepository(client = client, scheduler = testScheduler)

        val result = repository.sendReport(report(), target)

        assertThat(result).isInstanceOf(IssueReportResult.Error::class.java)
        val error = result as IssueReportResult.Error
        assertThat(error.status).isEqualTo(HttpStatusCode.BadRequest)
        assertThat(error.message).isEqualTo("fail")
    }

    @Test
    fun `sendReport without token omits header`() = runTest {
        var capturedRequest: HttpRequestData? = null
        val engine = MockEngine { request ->
            capturedRequest = request
            respond("""{"html_url":"https://example.com/issue/2"}""", HttpStatusCode.Created)
        }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository = createRepository(client = client, scheduler = testScheduler)

        val result = repository.sendReport(report(), target, token = null)

        assertThat(result).isInstanceOf(IssueReportResult.Success::class.java)
        assertThat(capturedRequest?.headers?.get(HttpHeaders.Authorization)).isNull()
    }

    @Test
    fun `sendReport network exception`() = runTest {
        val engine = MockEngine { throw SocketTimeoutException("timeout") }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository = createRepository(client = client, scheduler = testScheduler)

        assertFailsWith<SocketTimeoutException> {
            repository.sendReport(report(), target)
        }
    }

    @Test
    fun `sendReport malformed json`() = runTest {
        val engine = MockEngine { respond("{", HttpStatusCode.Created) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository = createRepository(client, testScheduler)

        assertFailsWith<kotlinx.serialization.SerializationException> {
            repository.sendReport(report(), target)
        }
    }

    @Test
    fun `sendReport unsupported status`() = runTest {
        val engine = MockEngine { respond("weird", HttpStatusCode.PaymentRequired) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository = createRepository(client, testScheduler)

        val result = repository.sendReport(report(), target)

        assertThat(result).isInstanceOf(IssueReportResult.Error::class.java)
        val error = result as IssueReportResult.Error
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
        val repository = createRepository(client, testScheduler)

        repository.sendReport(report(), target)

        assertThat(capturedRequest?.headers?.get(HttpHeaders.Accept))
                .isEqualTo("application/vnd.github+json")
    }

    @Test
    fun `sendReport handles bad gateway`() = runTest {
        val engine = MockEngine { respond("broke", HttpStatusCode.BadGateway) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository = createRepository(client, testScheduler)

        val result = repository.sendReport(report(), target)

        assertThat(result).isInstanceOf(IssueReportResult.Error::class.java)
        val error = result as IssueReportResult.Error
        assertThat(error.status).isEqualTo(HttpStatusCode.BadGateway)
        assertThat(error.message).isEqualTo("broke")
    }

    @Test
    fun `sendReport handles teapot`() = runTest {
        val engine = MockEngine { respond("hot", HttpStatusCode.fromValue(418)) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository = createRepository(client, testScheduler)

        val result = repository.sendReport(report(), target)

        assertThat(result).isInstanceOf(IssueReportResult.Error::class.java)
        val error = result as IssueReportResult.Error
        assertThat(error.status).isEqualTo(HttpStatusCode.fromValue(418))
        assertThat(error.message).isEqualTo("hot")
    }

    @Test
    fun `sendReport null pointer exception`() = runTest {
        val engine = MockEngine { throw NullPointerException("boom") }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository = createRepository(client, testScheduler)

        assertFailsWith<NullPointerException> {
            repository.sendReport(report(), target)
        }
    }

    @Test
    fun `sendReport illegal state exception`() = runTest {
        val engine = MockEngine { throw IllegalStateException("illegal") }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository = createRepository(client, testScheduler)

        assertFailsWith<IllegalStateException> {
            repository.sendReport(report(), target)
        }
    }

    @Test
    fun `sendReport unauthorized`() = runTest {
        val engine = MockEngine { respond("unauth", HttpStatusCode.Unauthorized) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository = createRepository(client, testScheduler)

        val result = repository.sendReport(report(), target)

        assertThat(result).isInstanceOf(IssueReportResult.Error::class.java)
        val error = result as IssueReportResult.Error
        assertThat(error.status).isEqualTo(HttpStatusCode.Unauthorized)
        assertThat(error.message).isEqualTo("unauth")
    }

    @Test
    fun `sendReport forbidden`() = runTest {
        val engine = MockEngine { respond("stop", HttpStatusCode.Forbidden) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository = createRepository(client, testScheduler)

        val result = repository.sendReport(report(), target)

        assertThat(result).isInstanceOf(IssueReportResult.Error::class.java)
        val error = result as IssueReportResult.Error
        assertThat(error.status).isEqualTo(HttpStatusCode.Forbidden)
        assertThat(error.message).isEqualTo("stop")
    }

    @Test
    fun `sendReport created without url`() = runTest {
        val engine = MockEngine { respond("{}", HttpStatusCode.Created) }
        val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
        val repository = createRepository(client = client, scheduler = testScheduler)

        val result = repository.sendReport(report(), target)

        assertThat(result).isInstanceOf(IssueReportResult.Success::class.java)
        val success = result as IssueReportResult.Success
        assertThat(success.url).isEmpty()
    }
}
