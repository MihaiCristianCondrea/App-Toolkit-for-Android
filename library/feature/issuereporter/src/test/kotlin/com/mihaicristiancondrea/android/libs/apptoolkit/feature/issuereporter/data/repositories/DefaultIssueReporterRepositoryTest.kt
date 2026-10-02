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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.exceptions.IssueReportRejectedException
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.remote.IssueReporterRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.DeviceInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.Report
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.github.ExtraInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.github.GithubTarget
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.net.SocketTimeoutException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultIssueReporterRepositoryTest {

    companion object {
        @JvmStatic
        fun rejections(): List<Arguments> = listOf(
            Arguments.of(HttpStatusCode.Unauthorized, IssueReportRejectedException.Reason.UNAUTHORIZED),
            Arguments.of(HttpStatusCode.Forbidden, IssueReportRejectedException.Reason.FORBIDDEN),
            Arguments.of(HttpStatusCode.Gone, IssueReportRejectedException.Reason.GONE),
            Arguments.of(HttpStatusCode.UnprocessableEntity, IssueReportRejectedException.Reason.UNPROCESSABLE),
        )

        @JvmStatic
        fun failedAnswers(): List<Arguments> = listOf(
            Arguments.of(HttpStatusCode.BadRequest, NetworkException.Reason.CLIENT),
            Arguments.of(HttpStatusCode.PaymentRequired, NetworkException.Reason.CLIENT),
            Arguments.of(HttpStatusCode.fromValue(418), NetworkException.Reason.CLIENT),
            Arguments.of(HttpStatusCode.TooManyRequests, NetworkException.Reason.RATE_LIMITED),
            Arguments.of(HttpStatusCode.BadGateway, NetworkException.Reason.SERVER),
            Arguments.of(HttpStatusCode.OK, NetworkException.Reason.UNEXPECTED_RESPONSE),
        )
    }

    private val target = GithubTarget(username = "user", repository = "repo")

    private fun createRepository(handler: MockRequestHandler): IssueReporterRepository =
        DefaultIssueReporterRepository(
            remoteDataSource = IssueReporterRemoteDataSource(client = HttpClient(MockEngine(handler))),
            deviceInfoProvider = { deviceInfo() },
            telemetryRepository = FakeTelemetryRepository(),
        )

    private fun report(email: String? = null): Report = Report(
        title = "t",
        description = "d",
        deviceInfo = deviceInfo(),
        extraInfo = ExtraInfo(),
        email = email,
    )

    @Test
    fun `a filed report returns the issue url and sends the token`() = runTest {
        var request: HttpRequestData? = null
        val repository = createRepository { captured ->
            request = captured
            respond("""{"html_url":"https://example.com/issue/1"}""", HttpStatusCode.Created)
        }

        val url = repository.sendReport(report(email = "me@test.com"), target, token = "token123")

        assertEquals("https://example.com/issue/1", url)
        assertEquals("Bearer token123", request?.headers?.get(HttpHeaders.Authorization))
        assertEquals("application/vnd.github+json", request?.headers?.get(HttpHeaders.Accept))
        assertEquals("https://api.github.com/repos/user/repo/issues", request?.url?.toString())
    }

    @Test
    fun `a report without a token sends no authorization header`() = runTest {
        var request: HttpRequestData? = null
        val repository = createRepository { captured ->
            request = captured
            respond("""{"html_url":"https://example.com/issue/2"}""", HttpStatusCode.Created)
        }

        repository.sendReport(report(), target, token = null)

        assertNull(request?.headers?.get(HttpHeaders.Authorization))
    }

    @Test
    fun `a filed report without a url returns an empty one`() = runTest {
        val repository = createRepository { respond("{}", HttpStatusCode.Created) }

        assertEquals("", repository.sendReport(report(), target))
    }

    @ParameterizedTest
    @MethodSource("rejections")
    fun `a refusal github explains throws its reason`(
        status: HttpStatusCode,
        expected: IssueReportRejectedException.Reason,
    ) = runTest {
        val repository = createRepository { respond("refused", status) }

        val failure = assertFailsWith<IssueReportRejectedException> { repository.sendReport(report(), target) }

        assertEquals(expected, failure.reason)
    }

    @ParameterizedTest
    @MethodSource("failedAnswers")
    fun `any other answer throws a network exception`(
        status: HttpStatusCode,
        expected: NetworkException.Reason,
    ) = runTest {
        val repository = createRepository { respond("failed", status) }

        val failure = assertFailsWith<NetworkException> { repository.sendReport(report(), target) }

        assertEquals(expected, failure.reason)
    }

    @Test
    fun `a malformed answer throws a serialization failure`() = runTest {
        val repository = createRepository { respond("{", HttpStatusCode.Created) }

        val failure = assertFailsWith<NetworkException> { repository.sendReport(report(), target) }

        assertEquals(NetworkException.Reason.SERIALIZATION, failure.reason)
    }

    @Test
    fun `a timeout throws a timeout failure`() = runTest {
        val repository = createRepository { throw SocketTimeoutException("timeout") }

        val failure = assertFailsWith<NetworkException> { repository.sendReport(report(), target) }

        assertEquals(NetworkException.Reason.TIMEOUT, failure.reason)
    }

    @Test
    fun `a failure that is not a network one passes through`() = runTest {
        val repository = createRepository { throw IllegalStateException("illegal") }

        assertFailsWith<IllegalStateException> { repository.sendReport(report(), target) }
    }

    @Test
    fun `device capture comes from the provider`() = runTest {
        val repository = createRepository { respond("{}", HttpStatusCode.Created) }

        assertEquals(deviceInfo(), repository.captureDeviceInfo())
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
}
