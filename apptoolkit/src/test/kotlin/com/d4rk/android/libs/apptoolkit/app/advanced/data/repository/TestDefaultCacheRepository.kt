package com.d4rk.android.libs.apptoolkit.app.advanced.data.repository

import android.content.Context
import app.cash.turbine.test
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.io.path.createTempDirectory
import kotlin.test.assertFalse

class TestDefaultCacheRepository {

    @Test
    fun `clearCache deletes cache directories`() = runTest {
        val dir1 = createTempDirectory().toFile()
        val dir2 = createTempDirectory().toFile()
        val dir3 = createTempDirectory().toFile()

        val context = mockk<Context>()
        every { context.cacheDir } returns dir1
        every { context.codeCacheDir } returns dir2
        every { context.filesDir } returns dir3

        val repository = DefaultCacheRepository(context, TestDispatchers())
        val result = repository.clearCache().single()

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertFalse(dir1.exists())
        assertFalse(dir2.exists())
        assertFalse(dir3.exists())
    }

    @Test
    fun `clearCache returns false when deletion fails`() = runTest {
        val dir1 = createTempDirectory().toFile()
        val failing = mockk<java.io.File>()
        every { failing.deleteRecursively() } returns false
        every { failing.exists() } returns true
        val dir3 = createTempDirectory().toFile()

        val context = mockk<Context>()
        every { context.cacheDir } returns dir1
        every { context.codeCacheDir } returns failing
        every { context.filesDir } returns dir3

        val repository = DefaultCacheRepository(context, TestDispatchers())
        val result = repository.clearCache().single()

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertFalse(dir1.exists())
        assertFalse(dir3.exists())
    }

    @Test
    fun `clearCache emits error when directory inaccessible`() = runTest {
        val context = mockk<Context>()
        every { context.cacheDir } throws SecurityException("denied")

        val repository = DefaultCacheRepository(context, TestDispatchers())
        val result = repository.clearCache().single()
        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `clearCache handles missing directories`() = runTest {
        val dir1 = createTempDirectory().toFile().also { it.deleteRecursively() }
        val dir2 = createTempDirectory().toFile().also { it.deleteRecursively() }
        val dir3 = createTempDirectory().toFile().also { it.deleteRecursively() }

        val context = mockk<Context>()
        every { context.cacheDir } returns dir1
        every { context.codeCacheDir } returns dir2
        every { context.filesDir } returns dir3

        val repository = DefaultCacheRepository(context, TestDispatchers())
        val result = repository.clearCache().single()

        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `clearCache emits error when io exception`() = runTest {
        val dir1 = createTempDirectory().toFile()
        val failing = mockk<java.io.File>()
        every { failing.deleteRecursively() } throws java.io.IOException("io")
        every { failing.exists() } returns true
        val dir3 = createTempDirectory().toFile()

        val context = mockk<Context>()
        every { context.cacheDir } returns dir1
        every { context.codeCacheDir } returns failing
        every { context.filesDir } returns dir3

        val repository = DefaultCacheRepository(context, TestDispatchers())
        val result = repository.clearCache().single()
        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `clearCache emits success and completes`() = runTest {
        val dir1 = createTempDirectory().toFile()
        val dir2 = createTempDirectory().toFile()
        val dir3 = createTempDirectory().toFile()

        val context = mockk<Context>()
        every { context.cacheDir } returns dir1
        every { context.codeCacheDir } returns dir2
        every { context.filesDir } returns dir3

        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = DefaultCacheRepository(context, TestDispatchers(dispatcher))

        repository.clearCache().test {
            assertThat(awaitItem()).isInstanceOf(Result.Success::class.java)
            awaitComplete()
        }

        assertFalse(dir1.exists())
        assertFalse(dir2.exists())
        assertFalse(dir3.exists())
    }
}
