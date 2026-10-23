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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.gms.data.repositories

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.runner.RunWith
import org.junit.Test
import org.robolectric.annotation.Config
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [27, 28, 33])
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class DefaultGooglePlayServicesRepositoryTest {
    private val context = mockk<Context>()
    private val manager = mockk<PackageManager>()
    private val info = mockk<PackageInfo>()

    private fun prepare(failure: Exception? = null) {
        every { context.applicationContext } returns context
        every { context.packageManager } returns manager
        if (Build.VERSION.SDK_INT >= 33) {
            every {
                manager.getPackageInfo("com.google.android.gms", any<PackageManager.PackageInfoFlags>())
            } answers { failure?.let { throw it }; info }
        } else {
            @Suppress("DEPRECATION")
            every { manager.getPackageInfo("com.google.android.gms", 0) } answers {
                failure?.let { throw it }
                info
            }
        }
        info.versionName = "26.10"
        @Suppress("DEPRECATION")
        info.versionCode = 123
        if (Build.VERSION.SDK_INT >= 28) {
            every { info.longVersionCode } returns 4_294_967_300L
        }
    }

    @Test
    fun returnsInstalledVersionName() = runTest {
        prepare()
        val repository = DefaultGooglePlayServicesRepository(context, TestDispatchers(StandardTestDispatcher(testScheduler)))
        assertEquals("26.10", repository.getVersion())
    }

    @Test
    fun fallsBackToThePlatformVersionCode() = runTest {
        prepare()
        info.versionName = null
        val repository = DefaultGooglePlayServicesRepository(context, TestDispatchers(StandardTestDispatcher(testScheduler)))
        assertEquals(if (Build.VERSION.SDK_INT >= 28) "4294967300" else "123", repository.getVersion())
    }

    @Test
    fun missingGooglePlayServicesReturnsNull() = runTest {
        prepare(PackageManager.NameNotFoundException())
        val repository = DefaultGooglePlayServicesRepository(context, TestDispatchers(StandardTestDispatcher(testScheduler)))
        assertNull(repository.getVersion())
    }

    @Test
    fun unexpectedPlatformFailureIsNotReportedAsMissing() = runTest {
        val failure = SecurityException("Package lookup denied")
        prepare(failure)
        val repository = DefaultGooglePlayServicesRepository(context, TestDispatchers(StandardTestDispatcher(testScheduler)))
        val actual = try {
            repository.getVersion()
            error("Expected lookup failure")
        } catch (error: SecurityException) {
            error
        }
        assertSame(failure, actual)
    }

    @Test
    fun packageLookupWaitsForTheInjectedIoDispatcher() = runTest {
        prepare()
        val repository = DefaultGooglePlayServicesRepository(context, TestDispatchers(StandardTestDispatcher(testScheduler)))
        val result = async(start = CoroutineStart.UNDISPATCHED) { repository.getVersion() }
        assertFalse(result.isCompleted)
        verify(exactly = 0) { manager.getPackageInfo(any<String>(), any<Int>()) }
        if (Build.VERSION.SDK_INT >= 33) {
            verify(exactly = 0) { manager.getPackageInfo(any<String>(), any<PackageManager.PackageInfoFlags>()) }
        }
        runCurrent()
        assertEquals("26.10", result.await())
    }
}
