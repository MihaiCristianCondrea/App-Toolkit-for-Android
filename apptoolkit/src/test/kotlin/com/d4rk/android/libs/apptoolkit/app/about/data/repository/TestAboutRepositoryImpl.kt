package com.d4rk.android.libs.apptoolkit.app.about.data.repository

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.CopyDeviceInfoResult
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class TestAboutRepositoryImpl {

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
    ): AboutRepositoryImpl =
        AboutRepositoryImpl(
            deviceProvider = deviceProvider,
            buildInfoProvider = buildInfoProvider,
            context = context,
            sdkIntProvider = sdkIntProvider,
            firebaseController = mockk<FirebaseController>(relaxed = true),
        )

    @Test
    fun `getAboutInfo returns expected info`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = repository()

        val result: AboutInfo = repo.getAboutInfo()

        assertThat(result.appVersion).isEqualTo(buildInfoProvider.appVersion)
        assertThat(result.appVersionCode).isEqualTo(buildInfoProvider.appVersionCode)
        assertThat(result.deviceInfo).isEqualTo(deviceProvider.deviceInfo)
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

        val copyResult = try {
            repo.copyDeviceInfo(
                "label",
                "info"
            )
        } finally {
            unmockkStatic(ClipData::class)
            unmockkStatic(Log::class)
        }

        verify { clipboardManager.setPrimaryClip(any()) }
        assertThat(copyResult).isEqualTo(
            CopyDeviceInfoResult(
                copied = true,
                shouldShowFeedback = true
            )
        )
    }
}
