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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.data.repositories

import android.content.Context
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import kotlin.io.path.createTempDirectory
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class DefaultCacheRepositoryTest {

    private fun tempDirectory(): File = createTempDirectory().toFile()

    private fun context(cacheDir: File, codeCacheDir: File, externalCacheDir: File?): Context {
        val context = mockk<Context>()
        every { context.cacheDir } returns cacheDir
        every { context.codeCacheDir } returns codeCacheDir
        every { context.externalCacheDir } returns externalCacheDir
        return context
    }

    @Test
    fun `clearCache deletes every cache directory`() = runTest {
        val dir1 = tempDirectory().also { File(it, "a.txt").writeText("x") }
        val dir2 = tempDirectory().also { File(it, "b.txt").writeText("x") }
        val dir3 = tempDirectory().also { File(it, "c.txt").writeText("x") }
        val repository = DefaultCacheRepository(
            context = context(cacheDir = dir1, codeCacheDir = dir2, externalCacheDir = dir3),
            telemetryRepository = FakeTelemetryRepository(),
        )

        repository.clearCache()

        assertFalse(dir1.exists())
        assertFalse(dir2.exists())
        assertFalse(dir3.exists())
    }

    @Test
    fun `clearCache succeeds without an external cache directory`() = runTest {
        val dir1 = tempDirectory()
        val dir2 = tempDirectory()
        val repository = DefaultCacheRepository(
            context = context(cacheDir = dir1, codeCacheDir = dir2, externalCacheDir = null),
            telemetryRepository = FakeTelemetryRepository(),
        )

        repository.clearCache()

        assertFalse(dir1.exists())
        assertFalse(dir2.exists())
    }

    @Test
    fun `clearCache treats missing directories as cleared`() = runTest {
        val repository = DefaultCacheRepository(
            context = context(
                cacheDir = tempDirectory().also { it.deleteRecursively() },
                codeCacheDir = tempDirectory().also { it.deleteRecursively() },
                externalCacheDir = tempDirectory().also { it.deleteRecursively() },
            ),
            telemetryRepository = FakeTelemetryRepository(),
        )

        repository.clearCache()
    }

    @Test
    fun `an incomplete deletion throws and still deletes the other directories`() = runTest {
        val dir1 = tempDirectory()
        val failing = tempDirectory()
        val dir3 = tempDirectory()
        val repository = DefaultCacheRepository(
            context = context(cacheDir = dir1, codeCacheDir = failing, externalCacheDir = dir3),
            telemetryRepository = FakeTelemetryRepository(),
            deleteRecursively = { file -> if (file == failing) false else file.deleteRecursively() },
        )

        val error = assertFailsWith<StorageException> { repository.clearCache() }

        assertEquals(StorageException.Reason.FAILED, error.reason)
        assertFalse(dir1.exists())
        assertTrue(failing.exists())
        assertFalse(dir3.exists())
    }

    @Test
    fun `a refused cache directory throws an unavailable storage failure`() = runTest {
        val context = mockk<Context>()
        every { context.cacheDir } throws SecurityException("denied")
        val repository = DefaultCacheRepository(
            context = context,
            telemetryRepository = FakeTelemetryRepository(),
        )

        val error = assertFailsWith<StorageException> { repository.clearCache() }

        assertEquals(StorageException.Reason.UNAVAILABLE, error.reason)
        assertIs<SecurityException>(error.cause)
    }

    @Test
    fun `a delete that throws a security failure throws an unavailable storage failure`() = runTest {
        val repository = DefaultCacheRepository(
            context = context(cacheDir = tempDirectory(), codeCacheDir = tempDirectory(), externalCacheDir = null),
            telemetryRepository = FakeTelemetryRepository(),
            deleteRecursively = { throw SecurityException("denied") },
        )

        val error = assertFailsWith<StorageException> { repository.clearCache() }

        assertEquals(StorageException.Reason.UNAVAILABLE, error.reason)
    }

    @Test
    fun `a delete that throws an IO failure throws a failed storage failure`() = runTest {
        val repository = DefaultCacheRepository(
            context = context(cacheDir = tempDirectory(), codeCacheDir = tempDirectory(), externalCacheDir = null),
            telemetryRepository = FakeTelemetryRepository(),
            deleteRecursively = { throw IOException("disk") },
        )

        val error = assertFailsWith<StorageException> { repository.clearCache() }

        assertEquals(StorageException.Reason.FAILED, error.reason)
    }
}
