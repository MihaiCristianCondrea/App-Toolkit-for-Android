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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.AppVersionMetadata
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.packagemanager.getVersionMetadata
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.packagemanager.isAppInstalled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

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

    @Test
    fun batchLookupFiltersBlankAndMissingPackages() = runTest {
        val extensions = "com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.packagemanager.PackageManagerExtensionsKt"
        mockkStatic(extensions)
        try {
            every { context.isAppInstalled("example.installed") } returns true
            every { context.isAppInstalled("example.missing") } returns false
            val repository = DefaultInstalledAppsRepository(context, TestDispatchers(StandardTestDispatcher(testScheduler)))
            assertEquals(
                setOf("example.installed"),
                repository.getInstalledPackages(listOf("", "example.installed", "example.missing", "example.installed")),
            )
            verify(exactly = 0) { context.isAppInstalled("") }
        } finally {
            unmockkStatic(extensions)
        }
    }

    @Test
    fun installInfoKeepsTheSharedVersionMetadata() = runTest {
        val extensions = "com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.packagemanager.PackageManagerExtensionsKt"
        mockkStatic(extensions)
        try {
            val version = AppVersionMetadata(versionName = "1.2", versionCode = 12)
            every { context.isAppInstalled("example.app") } returns true
            every { manager.getVersionMetadata("example.app") } returns version
            val repository = DefaultInstalledAppsRepository(context, TestDispatchers(StandardTestDispatcher(testScheduler)))
            val info = repository.getInstallInfo("example.app")
            assertTrue(info.isInstalled)
            assertEquals(version, info.versionInfo)
            assertEquals(null, repository.getInstallInfo("").versionInfo)
        } finally {
            unmockkStatic(extensions)
        }
    }

    @Test
    fun cancelledLauncherLookupPropagates() = runTest {
        every { manager.getLaunchIntentForPackage("example.app") } throws CancellationException("cancelled")
        val repository = DefaultInstalledAppsRepository(context, TestDispatchers(StandardTestDispatcher(testScheduler)))
        assertFailsWith<CancellationException> { repository.getLaunchIntent("example.app") }
    }

}
