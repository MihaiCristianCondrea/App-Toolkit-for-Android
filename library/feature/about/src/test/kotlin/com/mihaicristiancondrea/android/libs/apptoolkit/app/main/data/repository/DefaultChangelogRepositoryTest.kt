/*
 * Copyright (C) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.app.main.data.repository

import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
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

        val result = repository.fetchChangelog("com.example.app").first()

        assertEquals("# 2.0.0\n- Worker", assertIs<DataState.Success<String, Errors>>(result).data)
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

        val result = repository.fetchChangelog("com.example.missing").first()

        assertEquals("# Legacy", assertIs<DataState.Success<String, Errors>>(result).data)
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

        val result = repository.fetchChangelog(" ").first()

        assertEquals("# Legacy", assertIs<DataState.Success<String, Errors>>(result).data)
        assertEquals(listOf("legacy.example"), requestedHosts)
    }

    @Test
    fun `server failure is reported without invoking the legacy fallback`() = runTest {
        var requestCount = 0
        val repository = createRepository(
            engine = MockEngine {
                requestCount++
                respond(content = "unavailable", status = HttpStatusCode.ServiceUnavailable)
            },
        )

        val result = repository.fetchChangelog("com.example.app").first()

        assertEquals(
            Errors.Network.HTTP_SERVER_ERROR,
            assertIs<DataState.Error<String, Errors>>(result).error,
        )
        assertEquals(1, requestCount)
    }

    private fun createRepository(engine: MockEngine): DefaultChangelogRepository =
        DefaultChangelogRepository(
            client = HttpClient(engine),
            apiBaseUrl = "https://metadata.example",
            legacyChangelogUrl = "https://legacy.example/changelog.md",
            firebaseController = FakeFirebaseController(),
        )
}
