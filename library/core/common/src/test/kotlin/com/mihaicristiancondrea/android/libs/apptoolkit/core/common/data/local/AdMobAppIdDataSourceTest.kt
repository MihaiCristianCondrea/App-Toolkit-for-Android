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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.local

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import io.mockk.verify
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlin.test.assertFalse
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlinx.coroutines.test.runTest

/**
 * Guards the rule that the toolkit never invents an AdMob app id.
 *
 * The demo app's id used to live in the library's `ad_units.xml`, so every consumer app that did not
 * declare a resource with that exact name inherited it and pointed both the UMP consent request and
 * `MobileAds.initialize` at the toolkit's publisher account.
 */
class AdMobAppIdDataSourceTest {

    private companion object {
        const val HOST_APP_ID: String = "ca-app-pub-1234567890123456~1234567890"

        /** The id that used to be baked into the library's `ad_units.xml`. */
        const val LIBRARY_DEMO_APP_ID: String = "ca-app-pub-5294151573817700~1979784742"
    }

    @BeforeEach
    fun mockAndroidLog() {
        mockkStatic(Log::class)
        every { Log.w(any<String>(), any<String>()) } returns 0
        every { Log.w(any<String>(), any<String>(), any()) } returns 0
    }

    @AfterEach
    fun unmockAndroidLog() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `resolves the app id declared by the host manifest`() = runTest {
        val dataSource = dataSourceFor(metaDataValue = HOST_APP_ID)

        assertEquals(HOST_APP_ID, dataSource.adMobAppId())
    }

    @Test
    fun `trims surrounding whitespace from the manifest value`() = runTest {
        val dataSource = dataSourceFor(metaDataValue = "  $HOST_APP_ID\n")

        assertEquals(HOST_APP_ID, dataSource.adMobAppId())
    }

    @Test
    fun `returns null when the host declares no meta-data`() = runTest {
        val dataSource = dataSourceFor(metaDataValue = null)

        assertNull(dataSource.adMobAppId())
    }

    @Test
    fun `returns null when the application has no meta-data bundle at all`() = runTest {
        val dataSource = dataSourceFor(metaData = null)

        assertNull(dataSource.adMobAppId())
    }

    @Test
    fun `returns null for malformed values instead of forwarding them`() = runTest {
        val malformedValues: List<String> = listOf(
            "",
            "   ",
            "ca-app-pub-123~456",
            "ca-app-pub-1234567890123456/1234567890",
            "pub-1234567890123456~1234567890",
            "ca-app-pub-1234567890123456~1234567890extra",
        )

        malformedValues.forEach { value ->
            assertNull(dataSourceFor(metaDataValue = value).adMobAppId(), "Accepted '$value'")
        }
    }

    @Test
    fun `returns null when package metadata cannot be read`() = runTest {
        val context: Context = mockk()
        val packageManager: PackageManager = mockk()
        every { context.applicationContext } returns context
        every { context.packageManager } returns packageManager
        every { context.packageName } returns "com.example.host"
        every {
            packageManager.getApplicationInfo(any<String>(), any<Int>())
        } throws PackageManager.NameNotFoundException()

        assertNull(ManifestAdMobAppIdDataSource(context = context).adMobAppId())
    }

    @Test
    fun `never falls back to the library demo app id`() = runTest {
        val dataSource = dataSourceFor(metaDataValue = null)

        assertNull(dataSource.adMobAppId())
        assertNull(AdMobAppIdDataSource.sanitize(rawValue = null))
        assertEquals(
            LIBRARY_DEMO_APP_ID,
            AdMobAppIdDataSource.sanitize(rawValue = LIBRARY_DEMO_APP_ID),
            "The demo id is a well-formed id; it may only be resolved from a host manifest that " +
                    "actually declares it.",
        )
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    @Test
    fun `manifest lookup uses the injected IO dispatcher and caches its result`() = runTest {
        val bundle: Bundle = mockk()
        every { bundle.getString(AdMobAppIdDataSource.MANIFEST_METADATA_KEY) } returns HOST_APP_ID
        val applicationInfo: ApplicationInfo = mockk()
        ApplicationInfo::class.java.getField("metaData").set(applicationInfo, bundle)
        val packageManager: PackageManager = mockk()
        every { packageManager.getApplicationInfo(any<String>(), any<Int>()) } returns applicationInfo
        val context: Context = mockk()
        every { context.applicationContext } returns context
        every { context.packageManager } returns packageManager
        every { context.packageName } returns "com.example.host"
        val dataSource = ManifestAdMobAppIdDataSource(
            context = context,
            dispatchers = TestDispatchers(StandardTestDispatcher(testScheduler)),
        )
        val result = async(start = CoroutineStart.UNDISPATCHED) { dataSource.adMobAppId() }
        assertFalse(result.isCompleted)
        verify(exactly = 0) { packageManager.getApplicationInfo(any<String>(), any<Int>()) }
        runCurrent()
        assertEquals(HOST_APP_ID, result.await())
        assertEquals(HOST_APP_ID, dataSource.adMobAppId())
        verify(exactly = 1) { packageManager.getApplicationInfo(any<String>(), any<Int>()) }
    }

    private fun dataSourceFor(metaDataValue: String?): ManifestAdMobAppIdDataSource {
        val bundle: Bundle = mockk()
        every {
            bundle.getString(AdMobAppIdDataSource.MANIFEST_METADATA_KEY)
        } returns metaDataValue
        return dataSourceFor(metaData = bundle)
    }

    private fun dataSourceFor(metaData: Bundle?): ManifestAdMobAppIdDataSource {
        val applicationInfo: ApplicationInfo = mockk()
        ApplicationInfo::class.java.getField("metaData").set(applicationInfo, metaData)

        val packageManager: PackageManager = mockk()
        every {
            packageManager.getApplicationInfo(any<String>(), any<Int>())
        } returns applicationInfo

        val context: Context = mockk()
        every { context.applicationContext } returns context
        every { context.packageManager } returns packageManager
        every { context.packageName } returns "com.example.host"

        return ManifestAdMobAppIdDataSource(context = context)
    }
}
