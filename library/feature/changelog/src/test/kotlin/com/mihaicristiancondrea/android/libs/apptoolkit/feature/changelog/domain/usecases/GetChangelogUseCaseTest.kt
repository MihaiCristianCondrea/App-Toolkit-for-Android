/*
 * Copyright (C) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.domain.usecases

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.repositories.ChangelogRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class GetChangelogUseCaseTest {

    @Test
    fun `current version section is preferred`() = runTest {
        val history = "# 2.0.0\n- Current\n# 1.0.0\n- Previous"
        val repository = FakeChangelogRepository(markdown = history)
        val useCase = GetChangelogUseCase(repository, buildInfo(version = "2.0.0"))

        assertEquals("# 2.0.0\n- Current", useCase())
        assertEquals("com.example.app", repository.requestedPackage)
    }

    @Test
    fun `full history is shown when current version heading is absent`() = runTest {
        val history = "# 1.0.0\n- Previous"
        val useCase = GetChangelogUseCase(
            repository = FakeChangelogRepository(markdown = history),
            buildInfoProvider = buildInfo(version = "2.0.0"),
        )

        assertEquals(history, useCase())
    }

    @Test
    fun `blank response remains blank for the localized no updates state`() = runTest {
        val useCase = GetChangelogUseCase(
            repository = FakeChangelogRepository(markdown = "  "),
            buildInfoProvider = buildInfo(version = "2.0.0"),
        )

        assertEquals("", useCase())
    }

    @Test
    fun `a repository failure passes through`() = runTest {
        val failure = NetworkException(NetworkException.Reason.SERVER)
        val useCase = GetChangelogUseCase(
            repository = FakeChangelogRepository(failure = failure),
            buildInfoProvider = buildInfo(version = "2.0.0"),
        )

        assertSame(failure, runCatching { useCase() }.exceptionOrNull())
    }

    private fun buildInfo(version: String): BuildInfoProvider = object : BuildInfoProvider {
        override val appVersion: String = version
        override val appVersionCode: Int = 20
        override val packageName: String = "com.example.app"
        override val isDebugBuild: Boolean = false
    }
}

private class FakeChangelogRepository(
    private val markdown: String = "",
    private val failure: Throwable? = null,
) : ChangelogRepository {
    var requestedPackage: String? = null

    override suspend fun getChangelog(packageName: String): String {
        requestedPackage = packageName
        failure?.let { throw it }
        return markdown
    }
}
