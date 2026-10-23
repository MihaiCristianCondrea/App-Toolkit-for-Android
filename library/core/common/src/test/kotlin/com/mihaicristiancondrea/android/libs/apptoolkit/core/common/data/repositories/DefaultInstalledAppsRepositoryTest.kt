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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class DefaultInstalledAppsRepositoryTest {
    private val context = mockk<Context>()
    private val manager = mockk<PackageManager>()

    init {
        every { context.applicationContext } returns context
        every { context.packageManager } returns manager
    }

    @Test
    fun launcherLookupRunsOnTheInjectedIoDispatcher() = runTest {
        val intent = mockk<Intent>()
        every { manager.getLaunchIntentForPackage("example.app") } returns intent
        val repository = DefaultInstalledAppsRepository(context, TestDispatchers(StandardTestDispatcher(testScheduler)))
        val result = async(start = CoroutineStart.UNDISPATCHED) {
            repository.getLaunchIntent("example.app")
        }
        assertFalse(result.isCompleted)
        verify(exactly = 0) { manager.getLaunchIntentForPackage(any()) }
        runCurrent()
        assertSame(intent, result.await())
    }

    @Test
    fun packagesWithoutALauncherReturnNull() = runTest {
        every { manager.getLaunchIntentForPackage("example.app") } returns null
        val repository = DefaultInstalledAppsRepository(context, TestDispatchers(StandardTestDispatcher(testScheduler)))
        assertNull(repository.getLaunchIntent("example.app"))
    }

    @Test
    fun deniedLauncherLookupReturnsNull() = runTest {
        every { manager.getLaunchIntentForPackage("example.app") } throws SecurityException("Denied")
        val repository = DefaultInstalledAppsRepository(context, TestDispatchers(StandardTestDispatcher(testScheduler)))
        assertNull(repository.getLaunchIntent("example.app"))
    }

    @Test
    fun blankPackageNamesDoNotQueryThePlatform() = runTest {
        val repository = DefaultInstalledAppsRepository(context, TestDispatchers(StandardTestDispatcher(testScheduler)))
        assertNull(repository.getLaunchIntent(" "))
        assertEquals(false, repository.isInstalled(""))
        verify(exactly = 0) { manager.getLaunchIntentForPackage(any()) }
    }
}
