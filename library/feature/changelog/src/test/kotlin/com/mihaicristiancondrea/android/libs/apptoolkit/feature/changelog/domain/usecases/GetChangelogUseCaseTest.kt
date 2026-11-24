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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.BuildInfoRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.models.ChangelogRelease
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.repositories.ChangelogRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class GetChangelogUseCaseTest {

    @Test
    fun `full release list is requested with the host package and version`() = runTest {
        val expected = listOf(
            ChangelogRelease(heading = "2.0.0", sections = listOf("- Current"), isCurrent = true),
            ChangelogRelease(heading = "1.0.0", sections = listOf("- Previous"), isCurrent = false),
        )
        val repository = FakeChangelogRepository(releases = expected)
        val useCase = GetChangelogUseCase(
            repository = repository,
            buildInfoRepository = buildInfo(version = "2.0.0"),
        )

        assertEquals(expected, useCase())
        assertEquals("com.example.app", repository.requestedPackage)
        assertEquals("2.0.0", repository.requestedVersion)
    }

    @Test
    fun `empty release list remains empty for the localized no updates state`() = runTest {
        val useCase = GetChangelogUseCase(
            repository = FakeChangelogRepository(releases = emptyList()),
            buildInfoRepository = buildInfo(version = "2.0.0"),
        )

        assertEquals(emptyList(), useCase())
    }

    @Test
    fun `a repository failure passes through`() = runTest {
        val failure = NetworkException(reason = NetworkException.Reason.SERVER)
        val useCase = GetChangelogUseCase(
            repository = FakeChangelogRepository(failure = failure),
            buildInfoRepository = buildInfo(version = "2.0.0"),
        )

        assertSame(failure, runCatching { useCase() }.exceptionOrNull())
    }

    private fun buildInfo(version: String): BuildInfoRepository = object : BuildInfoRepository {
        override val appVersion: String = version
        override val appVersionCode: Int = 20
        override val packageName: String = "com.example.app"
        override val isDebugBuild: Boolean = false
    }
}

private class FakeChangelogRepository(
    private val releases: List<ChangelogRelease> = emptyList(),
    private val failure: Throwable? = null,
) : ChangelogRepository {
    var requestedPackage: String? = null
    var requestedVersion: String? = null

    override suspend fun getChangelog(
        packageName: String,
        currentVersion: String,
    ): List<ChangelogRelease> {
        requestedPackage = packageName
        requestedVersion = currentVersion
        failure?.let { throw it }
        return releases
    }
}
