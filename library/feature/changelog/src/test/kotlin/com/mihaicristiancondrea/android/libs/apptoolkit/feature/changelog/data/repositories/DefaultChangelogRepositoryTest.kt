/*
 * Copyright (C) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.net.UnknownHostException
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DefaultChangelogRepositoryTest {

    @Test
    fun `public package changelog is the primary source`() = runTest {
        val requestedPaths = mutableListOf<String>()
        val repository = createRepository(
            engine = MockEngine { request ->
                requestedPaths += request.url.encodedPath
                respond(content = "# 2.0.0\n- Worker", status = HttpStatusCode.OK)
            },
        )

        val changelog = repository.getChangelog("com.example.app")

        assertEquals("# 2.0.0\n- Worker", changelog)
        assertEquals(listOf("/api/v1/apps/com.example.app/changelog.md"), requestedPaths)
    }

    @Test
    fun `not found package uses the legacy changelog`() = runTest {
        val requestedPaths = mutableListOf<String>()
        val repository = createRepository(
            engine = MockEngine { request ->
                requestedPaths += request.url.encodedPath
                if (request.url.host == "legacy.example") {
                    respond(content = "# Legacy", status = HttpStatusCode.OK)
                } else {
                    respond(content = "{}", status = HttpStatusCode.NotFound)
                }
            },
        )

        val changelog = repository.getChangelog("com.example.missing")

        assertEquals("# Legacy", changelog)
        assertEquals(
            listOf(
                "/api/v1/apps/com.example.missing/changelog.md",
                "/changelog.md",
            ),
            requestedPaths,
        )
    }

    @Test
    fun `blank package uses the legacy changelog directly`() = runTest {
        val requestedHosts = mutableListOf<String>()
        val repository = createRepository(
            engine = MockEngine { request ->
                requestedHosts += request.url.host
                respond(content = "# Legacy", status = HttpStatusCode.OK)
            },
        )

        val changelog = repository.getChangelog(" ")

        assertEquals("# Legacy", changelog)
        assertEquals(listOf("legacy.example"), requestedHosts)
    }

    @Test
    fun `server failure is thrown without invoking the legacy fallback`() = runTest {
        var requestCount = 0
        val repository = createRepository(
            engine = MockEngine {
                requestCount++
                respond(content = "unavailable", status = HttpStatusCode.ServiceUnavailable)
            },
        )

        val failure = runCatching { repository.getChangelog("com.example.app") }.exceptionOrNull()

        assertEquals(NetworkException.Reason.SERVER, assertIs<NetworkException>(failure).reason)
        assertEquals(1, requestCount)
    }

    @Test
    fun `a failed legacy fallback is thrown`() = runTest {
        val repository = createRepository(
            engine = MockEngine { request ->
                if (request.url.host == "legacy.example") {
                    respond(content = "gone", status = HttpStatusCode.Gone)
                } else {
                    respond(content = "{}", status = HttpStatusCode.NotFound)
                }
            },
        )

        val failure = runCatching { repository.getChangelog("com.example.missing") }.exceptionOrNull()

        assertEquals(NetworkException.Reason.CLIENT, assertIs<NetworkException>(failure).reason)
    }

    @Test
    fun `a transport failure is thrown translated`() = runTest {
        val repository = createRepository(engine = MockEngine { throw UnknownHostException() })

        val failure = runCatching { repository.getChangelog("com.example.app") }.exceptionOrNull()

        assertEquals(NetworkException.Reason.NO_INTERNET, assertIs<NetworkException>(failure).reason)
    }

    private fun createRepository(engine: MockEngine): DefaultChangelogRepository =
        DefaultChangelogRepository(
            client = HttpClient(engine),
            apiBaseUrl = "https://metadata.example",
            legacyChangelogUrl = "https://legacy.example/changelog.md",
            telemetryRepository = FakeTelemetryRepository(),
        )
}
