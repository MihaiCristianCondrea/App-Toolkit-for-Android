package com.d4rk.android.libs.apptoolkit.app.advanced.data.repository

import android.content.Context
import app.cash.turbine.test
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class TestCacheRepositoryImpl {

    @Test
    fun `clearCache deletes cache directories`() = runTest {
        val dir1 = createTempDirectory().toFile()
        val dir2 = createTempDirectory().toFile()
        val dir3 = createTempDirectory().toFile()

        File(dir1, "a.txt").writeText("x")
        File(dir2, "b.txt").writeText("x")
        File(dir3, "c.txt").writeText("x")

        val context = mockk<Context>()
        every { context.cacheDir } returns dir1
        every { context.codeCacheDir } returns dir2
        every { context.externalCacheDir } returns dir3

        val repository = CacheRepositoryImpl(context)
        val result = repository.clearCache().single()

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertFalse(dir1.exists())
        assertFalse(dir2.exists())
        assertFalse(dir3.exists())
    }

    @Test
    fun `clearCache emits error when one directory fails deletion`() = runTest {
        val dir1 = createTempDirectory().toFile()
        val failing = createTempDirectory().toFile()
        val dir3 = createTempDirectory().toFile()

        val context = mockk<Context>()
        every { context.cacheDir } returns dir1
        every { context.codeCacheDir } returns failing
        every { context.externalCacheDir } returns dir3

        val repository = CacheRepositoryImpl(context)
        val result = repository.clearCache().single()

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertFalse(dir1.exists())
        assertTrue(failing.exists())
        assertFalse(dir3.exists())
    }

    @Test
    fun `clearCache emits error when context access throws`() = runTest {
        val context = mockk<Context>()
        every { context.cacheDir } throws SecurityException("denied")

        val repository = CacheRepositoryImpl(context)
        val result = repository.clearCache().single()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `clearCache handles missing directories as success`() = runTest {
        val dir1 = createTempDirectory().toFile().also { it.deleteRecursively() }
        val dir2 = createTempDirectory().toFile().also { it.deleteRecursively() }
        val dir3 = createTempDirectory().toFile().also { it.deleteRecursively() }

        val context = mockk<Context>()
        every { context.cacheDir } returns dir1
        every { context.codeCacheDir } returns dir2
        every { context.externalCacheDir } returns dir3

        val repository = CacheRepositoryImpl(context)
        val result = repository.clearCache().single()

        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `clearCache emits error when deleter throws`() = runTest {
        val dir1 = createTempDirectory().toFile()
        val dir2 = createTempDirectory().toFile()
        val dir3 = createTempDirectory().toFile()

        val context = mockk<Context>()
        every { context.cacheDir } returns dir1
        every { context.codeCacheDir } returns dir2
        every { context.externalCacheDir } returns dir3

        val repository = CacheRepositoryImpl(context)
        val result = repository.clearCache().single()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `clearCache emits once and completes`() = runTest {
        val dir1 = createTempDirectory().toFile()
        val dir2 = createTempDirectory().toFile()

        val context = mockk<Context>()
        every { context.cacheDir } returns dir1
        every { context.codeCacheDir } returns dir2
        every { context.externalCacheDir } returns null

        val repository = CacheRepositoryImpl(context)

        repository.clearCache().test {
            assertThat(awaitItem()).isInstanceOf(Result.Success::class.java)
            awaitComplete()
        }
    }
}
