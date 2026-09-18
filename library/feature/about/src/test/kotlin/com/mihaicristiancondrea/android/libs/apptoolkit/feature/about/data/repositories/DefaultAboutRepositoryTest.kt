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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.providers.GooglePlayServicesVersionProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.AboutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.CopyDeviceInfoResult
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.providers.AboutSettingsProvider
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class TestDefaultAboutRepository {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val deviceProvider = object : AboutSettingsProvider {
        override val deviceInfo: String = "device-info"
    }

    private val buildInfoProvider = object : BuildInfoProvider {
        override val appVersion: String = "1.0"
        override val appVersionCode: Int = 1
        override val packageName: String = "pkg"
        override val isDebugBuild: Boolean = false
    }

    private fun repository(
        context: Context = mockk(),
        sdkIntProvider: () -> Int = { Build.VERSION.SDK_INT },
        gmsVersionProvider: GooglePlayServicesVersionProvider = mockk {
            every { getVersion() } returns "24.01.12"
        },
        toolkitVersionProvider: () -> String = { "3.0.0-test" },
    ): DefaultAboutRepository =
        DefaultAboutRepository(
            deviceProvider = deviceProvider,
            buildInfoProvider = buildInfoProvider,
            context = context,
            sdkIntProvider = sdkIntProvider,
            gmsVersionProvider = gmsVersionProvider,
            toolkitVersionProvider = toolkitVersionProvider,
            firebaseController = mockk<FirebaseController>(relaxed = true),
        )

    @Test
    fun `getAboutInfo returns host and toolkit metadata`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = repository()

        val result: AboutInfo = repo.getAboutInfo()

        assertThat(result).isEqualTo(
            AboutInfo(
                appVersion = "1.0",
                appVersionCode = 1,
                appToolkitVersion = "3.0.0-test",
                googlePlayServicesVersion = "24.01.12",
                deviceInfo = deviceProvider.deviceInfo,
            )
        )
    }

    @Test
    fun `getAboutInfo reports absent google play services as null`() =
        runTest(dispatcherExtension.testDispatcher) {
            val gmsProvider = mockk<GooglePlayServicesVersionProvider> {
                every { getVersion() } returns null
            }
            val repo = repository(gmsVersionProvider = gmsProvider)

            val result: AboutInfo = repo.getAboutInfo()

            assertThat(result.googlePlayServicesVersion).isNull()
        }

    @Test
    fun `copyDeviceInfo with blank device info reports failure without copying`() {
        val blankDeviceProvider = object : AboutSettingsProvider {
            override val deviceInfo: String = "   "
        }
        val context = mockk<Context>()
        val repo = DefaultAboutRepository(
            deviceProvider = blankDeviceProvider,
            buildInfoProvider = buildInfoProvider,
            context = context,
            firebaseController = mockk(relaxed = true),
            sdkIntProvider = { Build.VERSION_CODES.S_V2 },
            gmsVersionProvider = mockk { every { getVersion() } returns null },
            toolkitVersionProvider = { "3.0.0-test" },
        )

        val copyResult = repo.copyDeviceInfo(label = "label")

        assertThat(copyResult).isEqualTo(
            CopyDeviceInfoResult(copied = false, shouldShowFeedback = false)
        )
        verify(exactly = 0) { context.getSystemService(ClipboardManager::class.java) }
    }

    @Test
    fun `copyDeviceInfo delegates to copyTextToClipboard`() {
        mockkStatic(Log::class)
        every { Log.w(any(), any(), any()) } returns 0
        mockkStatic(ClipData::class)
        val clipData = mockk<ClipData>()
        every { ClipData.newPlainText(any(), any()) } returns clipData
        val context = mockk<Context>()
        val clipboardManager = mockk<ClipboardManager>()
        every { context.getSystemService(ClipboardManager::class.java) } returns clipboardManager
        justRun { clipboardManager.setPrimaryClip(any()) }
        val repo = repository(
            context = context,
            sdkIntProvider = { Build.VERSION_CODES.S_V2 },
        )
        val copyResult = runCatching {
            repo.copyDeviceInfo(
                "label",
                "info"
            )
        }.also {
            unmockkStatic(ClipData::class)
            unmockkStatic(Log::class)
        }.getOrThrow()

        verify { clipboardManager.setPrimaryClip(any()) }
        assertThat(copyResult).isEqualTo(
            CopyDeviceInfoResult(
                copied = true,
                shouldShowFeedback = true
            )
        )
    }

    @Test
    fun `copyDeviceInfo without deviceInfo parameter uses deviceProvider deviceInfo`() {
        mockkStatic(Log::class)
        every { Log.w(any(), any(), any()) } returns 0
        mockkStatic(ClipData::class)
        val clipData = mockk<ClipData>()
        every { ClipData.newPlainText("label", "device-info") } returns clipData
        val context = mockk<Context>()
        val clipboardManager = mockk<ClipboardManager>()
        every { context.getSystemService(ClipboardManager::class.java) } returns clipboardManager
        justRun { clipboardManager.setPrimaryClip(any()) }
        val repo = repository(
            context = context,
            sdkIntProvider = { Build.VERSION_CODES.S_V2 },
        )
        val copyResult = try {
            val result = repo.copyDeviceInfo("label")
            verify { ClipData.newPlainText("label", "device-info") }
            verify { clipboardManager.setPrimaryClip(clipData) }
            result
        } finally {
            unmockkStatic(ClipData::class)
            unmockkStatic(Log::class)
        }

        assertThat(copyResult).isEqualTo(
            CopyDeviceInfoResult(
                copied = true,
                shouldShowFeedback = true
            )
        )
    }
}
