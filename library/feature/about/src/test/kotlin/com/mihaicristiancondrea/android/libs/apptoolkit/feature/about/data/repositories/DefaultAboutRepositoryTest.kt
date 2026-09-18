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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.providers.GooglePlayServicesVersionProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.AboutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.AboutItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.AboutItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.AboutItemKey
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
    fun `getAboutInfo returns expected info`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = repository()

        val result: AboutInfo = repo.getAboutInfo()

        assertThat(result.items).hasSize(8)

        val headerAppInfo = result.items[0] as AboutItem.Header
        assertThat(headerAppInfo.key).isEqualTo(AboutItemKey.HEADER_APP_INFO)

        val appName = result.items[1] as AboutItem.Preference
        assertThat(appName.key).isEqualTo(AboutItemKey.APP_NAME)
        assertThat(appName.position).isEqualTo(GroupedItemPosition.FIRST)

        val appBuildVersion = result.items[2] as AboutItem.Preference
        assertThat(appBuildVersion.key).isEqualTo(AboutItemKey.APP_BUILD_VERSION)
        assertThat((appBuildVersion.summary as UiTextHelper.DynamicString).content).isEqualTo("1.0 (1)")
        assertThat(appBuildVersion.position).isEqualTo(GroupedItemPosition.MIDDLE)
        assertThat(appBuildVersion.action).isEqualTo(AboutItemAction.VersionEasterEgg)

        val toolkitVersion = result.items[3] as AboutItem.Preference
        assertThat(toolkitVersion.key).isEqualTo(AboutItemKey.APP_TOOLKIT_VERSION)
        assertThat((toolkitVersion.summary as UiTextHelper.DynamicString).content).isEqualTo("3.0.0-test")
        assertThat(toolkitVersion.position).isEqualTo(GroupedItemPosition.MIDDLE)

        val gmsVersion = result.items[4] as AboutItem.Preference
        assertThat(gmsVersion.key).isEqualTo(AboutItemKey.GOOGLE_PLAY_SERVICES_VERSION)
        assertThat((gmsVersion.summary as UiTextHelper.DynamicString).content).isEqualTo("24.01.12")
        assertThat(gmsVersion.position).isEqualTo(GroupedItemPosition.MIDDLE)

        val ossLicenses = result.items[5] as AboutItem.Preference
        assertThat(ossLicenses.key).isEqualTo(AboutItemKey.OSS_LICENSES)
        assertThat(ossLicenses.position).isEqualTo(GroupedItemPosition.LAST)
        assertThat(ossLicenses.action).isEqualTo(AboutItemAction.OpenLicenses)

        val headerDeviceInfo = result.items[6] as AboutItem.Header
        assertThat(headerDeviceInfo.key).isEqualTo(AboutItemKey.HEADER_DEVICE_INFO)

        val deviceInfoPref = result.items[7] as AboutItem.Preference
        assertThat(deviceInfoPref.key).isEqualTo(AboutItemKey.DEVICE_INFO)
        assertThat((deviceInfoPref.summary as UiTextHelper.DynamicString).content).isEqualTo(deviceProvider.deviceInfo)
        assertThat(deviceInfoPref.position).isEqualTo(GroupedItemPosition.SINGLE)
        assertThat(deviceInfoPref.action).isEqualTo(AboutItemAction.CopyDeviceInfo)
    }

    @Test
    fun `getAboutInfo handles absent google play services`() = runTest(dispatcherExtension.testDispatcher) {
        val gmsProvider = mockk<GooglePlayServicesVersionProvider> {
            every { getVersion() } returns null
        }
        val repo = repository(gmsVersionProvider = gmsProvider)

        val result: AboutInfo = repo.getAboutInfo()

        assertThat(result.items.none { it.key == AboutItemKey.GOOGLE_PLAY_SERVICES_VERSION }).isTrue()
        assertThat(result.items).hasSize(7)

        val ossLicenses = result.items.first { it.key == AboutItemKey.OSS_LICENSES } as AboutItem.Preference
        assertThat(ossLicenses.position).isEqualTo(GroupedItemPosition.LAST)
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
